/* Ryan Martin TP058091 */
/* -------------------- */
/* This file contains the implementation for the menu related functionalities
 * of the program. Only main_menu is accessible from menu.h.
 */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "menu.h"
#include "table.h"
#include "utils.h"

/* struct: Vaccine
 * ---------------
 * Structure to hold vaccine info (used in show distribution log)
 * members:
 *   name: vaccine name
 *   code: vaccine code
 *   qty: vaccine quantity (starting value should be 0, for accumulation)
 */
typedef struct Vaccine {
    char *name;
    char *code;
    int qty;
} Vaccine;

/* HELPER FUNCTIONS FOR GETTING INPUT */

/* function: is_valid_code
 * -----------------------
 * Checks if a vaccine code is valid (inside vaccines.txt)
 * parameters:
 *   inp: input string
 *   table: pointer to table containing vaccines data
 * returns: 1 if code is valid else 0
 */
static int is_valid_code(char *inp, Table *table) {
    int i;
    for (i = 0; i < table->nrows; i++) {
        if (strcmp(inp, table->items[i][VAC_CODE]) == 0) return 1;
    }
    return 0;
}


/* function: get_vac_code
 * ----------------------
 * Helper function to get valid vaccine code from user
 * returns: the code inputted 
 */
static char *get_vac_code() {
    Table *table = parse_table(VAC_TABLE);
    char inp[32];
    do {
        printf("enter vaccine code (case sensitive, x to cancel): ");
        fgets(inp, 32, stdin);
        if (*inp == 'x') return NULL;
        inp[strlen(inp)-1] = '\0'; /* remove \n */
    } while (!is_valid_code(inp, table));
    table_free(table);
    return my_strdup(inp);
}

/* function: wait_for_input
 * ------------------------
 * Helper function to wait for input
 */
static void wait_for_input() {
    printf("press any key to continue: ");
    getchar();
}

/* function: get_vac_amt
 * ---------------------
 * Helper function to get the ammount of vaccine to be added/distributed 
 * parameters:
 *   msg: message to be printed to the user
 * returns: ammount of vaccine to be added/distributed
 */
static int get_vac_amt(char *msg) {
    char input[32];
    do {
        printf("%s", msg);
        fgets(input, 32, stdin);
        input[strlen(input)-1] = '\0'; /* remove \n */
    } while (!is_digit(input) || *input == '\0'); /* second cond for empty inp */

    int amt = strtoint(input);
    return amt;
}

/* HELPER FUNCTIONS FOR GETTING INPUT */


/* CREATE INVENTORY FUNCTIONS */

/* function: create_vacc_txt
 * -------------------------
 * Function to create vaccines.txt. vaccine quantity values are in millions
 */
static void create_vacc_txt() {
    FILE *fp = fopen("vaccines.txt", "w");
    if (fp == NULL) {
        fputs("error: can't open file vaccines.txt", stderr);
        exit(1);
    }
    fputs("vac_name,vac_code,vac_country,vac_dosage,vac_pop_covered,vac_quantity\n", fp);
    fprintf(fp, "Pfitzer,PF,USA,2,50,%d\n", INITIAL_QUANTITY);
    fprintf(fp, "Sinovac,SV,China,2,18.8,%d\n", INITIAL_QUANTITY);
    fprintf(fp, "AstraZeneca,AZ,UK,2,10,%d\n", INITIAL_QUANTITY);
    fprintf(fp, "Sputnik V,SP,Russia,2,10,%d\n", INITIAL_QUANTITY);
    fprintf(fp, "CanSinoBio,CS,China,1,10.9,%d\n", INITIAL_QUANTITY);
    fclose(fp);
    puts("vaccine inventory created");
}

/* function: create_dist_txt
 * -------------------------
 * Function to create dist.txt 
 */
static void create_dist_txt() {
    FILE *fp = fopen("dist.txt", "w");
    if (fp == NULL) {
        fputs("error: can't open file dist.txt", stderr);
        exit(1);
    }
    fputs("dist_vac,dist_qty,dist_datetime\n", fp);
    fclose(fp);
    puts("distribution log created");
}

/* function: create_inventory
 * --------------------------
 * Interface to create_vacc_txt and create_dist_txt.
 * First, asks user for which file to create, then asks for confirmation
 * before writing to the file. Will create file if not exist.
 */
static void create_inventory() {
    puts("CREATE INVENTORY");
    puts("1. Create vaccines inventory\n2. Create distribution log");
    char inp[8];
    /* get choice for which file to create */
    char choice;
    do {
        printf("enter choice (x to cancel): ");
        fgets(inp, 8, stdin);
        sscanf(inp, "%c", &choice);
    } while (choice != '1' && choice != '2' && choice != 'x');

    /* return if cancel */
    if (choice == 'x') return;

    /* function pointer to either create_vacc_txt or create_dist_txt */
    /* depending on user choice */
    void (*create_func)() = choice == '1' ? &create_vacc_txt : &create_dist_txt;
    do {
        printf("create inventory? (this will reset all existing data) [y/N]: ");
        fgets(inp, 8, stdin);
        sscanf(inp, "%c", &choice);
    } while (choice != 'y' && choice != 'Y' && choice != 'n' && choice != 'N');

    /* call the function */
    if (choice == 'y' || choice == 'Y') create_func();
}

