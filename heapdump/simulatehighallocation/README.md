### How to play

We need:  
1. Apache benchmark.  
2. [Java mission control](https://github.com/openjdk/jmc)

Run the app `HighAllocationStatisticsService`  
Run the `bench.sh`

Output of apache benchmark is like this  
```text
This is ApacheBench, Version 2.3 <$Revision: 1843412 $>
Copyright 1996 Adam Twiss, Zeus Technology Ltd, http://www.zeustech.net/
Licensed to The Apache Software Foundation, http://www.apache.org/

Benchmarking localhost (be patient).....done


Server Software:        Jetty(9.2.13.v20150730)
Server Hostname:        localhost
Server Port:            9000

Document Path:          /
Document Length:        1536 bytes

Concurrency Level:      1
Time taken for tests:   46.730 seconds
Complete requests:      50
Failed requests:        0
Total transferred:      83650 bytes
Total body sent:        2233474500
HTML transferred:       76800 bytes
Requests per second:    1.07 [#/sec] (mean)
Time per request:       934.601 [ms] (mean)
Time per request:       934.601 [ms] (mean, across all concurrent requests)
Transfer rate:          1.75 [Kbytes/sec] received
                        46675.03 kb/s sent
                        46676.77 kb/s total

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    0   0.0      0       0
Processing:   897  935  19.1    937     975
Waiting:       91  109  10.0    106     128
Total:        897  935  19.1    937     975

Percentage of the requests served within a certain time (ms)
  50%    937
  66%    944
  75%    947
  80%    951
  90%    959
  95%    968
  98%    975
  99%    975
 100%    975 (longest request)
```  
We can see response time per percentage of the request at the bottom.

Open java mission control and start flight recorder with event setting is Memory Allocation (turn on only memory allocation).  
Run the `bench.sh`

We can see the result like this  
![large array char allocation](https://github.com/bluething/learnjava/blob/main/images/arraycharlargeallocation.png?raw=true)  
From the stacktrace we can see this abjects allocation related to csv library that we use.  
The option is we change the library and see the memory allocation again, if smaller we change the lib.

The 2nd biggest allocation is  
![large double allocation](https://github.com/bluething/learnjava/blob/main/images/arraydoublelargeallocation.png?raw=true)  
From the stacktrace we have a clue about the method that we must inspect.  
In `HighAllocationStatisticsService.doPost` method there are primitive double (parse from String) as a value for hash map (auto boxing here).  
We use the map in other map iteration. Get the value then add value from other map, put it back to other map. There is the problem.  
Optimize code at `LowAllocationStatisticsService` class. Instead of put the double value into map, we calculate on the fly.
![optimized large double allocation](https://github.com/bluething/learnjava/blob/main/images/optimizedlargeallocation.png?raw=true)