Types of multitasking:  
1. Process-based. Allows the computer to run two or more programs concurrently.  
2. Thread-based. Allow the program to perform two or more tasks simultaneously.

Multitasking threads require less overhead than multitasking processes. Processes are heavyweight tasks that require their own separate address spaces. Interprocess communication is expensive and limited. Context switching from one process to another is also costly.

Multithreading enables you to write efficient programs that make maximum use of the processing power available in the system by keeping idle time to a minimum.

#### The Java thread model

Single-threaded systems use an approach called an event loop with polling, when a thread blocks (that is, suspends execution) because it is waiting for some resource, the entire program stops running.

The benefit of Java’s multithreading is that the main loop/polling mechanism is eliminated. When a thread blocks, only the single thread that is blocked pauses. All other threads continue to run.

Currently, we live in multicore system, it is possible for two or more threads to actually execute simultaneously.  
Before, in a single-core system, two or more threads do not actually run at the same time, but idle CPU time is utilized.

##### Thread Priorities

Java assigns to each thread a priority (default is 5). It's used to decide when to switch from one running thread to the next. This is called a context switch.

The rules when a context switch takes place  
1. A thread can voluntarily relinquish control.  
2. A thread can be preempted by a higher-priority thread.

For some operating systems, threads of equal priority are time-sliced automatically in round-robin fashion.  
For other types of operating systems, threads of equal priority must voluntarily yield control to their peers.

##### Synchronization

How two threads to communicate and share a complicated data structure? Using a _monitor_.  
In Java, there is no class “Monitor”; instead, each object has its own implicit monitor that is automatically entered when one of the object’s _synchronized_ methods is called.

##### Messaging

Java provides a clean, low-cost way for two or more threads to talk to each other, via calls to predefined methods that all objects have.

##### The Thread Class, and the Runnable Interface

Java’s multithreading system is built upon the `Thread` class, its methods, and its companion interface, `Runnable`.  
`Thread` encapsulates a thread of execution. Since you can't directly refer to the ethereal state of a running thread, you will deal with it through its proxy, the `Thread` instance that spawned it.

#### The Main thread

When a Java program starts up, one thread begins running immediately. We call this _main_ thread.

#### Creating a thread

##### Implement the Runnable interface

Runnable abstracts a unit of executable code. You can construct a thread on any object that implements Runnable. To implement Runnable, a class need only implement a single method called `run()`.  
`run()` can call other methods, use other classes, and declare variables, just like the main thread can. The only difference is that `run()` establishes the entry point for another, concurrent thread of execution within your program. This thread will end when `run()` returns.

After you create a class that implements Runnable, you will instantiate an object of type Thread from within that class.  
After the new thread is created, it will not start running until you call its `start()` method, which is declared within Thread. In essence, `start()` initiates a call to `run()`.

##### Extend the Thread class

The extending class must override the `run()` method, which is the entry point for the new thread. A call to `start()` begins execution of the new thread.

###### Which one to choose?

I prefer to implement Runnable interface. By implementing Runnable, your thread class does not need to inherit Thread, making it free to inherit a different class.  
Classes should be extended only when they are being enhanced or adapted in some way.

###### Multiple thread?

What if we have multiple thread start at the same time? All those threads share the CPU.

What if we want our main thread to finish last? We can use [`sleep()`](https://github.com/bluething/learnjava/blob/main/javathreadmodel/src/main/java/io/github/bluething/java/threadmodel/MultipleThreadDemo.java) with enough time. The question is how long enough main thread should wait?  
How can one thread know when another thread has ended? Using `isAlive()`.
Or we can use [`join()`](https://github.com/bluething/learnjava/blob/main/javathreadmodel/src/main/java/io/github/bluething/java/threadmodel/MultipleThreadWithJoinDemo.java). This method waits until the thread on which it is called terminates.