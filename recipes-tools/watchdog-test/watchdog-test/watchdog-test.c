/*
 * Watchdog Test - A small tool for watchdog verification. 
 *
 * Copyright (C) 2017-2019 Kontron Europe
 *
 * Author: Michael Brunner <michael.brunner@kontron.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, distribute with modifications, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE ABOVE COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR
 * THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * Except as contained in this notice, the name(s) of the above copyright
 * holders shall not be used in advertising or otherwise to promote the
 * sale, use or other dealings in this Software without prior written
 * authorization.
 */

#include <stdio.h>
#include <unistd.h>
#include <sys/ioctl.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <getopt.h>
#include <string.h>
#include <errno.h>

#include <linux/watchdog.h>

#define DEFAULT_WATCHDOGDEV "/dev/watchdog"

typedef enum {WRITE, IOCTL} t_keepalive;

static const char *const short_options = "xd:t:p:oew:i:c:";
static const struct option long_options[] = {
	{"help",		0,	NULL,	1  },
	{"nomagicclose",	0,	NULL,	'x'},
	{"dev",			1,	NULL,	'd'},
	{"timeout",		1,	NULL,	't'},
	{"pretimeout",		1,	NULL,	'p'},
	{"toggledisable",	0,	NULL,	'o'},
	{"toggleenable",	0,	NULL,	'e'},
	{"wkickinterval",	1,	NULL,	'w'},
	{"ikickinterval",	1,	NULL,	'i'},
	{"kickcount",		1,	NULL,	'c'},
	{NULL,			0,	NULL,	0  },
};

static const struct {
	int flag;
	const char *name;
} status_flag_table[] = {
	{ WDIOF_OVERHEAT,	"OVERHEAT"},
	{ WDIOF_FANFAULT,	"FANFAULT"},
	{ WDIOF_EXTERN1,	"EXTERN1"},
	{ WDIOF_EXTERN2,	"EXTERN2"},
	{ WDIOF_POWERUNDER,	"POWERUNDER"},
	{ WDIOF_CARDRESET,	"CARDRESET"},
	{ WDIOF_POWEROVER,	"POWEROVER"},
	{ WDIOF_SETTIMEOUT,	"SETTIMEOUT"},
	{ WDIOF_MAGICCLOSE,	"MAGICCLOSE"},
	{ WDIOF_PRETIMEOUT,	"PRETIMEOUT"},
	{ WDIOF_ALARMONLY,	"ALARMONLY"},
	{ WDIOF_KEEPALIVEPING,	"KEEPALIVEPING"},
	{ 0,			NULL},
};

static void print_usage(FILE *stream, char *exec_name, int exit_code)
{
	fprintf(stream, "Usage: %s [options]\n", exec_name);
	fprintf(stream,
		"     --help                  Display this usage information\n"
		" -d  --dev <device>          Use <device> as watchdog device file\n"
		"                             (default: /dev/watchdog)\n"
		" -t  --timeout <interval>    Change the watchdog timeout (in s)\n"
		" -p  --pretimeout <interval> Change the watchdog pre-timeout (in s)\n"
		" -x  --nomagicclose          Send no magic character on exit to leave\n"
		"                             watchdog active\n"
		" -o  --toggledisable         Toggle WDT enabled->disabled\n"
		" -e  --toggleenable          Toggle WDT disabled->enabled\n"
		" -w  --wkickinterval <interval>\n"
		"                             Kick with write method and specified\n"
		"                             interval (in s)\n"
		" -i  --ikickinterval <interval>\n"
		"                             Kick with ioctl method and specified\n"
		"                             interval (in s)\n"
		" -c  --kickcount             Changes the number of keepalives to be sent\n"
		"                             (default: 5)\n");

	exit(exit_code);
}

#define BUFSIZE 256
void print_active_wdt_status_flags(int status) {
	char buf[BUFSIZE];
	int count = 0;
	int i = 0;

	do {
		if (status & status_flag_table[i].flag) {
			count += snprintf(&buf[count], (BUFSIZE - count - 2),
			      "%s|", status_flag_table[i].name);
			status &= ~status_flag_table[i].flag;
		}
	} while (status_flag_table[++i].flag);

	if (status)
		count += snprintf(&buf[count], (BUFSIZE - count - 2),
				  "-unknown_flag-|");
	if (count)
		count--;

	buf[count]='\0';

	printf("%s\n", buf);
}

