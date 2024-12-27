#include <stdio.h>
#include <unistd.h>
#include <poll.h>
#include <errno.h>
#include <fcntl.h>
#include <stdlib.h>
//#include <time.h>

const char help_text[] =
    "USAGE:\n"
    "\n"
    "gpioint <gpio number> [timeout]\n";


int main(int argc, char *argv[])
{
    char fn_buf[128];
    char in_buf[80];
    struct pollfd fds[1];
    int timeout = 5, gpio_num;
    int fd, ret;

    if (argc < 2) {
        printf(help_text);
        exit(1);
    }

    gpio_num = strtoul(argv[1], NULL, 0);
    if (argc == 3)
        timeout = strtoul(argv[2], NULL, 0);

    sprintf(fn_buf, "/sys/class/gpio/gpio%d/value", gpio_num);
    fd = open(fn_buf, O_RDWR);
    if (fd < 0) {
        perror("open");
        exit(1);
    }

    ret = read(fd, in_buf, 80);
    if (ret < 0) {
        perror("read0");
        exit(1);
    }

    in_buf[ret] = 0;
//    printf("Value at start: %s", in_buf);

    fds[0].fd = fd;
    fds[0].events = POLLPRI | POLLERR;
    fds[0].revents = 0;

    ret = poll(fds, 1, timeout * 1000);
    if (ret < 0) {
        perror("poll");
        exit(1);
    } else if (ret == 0) {
        printf("Poll timeout\n");
        exit(1);
    }

    printf("GPIO %d interrupt occurred\n", gpio_num);

    lseek(fd, 0, SEEK_SET);
    ret = read(fd, in_buf, 80);
    if (ret < 0) {
        perror("read");
        exit(1);
    }
    in_buf[ret] = 0;
//    printf("New value: %s", in_buf);

    close(fd);

    return 0;
}
