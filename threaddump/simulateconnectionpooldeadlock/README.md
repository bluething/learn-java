### How to play

#### For auth module

The code in `auth` package created by [Uriah Levy](https://github.com/uriahl) for [Analyzing Java Thread Dumps](https://app.pluralsight.com/courses/ebb11bd1-c104-4be4-9cd7-5c19be357a66/table-of-contents) course at Pluralsight.  
This package simulates oauth.

Run `BearerAuthenticator` with `numberOfParallelRequests` = 1. It will run smoothly.  
Then change `numberOfParallelRequests` to 2. It will run then hung for a while then stop. Get the pid and dump the thread when the app still running.  
I attach sample of thread dump file.

We want to see the thread state (find the reason why our app hung).  
Search for `pool-1-thread-1` and we will see the state `java.lang.Thread.State: WAITING (parking)`. Read the stack strace from bottom to top.  
Around the middle of the stack, we can see this thread try to get token from local cache
```text
	at jdk.internal.misc.Unsafe.park(java.base@14.0.1/Native Method)
	- parking to wait for  <0x000000071a855560> (a com.google.common.util.concurrent.SettableFuture)
	at java.util.concurrent.locks.LockSupport.park(java.base@14.0.1/LockSupport.java:211)
	at com.google.common.util.concurrent.AbstractFuture.get(AbstractFuture.java:537)
	at com.google.common.util.concurrent.AbstractFuture$TrustedFuture.get(AbstractFuture.java:104)
	at com.google.common.util.concurrent.Uninterruptibles.getUninterruptibly(Uninterruptibles.java:240)
	at com.google.common.cache.LocalCache$LoadingValueReference.waitForValue(LocalCache.java:3582)
	at com.google.common.cache.LocalCache$Segment.waitForLoadingValue(LocalCache.java:2175)
	at com.google.common.cache.LocalCache$Segment.lockedGetOrLoad(LocalCache.java:2162)
	at com.google.common.cache.LocalCache$Segment.get(LocalCache.java:2045)
	at com.google.common.cache.LocalCache.get(LocalCache.java:3962)
	at com.google.common.cache.LocalCache.getOrLoad(LocalCache.java:3985)
	at com.google.common.cache.LocalCache$LocalLoadingCache.get(LocalCache.java:4946)
	at io.github.bluething.java.threaddump.simulateconnectionpooldeadlock.auth.TokenAuthenticationCache.getToken(TokenAuthenticationCache.java:74)
	at io.github.bluething.java.threaddump.simulateconnectionpooldeadlock.auth.BearerScheme.authenticate(BearerScheme.java:48)
```  
Intellij have nice tool to analyze stack strace, select Code | Analyze Stack Trace or Thread Dump. We can navigate easily to the source code.  
![intellij analyze stack trace tool](https://github.com/bluething/learnjava/blob/main/images/intellijanalyzestacktracetool.png?raw=true)

From the stack we can see `TokenAuthenticationCache.getToken` which is get cache from LocalCache.  
In same class we see there are method (called by constructor) to _lazily_ init the cache. Furthermore, it will use pool http.  
Lazily means it will execute after the 1st time thread ask for the token.  
The problem is here. When app start it will use http client with pool thread. The same pool use (indirectly) by `TokenAuthenticationCache` to init the token.

1st connection: request to protected resource  
2nd connection: request to token endpoint  
3rd connection: authenticated request to protected resource

Because we use two concurrent request threads, the same connection can't be reused for multiple threads, as long as one thread is already using the connection.  
So even an HTTP request to the same route will require a new connection.  
So in essence, only idle connections can be reused with HTTP 1.1.

We set `connectionManager.setMaxTotal(2);` so the 3rd one will block until a connection is made available by the connectionManager.  
The thread must wait to get a connection.

Next stack is `LocalLoadingCache.get`, from java doc we can say this method implement some lock mechanism but allow parallel reads for distinct keys.  
So why the thread state is waiting? Someone holds lock for "Jeff" token.

Check for other thread stack. In the middle of stack we can see  
```text
	at io.github.bluething.java.threaddump.simulateconnectionpooldeadlock.auth.BearerAuthenticator.executeMethod(BearerAuthenticator.java:158)
	at io.github.bluething.java.threaddump.simulateconnectionpooldeadlock.auth.TokenAuthenticationCache.fetchNewToken(TokenAuthenticationCache.java:51)
	at io.github.bluething.java.threaddump.simulateconnectionpooldeadlock.auth.TokenAuthenticationCache$1.load(TokenAuthenticationCache.java:36)
	at io.github.bluething.java.threaddump.simulateconnectionpooldeadlock.auth.TokenAuthenticationCache$1.load(TokenAuthenticationCache.java:33)
	at com.google.common.cache.LocalCache$LoadingValueReference.loadFuture(LocalCache.java:3529)
	at com.google.common.cache.LocalCache$Segment.loadSync(LocalCache.java:2278)
	at com.google.common.cache.LocalCache$Segment.lockedGetOrLoad(LocalCache.java:2155)
	- locked <0x000000071a860d60> (a com.google.common.cache.LocalCache$StrongWriteEntry)
	at com.google.common.cache.LocalCache$Segment.get(LocalCache.java:2045)
```  
This stack say we try to get new token, after that we try to make http call using the pool. But the stack said we need wait for something (the connection release by connection manager).  
When no connections are available, the thread waiting for a connection will block until a connection is made available.

#### Finding TCP connection started by thread

Run `debug-network.sh` then find lwp in worker thread stack trace.  
Open strace output file with suffix value same with lwp value above.  
We can see tcp connection status there.