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

#### Read more

[JVM Stacks and Stack Frames](https://alvinalexander.com/scala/fp-book/recursion-jvm-stacks-stack-frames/)