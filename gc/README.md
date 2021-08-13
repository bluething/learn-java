## Garbage Collection

### What Is Garbage Collection?

Garbage collection is the process of looking at heap memory, identifying which objects are in use and which are not, and deleting the unused objects.

### Garbage Algorithm

#### Reference Counting Algorithm

This algorithm will allocate a field in the object header to store the reference count of the object.  
If this object is referenced by another object, its reference count increments by one.  
If the reference to this object is deleted, the reference count decrements by one.  
When the reference count of this object drops to zero, the object will be garbage-collected.

What if we have two objects that hold referenced by each other? Those objects will never be collect by collector because the counter never be zero.

#### Reachability Analysis Algorithm

Start from GC root. Live object form a graph like a tree.  
GC traverses the whole object graph in the memory, starting from these roots and following references from the roots to other objects. The path is called the reference chain.  
If an object has no reference chain to the GC roots, that is the object cannot be reached from the GC roots, the object is unavailable.

### Garbage Collection Algorithm

JVM specifications do not clearly define how to implement the garbage collector. Each vendor decided which algorithm to be used.

#### Mark-Sweep Algorithm

1. Mark the objects to be garbage collected in the memory space.  
2. Clear the marked objects up from the space.

The problem is memory fragmentation, memory can be allocated only in contiguous form of blocks. How about empty block leave after the mark object cleared?

#### Copying Algorithm

1. Divides available memory into two equally sized semi-spaces. Only one semi-space is active at a time.  
2. When the active semi-space becomes full, living objects are copied to the other semi-space.  
3. The active but full memory space is cleared up.

No more memory fragmentation issue, but we only utilize half of memory. The cost is big.

#### Mark-Compact Algorithm

1. Mark the objects to be garbage collected in the memory space.  
2. Moves all living objects to one end.   
3. Reclaims the memory space beyond the end boundary.

Less efficient than Copying algorithm because there are more changes to the memory and needs to sort out the reference addresses of all living objects

#### Generational Collection Algorithm

It's a combination of three above algorithm.  
Memory is divided into block according to different lifespan of objects, young and old generation.

##### In young generation

We can always see a large number of dead object after a GC cycle and only few object survive. The Copying algorithm is adopted to complete the collection by copying only a few living objects.

##### In old generation

The Mark-Sweep or Mark-Compact algorithm is adopted because the objects have a high survival rate and no extra memory space is reserved specially for allocation.

### JVM Generations

