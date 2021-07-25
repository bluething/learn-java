### Thread dump

Thread dump is a bunch of text that represents the runtime state of the program at a particular point in time (what the program is doing).  
Thread dump is also a collection of all the call stacks of all the threads that are currently being tracked by the JVM.

#### Call stack

![call stack](https://github.com/bluething/learnjava/blob/main/images/callstack.png?raw=true)

Call stack is a cascading representation of all the methods the thread has called up to a particular point in time. They only retain information about the methods, or calls, that haven't finished executing yet.  
In a multithreaded application, each thread has its own private call stack.

From [The Java Virtual Machine](https://www.artima.com/insidejvm/ed2/jvm8.html)  
"When a new thread is launched, the JVM creates a new stack for the thread. A Java stack stores a thread's state in discrete frames. The JVM only performs two operations directly on Java stacks: it pushes and pops frames.  
The method that is currently being executed by a thread is the thread's current method. The stack frame for the current method is the current frame. The class in which the current method is defined is called the current class, and the current class’s constant pool is the current constant pool. As it executes a method, the JVM keeps track of the current class and current constant pool. When the JVM encounters instructions that operate on data stored in the stack frame, it performs those operations on the current frame.  
When a thread invokes a Java method, the JVM creates and pushes a new frame onto the thread’s stack. This new frame then becomes the current frame. As the method executes, it uses the frame to store parameters, local variables, intermediate computations, and other data."

#### Tools to capture thread dump

##### SIGQUIT (kill -3 in Linux)

`kill -3 $pid`

Throws the thread dump into the STDOUT out of the JVM process on which we invoked the signal.

##### jcmd

`jcmd <process id/main class> Thread.print`

##### jstack

`jstack [-F] [-l] [-m] <pid>`  
The options  
1. `-F` option forces a thread dump; handy to use when jstack pid does not respond (the process is hung)  
2. -`l` option instructs the utility to look for ownable synchronizers in the heap and locks  
3. `-m` option prints native stack frames (C & C++) in addition to the Java stack frames

Prefer to use `jcmd` for enhanced diagnostics and reduced performance overhead

#### How to capture thread dump

1. Find the pid of java process.  
   `ps aux | grep '[A]pp' | awk '{print $2}'`  
   App is a java process name.
2. Use command from tools we have to dump the thread.

##### Thread dump content

```text
Full thread dump OpenJDK 64-Bit Server VM (14.0.1+7 mixed mode, sharing):

Threads class SMR info:
_java_thread_list=0x00007f760427b490, length=10, elements={
0x00007f7604029000, 0x00007f76041f5000, 0x00007f76041f7000, 0x00007f76041fe000,
0x00007f7604200000, 0x00007f7604202000, 0x00007f7604204000, 0x00007f7604206000,
0x00007f7604275800, 0x00007f760427a000
}

"main" #1 prio=5 os_prio=0 cpu=28,46ms elapsed=22,56s tid=0x00007f7604029000 nid=0x6104 waiting on condition  [0x00007f7608e9a000]
   java.lang.Thread.State: TIMED_WAITING (sleeping)
	at java.lang.Thread.sleep(java.base@14.0.1/Native Method)
	at io.github.bluething.java.threaddump.simulatelongpausethread.App.sleep(App.java:12)
	at io.github.bluething.java.threaddump.simulatelongpausethread.App.main(App.java:7)

"Reference Handler" #2 daemon prio=10 os_prio=0 cpu=0,17ms elapsed=22,55s tid=0x00007f76041f5000 nid=0x610b waiting on condition  [0x00007f75cbffe000]
   java.lang.Thread.State: RUNNABLE
	at java.lang.ref.Reference.waitForReferencePendingList(java.base@14.0.1/Native Method)
	at java.lang.ref.Reference.processPendingReferences(java.base@14.0.1/Reference.java:241)
	at java.lang.ref.Reference$ReferenceHandler.run(java.base@14.0.1/Reference.java:213)

"Finalizer" #3 daemon prio=8 os_prio=0 cpu=0,28ms elapsed=22,55s tid=0x00007f76041f7000 nid=0x610c in Object.wait()  [0x00007f75cbefd000]
   java.lang.Thread.State: WAITING (on object monitor)
	at java.lang.Object.wait(java.base@14.0.1/Native Method)
	- waiting on <0x000000071ad027f0> (a java.lang.ref.ReferenceQueue$Lock)
	at java.lang.ref.ReferenceQueue.remove(java.base@14.0.1/ReferenceQueue.java:155)
	- locked <0x000000071ad027f0> (a java.lang.ref.ReferenceQueue$Lock)
	at java.lang.ref.ReferenceQueue.remove(java.base@14.0.1/ReferenceQueue.java:176)
	at java.lang.ref.Finalizer$FinalizerThread.run(java.base@14.0.1/Finalizer.java:170)

"Signal Dispatcher" #4 daemon prio=9 os_prio=0 cpu=0,22ms elapsed=22,55s tid=0x00007f76041fe000 nid=0x610d waiting on condition  [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"Service Thread" #5 daemon prio=9 os_prio=0 cpu=0,03ms elapsed=22,55s tid=0x00007f7604200000 nid=0x610e runnable  [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"C2 CompilerThread0" #6 daemon prio=9 os_prio=0 cpu=5,42ms elapsed=22,55s tid=0x00007f7604202000 nid=0x610f waiting on condition  [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE
   No compile task

"C1 CompilerThread0" #9 daemon prio=9 os_prio=0 cpu=10,01ms elapsed=22,55s tid=0x00007f7604204000 nid=0x6110 waiting on condition  [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE
   No compile task

"Sweeper thread" #10 daemon prio=9 os_prio=0 cpu=0,70ms elapsed=22,55s tid=0x00007f7604206000 nid=0x6111 runnable  [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"Notification Thread" #11 daemon prio=9 os_prio=0 cpu=0,11ms elapsed=22,54s tid=0x00007f7604275800 nid=0x6112 runnable  [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"Common-Cleaner" #12 daemon prio=8 os_prio=0 cpu=0,24ms elapsed=22,53s tid=0x00007f760427a000 nid=0x6114 in Object.wait()  [0x00007f75cb183000]
   java.lang.Thread.State: TIMED_WAITING (on object monitor)
	at java.lang.Object.wait(java.base@14.0.1/Native Method)
	- waiting on <0x000000071ad46a78> (a java.lang.ref.ReferenceQueue$Lock)
	at java.lang.ref.ReferenceQueue.remove(java.base@14.0.1/ReferenceQueue.java:155)
	- locked <0x000000071ad46a78> (a java.lang.ref.ReferenceQueue$Lock)
	at jdk.internal.ref.CleanerImpl.run(java.base@14.0.1/CleanerImpl.java:148)
	at java.lang.Thread.run(java.base@14.0.1/Thread.java:832)
	at jdk.internal.misc.InnocuousThread.run(java.base@14.0.1/InnocuousThread.java:134)

"VM Thread" os_prio=0 cpu=2,35ms elapsed=22,55s tid=0x00007f76041f2000 nid=0x610a runnable  

"GC Thread#0" os_prio=0 cpu=0,37ms elapsed=22,56s tid=0x00007f760408c000 nid=0x6105 runnable  

"G1 Main Marker" os_prio=0 cpu=0,11ms elapsed=22,56s tid=0x00007f76040ac800 nid=0x6106 runnable  

"G1 Conc#0" os_prio=0 cpu=0,06ms elapsed=22,56s tid=0x00007f76040ae800 nid=0x6107 runnable  

"G1 Refine#0" os_prio=0 cpu=0,10ms elapsed=22,56s tid=0x00007f76041a7000 nid=0x6108 runnable  

"G1 Young RemSet Sampling" os_prio=0 cpu=3,89ms elapsed=22,56s tid=0x00007f76041a8800 nid=0x6109 runnable  
"VM Periodic Task Thread" os_prio=0 cpu=22,12ms elapsed=22,53s tid=0x00007f7604277800 nid=0x6113 waiting on condition  

JNI global refs: 6, weak refs: 0

Heap
 garbage-first heap   total 253952K, used 836K [0x000000070b800000, 0x0000000800000000)
  region size 1024K, 1 young (1024K), 0 survivors (0K)
 Metaspace       used 153K, capacity 4486K, committed 4864K, reserved 1056768K
  class space    used 7K, capacity 386K, committed 512K, reserved 1048576K
```

#### Read more

[JVM Stacks and Stack Frames](https://alvinalexander.com/scala/fp-book/recursion-jvm-stacks-stack-frames/)