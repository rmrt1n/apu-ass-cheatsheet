#define _CRT_SECURE_NO_WARNINGS

#include <iostream>
#include <chrono>
#include <ctime>

#include "menu.h"
#include "list.h"
#include "utils.h"

#define USERNAME "p"
#define PASSWORD "p"

using namespace std;

void add_menu(List *l) {
    clear();
    double hourly_rate;
    int rating;

    cout << "ADD TUTOR" << endl;
    cout << "==============" << endl;
    cout << "Enter tutor details:" << endl;

    string name = get_not_empty_string("Name: ");
    string phone = get_not_empty_string("Phone: ");
    string addr = get_not_empty_string("Address: ");
    string date_joined = get_not_empty_string("Date Joined (in dd-mm-yyyy format): ");
    string date_terminated = getline_trim("Date Terminated (leave blank to skip): ");
    string tcentre_code = get_not_empty_string("Tuition Centre Code: ");
    string tcentre_name = get_not_empty_string("Tuition Centre Name: ");
    string subject_code = get_not_empty_string("Subject Code: ");
    string subject_name = get_not_empty_string("Subject Name: ");
    do {
        hourly_rate = get_dbl_inp("Hourly Rate (value between 40-80): ");
    } while (!(hourly_rate >= 40 && hourly_rate <= 80));
    do {
        rating = get_int_inp("Rating (value between 1-5): ");
    } while (!(rating >= 1 && rating <= 5));
    if (date_terminated.length() == 0) date_terminated = "-";

    Tutor *t = new Tutor(
        GLOBAL_ID++, name, phone, addr, date_joined,date_terminated, 
        hourly_rate, tcentre_code, tcentre_name, subject_code, subject_name, rating);
    l->push_at_end(t);
    cout << "Tutor successfully added" << endl;
    wait();
}

void display_menu(List *l) {
    while (true) {
        clear();
        cout << "DISPLAY TUTORS" << endl;
        cout << "==============" << endl;
        cout << "1. Display sorted by ID ascending" << endl;
        cout << "2. Display sorted by hourly rate ascending" << endl;
        cout << "3. Display sorted by rating ascending" << endl;
        cout << "4. Go back" << endl;

        int choice = get_int_inp("Enter choice: ");
        switch(choice) {
            case 1:
                l->sort_by_id();
                l->display_all();
                wait();
                break;
            case 2:
                l->sort_by_hourly_rate();
                l->display_all();
                wait();
                break;
            case 3:
                l->sort_by_rating();
                l->display_all();
                wait();
                break;
            case 4:
                return;
            default:
                cout << "Invalid choice" << endl;
        }
    }
}

void search_menu(List *l) {
    while (true) {
        clear();
        cout << "SEARCH TUTORS" << endl;
        cout << "==============" << endl;
        cout << "1. Search tutor by ID" << endl;
        cout << "2. Search tutors by rating" << endl;
        cout << "3. Go back" << endl;

        // for use in switch
        int inp;
        Tutor *t;
        List *result;

        int choice = get_int_inp("Enter choice: ");
        switch (choice) {
            case 1:
                inp = get_int_inp("Enter tutor ID: ");
                t = l->get_by_id(inp);
                if (!t) {
                    cout << "Tutor with ID " << inp << " does not exist!" << endl;
                } else {
                    t->display();
                }
                wait();
                break;
            case 2:
                inp = get_int_inp("Enter tutor rating: ");
                result = l->get_by_rating(inp);
                if (!result) {
                    cout << "Tutors with rating " << inp << " do not exist!" << endl;
                } else {
                    result->display_all();
                }
                wait();
                break;
            case 3:
                return;
            default:
                cout << "Invalid choice" << endl;
        }
    }
}

void modify_menu(List *l) {
    clear();
    cout << "MODIFY TUTORS" << endl;
    cout << "==============" << endl;
    int id = get_int_inp("Enter tutor ID to modify: ");
    Tutor *t = l->get_by_id(id);
    if (!t) {
        cout << "Tutor with ID " << id << " does not exist! returning..." << endl;
        wait();
        return;
    }

    string phone = getline_trim("Enter new phone number (leave blank to skip): ");
    string addr = getline_trim("Enter new address (leave blank to skip): ");

    bool is_terminated = t->date_terminated != "-";
    string to_terminate;
    if (!is_terminated) {
        do {
            to_terminate = get_not_empty_string("Terminate tutor? (y/n): ");
        } while (to_terminate != "y" && to_terminate != "n");
    }

    if (phone.length() > 0) l->modify_phone(id, phone);
    if (addr.length() > 0) l->modify_address(id, addr);
    if (!is_terminated && to_terminate == "y") l->terminate(id);

    cout << "Tutor successfully modified" << endl;
    t->display();
    wait();
}

void delete_menu(List *l) {
    clear();
    cout << "DELETE TUTORS" << endl;
    cout << "==============" << endl;
    int id = get_int_inp("Enter tutor ID to delete: ");
    Tutor *t = l->get_by_id(id);
    if (!t) {
        cout << "Tutor with ID " << id << " does not exist! returning..." << endl;
        wait();
        return;
    }
    if (t->date_terminated == "-") {
        cout << "Tutor with ID " << id << " haven't been terminated! returning..." << endl;
        wait();
        return;
    }
    // get month from tutor data
    int y2, m2, d;
    sscanf(t->date_terminated.c_str(), "%d-%d-%d", &d, &m2, &y2);
    // get current month
    time_t now_time = chrono::system_clock::to_time_t(chrono::system_clock::now());
    tm local = *localtime(&now_time);
    // https://www.codevscolor.com/c-print-current-time-day-month-year
    int y1 = local.tm_year + 1900, m1 = local.tm_mon + 1; 
    int duration = mon_duration(y1, m1, y2, m2);

    if (duration < 6) {
        cout << "Tutor with ID " << id 
             << " was terminated less than 6 months ago! returning..." << endl;
        wait();
        return;
    }

    l->del(id);
    cout << "Tutor successfully deleted" << endl;
    wait();
}

void login() {
    string username, password;
    clear();
    cout << "LOGIN" << endl;
    do {
        username = get_not_empty_string("Enter username: ");
        password = get_not_empty_string("Enter password: ");
    } while (!(username == USERNAME && password == PASSWORD));
}

void main_menu(List *l) {
    login();
    while (true) {
        clear();
        cout << "EXCEL TUITION CENTRE" << endl;
        cout << "==============" << endl;
        cout << "1. Add tutor\n2. Display tutors\n3. Search tutor" << endl;
        cout << "4. Modify tutor\n5. Delete tutor\n6. Exit" << endl;

        int choice = get_int_inp("Enter choice: ");

        switch (choice) {
            case 1: add_menu(l); break;
            case 2: display_menu(l); break;
            case 3: search_menu(l); break;
            case 4: modify_menu(l); break;
            case 5: delete_menu(l); break;
            case 6:
                cout << "Exiting program..." << endl;
                return;
            default:
                cout << "Invalid choice" << endl;
        }
    }
}
