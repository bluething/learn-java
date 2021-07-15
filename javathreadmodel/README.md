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

#### Thread priorities

In theory, over a given period of time, higher-priority threads get more CPU time than lower-priority threads.  
In practice, the amount of CPU time that a thread gets often depends on several factors besides its priority.  
A higher-priority thread can also preempt a lower-priority one.

In theory, threads of equal priority should get equal access to the CPU. Java by design can run in multiple environment. Those environments implement multitasking fundamentally differently than others.  
For safety, threads that share the same priority should yield control once in a while.

#### Synchronization

Key to synchronization is the concept of the monitor, using `synchronized` keyword.  
A monitor is an object that is used as a mutually exclusive lock. All objects have their own implicit monitor associated with them.  
Only one thread can own a monitor at a given time. When a thread acquires a lock, it is said to have entered the monitor.  
All other threads attempting to enter the locked monitor will be suspended until the first thread exits the monitor. These other threads are said to be waiting for the monitor.  
A thread that owns a monitor can reenter the same monitor if it so desires.  
Use the `synchronized` keyword to guard the state from race conditions.

What if the class not designed to multithreaded purpose? Use synchronized statement.

#### Interthread communication

One benefit of thread is they do away with polling.  
Polling is usually implemented by a loop that is used to check some condition repeatedly. Once the condition is true, appropriate action is taken. This wastes CPU time.  
To avoid polling, Java includes an elegant interprocess communication mechanism via the `wait()`, `notify()`, and `notifyAll()` methods.

`wait()` normally waits until `notify()` or `notifyAll()` is called, there is a possibility that in very rare cases the waiting thread could be awakened due to a spurious wakeup. In this case, a waiting thread resumes without `notify()` or `notifyAll()` having been called.  
Because of this remote possibility, the Java API documentation recommends that calls to wait( ) should take place within a loop that checks the condition on which the thread is waiting.

#### Deadlock

Deadlock occurs when two threads have a circular dependency on a pair of synchronized objects.  
Deadlock is a difficult error to debug for two reasons:  
1. In general, it occurs only rarely, when the two threads time-slice in just the right way.  
2. It may involve more than two threads and two synchronized objects. (That is, deadlock can occur through a more convoluted sequence of events than just described.)

#### Thread state

![thread state](https://github.com/bluething/learnjava/blob/main/images/threadstate.jpg?raw=true)

| State name | Description |
| --- | --- |
| NEW | A thread that has not yet started is in this state. |
| RUNNABLE | A thread executing in the Java virtual machine is in this state. |
| BLOCKED | A thread that is blocked waiting for a monitor lock is in this state. |
| WAITING | A thread that is waiting indefinitely for another thread to perform a particular action is in this state. |
| TIMED_WAITING | A thread that is waiting for another thread to perform an action for up to a specified waiting time is in this state. |
| TERMINATED | A thread that has exited is in this state. |

We can use `getState()` to obtain the state of a thread.  
It is important to understand that a thread’s state may change after the call to `getState()`. Thus, depending on the circumstances, the state obtained by calling `getState()` may not reflect the actual state of the thread only a moment later. For this (and other) reasons, `getState()` is not intended to provide a means of synchronizing threads. It’s primarily used for debugging or for profiling a thread’s run-time characteristics.