void print_wdt_status(int fd)
{
	int status;
	struct watchdog_info ident;

	if (ioctl(fd, WDIOC_GETSUPPORT, &ident) == 0) {
		printf("Identity: %s\n", ident.identity);
		printf("Firmware Version: %x\n", ident.firmware_version);
		printf("Options: 0x%08x\n", ident.options);
		printf("  WDIOF_OVERHEAT:      %d\n",
		       !!(ident.options & WDIOF_OVERHEAT));
		printf("  WDIOF_FANFAULT:      %d\n",
		       !!(ident.options & WDIOF_FANFAULT));
		printf("  WDIOF_EXTERN1:       %d\n",
		       !!(ident.options & WDIOF_EXTERN1));
		printf("  WDIOF_EXTERN2:       %d\n",
		       !!(ident.options & WDIOF_EXTERN2));
		printf("  WDIOF_POWERUNDER:    %d\n",
		       !!(ident.options & WDIOF_POWERUNDER));
		printf("  WDIOF_CARDRESET:     %d\n",
		       !!(ident.options & WDIOF_CARDRESET));
		printf("  WDIOF_POWEROVER:     %d\n",
		       !!(ident.options & WDIOF_POWEROVER));
		printf("  WDIOF_SETTIMEOUT:    %d\n",
		       !!(ident.options & WDIOF_SETTIMEOUT));
		printf("  WDIOF_MAGICCLOSE:    %d\n",
		       !!(ident.options & WDIOF_MAGICCLOSE));
		printf("  WDIOF_PRETIMEOUT:    %d\n",
		       !!(ident.options & WDIOF_PRETIMEOUT));
		printf("  WDIOF_ALARMONLY:     %d\n",
		       !!(ident.options & WDIOF_ALARMONLY));
		printf("  WDIOF_KEEPALIVEPING: %d\n",
		       !!(ident.options & WDIOF_KEEPALIVEPING));
	}	else
		printf("WDIOC_GETSUPPORT - %s\n", strerror(errno));

	if (ioctl(fd, WDIOC_GETSTATUS, &status) == 0) {
		printf("Status: 0x%08x  ", status);
		print_active_wdt_status_flags(status);
	} else
		printf("WDIOC_GETSTATUS - %s\n", strerror(errno));

	if (ioctl(fd, WDIOC_GETBOOTSTATUS, &status) == 0) {
		printf("Boot status: 0x%08x  ", status);
		print_active_wdt_status_flags(status);
	} else
		printf("WDIOC_GETBOOTSTATUS - %s\n", strerror(errno));
}

void print_wdt_timeouts(int fd)
{
	int status;

	if (ioctl(fd, WDIOC_GETTIMEOUT, &status) == 0)
		printf("Timeout: %d\n", status);
	else
		printf("WDIOC_GETTIMEOUT - %s\n", strerror(errno));

	if (ioctl(fd, WDIOC_GETPRETIMEOUT, &status) == 0)
		printf("Pre-Timeout: %d\n", status);
	else
		printf("WDIOC_GETPRETIMEOUT - %s\n", strerror(errno));
}

int set_wdt_options(int fd, int options)
{
	int ret;

	ret = ioctl(fd, WDIOC_SETOPTIONS, &options);
	if (ret == 0) {
		printf("Enabled options: ");
		if (options & WDIOS_DISABLECARD)
			printf("WDIOS_DISABLECARD ");
		if (options & WDIOS_ENABLECARD)
			printf("WDIOS_ENABLECARD ");
		if (options & WDIOS_TEMPPANIC)
			printf("WDIOS_TEMPPANIC ");
		printf("\n");
	} else
		printf("WDIOC_SETOPTIONS - %s\n", strerror(errno));
	return ret;
}

void toggle_wdt_enable(int fd)
{
	printf("Toggle watchdog enable option (disable -> enable)\n");

	set_wdt_options(fd, WDIOS_DISABLECARD);
	set_wdt_options(fd, WDIOS_ENABLECARD);

	return;
}

void toggle_wdt_disable(int fd)
{
	printf("Toggle watchdog disable option (enable -> disable)\n");

	set_wdt_options(fd, WDIOS_ENABLECARD);
	set_wdt_options(fd, WDIOS_DISABLECARD);

	return;
}

