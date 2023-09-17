#include <iostream>
#include "arraylist.h"
#include "utils.h"

using namespace std;

ArrayList::ArrayList() {
    capacity = ARRAY_INITIAL_CAPACITY;
    current = 0;
    tutors = new Tutor*[capacity];
}

ArrayList::~ArrayList() {
    for (int i = 0; i < (int)current; i++) {
        delete tutors[i];
    }
}

void ArrayList::resize() {
    capacity *= 2;
    Tutor **new_tutors = new Tutor*[capacity];
    for (int i = 0; i < (int)current; i++) new_tutors[i] = tutors[i];
    delete[] tutors;
    tutors = new_tutors;
}

size_t ArrayList::get_capacity() {
    return capacity;
}

size_t ArrayList::get_length() {
    return current;
}

void ArrayList::push_at_end(Tutor *t) {
    if (current == capacity) resize();
    tutors[current++] = t;
}

void ArrayList::display_all() {
    for (int i = 0; i < (int)current; i++) {
        tutors[i]->display();
        cout << endl;
    }
}

void ArrayList::modify_phone(int id, string phone) {
    for (int i = 0; i < (int)current; i++) {
        if (id == tutors[i]->id) {
            tutors[i]->phone = phone;
            return;
        }
    }
}

void ArrayList::modify_address(int id, string addr) {
    for (int i = 0; i < (int)current; i++) {
        if (id == tutors[i]->id) {
            tutors[i]->address = addr;
            return;
        }
    }
}

void ArrayList::terminate(int id) {
    for (int i = 0; i < (int)current; i++) {
        if (id == tutors[i]->id) {
            tutors[i]->date_terminated = get_cur_date();
            return;
        }
    }
}

void ArrayList::del(int id) {
    int del_id;
    for (int i = 0; i < (int)current; i++) {
        if (id == tutors[i]->id) {
            delete tutors[i];
            del_id = i;
            break;
        }
    }
    for (int i = del_id; i < (int)current; i++) tutors[i] = tutors[i + 1];
    tutors[--current] = NULL;
}

void ArrayList::swap(int i, int j) {
    Tutor *tmp = tutors[i];
    tutors[i] = tutors[j];
    tutors[j] = tmp;
}

int ArrayList::partition(int start, int end, SortBy method) {
    Tutor *pivot = tutors[end];
    int i = start - 1;
    switch (method) {
        case SORTBY_ID:
            for (int j = start; j <= end - 1; j++) {
                if (tutors[j]->id < pivot->id) swap(++i, j);
            }
            break;
        case SORTBY_HOURLY_RATE:
            for (int j = start; j <= end - 1; j++) {
                if (tutors[j]->hourly_rate < pivot->hourly_rate) swap(++i, j);
            }
            break;
        case SORTBY_RATING:
            for (int j = start; j <= end - 1; j++) {
                if (tutors[j]->rating < pivot->rating) swap(++i, j);
            }
            break;
    }
    swap(++i, end);
    return i;
}

void ArrayList::quicksort(int start, int end, SortBy method) {
    if (start >= end) return;
    int p = partition(start, end, method);
    quicksort(start, p - 1, method);
    quicksort(p + 1, end, method);
}

void ArrayList::sort_by_id() {
    quicksort(0, current - 1, SORTBY_ID);
}

void ArrayList::sort_by_hourly_rate() {
    quicksort(0, current - 1, SORTBY_HOURLY_RATE);
}

void ArrayList::sort_by_rating() {
    quicksort(0, current - 1, SORTBY_RATING);
}

Tutor *ArrayList::binary_search(int left, int right, int id) {
    while (left <= right) {
        int mid = left + (right - left) / 2;
        if (id == tutors[mid]->id) return tutors[mid];
        tutors[mid]->id < id ? left = mid + 1 : right = mid - 1;
    }
    return NULL;
}

List *ArrayList::linear_search(int rating) {
    List *res = new ArrayList();
    for (int i = 0; i < (int)current; i++) {
        Tutor *cur = tutors[i];
        if (rating == cur->rating) res->push_at_end(cur);
    }
    return res->get_length() == 0 ? NULL : res;
}

Tutor *ArrayList::get_by_id(int id) {
    this->sort_by_id(); // binary search needs sorted lists
    return binary_search(0, current - 1, id);
}

List *ArrayList::get_by_rating(int rating) {
    return linear_search(rating);
}

