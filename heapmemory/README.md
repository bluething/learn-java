The rule said "create objects sparingly and discard them as quickly as possible"  
But, frequently re-creating some kinds of objects can lead to worse overall performance (even if GC performance improves).

## Heap analysis

Our tools operate only on live objects in the heap.  
Objects that will be reclaimed during the next full GC cycle are not included in the tools output.  
In some cases there are performance impact because teh tool perform full GC.

### Heap Histograms

Histogram output contain the number of objects within an application without doing a full heap dump (since heap dumps can take a while to analyze, and they consume a large amount of disk space).

Heap histograms can be obtained by using jcmd
```text
jcmd <process_id> GC.class_histogram
```

The output from GC.class_histogram includes only live objects, as the command normally forces a full GC. Use `-all` flag in the command to skip the full GC, though then the histogram contains unreferenced (garbage) objects.

We can use jmap to get same output  
```text
jmap -histo <process_id>
```  
The output from jmap includes objects that are eligible to be collected (dead objects).

To force full GC  
```text
jmap -histo:live <process_id>
```

### Heap Dumps

Used for deeper analysis.

Using jcmd  
```text
jcmd <process_id> GC.heap_dump heap_dump.hprof
```  
By default jcmp force a full GC. If for some reason we want other (dead) objects included, use -all at the end of the jcmd command line.

Using jmap  
```text
jmap -dump:live,file=heap_dump.hprof <process_id>
```  
Including the live option in jmap will force a full GC to occur before the heap is dumped.

Using full GC will obviously introduce a long pause into the application, but even if we don’t force a full GC, the application will be paused for the time it takes to write the heap dump.

From documentation _it is recommended to use the latest utility, `jcmd` instead of `jmap` utility for enhanced diagnostics and reduced performance overhead_

Other tools  
1. jvisualvm  
2. mat

#### Analysis process

The first-pass analysis of a heap is recognized retained memory.  
The retained memory of an object is the amount of memory that would be freed if the object itself were eligible to be collected.

Two other useful terms for memory analysis are shallow and deep.  
- The shallow size of an object is the size of the object itself.  
- The deep size of an object includes the size of the object it references.

Objects that retain a large amount of heap space are often called the _dominators_ of the heap. 
What we need to do if we found dominators in our dump file?  
- Try to reduce the number of them.  
- Retain them for a shorter period of time.  
- Simplify their object graph.  
- Make them smaller.

Looking at the objects that directly retain the largest amount of memory isn’t going to solve the memory issues. This is because of shared object.

The next step is use histogram, the histogram aggregates objects of the same type.

Heap analysis tools provide a way to find the GC roots of a particular object (or set of objects in this case). The GC roots are the system objects that hold a static, global reference that (through a long chain of other objects) refers to the object in question.  
The references here are a tree structure in reverse.  
Find the lowest point in the object graph where the target object is shared. This is done by examining the objects and their incoming references and tracing those incoming references until the duplicate path is identified.