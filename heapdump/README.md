### Memory leak

A memory leak occur when memory that has been _allocated_ and is no longer needed doesn't get _released_.  
GC doesn't prevent all memory leak. GC is only allowed to free up memory that's no longer live or no longer referenced by anything. So if you retain references to objects you don't need, for example, by putting them in a field somewhere or maybe a field of a field of a field, then you will retain their memory, and that's the essence of a Java memory leak.

#### Heap dump

Heap dumps are a snapshot in time of the entire JVM heap.

Leak finding process:  
1. Retained heap.  
Find objects that cause the most heap to be retained.  
2. Filter.  
Top object will usually be noise.  
3. Investigate.  
Look at what these objects are referencing or being referenced by.

#### How to play klassified module

1. Run the app with `run.sh`  
2. While the app running, run `generate_load.sh`  
3. While we generate the load, dump the heap with `jcmd <process_id> GC.heap_dump heap_dump.hprof` command.  
4. Open MAT to analyze the dump file. Go to Histogram.
5. To see leak use Java 8 for runtime.

Look at which of those objects cause the most amount of heap memory to be retained.

#### Class loader

Class loader is a mechanism for dynamically loading (loaded during the runtime of an application) Java classes as raw bytecode into a JVM.  
The problem is there are situation where the class may get loaded and not unloaded.  
The JVM has 3 builtin class loader, bootstrap, extension and system. We can also build custom class loader as an extension mechanism, for example loading a plugin or servlet container (load .war).

##### Why class loader can cause memory leak?

If we hold a reference to a class, and we've loaded it with a custom class loader, and then we try and free the class loader, it won't get freed up by the garbage collector because that class will still be holding a reference to it.  
Now this gets worse because each class loader holds references to all its loaded classes, and these also hold references to their static fields. So leaking a reference to a single class from a custom class loader, also leaks the class loader, any other classes loaded, and all their fields, and that even includes the information that is about the bytecode itself that gets loaded.

#### How to play simulateclassloadermemoryleak module

1. Run the app.  
2. Press enter (to reload the plugin).  
3. It takes few times until we get `java.lang.OutOfMemoryError: Java heap space`  
4. Run this app in Java 8.  
5. Before we get the error, dump the heap.  
6. Analyze it with MAT.

#### ThreadLocal

A ThreadLocal is a type of field where each thread has its own independently initialized copy of the variable.  
A static field is associated with a class, an instance field is associated with a surrounding object. ThreadLocals are fields that give you a 1:1 relationship between instance copies and the thread itself.  
ThreadLocals will be cleaned up automatically when the thread exits, but if you reuse that thread for another action, for example, on the server container, then there's a big risk of a leak if you don't actually remove the value.

#### How to play simulatethreadlocalmemoryleak module

See [simulatethreadlocalmemoryleak module how to play](https://github.com/bluething/learnjava/tree/main/heapdump/simulatethreadlocalmemoryleak)

#### Off heap

Heap memory  
1. Managed by the JVM/GC  
2. Arena allocated region  
3. Java Objects allocated here

Off heap memory  
1. Managed manually  
2. Individually allocated buffer  
3. Custom data stores

Type of off heap memory  
1. Native code  
JNI invoked native code  
2. Native buffer  
Off heap buffer allocated by Java code (`ByteBuffer`)  
3. Memory mapped files  
Used for interprocess communications

The problem is off heap storage won't appear as retained heap.  
Tools that we can use to help us  
1. `top -p<pid>`  
Identified memory used per process  
2. `-XX:MaxDirectMemorySize=1g`  
Enforce buffer allocation limit. Stop unbounded growth. We still need to find leaks

VisualVM have _Buffer Monitor_ plugin to monitor direct buffer created by `ByteBuffer`

See [How to play simulateoffheapmemoryleak module](https://github.com/bluething/learnjava/tree/main/heapdump/simulateoffheapmemoryleak)

### java.lang.OutOfMemoryError: heap space

#### What causes Out of Memory Error?

1. Memory leak.  
Memory leak that occurs for a long time will cause memory to run out.
2. Memory overconsumption.  
Using too much memory to perform a given task. We have `-Xmx` to limit maximum memory that can be used.

#### Differences between memory leak and memory consumption

1. Memory leak.  
It grows with activity over time.  
Don't free what allocated.
2. Memory overconsumption.  
It grows with currently active work.  
Simply using too much memory.

Usually we make a mistake when we try to optimize to reduce memory consumption when we actually have memory leak problem. Or vice versa.

#### How to solve

1. Identify what's using your memory.  
2. Reduce memory consumption.  
Allocate less, don't reference too much.  
3. Measure again to validate.

_Measure what's using your memory first before changing any code!_

#### What we can do to reduce memory allocation?

1. Use primitive data types.  
2. Recalculate instead of storing.  
Cache take memory.  Think small, not just memcache, also using fields, small collections.  
3. Simplify domain model.  
Abstraction == overhead, every layer uses memory.   
Complexity === overhead, is our domain complex?  
Pragmatism, refactor when it helps your code.  
4. Increase available memory.  
Configured max heap and have enough ram for the jvm 
5. Don't hold objects in memory that you don't need.

#### Prevention is better than cure, how?

Remember, premature optimization is the root of all evil. There's no need to optimize things that don't need to be optimized.

##### Monitoring

1. Memory consumption.  
2. GC logs / JVisualVM.  
3. Application performance monitoring.

##### Performance testing

##### Drive system with stress test

##### Find and fix the problems

##### Use 20% time for

1. Research or pet projects.  
2. Periodically use it for performance analysis/improvements.

See [simulatememoryoverconsumption](https://github.com/bluething/learnjava/tree/main/heapdump/simulatememoryoverconsumption)

### Object Allocation Rate Problems

The problem caused by allocating objects too fast. The impact is performance of our application.

#### Latency/responsiveness

Latency is the delay before a response or a request is made. Too much allocation causes long gc pause (add latency).  
Y% request take at most X ms at Z request per second.  
How fast to keep users happy?

#### Throughput/efficiency

Throughput is the number of transactions processed per second in an application.  
How many request per second does my application have to support at peak time?

#### The myth

~~Allocating objects is basically instant and free of costs~~

If we allocate objects frequently enough compared to performing actual business logic, then we'll spend a lot of time in your application process and doing allocation, and not much time doing actual work.  
The cost is cpu cache locality and time spent allocating.

#### Cpu cache locality

CPU evolution faster than memory, memory access slower than cpu cache access. If we allocate lots and lots of new objects into memory, we'll end up washing the cache and reduces its efficiency.  
The cache usually LRU, they throw away the data that's not so useful.

#### Time spent allocating

Allocation in gc systems is very fast, but it's have a cost. Different garbage collectors have different costs.

### GC

#### Most object die young

They're no longer referenced, fairly quickly after they get created.

#### Split memory into generations

Because mostly objects die at young age the rest of them promoted to old generation (if they survive a certain number of garbage collections).  
Younger generations get collected more frequently and generally more efficiently.  
It takes longer to collect all the generations, and these are often collected when we collect the whole of our memory space, called a full GC.  
So, if we allocate a lot of objects (even though most of them die quickly) they will fill the old generation.  
For G1 gc the main problem is if it can't keep up with your allocation rate, it has to kind of fall back and ends up doing a full GC, so a full stop the world pause to sort out the mess.

See [simulatehighallocation](https://github.com/bluething/learnjava/tree/main/heapdump/simulatehighallocation)