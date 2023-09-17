#ifndef LIST_H
#define LIST_H

#include <string>
#include "tutor.h"

enum SortBy { SORTBY_ID, SORTBY_HOURLY_RATE, SORTBY_RATING };

class List {
  public:
    virtual size_t get_length() = 0;

    // insert
    virtual void push_at_end(Tutor *t) = 0;

    // display
    virtual void display_all() = 0;

    // modify
    virtual void modify_phone(int id, std::string phone) = 0;
    virtual void modify_address(int id, std::string addr) = 0;
    virtual void terminate(int id) = 0;

    // delete
    virtual void del(int id) = 0;

    // sort
    virtual void sort_by_id() = 0;
    virtual void sort_by_hourly_rate() = 0;
    virtual void sort_by_rating() = 0;

    // search
    virtual Tutor *get_by_id(int id) = 0;
    virtual List *get_by_rating(int rating) = 0;
};

#endif
