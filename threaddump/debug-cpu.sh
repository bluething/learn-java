#!/bin/bash

java -jar catastrophic-backtracker.jar 100000 12 | /home/nid-translator.pl > thread-dumps-w-decimal-nids.out &

sh /home/dump-threads.sh

