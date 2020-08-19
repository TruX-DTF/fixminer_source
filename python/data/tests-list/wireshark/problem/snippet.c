#include <klee/klee.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdarg.h>

char console[20];

int printf ( const char * format, ... ){
    va_list args;
    va_start (args, format);
    vsnprintf (console, 20, format, args);
    va_end (args);
    return 0;
}

int fprintf ( FILE * stream, const char * format, ... ){
    va_list args;
    va_start (args, format);
    if (stream == stdout)
        vsnprintf (console, 20, format, args);
    else
        vfprintf (stream, format, args);
    va_end (args);
    return 0;
}

#define break
#include "/usr/include/stdio.h"
#include "/usr/include/stdlib.h"
#include "/usr/include/string.h"
#include "/usr/include/errno.h"
#include "/usr/include/glib-2.0/glib.h"
#include "/experiment/src/epan/packet.h"
#include "/experiment/src/epan/filesystem.h"
#include "/experiment/src/epan/plugins.h"
#include "/experiment/src/epan/report_err.h"
#include "/experiment/src/wsutil/privileges.h"
#include "/experiment/src/wsutil/wsgetopt.h"
#include "/experiment/src/svnversion.h"
typedef struct _capture_info {
  const char    *filename;
  guint16       file_type;
  int           file_encap;
  gint64        filesize;
  guint64       packet_bytes;
  double        start_time;
  double        stop_time;
  guint32       packet_count;
  gboolean      snap_set;                /* If set in capture file header      */
  guint32       snaplen;                 /* value from the capture file header */
  guint32       snaplen_min_inferred;    /* If caplen < len for 1 or more rcds */
  guint32       snaplen_max_inferred;    /*  ...                               */
  gboolean      drops_known;
  guint32       drop_count;
  double        duration;
  double        packet_rate;
  double        packet_size;
  double        data_rate;              /* in bytes */
  gboolean      in_order;
} capture_info;
void foo(gboolean cap_file_size, capture_info* cf_info, gboolean cap_packet_count, gboolean cap_data_size){
if (cap_packet_count)   printf     ("Number of packets:   %u\n", cf_info->packet_count);
  if (cap_file_size)      printf     ("File size:           %" G_GINT64_MODIFIER "d bytes\n", cf_info->filesize);
  if (cap_data_size)      printf     ("Data size:           %" G_GINT64_MODIFIER "u bytes\n", cf_info->packet_bytes);
            }

            int main(){
            gboolean cap_file_size;
klee_make_symbolic(&cap_file_size, sizeof(cap_file_size), "cap_file_size");
capture_info* cf_info = malloc( sizeof(capture_info ));
klee_make_symbolic(cf_info, sizeof(capture_info), "cf_info");
gboolean cap_packet_count;
klee_make_symbolic(&cap_packet_count, sizeof(cap_packet_count), "cap_packet_count");
gboolean cap_data_size;
klee_make_symbolic(&cap_data_size, sizeof(cap_data_size), "cap_data_size");
char console_out[20];
klee_make_symbolic(&console_out, sizeof(console_out), "console");
foo(cap_file_size, cf_info, cap_packet_count, cap_data_size);

        klee_assume(strcmp(console_out, console) == 0);
        return 0;
        }
        