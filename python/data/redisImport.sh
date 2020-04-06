#!/bin/bash

file=$1
port=$2
pairName=$3
awk -F, '{ print "SADD pairs", "\"'$pairName'_"$1"_"$2"\"" }' "$file" | sed s/$/$'\r'/ | redis-cli -p $port --pipe #--pipe-timeout 0
