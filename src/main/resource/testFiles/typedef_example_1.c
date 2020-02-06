#include <linux/module.h>
#include <linux/init.h>
#include <linux/slab.h>
#include <linux/jiffies.h>
#include <linux/i2c.h>
typedef struct club
{
    char name[30];
    int size, year;
} GROUP;
