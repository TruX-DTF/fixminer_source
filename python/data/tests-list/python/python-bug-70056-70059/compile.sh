#!/bin/bash
DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )

cd $DIR/src
#make clean
make &> /tmp/make.out
if [ $? = 0 ] ; then
    sed -n "/Failed to build these modules:/{n;p}" /tmp/make.out | grep "_io"
    if [ $? = 0 ] ; then
        echo "error"
        exit 1
    else
        exit 0
    fi
else
    exit 1
fi 
