#!/bin/bash

java -jar bearer-authenticator.jar | /home/nid-translator.pl > thread-dumps-w-decimal-nids.out &

sh /home/dump-threads.sh &

sleep 1 &&
ps aux| grep [j]ava| awk '{print $2}'| xargs strace -o bearer-authenticator -ff -e trace=network -tt -T -p
