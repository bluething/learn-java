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

Less efficient than Copying algorithm because there are more changes to the memory and needs to sort out the reference addresses of all living objects.

##### GC pause time

The Java program expects to find an object at a particular address. If the garbage collector moves the object, the Java program needs to know the new location.  
The easiest way to do this is to stop all the Java threads, compact all the objects, update all the references to the old addresses to now point to the new addresses, and resume the Java program.  
However, this approach can lead to long periods (called GC pause times) when the Java threads aren't running.

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

Unless the initial and maximum heap sizes are specified on the command line, they're calculated based on the amount of memory on the machine.  
The default maximum heap size is 1/4 of the physical memory while the initial heap size is 1/64th of physical memory.  
The maximum amount of space allocated to the young generation is one third of the total heap size.

The downside to the parallel collector is that it will stop application threads when performing either a minor or full GC collection.  
The parallel collector is best suited for apps that can tolerate application pauses and are trying to optimize for lower CPU overhead caused by the collector, for example batch applications.

#### Concurrent Mark Sweep (CMS) Garbage Collector

This algorithm uses multiple threads ("concurrent") to scan through the heap ("mark") for unused objects that can be recycled ("sweep").  
This algorithm will enter "stop the world" (STW) mode in two cases:  
1. When initializing the initial marking of roots (objects in the old generation that are reachable from thread entry points or static variables).  
2. When the application has changed the state of the heap while the algorithm was running concurrently, forcing it to go back and do some final touches to make sure it has the right objects marked.

The biggest concern when using this collector is  
1. Encountering _promotion failures_ which are instances where a race condition occurs between collecting the young and old generations.  
2. It uses more CPU in order to provide the application with higher levels of continuous throughput, by using multiple threads to perform scanning and collection.

Use `XX:+USeParNewGC` to enable this gc.

#### Garbage-First (G1) Garbage Collector

G1 is a generational, incremental, parallel, mostly concurrent, stop-the-world, and evacuating garbage collector which monitors pause-time goals in each of the stop-the-world pauses.  
Space-reclamation efforts concentrate on the young generation where it is most efficient to do so, with occasional space-reclamation.  
G1 reclaims space mostly by using evacuation: live objects found within selected memory areas to collect are copied into new memory areas, compacting them in the process. After an evacuation has been completed, the space previously occupied by live objects is reused for allocation by the application.

Some operations are always performed in stop-the-world pauses to improve throughput. Other operations that would take more time with the application stopped such as whole-heap operations like global marking are performed in parallel and concurrently with the application.  
To keep stop-the-world pauses short for space-reclamation, G1 performs space-reclamation incrementally in steps and in parallel.  
G1 achieves `predictability` by tracking information about previous application behavior and garbage collection pauses to build a model of the associated costs. It uses this information to size the work done in the pauses.

