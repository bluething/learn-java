### How to play

#### Build image

`sudo DOCKER_BUILDKIT=1 docker build -t simulateiocognestion .`

#### Run the container

##### Without rate limit

`sudo docker run -it simulateiocognestion bash`

##### With rate limit

Read limiter  
`docker run -d --cap-add SYS_PTRACE --security-opt seccomp:unconfined --name slow-reader -it --device-read-bps /dev/nvme0n1:150kb simulateiocognestion`  
Write limiter  
`docker run -d --cap-add SYS_PTRACE --security-opt seccomp:unconfined --name slow-writer -it --device-write-bps /dev/nvme0n1:150kb simulateiocognestion`

`/dev/nvme0n1` is our device, find it by using `sudo parted -l to list all drives` command

Enter the bash shell using  
`docker exec -it slow-reader /bin/bash`

#### Run the app

`java -jar simulateiocongestion-1.0.0-SNAPSHOT.jar read 1 > threaddump.out`  
We print the output into file to make analysis easier. 

#### Troubleshooting

While the app running, in other terminal (inside the container) run this command to dump the thread  
``a=1; while [ $a -le 10 ]; do pid=$(ps aux | grep '[j]ava' | awk '{print $2}') && kill -3 $pid && sleep 1 && a=`expr $a + 1`; done``

While the app running, in other terminal (inside the container) attach to the JVM process with `strace` command.  
`ps aux | grep [j]ava | awk '{print $2}' | xargs strace -o fs-operator -tt -T -ff -p`

Our goal is to link between `nid` in thread dump and os thread id (in strace output).

Inside `threaddump.out` file, find the worker thread. Because we use ExecutorService, find something `like pool-x-thread-x` while `x` is an integer. The value in hexadecimal format, convert it into decimal.  
```text
"pool-1-thread-1" #12 prio=5 os_prio=0 cpu=72.57ms elapsed=1.40s tid=0x00007f8148368800 nid=0x6b20 runnable  [0x00007f81243a6000]
   java.lang.Thread.State: RUNNABLE
	at net.smacke.jaydio.DirectIoLib.pread(Native Method)
```

There are many `fs-operator` files, suffix by integer, this is the `nid` from thread dump. Open the file with `nid` we interested.  
Our thread call `pread` native method. Find this in the file.  
```text
10:04:05.197325 pread64(7, "oobxyizrxnddaikxwwwznxhiscfhktsi"..., 1048576, 1048576) = 1048576 <6.822158>
```
As we can see, this line tell us the process took almost 7s to finish. I can assume this process is I/O bound.

##### The debugging chain

Run dump-fd.sh for shortcut debugging tool.

#### Watch out of I/O weight

Find with top command  
`top -d 0.1 -H`  
This number resembles roughly how much of the current executing tasks are currently I/O bound. The number actually represents the percentage of the time processes are waiting for I/O out of the total execution time in a given sampling period.

Play with 1 and 4 worker, this value increase significantly.  
![ioweight1worker](https://github.com/bluething/learnjava/blob/main/images/ioweight1worker.png?raw=true)  
![ioweight4worker](https://github.com/bluething/learnjava/blob/main/images/ioweight4worker.png?raw=true)  
If we look at the CPU column on the list of processes, it shows that none of the currently running threads are actually CPU bound (except top itself). So the CPU isn't doing any hard work.