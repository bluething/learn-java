### How to play

#### For auth module

The code in `auth` package created by [Uriah Levy](https://github.com/uriahl) for [Analyzing Java Thread Dumps](https://app.pluralsight.com/courses/ebb11bd1-c104-4be4-9cd7-5c19be357a66/table-of-contents) course at Pluralsight.

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