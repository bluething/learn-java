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

### How to play

1. Run the app with `run.sh`  
2. While the app running, run `generate_load.sh`  
3. While we generate the load, dump the heap with `jcmd <process_id> GC.heap_dump heap_dump.hprof` command.  
4. Open MAT to analyze the dump file. Go to Histogram.

Look at which of those objects cause the most amount of heap memory to be retained.