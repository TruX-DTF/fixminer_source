For this bug since the value of a `const` needs modification, we removed
the `const` from the definition of `window_size`.
For SOS+ you also need to limit the number of live variables for it to be
able to find the perfect patch.
