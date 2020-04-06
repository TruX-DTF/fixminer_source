static int test(){
    field = ATOM(TST);
    if(IS_ERR(fields->mode))
        return PTR_ERROR(fields->mode);
    return 0;
}