/* CREATE INVENTORY FUNCTIONS  */


/* STOCK UP & DISTRIBUTE VACCINE FUNCTIONS */

/* function: shrink_table
 * ---------------------
 * Move vac_quantity to 3rd column, and change ncols to 3.
 * This function is used only for printing the necessary columns, so instead of
 * all the columns, just print vaccine name, code, and quantity
 * parameters:
 *   table: pointer to Table struct to shrink
 */
static void shrink_table(Table *table) {
    int i;
    table->ncols = 3;
    for (i = 0; i < table->nrows; i++) {
        table->items[i][VAC_COUNTRY] = table->items[i][VAC_QTY];
    }
}

/* function: stock_up_vaccine
 * --------------------------
 * Function for stocking up vaccine. First, it prints the name, code, and 
 * quantity of existing vaccines. Then it asks for the user to input the vaccine
 * code to modify. The input is case sensitive. Then it asks for the ammount to
 * add. A zero or negative number will cause the program to print a message and
 * return to the main menu. After updating the file, print a message and return 
 * to main menu
 */
static void stock_up_vaccine() {
    puts("STOCK UP VACCINE");
    Table *table = parse_table(VAC_TABLE),
          *small_table = parse_table(VAC_TABLE);
    /* if parse_table fails then file doesn't exists */
    if (!table) {
        puts("error: no vaccines.txt file.");
        puts("create the file with the first option in the main menu.");
        return;
    }

    /* move vac_qty to 3rd column, and change table ncols to 3, so the table */
    /* printing functions only print upto the 3rd column */
    shrink_table(small_table);
    print_table(small_table);
    
    /* get vaccine code */
    char *code = get_vac_code();
    if (code == NULL) return;

    int new_amt = get_vac_amt("enter amount to add (in millions): ");
    /* return if new ammount is zero or less */
    if (new_amt <= 0) {
        puts("error: ammount must be higher than zero");
        return;
    }

    /* calculate new quantity */
    char *old_qty = table_select_where(table, VAC_CODE, code, VAC_QTY);
    int old_amt = strtoint(old_qty);
    char *new_qty = intostr(old_amt + new_amt);

    /* update & write table */
    table_update_where(table, VAC_CODE, code, VAC_QTY, new_qty);
    table_write_file(table);
    puts("Updated vaccine stock");

    /* cleanup */
    table_free(small_table);
    table_free(table);
}

/* function: write_dist
 * --------------------
 * Write distributed vac code, quantity, and date to dist.txt
 * parameters:
 *   code: code of distributed vaccine
 *   amt: ammount being distributed (in millions)
 */
static void write_dist(char *code, int amt) {
    if (!fopen("dist.txt", "r")) create_dist_txt();
    FILE *fp = fopen("dist.txt", "a");
    char *datetime = get_current_datetime();
    fprintf(fp, "%s,%d,%s\n", code, amt, datetime);
    fclose(fp);
}

/* function: distribute_vaccine
 * ----------------------------
 * Distribute vaccine (mostly the same as stock_up_vaccine), only that instead
 * of adding to old quantity, subtract. Will print error message if inputted 
 * ammount is greater than current amount (meaning use is trying to distribute
 * more vaccines than available).
 */
void distribute_vaccine() {
    puts("DISTRIBUTE VACCINE");
    Table *table = parse_table(VAC_TABLE),
          *small_table = parse_table(VAC_TABLE);
    if (!table) {
        puts("error: no vaccines.txt file.");
        puts("create the file with the first option in the main menu.");
        return;
    }
    shrink_table(small_table);
    print_table(small_table);
    
    char *code = get_vac_code();
    if (code == NULL) return;

    int new_amt = get_vac_amt("enter amount to distribute (in millions): ");
    if (new_amt <= 0) {
        puts("error: ammount must be higher than zero");
        return;
    }

    /* calculate new quantity */
    char *old_qty = table_select_where(table, VAC_CODE, code, VAC_QTY);
    int old_amt = strtoint(old_qty);
    /* if new qty is more than old ammount, print message and return */
    if (new_amt > old_amt) {
        puts("error: not enough vaccine to distribute");
        return;
    }
    int new_qty = old_amt - new_amt;

    /* update & write table & dist.txt */
    table_update_where(table, VAC_CODE, code, VAC_QTY, intostr(new_qty));
    table_write_file(table);
    write_dist(code, new_amt);
    puts("Updated vaccine stock & distribution log");

    /* cleanup */
    table_free(small_table);
    table_free(table);
}

/* STOCK UP & DISTRIBUTE VACCINE FUNCTIONS */


/* SEARCH VACCINE BY CODE FUNCTIONS */

/* function: print_codes
 * ------------------------
 * prints only the names and code of vaccines from a Table struct
 * parameters:
 *   table: a pointer to a Table struct
 */
