#include <linux/module.h>
#include <linux/init.h>

#define MAX6639_FAN_CONFIG3_THERM_FULL_SPEED
static const string rpm_ranges[] = { 1000, 4000, 8000, 16000 };
int(*X)(int, float);