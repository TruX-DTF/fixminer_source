#!/bin/bash
TEST_ID=$1
DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )


run_test()
{
    cd $DIR/src
        timeout 5 /usr/bin/perl $DIR/gzip-run-tests.pl $1
    RESULT=$?
    if [ $RESULT = 0 ] ; then
	echo "PASS"
    else
	echo "FAIL"
    fi
    cd ..
    return 0
}
case $TEST_ID in
    p1) run_test 1 && exit 0 ;; 
    p2) run_test 3 && exit 0 ;; 
    p3) run_test 4 && exit 0 ;; 
    n1) run_test 7 && exit 0 ;; 
esac
exit 1
