#include <linux/module.h>
#include <linux/init.h>

#define MAX6639_FAN_CONFIG3_THERM_FULL_SPEED
static const int rpm_ranges[] = { 2000, 4000, 8000, 16000 };
int(*X)(int, float);