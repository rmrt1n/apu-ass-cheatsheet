/* Ryan Martin TP058091 */
/* -------------------- */
/* this file contains the necessary constants and data structures for the
 * program. the enums VacHeader and DistHeader maps to the column names in the
 * files vaccines.txt and dist.txt. the Table struct is used to store the file
 * in the program.
 */
#ifndef COMMON_H
#define COMMON_H

/* initial quantity of the vaccines (in millions) */
#define INITIAL_QUANTITY 10
/* number of vaccines in vaccines.txt */
#define NVACS 5

/* enum: VacHeader
 * ---------------
 * columns in vaccines.txt
 */
enum VacHeader {
    VAC_NAME = 0, VAC_CODE, VAC_COUNTRY,
    VAC_DOSE, VAC_COVERAGE, VAC_QTY
};

/* enum: DistHeader
 * ----------------
 * columns in dist.txt */
enum DistHeader {
    DIST_VAC = 0, DIST_QTY, DIST_TIME
};

/* enum: TableType
 * ---------------
 * codes for the txt files
 * VAC_TABLE = "vaccines.txt",
 * DIST_TABLE = "dist.txt"
 */
enum TableType {VAC_TABLE, DIST_TABLE};

/* struct: Table
 * -------------
 * Structure to store csv files in program
 * members:
 *   filename: name of the file parsed to this structure
 *   nrows: the number of rows in the file
 *   ncols: the number of columns in the file
 *   items: matrix of character arrays (strings) of items in file
 */
typedef struct Table {
    char *filename;
    int nrows;
    int ncols;
    char ***items;
} Table;

#endif

