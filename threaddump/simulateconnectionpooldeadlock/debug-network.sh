#!/bin/bash

java -jar simulateconnectionpooldeadlock-1.0.0-SNAPSHOT.jar | ./nid-translator.pl > thread-dumps-w-decimal-nids.out &

sh dump-threads.sh &

sleep 1 &&
ps aux| grep '[s]imulateconnectionpooldeadlock-1.0.0-SNAPSHOT.jar'| awk '{print $2}'| xargs strace -o simulateconnectionpooldeadlock -ff -e trace=network -tt -T -p
