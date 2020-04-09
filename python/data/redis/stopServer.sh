#!/bin/bash

#source activate redisEnv
redis-cli -p $1 shutdown save
