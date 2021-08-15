### Memory leak

A memory leak occur when memory that has been _allocated_ and is no longer needed doesn't get _released_.  
GC doesn't prevent all memory leak. GC is only allowed to free up memory that's no longer live or no longer referenced by anything. So if you retain references to objects you don't need, for example, by putting them in a field somewhere or maybe a field of a field of a field, then you will retain their memory, and that's the essence of a Java memory leak.

### Heap dump

Heap dumps are a snapshot in time of the entire JVM heap.

Leak finding process:  
1. Retained heap.  
Find objects that cause the most heap to be retained.  
2. Filter.  
Top object will usually be noise.  
3. Investigate.  
Look at what these objects are referencing or being referenced by.

### How to play klassified module

1. Run the app with `run.sh`  
2. While the app running, run `generate_load.sh`  
3. While we generate the load, dump the heap with `jcmd <process_id> GC.heap_dump heap_dump.hprof` command.  
4. Open MAT to analyze the dump file. Go to Histogram.
5. To see leak use Java 8 for runtime.

Look at which of those objects cause the most amount of heap memory to be retained.

### Class loader

Class loader is a mechanism for dynamically loading (loaded during the runtime of an application) Java classes as raw bytecode into a JVM.  
The problem is there are situation where the class may get loaded and not unloaded.  
The JVM has 3 builtin class loader, bootstrap, extension and system. We can also build custom class loader as an extension mechanism, for example loading a plugin or servlet container (load .war).

#### Why class loader can cause memory leak?

If we hold a reference to a class, and we've loaded it with a custom class loader, and then we try and free the class loader, it won't get freed up by the garbage collector because that class will still be holding a reference to it.  
Now this gets worse because each class loader holds references to all its loaded classes, and these also hold references to their static fields. So leaking a reference to a single class from a custom class loader, also leaks the class loader, any other classes loaded, and all their fields, and that even includes the information that is about the bytecode itself that gets loaded.

### How to play simulateclassloadermemoryleak module

1. Run the app.  
2. Press enter (to reload the plugin).  
3. It takes few times until we get `java.lang.OutOfMemoryError: Java heap space`  
4. Run this app in Java 8.  
5. Before we get the error, dump the heap.  
6. Analyze it with MAT.

### ThreadLocal

A ThreadLocal is a type of field where each thread has its own independently initialized copy of the variable.  
A static field is associated with a class, an instance field is associated with a surrounding object. ThreadLocals are fields that give you a 1:1 relationship between instance copies and the thread itself.  
ThreadLocals will be cleaned up automatically when the thread exits, but if you reuse that thread for another action, for example, on the server container, then there's a big risk of a leak if you don't actually remove the value.

### How to play simulatethreadlocalmemoryleak module

See [simulatethreadlocalmemoryleak module how to play](https://github.com/bluething/learnjava/tree/main/heapdump/simulatethreadlocalmemoryleak)