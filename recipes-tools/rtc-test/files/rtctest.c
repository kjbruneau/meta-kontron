/*
 *      Real Time Clock Driver Test/Example Program
 *
 *      Compile with:
 *		     gcc -s -Wall -Wstrict-prototypes rtctest.c -o rtctest
 *
 *      Copyright (C) 1996, Paul Gortmaker.
 *
 *      Released under the GNU General Public License, version 2,
 *      included herein by reference.
 *
 */

#include <stdio.h>
#include <linux/rtc.h>
#include <sys/ioctl.h>
#include <sys/time.h>
#include <sys/types.h>
#include <fcntl.h>
#include <unistd.h>
#include <stdlib.h>
#include <errno.h>
#include <time.h>
#include <string.h>


/*
 * This expects the new RTC class driver framework, working with
 * clocks that will often not be clones of what the PC-AT had.
 * Use the command line to specify another RTC if you need one.
 */
static const char default_rtc[] = "/dev/rtc0";


int main(int argc, char **argv)
{
	int i, fd, retval, irqcount = 0, min_mode = 0;
	unsigned long tmp, data;
	struct rtc_time rtc_tm;
	const char *rtc = default_rtc;
	struct timeval start, end, diff;
	time_t T1, T2, T_diff;
	struct rtc_time rtc_tm1, rtc_tm2;

	switch (argc) {
	case 3:
		if (!strncmp(argv[1], "-m", 2))
			min_mode = 1;
		rtc = argv[2];
		break;
	case 2:
		if (!strncmp(argv[1], "-m", 2))
			min_mode = 1;
		else
			rtc = argv[1];
		break;
		/* FALLTHROUGH */
	case 1:
		break;
	default:
		fprintf(stderr, "usage:  rtctest [-m] [rtcdev]\n");
		return 1;
	}

	fd = open(rtc, O_RDONLY);

	if (fd ==  -1) {
		perror(rtc);
		exit(errno);
	}

	fprintf(stderr, "\n\t\t\tRTC Driver Test Example.\n\n");

	/* Turn on update interrupts (one per second) */
	retval = ioctl(fd, RTC_UIE_ON, 0);
	if (retval == -1) {
		if (errno == EINVAL) {
			fprintf(stderr,
				"\n...Update IRQs not supported.\n");
			goto test_READ;
		}
		perror("RTC_UIE_ON ioctl");
		exit(errno);
	}

	fprintf(stderr, "Counting 5 update (1/sec) interrupts from reading %s:",
			rtc);
	fflush(stderr);
	for (i=1; i<6; i++) {
		/* This read will block */
		retval = read(fd, &data, sizeof(unsigned long));
		if (retval == -1) {
			perror("read");
			exit(errno);
		}
		fprintf(stderr, " %d",i);
		fflush(stderr);
		irqcount++;
	}

	fprintf(stderr, "\nAgain, from using select(2) on /dev/rtc:");
	fflush(stderr);
	for (i=1; i<6; i++) {
		struct timeval tv = {5, 0};     /* 5 second timeout on select */
		fd_set readfds;

		FD_ZERO(&readfds);
		FD_SET(fd, &readfds);
		/* The select will wait until an RTC interrupt happens. */
		retval = select(fd+1, &readfds, NULL, NULL, &tv);
		if (retval == -1) {
		        perror("select");
		        exit(errno);
		}
		/* This read won't block unlike the select-less case above. */
		retval = read(fd, &data, sizeof(unsigned long));
		if (retval == -1) {
		        perror("read");
		        exit(errno);
		}
		fprintf(stderr, " %d",i);
		fflush(stderr);
		irqcount++;
	}

	/* Turn off update interrupts */
	retval = ioctl(fd, RTC_UIE_OFF, 0);
	if (retval == -1) {
		perror("RTC_UIE_OFF ioctl");
		exit(errno);
	}

test_READ:
	/* Read the RTC time/date */
	retval = ioctl(fd, RTC_RD_TIME, &rtc_tm);
	if (retval == -1) {
		perror("RTC_RD_TIME ioctl");
		exit(errno);
	}

	fprintf(stderr, "\n\nCurrent RTC date/time is %d-%d-%d, %02d:%02d:%02d.\n",
		rtc_tm.tm_mday, rtc_tm.tm_mon + 1, rtc_tm.tm_year + 1900,
		rtc_tm.tm_hour, rtc_tm.tm_min, rtc_tm.tm_sec);

	/* Set the alarm to 5 sec or nearest minute in the future, and check for rollover */
	if (min_mode) {
		T_diff = 60 - rtc_tm.tm_sec;
		rtc_tm.tm_min += 1;
		rtc_tm.tm_sec = 0;
	} else
		rtc_tm.tm_sec += 5;

	if (rtc_tm.tm_sec >= 60) {
		rtc_tm.tm_sec %= 60;
		rtc_tm.tm_min++;
	}
	if (rtc_tm.tm_min == 60) {
		rtc_tm.tm_min = 0;
		rtc_tm.tm_hour++;
	}
	if (rtc_tm.tm_hour == 24)
		rtc_tm.tm_hour = 0;

	retval = ioctl(fd, RTC_ALM_SET, &rtc_tm);
	if (retval == -1) {
		if (errno == EINVAL) {
			fprintf(stderr,
				"\n...Alarm IRQs not supported.\n");
			goto test_PIE;
		}

		perror("RTC_ALM_SET ioctl");
		exit(errno);
	}

	T1= time(NULL);
	ioctl(fd, RTC_RD_TIME, &rtc_tm1);

	/* Read the current alarm settings */
	retval = ioctl(fd, RTC_ALM_READ, &rtc_tm);
	if (retval == -1) {
		perror("RTC_ALM_READ ioctl");
		exit(errno);
	}

	fprintf(stderr, "Alarm time now set to %02d:%02d:%02d.\n",
		rtc_tm.tm_hour, rtc_tm.tm_min, rtc_tm.tm_sec);

	/* Enable alarm interrupts */
	retval = ioctl(fd, RTC_AIE_ON, 0);
	if (retval == -1) {
		if (errno == EINVAL) {
			fprintf(stderr,
				"\n...Alarm IRQs not supported.\n");
			goto test_PIE;
		}

		perror("RTC_AIE_ON ioctl");
		exit(errno);
	}

	if (min_mode)
		fprintf(stderr, "Waiting nearest minute for alarm...");
	else
		fprintf(stderr, "Waiting 5 seconds for alarm...");
	fflush(stderr);
	/* This blocks until the alarm ring causes an interrupt */
	retval = read(fd, &data, sizeof(unsigned long));
	if (retval == -1) {
		perror("read");
		exit(errno);
	}
	irqcount++;

	T2= time(NULL);
	ioctl(fd, RTC_RD_TIME, &rtc_tm2);

	if (min_mode) {
		if (T2 - T1 == T_diff)
			fprintf(stderr, " okay. Alarm rang at %02d:%02d:%02d.\n",
				rtc_tm2.tm_hour, rtc_tm2.tm_min, rtc_tm2.tm_sec);
		else
			fprintf(stderr, " Alarm failed to rang in nearest minute. Actual time is %d minutes, %d seconds.\n",
				rtc_tm2.tm_min - rtc_tm1.tm_min, rtc_tm2.tm_sec);
	} else {
		if (T2 - T1 == 5)
			fprintf(stderr, " okay. Alarm rang at %02d:%02d:%02d.\n",
				rtc_tm2.tm_hour, rtc_tm2.tm_min, rtc_tm2.tm_sec);
		else
			fprintf(stderr, " Alarm failed to rang in 5 seconds. Actual time is %ld seconds.\n", T2 - T1);
	}

	/* Disable alarm interrupts */
	retval = ioctl(fd, RTC_AIE_OFF, 0);
	if (retval == -1) {
		perror("RTC_AIE_OFF ioctl");
		exit(errno);
	}

test_PIE:
	/* Read periodic IRQ rate */
	retval = ioctl(fd, RTC_IRQP_READ, &tmp);
	if (retval == -1) {
		/* not all RTCs support periodic IRQs */
		if (errno == EINVAL) {
			fprintf(stderr, "\nNo periodic IRQ support\n");
			goto done;
		}
		perror("RTC_IRQP_READ ioctl");
		exit(errno);
	}
	fprintf(stderr, "\nPeriodic IRQ rate is %ldHz.\n", tmp);

	fprintf(stderr, "Counting 20 interrupts at:");
	fflush(stderr);

	/* The frequencies 128Hz, 256Hz, ... 8192Hz are only allowed for root. */
	for (tmp=2; tmp<=64; tmp*=2) {

		retval = ioctl(fd, RTC_IRQP_SET, tmp);
		if (retval == -1) {
			/* not all RTCs can change their periodic IRQ rate */
			if (errno == EINVAL) {
				fprintf(stderr,
					"\n...Periodic IRQ rate is fixed\n");
				goto done;
			}
			perror("RTC_IRQP_SET ioctl");
			exit(errno);
		}

		fprintf(stderr, "\n%ldHz:\t", tmp);
		fflush(stderr);

		/* Enable periodic interrupts */
		retval = ioctl(fd, RTC_PIE_ON, 0);
		if (retval == -1) {
			perror("RTC_PIE_ON ioctl");
			exit(errno);
		}

		for (i=1; i<21; i++) {
			gettimeofday(&start, NULL);
			/* This blocks */
			retval = read(fd, &data, sizeof(unsigned long));
			if (retval == -1) {
				perror("read");
				exit(errno);
			}
			gettimeofday(&end, NULL);
			timersub(&end, &start, &diff);
			if (diff.tv_sec > 0 ||
			    diff.tv_usec > ((1000000L / tmp) * 1.10)) {
				fprintf(stderr, "\nPIE delta error: %ld.%06ld should be close to 0.%06ld\n",
				       diff.tv_sec, diff.tv_usec,
				       (1000000L / tmp));
				fflush(stdout);
				exit(-1);
			}

			fprintf(stderr, " %d",i);
			fflush(stderr);
			irqcount++;
		}

		/* Disable periodic interrupts */
		retval = ioctl(fd, RTC_PIE_OFF, 0);
		if (retval == -1) {
			perror("RTC_PIE_OFF ioctl");
			exit(errno);
		}
	}

done:
	fprintf(stderr, "\n\n\t\t\t *** Test complete ***\n");

	close(fd);

	return 0;
}