void test_wdt_keepalive(int fd, int count, int interval, t_keepalive type)
{
	int timer;
	int errcount = 0;

	printf("Test %s keepalive (interval %d s, count %d):\n",
	       (type == WRITE) ? "write" : "ioctl", interval, count);

	if ((count < 1) || (interval < 1)) {
		fprintf(stderr, "Invalid keepalive parameters...\n");
		return;
	}

	for (timer=0; timer<=((count-1)*interval); timer++) {
		if (timer > 0)
			sleep(1);
		printf("Timer: %d", timer);
		if (!(timer % interval)) {
			printf(" - PING ");
			if (type == WRITE) {
				errcount += 1;
				if (write(fd, "w", 1) == -1)
					printf("write error %d - %s\n",
					       errcount, strerror(errno));
			} else {
				if (ioctl(fd, WDIOC_KEEPALIVE, NULL) != 0) {
					errcount += 1;
					printf("keepalive error %d - %s\n",
					       errcount, strerror(errno));
				}
			}
		}
		printf("\n");
	}
}


int main(int argc, char **argv)
{
	int fd;
	int timeout;
	int pretimeout;
	int magicclose;
	int toggle_enable;
	int toggle_disable;
	int kickcount;
	int wkickinterval;
	int ikickinterval;
	char *dev;
	int next_option;

	dev = DEFAULT_WATCHDOGDEV;
	timeout = -1;
	pretimeout = -1;
	magicclose = 1;
	toggle_enable = 0;
	toggle_disable = 0;
	wkickinterval = 0;
	ikickinterval = 0;
	kickcount = 5;

	do {
		next_option = getopt_long(argc, argv, short_options,
					  long_options, NULL);
		switch (next_option) {
			case 1:
				print_usage(stdout, argv[0], EXIT_SUCCESS);
				exit(EXIT_SUCCESS);
			case 'd':
				dev = optarg;
				break;
			case 't':
				timeout = atoi(optarg);
				break;
			case 'p':
				pretimeout = atoi(optarg);
				break;
			case 'x':
				magicclose = 0;
				break;
			case 'o':
				toggle_disable = 1;
				break;
			case 'e':
				toggle_enable = 1;
				break;
			case 'w':
				wkickinterval = atoi(optarg);
				break;
			case 'i':
				ikickinterval = atoi(optarg);
				break;
			case 'c':
				kickcount = atoi(optarg);
				break;
			case '?':
				print_usage(stderr, argv[0], EXIT_FAILURE);
				break;
			case -1:
				break;
			default:   /* Unexpected stuffs */
				abort();
		}
	} while (next_option != -1);

	fd = open(dev, O_RDWR);
	if (-1 == fd) {
		fprintf(stderr, "ERROR %s - opening %s\n", strerror(errno),
			dev);
		exit(EXIT_FAILURE);
	}
	print_wdt_status(fd);
	print_wdt_timeouts(fd);

	if (pretimeout >= 0) {
		printf("WDIOC_SETPRETIMEOUT: %d\n", pretimeout);
		if (ioctl(fd, WDIOC_SETPRETIMEOUT, &pretimeout) != 0)
			printf("WDIOC_SETPRETIMEOUT - %s\n", strerror(errno));
	}

	if (timeout >= 0) {
		printf("WDIOC_SETTIMEOUT: %d\n", timeout);
		if (ioctl(fd, WDIOC_SETTIMEOUT, &timeout) != 0)
			printf("WDIOC_SETTIMEOUT - %s\n", strerror(errno));
	}

	if ((timeout >= 0) || (pretimeout >= 0))
		print_wdt_timeouts(fd);

	if (toggle_enable)
		toggle_wdt_enable(fd);

	if (toggle_disable)
		toggle_wdt_disable(fd);


	if (ikickinterval)
		test_wdt_keepalive(fd, kickcount, ikickinterval, IOCTL);

	if (wkickinterval)
		test_wdt_keepalive(fd, kickcount, wkickinterval, WRITE);

	if (magicclose) {
		printf("Sending magic close character!\n");
		if (write(fd, "V", 1) == -1)
			printf("SEND 'V' - %s\n", strerror(errno));
	}

	close(fd);

	exit(EXIT_SUCCESS);
}

