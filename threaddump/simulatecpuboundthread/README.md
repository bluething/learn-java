### How to play

The code created by [Uriah Levy](https://github.com/uriahl) for [Analyzing Java Thread Dumps](https://app.pluralsight.com/courses/ebb11bd1-c104-4be4-9cd7-5c19be357a66/table-of-contents) course at Pluralsight.

Run `debug-cpu.sh` then `top -d 0.1 -H -p $pid` ($pid value got from first command output).  
At top command output we can see the process (with id 21301) consume almost 100% cpu but low memory usage and this is constant all the time.  
![cpu bound](https://github.com/bluething/learnjava/blob/main/images/cpubound.png?raw=true)

Using this id (lwp in thread stack), find the thread stack  
```java
"main" #1 prio=5 os_prio=0 cpu=5011,17ms elapsed=5,02s tid=0x00007f309c028800 lwp=21301 nid=0x5335 runnable  [0x00007f30a3565000]
   java.lang.Thread.State: RUNNABLE
	at java.util.regex.Pattern$Curly.match1(java.base@14.0.1/Pattern.java:4450)
	at java.util.regex.Pattern$Curly.match(java.base@14.0.1/Pattern.java:4386)
	at java.util.regex.Pattern$GroupHead.match(java.base@14.0.1/Pattern.java:4809)
	at java.util.regex.Pattern$Loop.match(java.base@14.0.1/Pattern.java:4897)
	at java.util.regex.Pattern$GroupTail.match(java.base@14.0.1/Pattern.java:4840)
	at java.util.regex.Pattern$BmpCharProperty.match(java.base@14.0.1/Pattern.java:3974)
	at java.util.regex.Pattern$Curly.match1(java.base@14.0.1/Pattern.java:4437)
	at java.util.regex.Pattern$Curly.match(java.base@14.0.1/Pattern.java:4386)
	at java.util.regex.Pattern$GroupHead.match(java.base@14.0.1/Pattern.java:4809)
	at java.util.regex.Pattern$Loop.match(java.base@14.0.1/Pattern.java:4897)
	at java.util.regex.Pattern$GroupTail.match(java.base@14.0.1/Pattern.java:4840)
	at java.util.regex.Pattern$BmpCharProperty.match(java.base@14.0.1/Pattern.java:3974)
	at java.util.regex.Pattern$Curly.match1(java.base@14.0.1/Pattern.java:4437)
	at java.util.regex.Pattern$Curly.match(java.base@14.0.1/Pattern.java:4386)
	at java.util.regex.Pattern$GroupHead.match(java.base@14.0.1/Pattern.java:4809)
	at java.util.regex.Pattern$Loop.match(java.base@14.0.1/Pattern.java:4897)
	at java.util.regex.Pattern$GroupTail.match(java.base@14.0.1/Pattern.java:4840)
	at java.util.regex.Pattern$BmpCharProperty.match(java.base@14.0.1/Pattern.java:3974)
	at java.util.regex.Pattern$Curly.match1(java.base@14.0.1/Pattern.java:4437)
	at java.util.regex.Pattern$Curly.match(java.base@14.0.1/Pattern.java:4386)
	at java.util.regex.Pattern$GroupHead.match(java.base@14.0.1/Pattern.java:4809)
	at java.util.regex.Pattern$Loop.matchInit(java.base@14.0.1/Pattern.java:4940)
	at java.util.regex.Pattern$Prolog.match(java.base@14.0.1/Pattern.java:4864)
	at java.util.regex.Pattern$Begin.match(java.base@14.0.1/Pattern.java:3691)
	at java.util.regex.Matcher.match(java.base@14.0.1/Matcher.java:1756)
	at java.util.regex.Matcher.matches(java.base@14.0.1/Matcher.java:713)
	at java.util.regex.Pattern.matches(java.base@14.0.1/Pattern.java:1177)
	at java.lang.String.matches(java.base@14.0.1/String.java:2039)
	at io.github.bluething.java.threaddump.simulatecpuboundthread.CatastrophicBacktrackingRegex.main(CatastrophicBacktrackingRegex.java:21)
```  
From the stack we can see that the app doing recursive, there are redundant stack frames.  
In some condition this can cause StackOverflow exception.  
To simulate SO exception, use this command `java -jar simulatecpuboundthread-1.0.0-SNAPSHOT.jar 100000 200`