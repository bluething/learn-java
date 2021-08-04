#!/bin/bash

java -jar fs-operator.jar read 1 | /home/nid-translator.pl > thread-dumps-w-decimal-nids.out &

sh /home/dump-threads.sh &

ps aux| grep [j]ava| awk '{print $2}'| xargs strace -o fs-operator -tt -T -ff -p
