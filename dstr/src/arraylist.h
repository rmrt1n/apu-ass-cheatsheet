#ifndef ARRAYLIST_H
#define ARRAYLIST_H

#include "list.h"

#define ARRAY_INITIAL_CAPACITY 10

class ArrayList: public List {
  private:
    Tutor **tutors;
    size_t current, capacity;

    // resize array when full
    void resize(void);
    // helper function for sorting
    void swap(int i, int j);
    // main worker in quicksort
    int partition(int start, int end, SortBy method);
    void quicksort(int start, int end, SortBy method);
    // overload when want to search with other param
    Tutor *binary_search(int left, int right, int id);
    List *linear_search(int rating);

  public:
    ArrayList();
    ~ArrayList();
    size_t get_capacity();

    // list functions
    virtual size_t get_length();
    virtual void push_at_end(Tutor *t);
    virtual void display_all();
    virtual void modify_phone(int id, std::string phone);
    virtual void modify_address(int id, std::string addr);
    virtual void terminate(int id);
    virtual void del(int id);
    virtual void sort_by_id();
    virtual void sort_by_hourly_rate();
    virtual void sort_by_rating();
    virtual Tutor *get_by_id(int id);
    virtual List *get_by_rating(int rating);
};

#endif