![hotspot heap structure](https://github.com/bluething/learnjava/blob/main/images/hotspotheapstructure.PNG?raw=true)

Heap space = young generation (1/3) + old generation (2/3)  
Young generation = eden (8/10) + survivor0 (1/10) + survivor1 (1/10)

#### Young generation

The Young Generation is where all new objects are allocated and aged. When the young generation fills up, this causes a _minor garbage collection_.  
Minor collections can be optimized assuming a high object mortality rate. A young generation full of dead objects is collected very quickly.  
Some surviving objects are aged and eventually move to the old generation.

All minor garbage collections are "_Stop the World_" events. This means that all application threads are stopped until the operation completes.

##### Eden

Filled by newly created objects, almost all objects have a short life. When the Eden space is not large enough for memory allocation, the VM initiates a minor GC.  
The objects that survive the minor GC will be moved to the survivor0/survivor1 space. If the survivor space is not large enough, these objects will be moved directly to the old generation.

##### Survivor

The Survivor space acts like a buffer between the Eden space and the old generation.

###### Why Is the Survivor Space Required?

1. Reduces the occurrence of major GC. Remember, many object have short life, even though they survive from minor gc the lifetime is not long enough. They maybe collected in 2nd or 3rd minor gc.  
2. Reduce the number of objects sent to the old generation. If we don't have a buffer, old generation will soon be filled up.

The pre-screening by the Survivor space ensures that only objects that can survive 16 minor GCs will be promoted to the old generation.

###### Why Is the Survivor Space Further Divided into Two Spaces?

To solve the problem of memory fragmentation. Because we can only use Mark-Sweep Algorithm here. The process is insert(from eden)-delete(existing object in survivor). Other algorithm make the process more complex.

Surviving objects in the previous Eden and survivor0 spaces will be copied to the survivor1 space after a minor GC.  
In the second Minor GC, the roles of the survivor0 and survivor1 spaces are exchanged.

###### Why two?

If the Survivor space is further subdivided, the space of each partition will be relatively small and can be easily filled up.

#### Old generation

The Old Generation is used to store long surviving objects. Typically, a threshold is set for young generation object and when that age is met, the object gets moved to the old generation.  
Eventually the old generation needs to be collected. This event is called a major garbage collection. Major garbage collection are also Stop the World events. Often a major collection is much slower because it involves all live objects. So, it is not always better to have a larger memory.  
The length of the Stop the World event for a major garbage collection is affected by the kind of garbage collector that is used for the old generation space.

The following objects will also be placed in the old generation:  
1. Large objects.  
   An object that requires a large amount of contiguous memory space.
2. Long-lived objects.  
   The VM sets an age counter for each object. In normal conditions, objects are constantly moving between the survivor0 and survivor1 survivor spaces. After objects survive a minor GC, they have their age incremented by one. When the age of an object is increased to 15, it will be promoted to the old generation. Of course, JVM also supports setting the age threshold.

#### Permanent generation

The Permanent generation contains metadata required by the JVM to describe the classes and methods used in the application. The permanent generation is populated by the JVM at runtime based on classes in use by the application.  
Classes may get collected (unloaded) if the JVM finds they are no longer needed and space may be needed for other classes. The permanent generation is included in a full garbage collection.

### Garbage collector type

#### Serial Garbage Collector

The serial collector uses a single thread to perform all garbage collection work, which makes it relatively efficient because there is no communication overhead between threads.  
The amount of data structures the footprint required for this Garbage collector to run is very minimal.  
It's best-suited to single processor machines because it can't take advantage of multiprocessor hardware, although it can be useful on multiprocessors for applications with small data sets (up to approximately 100 MB).  
The serial collector is selected by default on certain hardware and operating system configurations, or can be explicitly enabled with the option `-XX:+UseSerialGC`

#### Parallel Garbage Collector

It's like the serial collector, the primary difference is that multiple threads are used to speed up garbage collection.  
The parallel collector is enabled with the command-line option `-XX:+UseParallelGC`. By default, with this option, both minor and major collections are executed in parallel to further reduce garbage collection overhead.

It is named Parallel because it has multiple threads of the Garbage collection itself and all of those threads run parallel but when the Garbage collector is running all the threads are _stopped_ and if our application is deployed on a multicore or multiprocessor systems this collector will give us the greatest throughput.

The number of garbage collector threads can be controlled with the command-line option `-XX:ParallelGCThreads=<N>`  
On a machine with N hardware threads where N is greater than 8, the parallel collector uses approximately 5/8 for large values of N. At values of N below 8, the number used is N. On selected platforms, the fraction drops to 5/16.

Because multiple garbage collector threads are participating in a minor collection, some fragmentation is possible due to promotions from the young generation to the tenured generation during the collection. Each garbage collection thread involved in a minor collection reserves a part of the tenured generation for promotions and the division of the available space into these "promotion buffers" can cause a fragmentation effect.  
Reducing the number of garbage collector threads and increasing the size of the tenured generation will reduce this fragmentation effect.

Customizable setting  
1. Maximum Garbage Collection Pause Time  
The maximum pause time goal is specified with the command-line option `-XX:MaxGCPauseMillis=<N>`. This is interpreted as a hint that pause times of <N> milliseconds or less are desired; by default, there is no maximum pause time goal. If a pause time goal is specified, the heap size and other parameters related to garbage collection are adjusted in an attempt to keep garbage collection pauses shorter than the specified value. These adjustments may cause the garbage collector to reduce the overall throughput of the application, and the desired pause time goal cannot always be met.  
2. Throughput  
The throughput goal is measured in terms of the time spent doing garbage collection versus the time spent outside of garbage collection (referred to as application time). The goal is specified by the command-line option `-XX:GCTimeRatio=<N>`  
3. Footprint  
Maximum heap footprint is specified using the option `-Xmx<N>`

The downside to the parallel collector is that it will stop application threads when performing either a minor or full GC collection.  
The parallel collector is best suited for apps that can tolerate application pauses and are trying to optimize for lower CPU overhead caused by the collector, for example batch applications.

#### Concurrent Mark Sweep (CMS) Garbage Collector

#### Garbage-First (G1) Garbage Collector

#### Z Garbage Collector

#### Shenandoah Garbage Collector

### Reference

[How Does Garbage Collection Work in Java?](https://www.alibabacloud.com/blog/how-does-garbage-collection-work-in-java_595387)  
[Java Garbage Collection Basics](https://www.oracle.com/webfolder/technetwork/tutorials/obe/java/gc01/index.html)  
[Serial Collector](https://docs.oracle.com/javase/9/gctuning/available-collectors.htm#GUID-45794DA6-AB96-4856-A96D-FDE5F7DEE498)  
[6 The Parallel Collector](https://docs.oracle.com/javase/8/docs/technotes/guides/vm/gctuning/parallel.html)  
[Getting Started with the G1 Garbage Collector](https://www.oracle.com/technetwork/tutorials/tutorials-1876574.html)  
[Garbage-First Garbage Collector](https://docs.oracle.com/javase/9/gctuning/garbage-first-garbage-collector.htm#JSGCT-GUID-ED3AB6D3-FD9B-4447-9EDF-983ED2F7A573)  
[G1: One Garbage Collector To Rule Them All](https://www.infoq.com/articles/G1-One-Garbage-Collector-To-Rule-Them-All/)