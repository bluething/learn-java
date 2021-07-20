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

