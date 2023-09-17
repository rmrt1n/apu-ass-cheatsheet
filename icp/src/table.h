/* Ryan Martin TP058091 */
/* -------------------- */
/* This file contains the functions declarations for handling tables (csv files).
 * The data from the text files are parsed into the Table data structure
 * defined in common.h. The function definitions are in table.c
 */
#ifndef TABLE_H
#define TABLE_H

#include "common.h"

/* function: parse_table
 * ---------------------
 * Function to parse csv files into a Table struct
 * parameters:
 *   tabletype: enum TableType defined in common.h. The only allowed values 
 *              are VAC_TABLE and DIST_TABLE.
 * returns: a pointer to a Table struct or NULL if file doesn't exist
 */
Table *parse_table(int tabletype);

/* function: print_table
 * ---------------------
 * Helper function to pretty print a Table struct.
 * parameters:
 *   table: a pointer to a Table struct
 */
void print_table(Table *table);

/* function: table_free
 * Frees allocated Table structs after use
 * parameters:
 *   table: pointer to a Table struct
 */
void table_free(Table *table);

/* function: table_update_where
 * ----------------------------
 * Function to update a Table's specific column where the value of another
 * column is equal to a specified value. to ensure no index errors, use the 
 * enums VacHeader & DistHeader defined in common.h for cond_col & dst_col
 * parameters:
 *   table:    pointer to a Table struct
 *   cond_col: column index of the condition column
 *   cond_val: value to be compared in column index item 
 *   dst_col:  column index to be changed
 *   dst_val:  new value for column index
 */
void table_update_where(Table *table,
                        int cond_col, char *cond_val,
                        int dst_col, char *dst_val);

/* function: table_select_where
 * ----------------------------
 * Function to get item from a specific index where a condition in another
 * column is satisfied. The same rules for table_update_where also applies here
 * parameters:
 *   table:    pointer to a Table struct
 *   cond_val: value to be compared in column index item 
 *   cond_col: column index of the condition column
 *   dst_col:  column index to be returned
 * returns: value (string) stored in Table, column dst_col, that fulfills condition
 */
char *table_select_where(Table *table, int cond_col, char *cond_val, int dst_col);

/* function: table_write_file
 * ---------------------
 * writes a Table struct to it's file. The filename is already contained in the struct
 * parameters:
 *   table: pointer to a Table struct
 */
void table_write_file(Table *table);

#endif

