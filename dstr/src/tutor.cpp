#include <iostream>
#include "tutor.h"

using namespace std;

Tutor::Tutor() {}

Tutor::Tutor(int id,
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
             int rating) {
    this->id = id;
    this->name = name;
    this->phone = phone;
    this->address = address;
    this->date_joined = date_joined;
    this->date_terminated = date_terminated;
    this->hourly_rate = hourly_rate;
    this->tcentre_code = tcentre_code;
    this->tcentre_name = tcentre_name;
    this->subject_code = subject_code;
    this->subject_name = subject_name;
    this->rating = rating;
}

void Tutor::display() {
    for (int i = 0; i < 48; i++) cout << "=";
    cout << endl << "Tutor " << id << ": " << name << endl; 
    cout << "Phone: " << phone << ", Address: " << address << endl; 
    cout << "Date Joined: " << date_joined; 
    cout << ", Date Terminated: " << date_terminated << endl; 
    cout << "Tuition Centre: " << tcentre_name;
    cout << " (" << tcentre_code << ")" << endl; 
    cout << "Subject: " << subject_name << " (" << subject_code << ")" << endl; 
    cout << "Hourly Rate: " << hourly_rate << endl; 
    cout << "Rating: " << rating << endl; 
    for (int i = 0; i < 48; i++) cout << "=";
    cout << endl;
}
