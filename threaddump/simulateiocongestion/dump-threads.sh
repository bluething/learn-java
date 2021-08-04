#!/bin/bash
a=1
numberOfKills=10
pid=$(ps aux| grep [j]ava| awk '{print $2}')

echo "Sending kill -3 at PID:" $pid

while [ $a -le $numberOfKills ]; do
         kill -3 $pid
         sleep 1
         a=`expr $a + 1`
done
