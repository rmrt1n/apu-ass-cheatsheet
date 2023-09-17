#ifndef LINKEDLIST_H
#define LINKEDLIST_H

#include "list.h"

struct Node {
    Tutor *data;
    Node *next;

    Node();
    Node(Tutor *t);
    ~Node();
};

class LinkedList: public List {
  private:
    Node *head, *tail;
    size_t length;

    // helper for sorting
    Node *find_mid(Node *head);
    // main worker in mergesort
    Node *merge(Node *a, Node *b, SortBy method);
    Node *mergesort(Node *head, SortBy method);
    Tutor *linear_search_id(int id);
    List *linear_search_rating(int rating);

  public:
    LinkedList();
    ~LinkedList();

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
