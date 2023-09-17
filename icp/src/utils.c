/* Ryan Martin TP058091 */
/* -------------------- */
/* This file contains the implementations of the functions declared in utils.h
 */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include "utils.h"

/* returns integer representation of string */
int strtoint(char *str) {
    int result;
    sscanf(str, "%d", &result);
    return result;
}

/* function: ndigits
 * -----------------
 * Returns number of digits of an integer
 * parameters:
 *   x: an integer
 * returns: the number of digits
 */
static int ndigits(int x) {
    int n = x < 0 ? -x : x;
    int res = 0;
    while (n > 0) {
        n /= 10;
        res++;
    }
    return res;
}

/* returns string representation of integer */
char *intostr(int x) {
    char *res = malloc((ndigits(x) + 2) * sizeof(char));
    if (!res) err_exit();
    sprintf(res, "%d", x);
    return res;
}

/* return string of current datetime */
char *get_current_datetime() {
    time_t rawtime;
    struct tm *info;
    time(&rawtime);
    info = localtime(&rawtime);
    char *result = malloc(80);
    if (!result) err_exit();
    strftime(result, 80, "%Y-%m-%d %X", info);
    return result;
}

/* helper function to return the sum of an integer array */
int sum_of_array(int *array, int len) {
    int i;
    int sum = 0;
    for (i = 0; i < len; i++) sum += array[i];
    return sum;
}

/* helper function to print a character c, n times */
void print_nchar(char c, int n) {
    int i;
    for (i = 0; i < n; i++) putchar(c);
}

/* check if a string is numeric */
int is_digit(char *str) {
    if (*str == '-') str++;
    while (*str != '\0') {
        if (!(*str >= '0' && *str <= '9')) return 0;
        str++;
    }
    return 1;
}

/* my strdup function */
char *my_strdup(const char *str) {
    char *new_str = malloc(strlen(str) + 1);
    if (new_str == NULL) err_exit();
    strcpy(new_str, str);
    return new_str;
}

/* print error and exit */
void err_exit() {
    fputs("error: can't allocate memory", stderr);
    exit(1);
}

