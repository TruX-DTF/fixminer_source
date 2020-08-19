## Running `libtiff` bugs

After running the container you need to follow the following
steps to prepare `SOSRepair` for running:

1. Copy `makeout`, `compile.sh`, `test.sh` and `tests-list` to
the container's `/experiment/`.
2. Copy `settings.py` to the container's `/opt/sosrepair/sosrepair`.
3. In the container, reconfigure the project with coverage flags:
```
cd /experiment/src
./configure "CFLAGS=-fprofile-arcs -ftest-coverage" "CXXFLAGS=-fprofile-arcs -ftest-coverage" "LDFLAGS=-lgcov"
make clean
make
```
4. Run `/opt/sosrepair/prepare/setup.sh`.
5. Set proper permissions by running `sudo chmod -R 777 /opt/sosrepair/sosrepair`.
6. Setup environment variables:
```
export PYTHONPATH="/opt/sosrepair/bindings:${PYTHONPATH}"
export CPATH=":/opt/sosrepair/include"
export PATH="/opt/sosrepair/bin:$PATH"
```
