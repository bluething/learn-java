#!/bin/bash
java -jar simulateiocongestion-1.0.0-SNAPSHOT.jar read 5 | ./nid-translator.pl > threaddumpwithdecimalnid.out &

sh dump-threads.sh 10 &

ps aux | grep [j]ava | awk '{print $2}' | xargs strace -o fs-operator -tt -T -ff -p