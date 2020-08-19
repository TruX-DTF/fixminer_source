## Running `wireshark` bugs

After running the container you need to follow the following
steps to prepare `SOSRepair` for running:

1. Copy `makeout`, `compile.sh`, `test.sh` and `tests-list` to
the container's `/experiment/`.
2. Copy `settings.py` to the container's `/opt/sosrepair/sosrepair`.
3. Unfortunately `wireshark` on `ManyBugs` dataset is preprocessed. So we need to 
download it from git repository:
```
cd /experiment
rm -r src
git clone https://github.com/wireshark/wireshark.git src
cd src
./autogen.sh
```
4. In the container, reconfigure the project with coverage flags:
```
cd /experiment/src
./configure --disable-warnings-as-errors "CFLAGS=-fprofile-arcs -ftest-coverage" "CXXFLAGS=-fprofile-arcs -ftest-coverage" "LDFLAGS=-lgcov --coverage"
make clean
make
```
5. Run `/opt/sosrepair/prepare/setup.sh`.
6. Set proper permissions by running `sudo chmod -R 777 /opt/sosrepair/sosrepair`.
7. Setup environment variables:
```
export PYTHONPATH="/opt/sosrepair/bindings:${PYTHONPATH}"
export CPATH=":/opt/sosrepair/include"
export PATH="/opt/sosrepair/bin:$PATH"
```
8. You need to change permission of the test script:
```
chmod +x /experiment/wireshark-run-tests.sh
```
