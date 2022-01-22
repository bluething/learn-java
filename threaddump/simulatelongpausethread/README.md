#### How to Play

1. Run the main method  
2. Find the pid then dump the thread using sigquit  
```java
ps aux | grep '[A]pp' | awk '{print $2}' | xargs kill -3
```

The result  
```text
2022-01-22 10:40:23
Full thread dump OpenJDK 64-Bit Server VM (14.0.1+7 mixed mode, sharing):

Threads class SMR info:
_java_thread_list=0x00007fb6782735f0, length=10, elements={
0x00007fb678029000, 0x00007fb6781f5000, 0x00007fb6781f7000, 0x00007fb6781fe000,
0x00007fb678200000, 0x00007fb678202000, 0x00007fb678204000, 0x00007fb678206000,
0x00007fb67826d800, 0x00007fb678272000
}

"main" #1 prio=5 os_prio=0 cpu=23,04ms elapsed=4,08s tid=0x00007fb678029000 nid=0x2393 waiting on condition  [0x00007fb67d08c000]
   java.lang.Thread.State: TIMED_WAITING (sleeping)
	at java.lang.Thread.sleep(java.base@14.0.1/Native Method)
	at io.github.bluething.java.threaddump.simulatelongpausethread.App.sleep(App.java:12)
	at io.github.bluething.java.threaddump.simulatelongpausethread.App.main(App.java:7)

"Reference Handler" #2 daemon prio=10 os_prio=0 cpu=0,14ms elapsed=4,08s tid=0x00007fb6781f5000 nid=0x239a waiting on condition  [0x00007fb654275000]
   java.lang.Thread.State: RUNNABLE
	at java.lang.ref.Reference.waitForReferencePendingList(java.base@14.0.1/Native Method)
	at java.lang.ref.Reference.processPendingReferences(java.base@14.0.1/Reference.java:241)
	at java.lang.ref.Reference$ReferenceHandler.run(java.base@14.0.1/Reference.java:213)

"Finalizer" #3 daemon prio=8 os_prio=0 cpu=0,24ms elapsed=4,08s tid=0x00007fb6781f7000 nid=0x239b in Object.wait()  [0x00007fb654174000]
   java.lang.Thread.State: WAITING (on object monitor)
	at java.lang.Object.wait(java.base@14.0.1/Native Method)
	- waiting on <0x000000071af027f0> (a java.lang.ref.ReferenceQueue$Lock)
	at java.lang.ref.ReferenceQueue.remove(java.base@14.0.1/ReferenceQueue.java:155)
	- locked <0x000000071af027f0> (a java.lang.ref.ReferenceQueue$Lock)
	at java.lang.ref.ReferenceQueue.remove(java.base@14.0.1/ReferenceQueue.java:176)
	at java.lang.ref.Finalizer$FinalizerThread.run(java.base@14.0.1/Finalizer.java:170)

"Signal Dispatcher" #4 daemon prio=9 os_prio=0 cpu=0,18ms elapsed=4,07s tid=0x00007fb6781fe000 nid=0x239c waiting on condition  [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"Service Thread" #5 daemon prio=9 os_prio=0 cpu=0,12ms elapsed=4,07s tid=0x00007fb678200000 nid=0x239d runnable  [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"C2 CompilerThread0" #6 daemon prio=9 os_prio=0 cpu=4,52ms elapsed=4,07s tid=0x00007fb678202000 nid=0x239e waiting on condition  [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE
   No compile task

"C1 CompilerThread0" #9 daemon prio=9 os_prio=0 cpu=8,01ms elapsed=4,07s tid=0x00007fb678204000 nid=0x239f waiting on condition  [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE
   No compile task

"Sweeper thread" #10 daemon prio=9 os_prio=0 cpu=0,57ms elapsed=4,07s tid=0x00007fb678206000 nid=0x23a0 runnable  [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"Notification Thread" #11 daemon prio=9 os_prio=0 cpu=0,19ms elapsed=4,07s tid=0x00007fb67826d800 nid=0x23a1 runnable  [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"Common-Cleaner" #12 daemon prio=8 os_prio=0 cpu=0,31ms elapsed=4,06s tid=0x00007fb678272000 nid=0x23a3 in Object.wait()  [0x00007fb637385000]
   java.lang.Thread.State: TIMED_WAITING (on object monitor)
	at java.lang.Object.wait(java.base@14.0.1/Native Method)
	- waiting on <0x000000071af46a90> (a java.lang.ref.ReferenceQueue$Lock)
	at java.lang.ref.ReferenceQueue.remove(java.base@14.0.1/ReferenceQueue.java:155)
	- locked <0x000000071af46a90> (a java.lang.ref.ReferenceQueue$Lock)
	at jdk.internal.ref.CleanerImpl.run(java.base@14.0.1/CleanerImpl.java:148)
	at java.lang.Thread.run(java.base@14.0.1/Thread.java:832)
	at jdk.internal.misc.InnocuousThread.run(java.base@14.0.1/InnocuousThread.java:134)

"VM Thread" os_prio=0 cpu=0,94ms elapsed=4,08s tid=0x00007fb6781f2000 nid=0x2399 runnable  

"GC Thread#0" os_prio=0 cpu=0,27ms elapsed=4,08s tid=0x00007fb67808c000 nid=0x2394 runnable  

"G1 Main Marker" os_prio=0 cpu=0,03ms elapsed=4,08s tid=0x00007fb6780ac800 nid=0x2395 runnable  

"G1 Conc#0" os_prio=0 cpu=0,05ms elapsed=4,08s tid=0x00007fb6780ae800 nid=0x2396 runnable  

"G1 Refine#0" os_prio=0 cpu=0,18ms elapsed=4,08s tid=0x00007fb6781a7000 nid=0x2397 runnable  

"G1 Young RemSet Sampling" os_prio=0 cpu=0,70ms elapsed=4,08s tid=0x00007fb6781a8800 nid=0x2398 runnable  
"VM Periodic Task Thread" os_prio=0 cpu=4,15ms elapsed=4,07s tid=0x00007fb67826f800 nid=0x23a2 waiting on condition  

JNI global refs: 6, weak refs: 0

Heap
 garbage-first heap   total 253952K, used 836K [0x000000070ba00000, 0x0000000800000000)
  region size 1024K, 1 young (1024K), 0 survivors (0K)
 Metaspace       used 152K, capacity 4486K, committed 4864K, reserved 1056768K
  class space    used 7K, capacity 386K, committed 512K, reserved 1048576K
```