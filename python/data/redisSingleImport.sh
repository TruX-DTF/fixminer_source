#!/bin/bash

file=$1
port=$2
pairName=$3
awk -F, '{ print "SADD", "compare","\"'$pairName'/"$1"/"$2"\"",0 }' "$file" | sed s/$/$'\r'/ | redis-cli -p $port --pipe #--pipe-timeout 0
