#include <iostream>
#include "linkedlist.h"
#include "utils.h"

using namespace std;

Node::Node() {
}

Node::Node(Tutor *t) {
    data = t;
    next = NULL;
}

Node::~Node() {
    delete data;
}

LinkedList::LinkedList() {
    head = tail = NULL;
    length = 0;
}

LinkedList::~LinkedList() {
    while (head) {
        Node *tmp = head->next;
        delete head;
        head = tmp;
    }
}

size_t LinkedList::get_length() {
    return length;
}

void LinkedList::push_at_end(Tutor *t) {
    Node *n = new Node(t);
    ++length;
    if (!head) {
        head = tail = n;
        return;
    }
    tail = tail->next = n;
}

void LinkedList::display_all() {
    Node *cur = head;
    while (cur) {
        cur->data->display();
        cout << endl;
        cur = cur->next;
    }
}

void LinkedList::modify_phone(int id, string phone) {
    Node *cur = head;
    while (cur) {
        Tutor *t = cur->data;
        if (id == t->id) {
            t->phone = phone;
            return;
        }
        cur = cur->next;
    }
}

void LinkedList::modify_address(int id, string addr) {
    Node *cur = head;
    while (cur) {
        Tutor *t = cur->data;
        if (id == t->id) {
            t->address = addr;
            return;
        }
        cur = cur->next;
    }
}

void LinkedList::terminate(int id) {
    Node *cur = head;
    while (cur) {
        Tutor *t = cur->data;
        if (id == t->id) {
            t->date_terminated = get_cur_date();
            return;
        }
        cur = cur->next;
    }
}

void LinkedList::del(int id) {
    Node *cur = head, *prev = NULL;
    while (cur) {
        Tutor *t = cur->data;
        if (id == t->id) {
            --length;
            prev ? prev->next = cur->next : head = head->next;
            delete cur;
            return;
        }
        prev = cur;
        cur = cur->next;
    }
}

Node *LinkedList::find_mid(Node *head) {
    Node *slow = head, *fast = head->next;
    while (fast && fast->next) {
        slow = slow->next;
        fast = fast->next->next;
    }
    return slow;
}

Node *LinkedList::merge(Node *a, Node *b, SortBy method) {
    Node *merged = new Node();
    Node *tmp = merged;

    switch (method) {
        case SORTBY_ID:
            while (a && b) {
                tmp->next = a->data->id < b->data->id ? a : b;
                a->data->id < b->data->id ? a = a->next : b = b->next;
                tmp = tmp->next;
            }
            break;
        case SORTBY_HOURLY_RATE:
            while (a && b) {
                tmp->next = a->data->hourly_rate < b->data->hourly_rate ? a : b;
                a->data->hourly_rate < b->data->hourly_rate ? a = a->next : b = b->next;
                tmp = tmp->next;
            }
            break;
        case SORTBY_RATING:
            while (a && b) {
                tmp->next = a->data->rating < b->data->rating ? a : b;
                a->data->rating < b->data->rating ? a = a->next : b = b->next;
                tmp = tmp->next;
            }
            break;
    }

    // if a & b is different length
    while (a) {
        tmp->next = a;
        a = a->next;
        tmp = tmp->next;
    }
    while (b) {
        tmp->next = b;
        b = b->next;
        tmp = tmp->next;
    }

    return merged->next;
}

Node *LinkedList::mergesort(Node *head, SortBy method) {
    if (!head || !head->next) return head;

    Node *mid = find_mid(head);
    Node *head2 = mid->next;
    mid->next = NULL;
    Node *new_head1 = mergesort(head, method);
    Node *new_head2 = mergesort(head2, method);

    return merge(new_head1, new_head2, method);
}

void LinkedList::sort_by_id() {
    head = mergesort(head, SORTBY_ID);
}

void LinkedList::sort_by_hourly_rate() {
    head = mergesort(head, SORTBY_HOURLY_RATE);
}

void LinkedList::sort_by_rating() {
    head = mergesort(head, SORTBY_RATING);
}

Tutor *LinkedList::linear_search_id(int t) {
    Node *cur = head;
    while (cur) {
        if (t == cur->data->id) return cur->data;
        cur = cur->next;
    }
    return NULL;
}

List *LinkedList::linear_search_rating(int rating) {
    List *res = new LinkedList();
    Node *cur = head;
    while (cur) {
        if (rating == cur->data->rating) res->push_at_end(cur->data);
        cur = cur->next;
    }
    return res->get_length() == 0 ? NULL : res;
}

Tutor *LinkedList::get_by_id(int id) {
    return linear_search_id(id);
}

List *LinkedList::get_by_rating(int rating) {
    return linear_search_rating(rating);
}