![g1 heap layout](https://github.com/bluething/learnjava/blob/main/images/g1heap.PNG?raw=true)  
In G1 GC, HotSpot introduces the concept of "regions". A single large contiguous Java heap space divides into multiple fixed-sized heap regions. Neither the young nor the old generation has to be contiguous.   
A list of "free" regions maintains these regions. As the need arises, the free regions are assigned to either the young or the old generation.  
These regions can span from 1MB to 32MB in size depending on your total Java heap size. Adjust with `-XX:G1RegionSize`.  
The goal is to have around 2048 regions for the total heap.  
Once a region frees up, it goes back to the "free" regions list. The principle of G1 GC is to reclaim the Java heap as much as possible (while trying its best to meet the pause time goal) by collecting the regions with the least amount of live data i.e. the ones with most garbage, first; hence the name Garbage First.

the G1 GC algorithm does utilize some of HotSpot’s basic concepts. For example, the concepts of allocation, copying to survivor space and promotion to old generation are similar to previous HotSpot GC implementations.  
Eden regions and survivor regions still make up the young generation. Most allocations happen in eden except for “humongous” allocations. (Note: For G1 GC, objects that span more than half a region size are considered “Humongous objects” and are directly allocated into “humongous” regions out of the old generation.)  
G1 GC selects an adaptive young generation size based on your pause time goal. The young generation can range anywhere from the preset min to the preset max sizes, that are a function of the Java heap size. When eden reaches capacity, a “young garbage collection”, also known as an “evacuation pause”, will occur. This is a STW pause that copies (evacuates) the live objects from the regions that make up the eden, to the 'to-space' survivor regions.

Pay attention if we have large enough object in our app. The humongous objects never move, not even during a Full GC. This can cause premature slow Full GCs or unexpected out-of-memory conditions with lots of free space left due to fragmentation of the region space.

G1 collector step by step  
1. Heap allocation (see image above).  
2. Live objects are evacuated (i.e., copied or moved) to one or more survivor regions.  
   - If the aging threshold is met, some of the objects are promoted to old generation regions.  
   - This is a stop the world (STW) pause.  
   - Eden size and survivor size is calculated for the next young GC.  
   - Accounting information is kept to help calculate the size.  
   - Things like the pause time goal are taken into consideration.
3. Live objects have been evacuated to survivor regions or to old generation regions.

G1 collection phases on old generation

Step | Phase | Description
---- | ----- | ----------- 
1 | Initial Mark (STW) | Mark survivor regions (root regions) which may have references to objects in old generation.
2 | Root Region Scanning | Scan survivor regions for references into the old generation. This happens while the application continues to run. The phase must be completed before a young GC can occur.
3 | Concurrent Marking | Find live objects over the entire heap. This happens while the application is running. This phase can be interrupted by young generation garbage collections.
4 | Remark (STW) | Completes the marking of live object in the heap. Uses an algorithm called snapshot-at-the-beginning (SATB)
5 | Cleanup (STW and concurrent) | Performs accounting on live objects and completely free regions (STW). Scrubs the Remembered Sets (STW). Reset the empty regions and return them to the free list (STW)
\ | Copying (STW) | Evacuate or copy live objects to new unused regions.

G1 footprint have larger JVM process size than Parallel or CMS. This is largely related to "accounting" data structures such as _Remembered Sets_ and _Collection Sets_.

Use cases for G1 is when we have requirement of large heaps with limited GC latency.

When we must move from Parallel or CMS to G1?  
- Full GC durations are too long or too frequent.  
- The rate of object allocation rate or promotion varies significantly.  
- Undesired long garbage collection or compaction pauses (longer than 0.5 to 1 second)

#### Z Garbage Collector

#### Shenandoah Garbage Collector

### Consideration when choosing GC

1. Memory.  
The amount of memory that is assigned to the program and this is called HEAP memory. Please don't confuse with footprint (amount of memory required by GC algorithm to run)  
2. Throughput.  
For example, if your throughput is 99% that means 99% of the time the code was running and 1% of the time the Garbage collection was running.  
3. Latency.  
Latency is whenever the Garbage collection runs, how much amount of time our program stops for the Garbage collection to run properly.

### Reference

[How Does Garbage Collection Work in Java?](https://www.alibabacloud.com/blog/how-does-garbage-collection-work-in-java_595387)  
[Java Garbage Collection Basics](https://www.oracle.com/webfolder/technetwork/tutorials/obe/java/gc01/index.html)  
[Garbage Collectors – Serial vs. Parallel vs. CMS vs. G1 (and what’s new in Java 8)](https://www.overops.com/blog/garbage-collectors-serial-vs-parallel-vs-cms-vs-the-g1-and-whats-new-in-java-8/)  
[Serial Collector](https://docs.oracle.com/javase/9/gctuning/available-collectors.htm#GUID-45794DA6-AB96-4856-A96D-FDE5F7DEE498)  
[6 The Parallel Collector](https://docs.oracle.com/javase/8/docs/technotes/guides/vm/gctuning/parallel.html)  
[8 Concurrent Mark Sweep (CMS) Collector](https://docs.oracle.com/javase/8/docs/technotes/guides/vm/gctuning/cms.html)  
[Getting Started with the G1 Garbage Collector](https://www.oracle.com/technetwork/tutorials/tutorials-1876574.html)  
[Garbage-First Garbage Collector](https://docs.oracle.com/javase/9/gctuning/garbage-first-garbage-collector.htm#JSGCT-GUID-ED3AB6D3-FD9B-4447-9EDF-983ED2F7A573)  
[G1: One Garbage Collector To Rule Them All](https://www.infoq.com/articles/G1-One-Garbage-Collector-To-Rule-Them-All/)  
[humongous-allocations](https://plumbr.io/handbook/gc-tuning-in-practice/other-examples/humongous-allocations)