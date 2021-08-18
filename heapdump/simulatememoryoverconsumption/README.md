### How to play

1. To get `java.lang.OutOfMemoryError` set `-Xmx` to appropriate value (below 64M).  
2. Run the app (`MemoryHeavyStatisticsService`).  
3. Run `run.sh` several times.

![heavy memory consumption](https://github.com/bluething/learnjava/blob/main/images/heavymemoryconsumption.jpg?raw=true)  
We can see a lot of memory use when process the request

The stackstrace  
```text
java.lang.OutOfMemoryError: GC overhead limit exceeded
Dumping heap to java_pid34475.hprof ...
Heap dump file created [65389959 bytes in 0,247 secs]
2021-08-18 14:13:10.412:WARN:oejs.ServletHandler:qtp933699219-13: Error for /
java.lang.OutOfMemoryError: GC overhead limit exceeded
	at java.util.Arrays.copyOfRange(Arrays.java:3664)
	at java.lang.String.<init>(String.java:207)
	at java.io.BufferedReader.readLine(BufferedReader.java:356)
	at java.io.BufferedReader.readLine(BufferedReader.java:389)
	at java.io.BufferedReader$1.hasNext(BufferedReader.java:571)
	at java.util.Iterator.forEachRemaining(Iterator.java:115)
	at java.util.Spliterators$IteratorSpliterator.forEachRemaining(Spliterators.java:1801)
	at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:482)
	at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:472)
	at java.util.stream.ReduceOps$ReduceOp.evaluateSequential(ReduceOps.java:708)
	at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
	at java.util.stream.ReferencePipeline.collect(ReferencePipeline.java:566)
	at io.github.bluething.java.heapdump.simulatememoryoverconsumption.MemoryHeavyStatisticsService.readLines(MemoryHeavyStatisticsService.java:45)
	at io.github.bluething.java.heapdump.simulatememoryoverconsumption.MemoryHeavyStatisticsService.doPost(MemoryHeavyStatisticsService.java:35)
	at javax.servlet.http.HttpServlet.service(HttpServlet.java:707)
	at javax.servlet.http.HttpServlet.service(HttpServlet.java:790)
	at org.eclipse.jetty.servlet.ServletHolder.handle(ServletHolder.java:808)
	at org.eclipse.jetty.servlet.ServletHandler.doHandle(ServletHandler.java:587)
	at org.eclipse.jetty.servlet.ServletHandler.doScope(ServletHandler.java:517)
	at org.eclipse.jetty.server.handler.ScopedHandler.handle(ScopedHandler.java:141)
	at org.eclipse.jetty.server.handler.HandlerWrapper.handle(HandlerWrapper.java:97)
	at org.eclipse.jetty.server.Server.handle(Server.java:499)
	at org.eclipse.jetty.server.HttpChannel.handle(HttpChannel.java:310)
	at org.eclipse.jetty.server.HttpConnection.onFillable(HttpConnection.java:257)
	at org.eclipse.jetty.io.AbstractConnection$2.run(AbstractConnection.java:540)
	at org.eclipse.jetty.util.thread.QueuedThreadPool.runJob(QueuedThreadPool.java:635)
	at org.eclipse.jetty.util.thread.QueuedThreadPool$3.run(QueuedThreadPool.java:555)
	at java.lang.Thread.run(Thread.java:748)
2021-08-18 14:13:10.420:WARN:oejh.HttpParser:qtp933699219-13: badMessage: java.lang.IllegalStateException: too much data after closed for HttpChannelOverHttp@710efafe{r=1,c=false,a=IDLE,uri=-}
```  
We can see the error happen when we call this line  
```java
return reader.lines().skip(1).collect(Collectors.toList());
```  
But this is not enough, because it might be that there was another piece of application code that wasn't using much memory that just happened to allocate an object at the same time, and caused this to.

![heap dump heavy memory consumption](https://github.com/bluething/learnjava/blob/main/images/heapdumpheavymemoryconcumption.png?raw=true)  
From the heap dump we can see that a lot of object String inside the ArrayList. This is same with the readLines logic that read from file then save to memory.

The solution is `MemoryLightStatisticsService.java`