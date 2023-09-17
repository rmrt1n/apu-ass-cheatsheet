/* Ryan Martin TP058091 */
/* -------------------- */
/* This file contains the function definitions for the declarations in table.h,
 * along with several helper functions
 */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "table.h"
#include "utils.h"


/* function: table_init
 * --------------------
 * Table struct constructor function
 * parameters:
 *   type: enum TableType from common.h
 * returns: pointer to the allocated Table struct
 */
static Table *table_init(int type) {
    Table *table = malloc(sizeof(Table));
    if (!table) err_exit();
    table->filename = my_strdup(type == VAC_TABLE ? "vaccines.txt" : "dist.txt");
    table->nrows = 0;
    table->ncols = type == VAC_TABLE ? 6 : 3;
    table->items = malloc(sizeof(char **));
    if (!table->items) err_exit();
    return table;
}

/* function: peek
 * --------------
 * peek the next character in the file stream
 * parameters:
 *   fp: file pointer to character stream
 * returns: peeked character
 */
static char peek(FILE *fp) {
    char c = getc(fp);
    ungetc(c, fp);
    return c;
}

/* function: parse_item
 * --------------------
 * Parses an item from the file
 * parameters:
 *   fp: pointer to the file being parsed
 * returns: parsed item as a string
 */
static char *parse_item(FILE *fp) {
    int size = 1;
    char c;
    char *result = malloc(size);
    if (!result) err_exit();
    *result = '\0';
    /* loop while not fp at end of line or at end of file */
    while ((c = getc(fp)) != ',') {
        /* break at EOL / EOF */
        if (c == '\n' || c == EOF) {
            ungetc(c, fp);
            break;
        }
        result = realloc(result, ++size);
        if (!result) err_exit();
        strncat(result, &c, 1);
    }
    result[size-1] = '\0';
    return result;
}

/* function: parse_row
 * -------------------
 * Parses an entire row from the file
 * parameters: 
 *   fp:    pointer to file being parsed
 *   ncols: the number of columns to be parsed
 * returns: a string array of items parsed
 */
static char **parse_row(FILE *fp, int ncols) {
    int i;
    char **res = malloc(ncols * sizeof(char *));
    if (!res) err_exit();
    for (i = 0; i < ncols; i++) {
        res[i] = parse_item(fp);
    }
    getc(fp); /* get newline '\n' */
    return res;
}

/* The actual function that is called to parse the file */
Table *parse_table(int tabletype) {
    Table *table = table_init(tabletype);
    FILE *fp = fopen(table->filename, "r");
    if (!fp) return NULL;
    /* while not at end of file: parse rows and increment table->nrows */
    while (peek(fp) != EOF) {
        int nrows = table->nrows, ncols = table->ncols;
        table->items = realloc(table->items, (nrows + 1) * sizeof(char **));
        if (!table->items) err_exit();
        table->items[nrows] = parse_row(fp, ncols);
        table->nrows++;
    }
    fclose(fp);
    return table;
}

/* function: longest_in_col
 * ------------------------
 * Find the number of characters for the longest string in a column
 * parameters:
 *   array: string matrix of items parsed
 *   rows:  the number of rows
 *   col:   which column to check
 * returns: length of the longest string in the column
 */
static int longest_in_col(char ***array, int rows, int col) {
    int i;
    int max = strlen(array[0][col]);
    for (i = 1; i < rows; i++) {
        int len = strlen(array[i][col]);
        max = len > max ? len : max;
    }
    return max;
}

/* function: print_row
 * -------------------
 * Helper function to print a row from Table
 * parameters:
 *   row:     array of string (item) from table
 *   ncols:   the number of columns in a row
 *   padding: array of length of the longest string in each column
 */
static void print_row(char **row, int ncols, int *padding) {
    int i;
    putchar('|');
    for (i = 0; i < ncols; i++) {
        printf(" %s", row[i]);
        print_nchar(' ', padding[i] - strlen(row[i]) + 1);
        putchar('|');
    }
    putchar('\n');
}

/* The actual table print function */
void print_table(Table *table) {
    int i;
    int nrows = table->nrows, ncols = table->ncols;
    /* get paddings for pretty printing */
    int padding[ncols];
    for (i = 0; i < ncols; i++) {
        padding[i] = longest_in_col(table->items, nrows, i);
    }

    /* print headers */
    print_row(table->items[0], ncols, padding);
    /* print separator */
    print_nchar('-', sum_of_array(padding, ncols) + (3 * ncols) + 1);
    putchar('\n');

    /* print body */
    for (i = 1; i < nrows; i++) {
        print_row(table->items[i], ncols, padding);
    }
}

/* Update table function */
void table_update_where(Table *table,
                        int cond_col, char *cond_val,
                        int dst_col, char *dst_val) {
    int i;
    /* for row in table:
     *   if row[cond_col] is cond_val:
     *     set row[dst_col] to dst_val
     */
    for (i = 0; i < table->nrows; i++) {
        if (strcmp(table->items[i][cond_col], cond_val) == 0) {
            char *tmp = table->items[i][dst_col];
            table->items[i][dst_col] = dst_val;
            free(tmp); /* free old value */
            break;
        }
    }
}

/* select where function */
char *table_select_where(Table *table, int cond_col, char *cond_val, int dst_col) {
    int i;
    /* for row in table:
     *   if row[cond_col] is cond_val:
     *     return row[dst_col]
     */
    for (i = 0; i < table->nrows; i++) {
        if (strcmp(table->items[i][cond_col], cond_val) == 0) {
            return table->items[i][dst_col];
        }
    }
    /* return null if no row fulfils condition */
    return NULL;
}

/* write table to file */
void table_write_file(Table *table) {
    int i, j;
    FILE *fp = fopen(table->filename, "w");
    for (i = 0; i < table->nrows; i++) {
        fprintf(fp, "%s", table->items[i][0]);
        for (j = 1; j < table->ncols; j++) {
            fprintf(fp, ",%s", table->items[i][j]);
        }
        fputc('\n', fp);
    }
    fclose(fp);
}

/* free table function */
void table_free(Table *table) {
    int i, j;
    for (i = 0; i < table->nrows; i++) {
        for (j = 0; j < table->ncols; j++) {
            free(table->items[i][j]);
        }
        free(table->items[i]);
    }
    free(table->items);
    free(table->filename);
    free(table);
}

