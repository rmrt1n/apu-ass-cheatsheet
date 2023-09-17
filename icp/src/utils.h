/* Ryan Martin TP058091 */
/* -------------------- */
/* This file contains the function declarations for some helper functions.
 * The definition for the functions are in utils.c
 */
#ifndef UTILS_H
#define UTILS_H

/* function: strtoint
 * ------------------
 * convert string into int
 * parameters:
 *   str: string to convert to int
 * returns: integer representation of str
 */
int strtoint(char *str);

/* function: intostr
 * -----------------
 * The opposite of strtoint
 * parameters:
 *   x: integer to turn to string
 * returns: string representation of int
 */
char *intostr(int x);

/* function: get_current_datetime
 * ------------------------------
 * Function to return the current datetime in the format YYYY-mm-dd HH:MM:SS
 * returns: string of time format
 */
char *get_current_datetime(void);

/* function: sum_of_array
 * ----------------------
 * Function to return sum of an integer array
 * parameters:
 *   array: array to be summed
 *   len:   length of the array
 * returns: the sum of items in the array
 */
int sum_of_array(int *array, int len);

/* function: print_nchar
 * ---------------------
 * Function that prints the character c, n times
 * parameters:
 *   c: the character to be printed
 *   n: the number of times to print the characters
 */
void print_nchar(char c, int n);

/* function: is_digit
 * ------------------
 * Checks if a string is numeric
 * parameters:
 *   str: string to be checked
 * returns: 1 if string is numeric else 0
 */
int is_digit(char *str);

/* function: my_strdup
 * -------------------
 * Just a redefinition of strdup, because it is not supported in ANSI C (C89)
 * Duplicates a string and allocate it memory
 * parameters:
 *   str: a string literal
 * returns: allocated string
 */
char *my_strdup(const char *str);

/* function: err_exit
 * ------------------
 *  Prints error and exit if malloc/realloc fails
 */
void err_exit();

#endif

