### How to play

Run the app with Java 8. Make sure to comment line 20 at `LeakingAction`.  
In `ThreadLocalLeakRunner` we use thread pool to execute the runnable object. It will run the threads in the background, and it'll only close when the thread pool is shut down (we don't do that).  
We can confirm it with the image below.  
![thread pool thread](https://github.com/bluething/learnjava/blob/main/images/threadpoolthread.png?raw=true)  
When we see the heap, we find something hold the memory.  
![memory hold by somethind](https://github.com/bluething/learnjava/blob/main/images/memoryholdbysomething.png?raw=true)  
And we can find who is hold the reference from heap dump file.  
![char reference by thread local](https://github.com/bluething/learnjava/blob/main/images/chararrayreferencebythreadlocal.png?raw=true)

How we solve this issue?  
Remove the thread local (open the comment at line 20 at `LeakingAction`)