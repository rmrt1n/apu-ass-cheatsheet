#ifndef TUTOR_H
#define TUTOR_H

#include <string>

struct Tutor {
    int id;
    std::string name, phone, address;
    std::string date_joined, date_terminated;
    double hourly_rate;
    std::string tcentre_code, tcentre_name;
    std::string subject_code, subject_name;
    int rating;

    Tutor();

    Tutor(int id,
          std::string name,
          std::string phone,
          std::string address,
          std::string date_joined,
          std::string date_terminated,
          double hourly_rate,
          std::string tcentre_code,
          std::string tcentre_name,
          std::string subject_code,
          std::string subject_name,
          int rating);

    void display(void);
};

#endif
