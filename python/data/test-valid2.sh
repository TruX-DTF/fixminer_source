#!/bin/bash
EXEFILE=$3
ANGELIXPRE=$ANGELIX_RUN
INPUT_NAME=$1
OUTPUT_NAME=$2
MY_TIMEOUT=$4
MY_NAME=my_output
# rm -R $MY_NAME* &>/dev/null
run_test()
{
test_case="$1"

echo $MY_TIMEOUT
if ! `which gtime` -o $MY_TIMEOUT -f "(%es)" gtimeout -k 50s 50s $ANGELIXPRE $EXEFILE < $test_case | sed -e '/^$/d' -e 's/^[ \t]*//' > $test_case$MY_NAME; then
echo Sample test \#$test_case: Runtime Error`cat $MY_TIMEOUT`
echo ========================================
echo Sample Input \#$test_case
        cat $test_case
exit 2
else
if grep "Command" $MY_TIMEOUT; then 
 echo "ERROR";
 exit -1;
fi
	if diff --brief -w $test_case$MY_NAME $2; then
echo Sample test \#$test_case: Accepted`cat $MY_TIMEOUT`
exit 0
	else
echo Sample test \#$test_case: Wrong Answer`cat $MY_TIMEOUT`
echo ========================================
 echo Sample Input \#$test_case
 cat $test_case
echo ========================================
echo Sample Output \#$2
cat $2
echo ========================================
echo My Output \#$test_case$MY_NAME
cat $test_case$MY_NAME
echo ========================================
exit 1
fi
    fi
}

run_test "$INPUT_NAME" "$OUTPUT_NAME" ;

esac
exit 1

