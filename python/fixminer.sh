#!/bin/bash


source activate redisEnv

PYTHONPATH=$(pwd) python -u main.py -root $(pwd) -job $1
