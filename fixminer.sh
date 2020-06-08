#!/bin/bash


source activate fixminerEnv

PYTHONPATH=$(pwd) python -u python/main.py -root $(pwd)/python -job $2 -prop $1
