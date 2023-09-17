#ifndef UTILS_H
#define UTILS_H

void clear(void);
std::string getline_trim(std::string msg);
std::string get_not_empty_string(std::string msg);
int get_int_inp(std::string msg);
double get_dbl_inp(std::string msg);
void wait(void);
int mon_duration(int y1, int m1, int y2, int m2);
std::string get_cur_date(void);

#endif
