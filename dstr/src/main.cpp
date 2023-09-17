#include <iostream>

#include "menu.h"
#include "tutor.h"
#include "utils.h"
#include "arraylist.h"
#include "linkedlist.h"

using namespace std;

int GLOBAL_ID = 5;

int main() {
    Tutor *a = new Tutor(
        1, "John Doe", "01928374827", "Wall street", "11-04-2022",
        "19-05-2022",4, "T1", "Centre A", "S2", "Physics", 1); 
    Tutor *b = new Tutor(
        2, "Peter Parker", "9384938493", "Taman teknologi 5", "28-04-2021",
        "28-05-2021", 3, "T2", "Centre C", "S1", "Maths", 1); 
    Tutor *c = new Tutor(
        3, "Vin Diesel", "8837402837", "California", "01-02-2012",
        "02-01-2020", 2, "T3", "Centre A", "S1", "Maths", 2); 
    Tutor *d = new Tutor(
        4, "Stephen Strange", "1192837384", "Baker street 22", "20-01-2022",
        "-", 1, "T4", "Centre D", "S3", "Biology", 3); 

    cout << "Would you like to use an array or a linked list for this program?" << endl;
    cout << "1. Array\n2. LinkedList" << endl;

    int choice;
    do {
        choice = get_int_inp("Enter choice: ");
    } while (!(choice == 1 || choice == 2));

    List *l = choice == 1 ? (List *)new ArrayList() : (List *)new LinkedList();

    l->push_at_end(a);
    l->push_at_end(b);
    l->push_at_end(d);
    l->push_at_end(c);

    main_menu(l);
    return 0;
}

