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