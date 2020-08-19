#!/bin/bash


CONTAINER=$1
BUG=$2

docker cp $BUG/compile.sh $CONTAINER:/experiment/
docker cp $BUG/test.sh $CONTAINER:/experiment/
docker cp $BUG/tests-list.txt $CONTAINER:/experiment/
docker cp $BUG/settings.py $CONTAINER:/opt/sosrepair/sosrepair/
