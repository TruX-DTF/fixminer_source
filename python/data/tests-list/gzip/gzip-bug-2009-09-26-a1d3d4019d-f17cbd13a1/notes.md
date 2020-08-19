For running SOS+ on this bug, we manually helped in finding
correct set of live variables. Variable `ifd` is global variable
therefore not considered by SOS+ as a live variable.
We manually changed code in `fault_localization/suspicious_block.py`
to include `ifd` as a variable.