void print_codes(Table *table) {
    int i;
    /* start from 1, skip file header */
    for (i = 1; i < table->nrows; i++) {
        printf("%d. %s (%s)\n", i, table->items[i][VAC_NAME], table->items[i][VAC_CODE]);
    }
}

/* function: search_vaccine
 * ------------------------
 * Prompts user to enter vaccine code, validates it, and searches the file
 * vaccine.txt for its current quantity 
 */
static void search_vaccine() {
    puts("SEARCH VACCINE BY CODE");
    Table *table = parse_table(VAC_TABLE);
    if (!table) {
        puts("error: no vaccines.txt file.");
        puts("create the file with the first option in the main menu.");
        return;
    }
    print_codes(table);

    /* get code to search by */
    char *code = get_vac_code();
    if (code == NULL) return;

    /* get vaccine name & quantity by code */
    char *vac_name = table_select_where(table, VAC_CODE, code, VAC_NAME),
         *vac_qty = table_select_where(table, VAC_CODE, code, VAC_QTY);
    printf("Vaccine \"%s\" (%s):\n", vac_name, code);
    printf("Available stock (in millions): %s\n", vac_qty);
}

/* SEARCH VACCINE BY CODE FUNCTIONS */


/* SHOW DISTRIBUTION LOG FUNCTIONS */

/* function: bubble_sort_acc
 * -------------------------
 * Sorts Vaccine array based on accumulated vaccine quantities descendingly
 * using the bubble sort algorithm
 * parameters:
 *   vacs: array of Vaccine struct
 *   n: the number of elements in vacs
 */
void bubble_sort_acc(Vaccine *vacs, int n) {
    int i, j;
    for (i = 0; i < n - 1; i++) {
        for (j = 0; j < n - i - 1; j++) {
            if (vacs[j].qty < vacs[j+1].qty) {
                Vaccine tmp = vacs[j];
                vacs[j] = vacs[j+1];
                vacs[j+1] = tmp;
            }
        }
    }
}

/* function: print_vaccine
 * -----------------------
 * Function to pretty print Vaccine struct
 * parameters:
 *   v: Vaccine struct to be printed
 */
static void print_vaccine(Vaccine v) {
    printf("%s (%s):\n", v.name, v.code);
    printf("accumulated distributed quantity = %d\n", v.qty);
}

/* function: show_dist_log
 * -----------------------
 * Print sorted accumulated vaccine quantities and distribution log
 */
void show_dist_log() {
    puts("SHOW DISTRIBUTION LOG");
    /* parse dist.txt into table */
    Table *table = parse_table(DIST_TABLE);
    if (!table) {
        puts("error: no dist.txt file.");
        puts("create the file with the first option in the main menu.");
        return;
    }
    /* Vaccine array to store accumulated quantities from table */
    Vaccine vacs[NVACS] = {
        {"Pfitzer", "PF", 0},
        {"Sinovac", "SV", 0},
        {"AstraZeneca", "AZ", 0},
        {"Sputnik V", "SP", 0},
        {"CanSinoBio", "CS", 0}
    };

    /* update vacs array */
    int i;
    for (i = 0; i < table->nrows; i++) {
        char *code = table->items[i][DIST_VAC];
        int qty = strtoint(table->items[i][DIST_QTY]);

        if (strcmp(code, "PF") == 0) vacs[0].qty += qty;
        else if (strcmp(code, "SV") == 0) vacs[1].qty += qty;
        else if (strcmp(code, "AZ") == 0) vacs[2].qty += qty;
        else if (strcmp(code, "SP") == 0) vacs[3].qty += qty;
        else vacs[4].qty += qty;
    }
    /* sort vacs */
    bubble_sort_acc(vacs, NVACS);

    /* print results */
    puts("ACCUMULATED QUANTITIES:");
    for (i = 0; i < NVACS; i++) {
        printf("%d. ", i + 1);
        print_vaccine(vacs[i]);
    }
    puts("\nDISTRIBUTION LOG:");
    print_table(table);

    /* cleanup */
    table_free(table);
}

/* SHOW DISTRIBUTION LOG FUNCTIONS */


/* main entry point fo the program */
void main_menu() {
    while (1) {
        /* clear screen */
        system("clear || cls");
        /* print options */
        puts("VACCINE INVENTORY MANAGEMENT SYSTEM"); 
        puts("1. Create Inventory\n2. Stock Up Vaccine");
        puts("3. Distribute Vaccine\n4. Search Vaccine Quantity");
        puts("5. Show Distribution Log\n6. Exit");

        /* get choice from user */
        char inp[8];
        int choice;
        do {
            printf("enter choice: ");
            fgets(inp, 8, stdin);
            choice = strtoint(inp);
        } while (!(choice >= 1 && choice <= 6));
        putchar('\n');

        /* switch based on choice */
        switch (choice) {
            case 1: create_inventory(); break;
            case 2: stock_up_vaccine(); break;
            case 3: distribute_vaccine(); break;
            case 4: search_vaccine(); break;
            case 5: show_dist_log(); break;
            default: return;
        }
        wait_for_input();
    } 
}

