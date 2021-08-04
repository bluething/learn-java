#!/bin/bash

java -jar simulatecpuboundthread-1.0.0-SNAPSHOT.jar 100000 12 | nid-translator.pl > thread-dumps-w-decimal-nids.out &

sh dump-threads.sh

