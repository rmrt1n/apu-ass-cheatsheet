#include <iostream>
#include <regex>
#include <limits>
#include <chrono>
#include <ctime>

using namespace std;

void clear() {
    cout << "\033[2J\033[1;1H";
}

string getline_trim(string msg) {
    cout << msg;
    string inp;
    getline(cin, inp);
    return regex_replace(inp, regex("(^[ ]+)|([ ]+$)"),"");
}

string get_not_empty_string(string msg) {
    string inp;
    do {
        inp = getline_trim(msg);
    } while (inp.length() == 0);
    return inp;
}

int get_int_inp(string msg) {
    int t;
    cout << msg;
    cin >> t;
    while (cin.fail()) {
        cin.clear();
        cin.ignore(numeric_limits<streamsize>::max(), '\n');
        cout << msg;
        cin >> t;
    }
    cin.ignore(numeric_limits<streamsize>::max(), '\n');
    return t;
}

double get_dbl_inp(string msg) {
    double t;
    cout << msg;
    cin >> t;
    while (cin.fail()) {
        cin.clear();
        cin.ignore(numeric_limits<streamsize>::max(), '\n');
        cout << msg;
        cin >> t;
    }
    return t;
}

void wait() {
    string tmp;
    cout << "Press any key to continue: ";
    getline(cin, tmp);
}

// for this date1 must be after date2
int mon_duration(int y1, int m1, int y2, int m2) {
    if (y1 == y2) return m1 - m2;
    return 12 * (y1 - y2 - 1) + (12 - m2) + m1;
}

string get_cur_date() {
    time_t now_time = chrono::system_clock::to_time_t(chrono::system_clock::now());
    tm local = *localtime(&now_time);
    return (local.tm_mday < 10 ? "0" : "") +
           to_string(local.tm_mday) + "-" +
           (local.tm_mon < 10 ? "0" : "") + 
           to_string(local.tm_mon + 1) + "-" +
           to_string(local.tm_year + 1900);
}
