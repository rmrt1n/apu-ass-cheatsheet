# Ryan Martin
# TP058091

from datetime import datetime
import hashlib
import os


### INPUT VALIDATION FUNCTIONS
def validate_empty_input(inp, value):
    '''asks for user input until not empty input is given

    parameters:
    inp (str): initial value to be checked
    value (str): value that is asked to the user. this is for input prompt

    returns:
    str: user's input string
    '''

    inp = inp.strip().lower()
    while inp == '':
        inp = input(value + ' must not be empty, enter ' + value + ': ').strip().lower()
    return inp


def validate_choice_input(inp, valid_choices, msg, empty=False):
    '''asks for user input until expected value is given

    parameters:
    inp (str): initial value to be checked
    valid_choices (List[str]): list of valid values for inp
    msg (str): message to be printed if input is invalid
    empty (bool): for cases where empty input is acceptable

    returns:
    str: user's input string or empty string if empty=True
    '''

    inp = inp.strip().lower()
    if empty and inp == '':
        return ''

    while inp not in valid_choices:
        inp = input(msg).strip().lower()
    return inp


def validate_number_input(inp, empty=False):
    '''asks for number input until a valid value is given

    parameters: 
    inp (str): initial value to be checked (all characters must be digits)
    empty (bool): for cases where empty input is acceptable

    returns:
    str: user's input string or empty if empty=True
    '''

    while True:
        inp = inp.strip()
        if empty and inp == '':
            return ''
        
        if inp.isdigit():
            return inp
        else:
            inp = input('invalid value, please enter a number: ')


def validate_date_input(inp, empty=False):
    '''asks for date input until a valid value is given

    valid date is in DD-MM-YYYY format. this means single digit numbers must
    be padded to be accepted. doesn't take leap years into account

    parameters:
    inp (str): initial value to be checked
    empty (bool): for cases where empty input is acceptable

    returns:
    str: user's input string or empty if empty=True
    '''

    while True:
        inp = inp.strip()
        if empty and inp == '':
            return ''
        elif inp == '':
            return get_date()
        else:
            if len(inp) == 10 and inp[2] == '-' and inp[5] == '-':
                dup = inp.split('-')
                day, month, year = dup[0], dup[1], dup[2]
                if day.isdigit() and month.isdigit() and year.isdigit():
                    iday, imonth = int(day), int(month)
                    if (iday >= 1 and iday <= 31) and (imonth >= 1 and imonth <= 12):
                        return inp
            inp = input('invalid date, enter date (in DD-MM-YYYY format), leave blank for current date: ')


def validate_time_input(inp, empty=False):
    '''asks for time input until a valid value is given

    valid time is in HH:MM 24-hour format. this means single digit numbers must
    be padded to be accepted. input must also be in between 08:00 and 16:00 
    inclusive. sport centre opens from 08:00 until 17:00.

    parameters:
    inp (str): initial value to be checked
    empty (bool): for cases where empty input is acceptable

    returns:
    str: user's input string or empty if empty=True
    '''

    while True:
        inp = inp.strip()
        if empty and inp == '':
            return ''

        if len(inp) == 5 and inp[2] == ':' \
           and inp[:2].isdigit() and inp[3:].isdigit():
            ihour = int(inp[:2])
            imin = int(inp[3:])
            if (ihour >= 0 and ihour <= 23) and (imin >= 0 and imin <= 60):
                seconds = ihour * 3600 + imin * 60
                # values below are number of seconds from midnight until 08:00 and 16:00
                if seconds >= 28800 and seconds <= 57600: 
                    return inp
                else:
                    inp = input('invalid time, please enter time between 08:00-16:00: ')
            else:
                inp = input('invalid time, enter time in 24-hour format (HH:MM): ')
        else:
            inp = input('invalid time, enter time in 24-hour format (HH:MM): ')


def validate_fee_input(inp, empty=False):
    '''asks for fee input until a valid value is given

    valid fee value is an integer between 100 and 500 inclusive.

    parameters:
    inp (str): initial value to be checked
    empty (bool): for cases where empty input is acceptable

    returns:
    str: user's input string or empty if empty=True
    '''

    while True:
        inp = inp.strip()
        if empty and inp == '':
            return ''
        
        if inp.isdigit():
            iinp = int(inp)
            if iinp >= 100 and iinp <= 500:
                return inp
            else:
                inp = input('invalid fee, allowed values are between 100-500: ')
        else:
            inp = input('invalid fee, please enter a valid value: ')


def validate_duration_input(inp, time, empty=False):
    '''asks for duration input until a valid value is given

    duration unit is hours. only takes in integer values. this function checks
    if class will exceed sport centre closing time.

    parameters:
    inp (str): initial value to be checked
    time (str): start time of class
    empty (bool): for cases where empty input is acceptable

    returns:
    str: user's input string or empty if empty=True
    '''

    while True:
        inp = inp.strip()
        if (time == '') or (empty and inp == ''):
            return inp

        time = hhmm_to_seconds(time)
        if inp.isdigit():
            iinp = int(inp)
            if (iinp >= 1 and iinp <= 9) and iinp * 3600 + time <= 61200:
                return inp
        print('invalid duration, duration must be between 1-9 and class must not pass 17:00')
        inp = input('enter valid duration: ')


def validate_schedule(schedule_id, date, time, duration, coach_id, empty=False):
    '''checks if new class to be added collides with other classes

    parameters:
    schedule_id (str): schedule id to modify
    date (str): date of new class
    time (str): start time of new class
    duration (int): duration of new class
    coach_id (int): coach id of new class
    empty (bool): False to allow empty inputs

    returns:
    bool: True if class is valid else False
    '''

    # read records from file and get indexes for columns to be checked
    schedule_records = read_csv('data/schedule.txt')
    date_index = get_column_index('schedule_date', schedule_records)
    coach_id_index = get_column_index('schedule_coach_id', schedule_records)
    time_index = get_column_index('schedule_time', schedule_records)
    duration_index = get_column_index('schedule_duration', schedule_records)

    toskip = 0
    # get start and end time of new class
    start_time = hhmm_to_seconds(time)
    end_time = start_time + int(duration) * 3600
    for i in range(len(schedule_records)):
        if schedule_records[i][0] == schedule_id:
            toskip = str(schedule_records[i][0])
            break

    for i in schedule_records[1:]: # skip header
        # checks every record with same date & coach for schedule collision.
        # collision happens when new class times lies in between old class times.
        if i[0] != toskip:
            if date == i[date_index] and coach_id == i[coach_id_index]:
                lower_bound = hhmm_to_seconds(i[time_index])
                upper_bound = lower_bound + int(i[duration_index]) * 3600
                if not ((start_time < lower_bound and end_time < lower_bound) \
                   or (start_time > upper_bound and end_time > upper_bound)):
                    return False
    return True


def validate_password(inp, empty=False):
    '''asks for password input until a valid value is given

    valid password must be atleast 8 characters

    parameters:
    inp (str): initial value to be checked
    empty (bool): for cases where empty input is acceptable

    returns:
    str: user's input string or empty if empty=True
    '''

    inp = inp.strip()
    if empty == 1 and inp == '':
        return ''

    while len(inp) < 8:
        print('password must be atleast 8 characters!')
        inp = input('enter password: ').strip()
    return inp


def validate_login(email, password):
    '''checks if user is registered and password is correct

    parameters:
    email (str): email of user to be checked
    password (str): password of user to be checked

    returns:
    bool: True if user is registered else False
    '''

    # reads student records and get email and password columns
    student_records = read_csv('data/student.txt')
    student_records = select_columns_from_table(['student_email', 'student_password'], student_records)

    for i in student_records:
        if i[0] == email and hash_password(password) == i[1]:
            return True
    return False

def validate_rating(coach_id, student_id, records):
    '''returns True if student has already rated coach

    parameters:
    coach_id (str): coach id of coach to rate
    student_id (str): student id of student wanting to rate coach
    records (list): 2d list of records to check

    return:
    bool: True if student with student_id has rated coach with coach_id, else
          False
    '''

    # get indexes
    coach_id_index = get_column_index('rating_coach_id', records)
    student_id_index = get_column_index('rating_student_id', records)

    for i in records:
        if i[coach_id_index] == coach_id and i[student_id_index] == student_id:
           return True
    return False
### INPUT VALIDATION FUNCTIONS


### RECORDS MANIPULATION FUNCTIONS
def read_csv(filename):
    '''reads csv file and returns table (list of list or 2d array/list)
    
    parameters:
    filename (str): relative path of file to be read

    returns:
    list: 2d list of file contents
    '''

    records = []
    with open(filename) as file:
        for i in file:
            splitted = i.strip().split(',')
            for j in range(len(splitted)):
                # replaces empty columns with '-' (for pretty_print function)
                if splitted[j] == '':
                    splitted[j] = '-'
            records.append(splitted)
    return records


def get_column_index(col_name, records):
    '''returns list index of record with a specific column name
    
    parameters:
    col_name (str): name of column to be searched
    records (list): 2d list of record to be searched

    returns:
    int: list index of column
    '''

    header = records[0]
    for i in range(len(header)):
        if header[i] == col_name:
            return i


def select_column_from_table(col_name, records):
    '''returns column with name of col_name

    parameters:
    col_name (str): name of column to be selected
    records (list): 2d list of records to be queried from

    returns:
    list: column from records as a list
    '''

    index = get_column_index(col_name, records)
    result = []
    for i in records:
        result.append(i[index])
    return result


def select_columns_from_table(col_list, records):
    '''returns a list of columns from records
    
    just like select_column_from_table, only this function takes in a list of
    column names to search for, and returns a 2d list of those columns.

    parameters:
    col_list (list): list of column names to be selected
    records (list): 2d list of records to be queried from

    returns:
    list: 2d list of selected columns
    '''

    indices = []
    for i in col_list:
        indices.append(get_column_index(i, records))

    result = []
    for i in records:
        new_record = []
        for j in indices:
            new_record.append(i[j])
        result.append(new_record)
    return result


def select_column_from_table_where(target_col, query_col, query, records):
    '''returns column items that fulfill a condition from another column

    selects items from target_col, only if the value in query_col is equal to query

    parameters:
    target_col (str): column name to be selected
    query_col (str): column name where condition to be fulfilled
    query (str): desired value from query_col
    records (list): 2d list of records to be queried from

    returns:
    list: column from records in a list
    '''

    target_index = get_column_index(target_col, records)
    query_index = get_column_index(query_col, records)
    result = []
    for i in records:
        if i[query_index] == query:
            result.append(i[target_index])
    return result


def select_row_from_table_where(col_name, query, records):
    '''returns row from table that fulfills a condition

    selects the rows with value of query in column with col_name

    parameters:
    col_name (str): name of column to be selected
    query (str): desired value from col_name
    records (list): 2d list of records to be queried from

    returns:
    list: 2d list of rows that fulfill condition
    '''

    index = get_column_index(col_name, records)
    result = [records[0]]
    for i in records:
        if i[index] == query:
            result.append(i)
    return result


def write_record(record, filename, interactive=True):
    '''append new record to filename
    
    parameters:
    record (list): record (row) to append to records
    filename (str): filename of records to append to
    interactive (bool): set False if pause after write is not needed
    '''

    record = ','.join(record)
    with open(filename, 'a') as file:
        file.write(record + '\n')
    if interactive:
        input('Succesfully added record. Press enter to continue: ')


def write_records(records, filename, interactive=True):
    '''writes new records over old records
    
    parameters:
    records (list): new records to write
    filename (str): filename of records to write to
    interactive (bool): if True will print prompt after write
    '''

    for i in range(len(records)):
        for j in range(len(records[i])):
            # change '-' to empty string
            if records[i][j] == '-':
                records[i][j] = ''
        records[i] = ','.join(records[i]) + '\n'

    with open(filename, 'w') as file:
        file.writelines(records)

    if interactive:
        input('Succesfully modified record. Press enter to continue: ')


def bubble_sort_records(col_name, records):
    '''sort records by specific column

    parameters:
    col_name (str): name of column to be sorted
    records (list): 2d list of record to be sorted

    returns:
    list: 2d list of sorted records
    '''

    index = get_column_index(col_name, records)
    sorted_records = [records[0]]
    records = records[1:]

    n = len(records)
    for i in range(n - 1):
        for j in range(n - i - 1):
            if records[j][index] > records[j + 1][index]:
                temp =  records[j + 1]
                records[j + 1] = records[j]
                records[j] = temp
    return sorted_records + records


def reverse_records(records):
    '''returns reversed records not including header

    parameters:
    records (list): 2d list of records to be reversed

    returns:
    list: reversed records
    '''

    return [records[0]] + records[1:][::-1]


def modify_record(new_record, records):
    '''replaces record in records with new_record if their ids match
    
    parameters:
    new_record (list): new record values lsit
    records (list): 2d list of records to modify

    returns:
    list: 2d list of modified records
    '''

    modified_records = records
    for i in range(1, len(records)):
        if records[i][0] == new_record[0]:
            for j in range(1, len(new_record)):
                if new_record[j] != '':
                    modified_records[i][j] = new_record[j]
            break
    return modified_records


def left_join_records_on(records1, col1, records2, col2):
    '''join records1 and records2 where col1 matches col2

    parameters:
    records1 (list): 2d list of records to join (left)
    col1 (str): column name to match (left)
    records2 (list): 2d list of records to join (right)
    col2 (str): column name to match (left)

    returns:
    list: joined records
    '''

    # get column indexes
    col1_index = get_column_index(col1, records1)
    col2_index = get_column_index(col2, records2)

    result = [records1[0] + records2[0]]
    for i in records1[1:]:
        for j in records2[1:]:
            if i[col1_index] == j[col2_index]:
                result.append(i + j)
    return result
### RECORDS MANIPULATION FUNCTIONS


### HELPER FUNCTIONS
def hhmm_to_seconds(time):
    '''changes time string into seconds after 00:00

    parameters:
    time (str): time in HH:MM 24-hour format

    returns:
    int: number of seconds after 00:00 until time
    '''

    return int(time[:2]) * 3600 + int(time[3:]) * 60


def get_date():
    now = datetime.now()
    return now.strftime('%d-%m-%Y')


def hash_password(password):
    '''hash password using md5 algorithm

    parameters:
    password (str): string to hash

    returns:
    str: hashed password
    '''

    return hashlib.md5(password.encode()).hexdigest()


def int_list_sum(ls):
    '''returns sum of integers in list

    parameters:
    ls (list): list of integers

    returns:
    int: sum of items in list
    '''

    result = 0
    for i in ls:
        result += i
    return result


def get_pretty_schedule(booked=False):
    '''returns complete schedule with useful information
    
    joins schedule, coach, sport, and sport centre records and selects columns
    that contain useful information only

    parameters:
    booked (bool): if True, includes schedule_booked_ids column

    returns:
    list: 2d list of records
    '''

    # reads all necessary files
    schedule_records = read_csv('data/schedule.txt')
    coach_records = read_csv('data/coach.txt')
    sport_records = read_csv('data/sport.txt')
    sport_centre_records = read_csv('data/sport-centre.txt')

    # join records
    compound_records = left_join_records_on(schedule_records, 'schedule_coach_id',
                                            coach_records, 'coach_id')
    compound_records = left_join_records_on(compound_records, 'coach_sport_id',
                                            sport_records, 'sport_id')
    compound_records = left_join_records_on(compound_records, 'coach_sport_centre_id',
                                            sport_centre_records, 'sport_centre_id')

    columns = ['schedule_id', 'schedule_date', 'schedule_time',
               'schedule_duration', 'coach_name', 'sport_name', 'sport_centre_name']

    if booked:
        columns.append('schedule_booked_ids')
    records = select_columns_from_table(columns, compound_records)
    return records


def pretty_print(records, interactive=True):
    '''prints records in a pretty format
    
    parameters:
    records (list): 2d list of records to print
    interactive (bool): adds interactive input to pause after printing
    '''
    
    # gets longest lines for padding
    cols = len(records[0])
    longest_lines = [0] * cols
    for i in records:
        for j in range(cols):
            length = len(i[j])
            if length > longest_lines[j]:
                longest_lines[j] = length

    # print header
    for i in range(cols):
        print('| ' + records[0][i] + ' ' * (longest_lines[i] - len(records[0][i])) + ' ', end='')

    print('|')
    print('-' * (3 * cols + 1 + int_list_sum(longest_lines)))

    # print rest of records
    for i in records[1:]:
        for j in range(cols):
            print('| ' + i[j] + ' ' * (longest_lines[j] - len(i[j])) + ' ', end='')
        print('|')

    if interactive:
        input('press enter to continue: ')


def is_booked(schedule_id, records):
    '''checks if class is already booked

    parameters:
    schedule_id (str): id of class or schedule to check
    records (list): 2d list of schedule records. records have been added a new
                    field called booked, which can be either 'yes' or 'no'.

    returns:
    bool: True if class is booked, else False
    '''

    schedule_id_index = get_column_index('schedule_id', records)
    booked_index = get_column_index('booked', records)
    for i in records:
        if i[schedule_id_index] == schedule_id and i[booked_index] == 'yes':
            return True
    return False


def change_schedule(activity, schedule_id, records, student_id):
    '''modify schedule by either adding or removing student with id

    parameters:
    activity (str): values can only be 'book' or 'cancel'
    schedule_id (str): id of record to change
    records (list): 2d list of records to change
    student_id (str): student id to add or remove from records
    '''
    booked_index = get_column_index('schedule_booked_ids', records)
    for i in range(len(records)):
        if records[i][0] == schedule_id:
            new_cell = records[i][booked_index].split(':')
            if activity == 'book':
                new_cell.append(student_id)
            else:
                new_cell.remove(student_id)
            records[i][booked_index] = ':'.join(new_cell)
    write_records(records, 'data/schedule.txt')


def get_new_rating(new_rating, old_rating, coach_rating, rating_count, rated):
    '''returns new rating of coach based on student's input

    parameters:
    new_rating (float): new rating inputted by student
    old_rating (float): previous rating by student. 0.0 if haven't rated before
    coach_rating (float): current coach rating
    rating_count (int): number of times coach has been rated before
    rated (bool): True if coach has been rated by the student, else False

    return:
    float: new value of coach rating
    '''
    if not rated and coach_rating == 0:
        rating =  new_rating
    elif not rated:
        rating = round((coach_rating + new_rating) / 2, 2)
    else:
        rating = coach_rating * rating_count - old_rating
        rating = round((rating + new_rating) / rating_count, 2)
    return rating
### HELPER FUNCTIONS

### MAIN FUNCTIONS
### MAIN FUNCTIONS

### ADMIN MENU FUNCTIONS
def admin_login():
    '''validate admin login
    
    admin password is hardcoded. only 5 tries is given. returns True if input
    matches password

    returns:
    bool: True if input matches password, else if failed 5 times
    '''

    os.system('clear')
    print('ADMIN LOGIN')
    inp = input('enter password: ')

    max_tries = 4
    while inp != 'password' and max_tries > 0:
        print('wrong password, you have ' + str(max_tries) + ' tries left')
        inp = input('enter password: ')
        max_tries -= 1

    if max_tries == 0:
        input('too many failed attempts, press enter to continue: ')
        return False
    return True


def admin_menu():
    '''main admin menu, asks for choice then executes choice'''

    while True:
        # print menu
        os.system('clear')
        print('ADMIN MENU')
        print('1. Add Records\n2. Display Records\n3. Search Specific Record')
        print('4. Sort Records\n5. Modify Records\n6. Quit')

        # get and validate input
        inp = input('enter choice: ')
        inp = validate_choice_input(inp, ['1', '2', '3', '4', '5', '6'],
                                    'invalid choice, enter a valid choice: ')

        # execute choice 
        if inp == '1':
            admin_add_records()
        elif inp == '2':
            admin_display_records()
        elif inp == '3':
            admin_search_records()
        elif inp == '4':
            admin_sort_records()
        elif inp == '5':
            admin_modify_records()
        else:
            return


def admin_add_records():
    '''admin menu to add records, asks user for which records to add'''

    while True:
        # print menu
        os.system('clear')
        print('ADD RECORDS')
        print('1. Add Coach Record\n2. Add Schedule Record')
        print('3. Add Sport Record\n4. Add Sport Centre Record')
        print('5. Return to Admin Menu')

        # get and validate input
        inp = input('enter choice: ')
        inp = validate_choice_input(inp, ['1', '2', '3', '4', '5'],
                                    'invalid choice, enter a valid choice: ')

        # execute choice
        if inp == '1':
            add_coach_record()
        elif inp == '2':
            add_schedule_record()
        elif inp == '3':
            add_sport_record()
        elif inp == '4':
            add_sport_centre_record()
        else:
            return


def admin_display_records():
    '''admin menu to display records, asks user for which records to display'''

    while True:
        # print menu
        os.system('clear')
        print('DISPLAY RECORDS')
        print('1. Display Coach Records\n2. Display Student Records')
        print('3. Display Schedule Records\n4. Display Sport Records')
        print('5. Display Sport Centre Records\n6. Return to Admin Menu')

        # get and validate input
        inp = input('enter choice: ')
        inp = validate_choice_input(inp, ['1', '2', '3', '4', '5', '6'],
                                    'invalid choice, enter a valid choice: ')
        
        # display records based on choice
        if inp == '1':
            record = read_csv('data/coach.txt')
        elif inp == '2':
            record = read_csv('data/student.txt')
        elif inp == '3':
            record = read_csv('data/schedule.txt')
        elif inp == '4':
            record = read_csv('data/sport.txt')
        elif inp == '5':
            record = read_csv('data/sport-centre.txt')
        else:
            return
        pretty_print(record)


def admin_search_records():
    '''admin menu to search  and display records, asks user for which records
    to search for
    '''

    while True:
        # print menu
        os.system('clear')
        print('SEARCH RECORDS')
        print('1. Search for Coach Record\n2. Search for Student Record')
        print('3. Search for Schedule Record\n4. Search for Sport Record')
        print('5. Search for Sport Centre Record\n6. Return to Admin Menu')

        # get and validate input
        inp = input('enter choice: ')
        inp = validate_choice_input(inp, ['1', '2', '3', '4', '5', '6'],
                                    'invalid choice, enter a valid choice: ')
        
        # search by choice
        if inp == '1':
            search_coach_records()
        elif inp == '2':
            search_student_records()
        elif inp == '3':
            search_schedule_records()
        elif inp == '4':
            search_sport_records()
        elif inp == '5':
            search_sport_centre_records()
        else:
            return


def admin_sort_records():
    '''admin menu to sort and display records, asks user for which records
    to sort
    '''

    while True:
        # print menu
        os.system('clear')
        print('DISPLAY SORTED RECORDS')
        print('1. Sort Coach by Coach ID\n2. Sort Coach by Name')
        print('3. Sort Coach by Hourly Fee\n4. Sort Sport by Hourly Fee\n5. Return to Admin Menu')

        # get and validate input
        inp = input('enter choice: ')
        inp = validate_choice_input(inp, ['1', '2', '3', '4', '5'],
                                    'invalid choice, enter a valid choice: ')
        
        # sort by choice
        if inp == '5':
            return
        else:
            # get and validate input for sorting ascending or not
            asc = input('display records in ascending order? [Y/n]: ')
            asc = validate_choice_input(asc, ['y', 'n'],
                                        'invalid choice, enter either a y or n: ')
            if inp == '1':
                sort_coach_by_id(asc)
            elif inp == '2':
                sort_coach_by_name(asc)
            elif inp == '3':
                sort_coach_by_fee(asc)
            else:
                sort_sport_by_fee(asc)


def admin_modify_records():
    '''admin menu to modify records, asks user for which records to modify'''

    while True:
        # print menu
        os.system('clear')
        print('MODIFY RECORDS')
        print('1. Modify Coach Records\n2. Modify Schedule Records')
        print('3. Modify Sport Records\n4. Modify Sport Centre Records')
        print('5. Return to Admin Menu')

        # get and validate input
        inp = input('enter choice: ')
        inp = validate_choice_input(inp, ['1', '2', '3', '4', '5'],
                                    'invalid choice, enter a valid choice: ')

        # modify by choice
        if inp == '1':
            modify_coach_records()
        elif inp == '2':
            modify_schedule_records()
        elif inp == '3':
            modify_sport_records()
        elif inp == '4':
            modify_sport_centre_records()
        else:
            return
### ADMIN MENU FUNCTIONS

### ADMIN HELPER FUNCTIONS
## ADMIN ADD RECORD FUNCTIONS
def add_coach_record():
    '''asks for coach information and appends to coach records'''

    # reads needed records
    coach_records = read_csv('data/coach.txt')
    sport_records = read_csv('data/sport.txt')
    sport_centre_records = read_csv('data/sport-centre.txt')

    # get and validate input
    name = input('enter coach name: ')
    name = validate_empty_input(name, 'coach name')

    date_joined = input('enter date joined (in DD-MM-YYYY format), leave blank for current date: ')
    date_joined = validate_date_input(date_joined)

    date_terminated = input('enter date terminated (leave blank to skip): ')
    date_terminated = validate_date_input(date_terminated, empty=True)

    phone= input('enter coach phone number: ')
    phone = validate_number_input(phone, empty=True)

    address = input('enter coach address: ')
    address = validate_empty_input(address, 'coach address')

    sport = input('enter sport name: ')
    sport_list = select_column_from_table('sport_name', sport_records)
    sport = validate_choice_input(sport, sport_list,
                                  'invalid sport, enter a valid sport: ')

    sport_centre = input('enter sport centre name: ')
    sport_centre_list = select_column_from_table('sport_centre_name', sport_centre_records)
    sport_centre = validate_choice_input(sport_centre, sport_centre_list,
                                         'invalid sport centre, enter a valid sport center: ')

    coach_id = len(select_column_from_table('coach_id', coach_records))
    sport_id = select_column_from_table_where('sport_id', 'sport_name', sport, sport_records)[0]
    sport_centre_id = select_column_from_table_where('sport_centre_id', 'sport_centre_name',
                                                     sport_centre, sport_centre_records)[0]
    coach_rating = '0' # only students can give rating

    # add record to file
    record = [str(coach_id), name, date_joined, date_terminated,
              phone, address, sport_id, sport_centre_id, coach_rating]
    write_record(record, 'data/coach.txt')


def add_schedule_record():
    '''asks for schedule information and appends to schedule records'''

    # reads needed records
    coach_records = read_csv('data/coach.txt')
    schedule_records = read_csv('data/schedule.txt')

    # get and validate input
    date = input('enter class date (in DD-MM-YYYY), leave blank for current date: ')
    date = validate_date_input(date)

    time = input('enter class time (24-hour format HH:MM): ')
    time = validate_time_input(time)

    duration= input('enter duration (between 1-9 hours): ')
    duration = validate_duration_input(duration, time)

    coach_id = input('enter coach id: ')
    coach_id_list = select_column_from_table('coach_id', coach_records)
    coach_id = validate_choice_input(coach_id, coach_id_list,
                                     'invalid coach id, enter valid coach id: ')

    # validate schedule
    okay = validate_schedule("0", date, time, duration, coach_id) 
    if okay:
        schedule_id = len(select_column_from_table('schedule_id', schedule_records))
        record = [str(schedule_id), date, time, duration, coach_id, '']
        write_record(record, 'data/schedule.txt')
    else:
        input('coach is already booked! press enter to continue: ')


def add_sport_record():
    '''asks for sport information and appends to sport records'''

    # reads needed records
    sport_records = read_csv('data/sport.txt')

    # get and validate input
    name = input('enter sport name: ')
    name = validate_empty_input(name, 'sport name')

    fee = input('enter hourly fee: ')
    fee = validate_fee_input(fee)

    sport_id = len(select_column_from_table('sport_id', sport_records))

    # add record to file
    record = [str(sport_id), name, fee]
    write_record(record, 'data/sport.txt')


def add_sport_centre_record():
    '''asks for sport center information and appends to sport centre records'''

    # reads needed records
    sport_centre_records = read_csv('data/sport-centre.txt')

    # get and validate input
    name = input('enter sport centre name: ')
    name = validate_empty_input(name, 'sport centre name')

    location = input('enter location: ')
    location = validate_empty_input(location, 'location')

    sport_centre_id = len(select_column_from_table('sport_centre_id', sport_centre_records))

    # add record to file
    record = [str(sport_centre_id), name, location]
    write_record(record, 'data/sport-centre.txt')
## ADMIN ADD RECORD FUNCTIONS

## ADMIN SEARCH RECORD FUNCTIONS
def search_records_by(col, qry, records):
    '''search and display records by a specific column

    parameters:
    col (str): column name to search by
    qry (str): message to show when asking for input
    records (list): 2d list of records to search
    '''

    query = input('enter ' + qry + ': ')
    query = validate_empty_input(query, qry)

    if query in select_column_from_table(col, records):
        records = select_row_from_table_where(col, query, records)
        pretty_print(records, interactive=False)
    else:
        print('there is no record with ' + qry + ' of ' + query)


def search_coach_records():
    '''searches and displays coach records based on certain columns'''
    
    while True:
        # print menu
        os.system('clear')
        print('SEARCH COACH RECORDS\nwhat do you want to search by ?')
        print('1. Coach ID\n2. Name\n3. Phone Number')
        print('4. Sport Name\n5. Sport Centre Name')
        print('6. Rating\n7. Go Back')

        # get and validate input
        inp = input('enter choice: ')
        inp = validate_choice_input(inp, ['1', '2', '3', '4', '5', '6', '7'],
                                    'invalid choice, enter a valid choice: ')
        
        # read needed records
        coach_records = read_csv('data/coach.txt')
        sport_records = read_csv('data/sport.txt')
        sport_centre_records = read_csv('data/sport-centre.txt')

        # search by choice
        if inp == '1':
            search_records_by('coach_id', 'coach id', coach_records)
        elif inp == '2':
            search_records_by('coach_name', 'coach name', coach_records)
        elif inp == '3':
            search_records_by('coach_phone', 'coach phone number', coach_records)
        elif inp == '4':
            coach_sport_records = left_join_records_on(coach_records, 'coach_sport_id',
                                                       sport_records, 'sport_id') 
            columns = ['coach_id', 'coach_name', 'coach_date_joined',
                       'coach_date_terminated', 'coach_phone', 'coach_address',
                       'coach_sport_id', 'coach_sport_centre_id', 'coach_rating',
                       'sport_name']
            coach_sport_records = select_columns_from_table(columns, coach_sport_records)
            search_records_by('sport_name', 'coach sport name', coach_sport_records)
        elif inp == '5':
            coach_sport_centre_records = left_join_records_on(coach_records, 'coach_sport_centre_id',
                                                              sport_centre_records, 'sport_centre_id') 
            columns = ['coach_id', 'coach_name', 'coach_date_joined',
                       'coach_date_terminated', 'coach_address',
                       'coach_sport_id', 'coach_sport_centre_id', 'coach_rating',
                       'sport_centre_name']
            coach_sport_centre_records = select_columns_from_table(columns, coach_sport_centre_records)
            search_records_by('sport_centre_name', 'coach sport centre', coach_sport_centre_records)
        elif inp == '6':
            search_records_by('coach_rating', 'coach rating', coach_records)
        else:
            return
        input('press enter to go back: ')


def search_student_records():
    '''searches and displays student records base on certain columns'''

    while True:
        # print menu
        os.system('clear')
        print('SEARCH STUDENT RECORDS\nwhat do you want to search by ?')
        print('1. Student ID\n2. Name\n3. Phone Number\n4. Email\n5. Go Back')

        # get and validate input
        inp = input('enter choice: ')
        inp = validate_choice_input(inp, ['1', '2', '3', '4', '5'],
                                    'invalid choice, enter a valid choice: ')
        
        # read needed records and execute by choice
        student_records = read_csv('data/student.txt')

        if inp == '1':
            search_records_by('student_id', 'student id', student_records)
        elif inp == '2':
            search_records_by('student_name', 'student name', student_records)
        elif inp == '3':
            search_records_by('student_phone', 'phone number', student_records)
        elif inp == '4':
            search_records_by('student_email', 'email', student_records)
        else:
            return
        input('press enter to go back: ')


def search_schedule_records():
    '''searches and displays schedule records base on certain columns'''
    
    while True:
        # print menu
        os.system('clear')
        print('SEARCH SCHEDULE RECORDS\nwhat do you want to search by ?')
        print('1. Date\n2. Time\n3. Duration\n4. Coach ID\n5. Go Back')

        # get and validate input
        inp = input('enter choice: ')
        inp = validate_choice_input(inp, ['1', '2', '3', '4', '5'],
                                    'invalid choice, enter a valid choice: ')
        
        # read needed records and execute by choice
        schedule_records = read_csv('data/schedule.txt')

        if inp == '1':
            search_records_by('schedule_date', 'schedule date', schedule_records)
        elif inp == '2': # searches for class that in inputted time
            query = input('enter class time (HH:MM 24-hour format): ')
            query = validate_time_input(query)
            
            seconds = hhmm_to_seconds(query)
            time_index = get_column_index('schedule_time', schedule_records)
            duration_index = get_column_index('schedule_duration', schedule_records)

            records = [schedule_records[0]]
            for i in schedule_records[1:]:
                lower_bound = hhmm_to_seconds(i[time_index])
                upper_bound = lower_bound + int(i[duration_index]) * 3600
                if seconds >= lower_bound and seconds < upper_bound:
                    records.append(i)

            if len(records) > 1: # 1st item in records is header, so check > 1
                pretty_print(records, interactive=False)
            else:
                print('there is no record of class with time of ' + query)
        elif inp == '3':
            search_records_by('schedule_duration', 'schedule duration', schedule_records)
        elif inp == '4':
            search_records_by('schedule_coach_id', 'coach id', schedule_records)
        else:
            return
        input('press enter to go back: ')


def search_sport_records():
    '''searches and displays sport records base on certain columns'''

    while True:
        # print menu
        os.system('clear')
        print('what do you want to search by ?')
        print('1. Sport ID\n2. Name\n3. Hourly fee\n4. Go Back')

        # get and validate input
        inp = input('enter choice: ')
        inp = validate_choice_input(inp, ['1', '2', '3', '4'],
                                    'invalid choice, enter a valid choice: ')
        
        # read needed records and execute by choice
        sport_records = read_csv('data/sport.txt')

        if inp == '1':
            search_records_by('sport_id', 'sport id', sport_records)
        elif inp == '2':
            search_records_by('sport_name', 'sport name', sport_records)
        elif inp == '3':
            search_records_by('sport_hourly_fee', 'hourly fee', sport_records)
        else:
            return
        input('press enter to continue: ')


def search_sport_centre_records():
    '''searches and displays sport centre records base on certain columns'''

    while True:
        # print menu
        os.system('clear')
        print('SEARCH SPORT CENTRE RECORDS\nwhat do you want to search by ?')
        print('1. Sport Centre ID\n2. Name\n3. Location\n4. Go Back')

        # get and validate input
        inp = input('enter choice: ')
        inp = validate_choice_input(inp, ['1', '2', '3', '4'],
                                    'invalid choice, enter a valid choice: ')
        
        # read needed records and execute by choice
        sport_centre_records = read_csv('data/sport-centre.txt')

        if inp == '1':
            search_records_by('sport_centre_id', 'sport centre id', sport_centre_records)
        elif inp == '2':
            search_records_by('sport_centre_name', 'sport centre name', sport_centre_records)
        elif inp == '3':
            search_records_by('sport_centre_location', 'location', sport_centre_records)
        else:
            return
        input('press enter to continue: ')
## ADMIN SEARCH RECORD FUNCTIONS

## ADMIN SORT RECORD FUNCTIONS
def sort_coach_by_id(asc):
    '''sort coach records by id
    
    parameters:
    asc (str): sorts ascending if value is 'y' else descending
    '''

    # read and sort records
    coach_records = read_csv('data/coach.txt')
    sorted_records = bubble_sort_records('coach_id', coach_records)
    if asc == 'y':
        pretty_print(sorted_records)
    else:
        pretty_print(reverse_records(sorted_records))


def sort_coach_by_name(asc):
    '''sort coach records by name
    
    parameters:
    asc (str): sorts ascending if value is 'y' else descending
    '''

    # read and sort records
    coach_records = read_csv('data/coach.txt')
    sorted_records = bubble_sort_records('coach_name', coach_records)
    if asc == 'y':
        pretty_print(sorted_records)
    else:
        pretty_print(reverse_records(sorted_records))


def sort_coach_by_fee(asc):
    '''sort coach records by fee
    
    sort using sport_fee column from sport records

    parameters:
    asc (str): sorts ascending if value is 'y' else descending
    '''
    # read needed records
    coach_records = read_csv('data/coach.txt')
    sport_records = read_csv('data/sport.txt')

    # merge & sort records
    merged_records = left_join_records_on(coach_records, 'coach_sport_id',
                                          sport_records, 'sport_id')
    sorted_records = bubble_sort_records('sport_hourly_fee', merged_records)
    columns = ['coach_id', 'coach_name', 'coach_date_joined', 'coach_date_terminated',
               'sport_name', 'coach_sport_centre_id', 'coach_rating', 'sport_hourly_fee']
    sorted_records = select_columns_from_table(columns, sorted_records)
    
    # print records
    if asc == 'y':
        pretty_print(sorted_records)
    else:
        pretty_print(reverse_records(sorted_records))


def sort_sport_by_fee(asc):
    '''sort sport records by fee
    
    parameters:
    asc (str): sorts ascending if value is 'y' else descending
    '''

    # read and sort records
    sport_records = read_csv('data/sport.txt')
    sorted_records = bubble_sort_records('sport_hourly_fee', sport_records)
    if asc == 'y':
        pretty_print(sorted_records)
    else:
        pretty_print(reverse_records(sorted_records))
## ADMIN SORT RECORD FUNCTIONS

## ADMIN MODIFY RECORD FUNCTIONS
def modify_coach_records():
    '''modify coach records, item with blank input will not be modified'''

    # read needed records
    coach_records = read_csv('data/coach.txt')
    sport_records = read_csv('data/sport.txt')
    sport_centre_records = read_csv('data/sport-centre.txt')

    # get and validate input
    coach_id = input('enter coach id to modify: ')
    valid_ids = select_column_from_table('coach_id', coach_records)
    coach_id = validate_choice_input(coach_id, valid_ids,
                                     'invalid coach id, enter a valid id: ')
    
    name = input('enter new coach name (leave blank to skip): ')

    date_joined = input('enter new date joined (leave blank to skip): ')
    date_joined = validate_date_input(date_joined, empty=True)

    date_terminated = input('enter new date terminated (leave blank to skip): ')
    date_terminated = validate_date_input(date_terminated, empty=True)

    phone = input('enter new coach phone number (leave blank to skip): ')
    phone = validate_number_input(phone, empty=True)

    address = input('enter new coach address (leave blank to skip): ')

    sport = input('enter new sport id (leave blank to skip): ')
    sport_list = select_column_from_table('sport_id', sport_records)
    sport = validate_choice_input(sport, sport_list,
                                  'invalid sport id, enter a valid id: ', empty=True)

    sport_centre = input('enter new sport centre id (leave blank to skip): ')
    sport_centre_list = select_column_from_table('sport_centre_id', sport_centre_records)
    sport_centre = validate_choice_input(sport_centre, sport_centre_list,
                                         'invalid sport centre id, enter a valid id: ', empty=True)
    rating = input('enter new coach rating (leave blank to skip): ')
    rating = validate_choice_input(rating, ['1', '2', '3', '4', '5'],
                                   'invalid rating, enter a valid rating: ', empty=True)

    # modify records
    record = [str(coach_id), name, date_joined, date_terminated,
              phone, address, sport, sport_centre, rating]
    new_records = modify_record(record, coach_records)
    pretty_print(select_row_from_table_where('coach_id', coach_id, new_records), interactive=False)
    
    # last prompt before writing changes
    okay = input('enter y to change, n to cancel: ')
    okay = validate_choice_input(okay, ['y', 'n'],
                                 'invalid choice, enter either y or n: ')
    
    # write changes
    if okay == 'y':
        write_records(new_records, 'data/coach.txt')
    else:
        input('cancelling... press enter to continue: ')


def modify_schedule_records():
    '''modify schedule records, item with blank input will not be modified'''

    # read needed records
    schedule_records = read_csv('data/schedule.txt')
    coach_records = read_csv('data/coach.txt')

    # get and validate input
    schedule_id = input('enter schedule id to modify: ')
    valid_ids = select_column_from_table('schedule_id', schedule_records)
    schedule_id = validate_choice_input(schedule_id, valid_ids,
                                        'invalid schedule id, enter a valid id: ')

    date = input('enter new class date (leave blank to skip): ')
    date = validate_date_input(date, empty=True)

    time = input('enter new class time (leave blank to skip): ')
    time = validate_time_input(time, empty=True)

    duration= input('enter new duration (between 1-9 hours): ')
    duration = validate_duration_input(duration, time, empty=True)

    coach_id = input('enter new coach id (leave blank to skip): ')
    coach_id_list = select_column_from_table('coach_id', coach_records)
    coach_id = validate_choice_input(coach_id, coach_id_list,
                                     'invalid coach id, enter valid coach id: ', empty=True)

    # validate schedule
    record = [str(schedule_id), date, time, duration, coach_id, '']
    old_records = select_row_from_table_where("schedule_id", schedule_id, schedule_records)[1]
    for i in range(len(record)):
        if record[i] == '':
            record[i] = old_records[i]

    okay_schedule = validate_schedule(record[0], record[1], record[2], record[3], record[4], empty=True) 
    if okay_schedule:
        # modify record
        new_records = modify_record(record, schedule_records)
        pretty_print(select_row_from_table_where('schedule_id', schedule_id, new_records), interactive=False)
        
        # last prompt before writing changes
        okay = input('enter y to change, n to cancel: ')
        okay = validate_choice_input(okay, ['y', 'n'],
                                     'invalid choice, enter either y or n: ')

        # write changes
        if okay == 'y':
            write_records(new_records, 'data/schedule.txt')
        else:
            input('cancelling... press enter to continue: ') 
    else:
        input('invalid schedule. press enter to try again: ')


def modify_sport_records():
    '''modify sports records, item with blank input will not be modified'''

    # read needed records
    sport_records = read_csv('data/sport.txt')

    # get and validate input
    sport_id = input('enter sport id to modify: ')
    valid_ids = select_column_from_table('sport_id', sport_records)
    sport_id = validate_choice_input(sport_id, valid_ids,
                                     'invalid sport id, enter a valid id: ')

    name = input('enter new sport name (leave blank to skip): ')

    fee = input('enter new hourly fee (leave blank to skip): ')
    fee = validate_fee_input(fee, empty=True)

    # modify record
    record = [str(sport_id), name, fee]
    new_records = modify_record(record, sport_records)
    pretty_print(select_row_from_table_where('sport_id', sport_id, new_records), interactive=False)

    #  last prompt before write
    okay = input('enter y to change, n to cancel: ')
    okay = validate_choice_input(okay, ['y', 'n'],
                                 'invalid choice, enter either y or n: ')
    
    # write changes
    if okay == 'y':
        write_records(new_records, 'data/sport.txt')
    else:
        input('cancelling... press enter to continue: ')


def modify_sport_centre_records():
    '''modify sport centre records, item with blank input will not be 
    modified
    '''

    # read needed records
    sport_centre_records = read_csv('data/sport-centre.txt')

    # get and validate input
    sport_centre_id = input('enter sport centre id to modify: ')
    valid_ids = select_column_from_table('sport_centre_id', sport_centre_records)
    sport_centre_id = validate_choice_input(sport_centre_id, valid_ids,
                                     'invalid sport centre id, enter a valid id: ')

    name = input('enter new sport centre name (leave blank to skip): ')

    location = input('enter new location (leave blank to skip): ')

    # modify records
    record = [str(sport_centre_id), name, location]
    new_records = modify_record(record, sport_centre_records)
    pretty_print(select_row_from_table_where('sport_centre_id', sport_centre_id, new_records),
                                             interactive=False)

    # last prompt before write
    okay = input('enter y to change, n to cancel: ')
    okay = validate_choice_input(okay, ['y', 'n'],
                                 'invalid choice, enter either y or n: ')
    
    # write changes
    if okay == 'y':
        write_records(new_records, 'data/sport-centre.txt')
    else:
        input('cancelling... press enter to continue: ')
## ADMIN MODIFY RECORD FUNCTIONS
### ADMIN HELPER FUNCTIONS


### STUDENT MENU FUNCTIONS
def unregistered_student_menu():
    '''menu for students that are not logged-in or registered'''

    while True:
        # print menu
        os.system('clear')
        print('STUDENT MENU')
        print('1. View Details\n2. Login\n3. Register\n4. Quit')

        # get and validate input
        inp = input('enter choice: ')
        inp = validate_choice_input(inp, ['1', '2', '3', '4'],
                                    'invalid choice, enter a valid choice: ')
        
        # execute by choice
        if inp == '1':
            unregistered_student_view_details()
        elif inp == '2':
            student_id = student_login()
            if student_id != '0':
                registered_student_menu(student_id)
                return
        elif inp == '3':
            student_register()
        else:
            return


def unregistered_student_view_details():
    '''view details for students that are not logged-in or registered'''

    while True:
        # print menu
        os.system('clear')
        print('VIEW DETAILS')
        print('1. View Sport Details\n2. View Sport Schedule\n3. Return to Student Menu')

        # write changes
        inp = input('enter choice: ')
        inp = validate_choice_input(inp, ['1', '2', '3'],
                                    'invalid choice, enter a valid choice: ')

        # display by choice
        if inp == '1':
            records = read_csv('data/sport.txt')
        elif inp == '2':
            records = get_pretty_schedule()
        else:
            return
        pretty_print(records)


def student_login():
    '''login menu for students
    
    returns:
    str: 0 if login fails, else student id of student that logged in
    '''
    os.system('clear')
    print('STUDENT LOGIN')

    # get and validate input
    email = input('enter your email: ')
    email = validate_empty_input(email, 'email')

    password = input('enter your password: ')
    password = validate_empty_input(password, password)

    # validate login
    max_tries = 4
    while not validate_login(email, password) and max_tries > 0:
        print('invalid credentials, you have ' + str(max_tries) + ' tries left')
        email = input('enter your email: ')
        email = validate_empty_input(email, 'email')

        password = input('enter your password: ')
        password = validate_empty_input(password, password)

        max_tries -= 1

    if max_tries == 0:
        print('too many failed attempts, returning...')
        return 0
    else:
        student_records = read_csv('data/student.txt')
        student_id = select_column_from_table_where('student_id', 'student_email', email, student_records)
        return student_id[0]


def student_register():
    '''registration menu for unregistered student'''

    os.system('clear')
    print('STUDENT REGISTER')
    student_records = read_csv('data/student.txt')

    # get and validate input
    name = input('enter your name: ')
    name = validate_empty_input(name, 'name')
    
    email = input('enter your email address: ')
    email = validate_empty_input(email, 'email')

    password = input('enter your password (minimum 8 characters): ')
    password = validate_password(password)
    hashed_password = hash_password(password)

    phone= input('enter your phone number (leave blank to skip): ')
    phone = validate_number_input(phone, empty=True)

    address = input('enter your address (leave blank to skip): ')

    student_id = len(select_column_from_table('student_id', student_records))
    date_joined = get_date()

    # write record 
    record = [str(student_id), name, date_joined, email, hashed_password, phone, address]
    write_record(record, 'data/student.txt')


def registered_student_menu(student_id):
    '''menu for logged-in students

    parameters:
    student_id (str): id of currently logged in student
    '''

    while True:
        # print menu
        os.system('clear')
        print('STUDENT MENU')
        print('1. View Details\n2. Modify Self Record')
        print('3. Provide Coach Feedback\n4. Manage Schedule\n5. Quit')

        # get and validate input
        inp = input('enter choice: ')
        inp = validate_choice_input(inp, ['1', '2', '3', '4', '5'],
                                    'invalid choice, enter a valid choice: ')
        
        if inp == '1':
            registered_student_view_details(student_id)
        elif inp == '2':
            modify_student_records(student_id)
        elif inp == '3':
            registered_student_give_feedback(student_id)
        elif inp == '4':
            registered_student_manage_schedule(student_id)
        else:
            return


def modify_student_records(student_id):
    '''menu to modify student record values

    parameters:
    student_id (str): id of currently logged in student
    '''
    name = input('enter new name (leave blank to skip): ')

    email = input('enter new email (leave blank to skip): ')

    phone = input('enter new phone (leave blank to skip): ')
    phone = validate_number_input(phone, empty=True)

    address = input('enter new address (leave blank to skip): ')

    password = input('enter new password (leave blank to skip): ')
    password = validate_password(password, empty=True)

    student_records = read_csv('data/student.txt')
    record = [student_id, name, '', email, password, phone, address]
    new_records = modify_record(record, student_records)
    pretty_print(select_row_from_table_where('student_id', student_id, new_records), interactive=False)

    okay = input('enter y to change, n to cancel: ')
    okay = validate_choice_input(okay, ['y', 'n'],
                                 'invalid choice, enter either y or n: ')

    if okay == 'y':
        write_records(new_records, 'data/student.txt')
    else:
        input('cancelling... press enter to continue: ')


def registered_student_view_details(student_id):
    '''view details for registered and logged-in students

    parameters:
    student_id (str): id of currently logged in student
    '''

    while True:
        os.system('clear')
        print('VIEW DETAILS')
        print('1. View Sport Details\n2. View Profile')
        print('3. View Coach Details\n4. View Sport Schedule')
        print('5. View Booked Classes\n6. Return to Student Menu')

        inp = input('enter choice: ')
        inp = validate_choice_input(inp, ['1', '2', '3', '4', '5', '6'],
                                    'invalid choice, enter a valid choice: ')

        if inp == '1':
            records = read_csv('data/sport.txt')
        elif inp == '2':
            student_records = read_csv('data/student.txt')
            records = select_row_from_table_where('student_id', student_id, student_records)
        elif inp == '3':
            coach_records = read_csv('data/coach.txt')
            sport_records = read_csv('data/sport.txt')
            sport_centre_records = read_csv('data/sport-centre.txt')

            compound_records = left_join_records_on(coach_records, 'coach_sport_id',
                                                    sport_records, 'sport_id')
            compound_records = left_join_records_on(compound_records, 'coach_sport_centre_id',
                                                    sport_centre_records, 'sport_centre_id')
            columns = ['coach_id', 'coach_name', 'coach_phone',
                       'sport_name', 'sport_centre_name', 'coach_rating']
            records = select_columns_from_table(columns, compound_records)
        elif inp == '4':
            records = get_pretty_schedule()
        elif inp == '5':
            old_records = get_pretty_schedule(booked=True)
            index = get_column_index('schedule_booked_ids', old_records)
            
            records = [old_records[0][:-1]]
            for i in old_records[1:]:
                ids = i[index].split(':')
                if student_id in ids:
                    records.append(i[:-1])
            if len(records) == 1:
                input('you don\'t have any booked classes. press enter to continue: ')
                return
        else:
            return
        pretty_print(records)


def registered_student_give_feedback(student_id):
    '''menu for students to rate coaches and provide feedback

    students can rate each coach exactly once only. feedback provided is
    anonymous.

    parameters:
    student_id (str): id of currently logged in student
    '''

    # print menu
    while True:
        os.system('clear')
        print('RATE COACH AND GIVE FEEDBACK')
        print('(feedbacks are anonymous)')
        print('1. Rate Coach\n2. Provide Feedback\n3. Return to Student Menu')

        # get and validate input
        inp = input('enter choice: ')
        inp = validate_choice_input(inp, ['1', '2', '3'],
                                    'invalid choice, enter a valid choice: ')

        # return if choice == 3
        if inp == '3':
            return

        # read needed records
        coach_records = read_csv('data/coach.txt')
        rating_records = read_csv('data/rating.txt')

        pretty_print(coach_records, interactive=False)

        # get and validate coach id input
        coach_id = input('enter coach id: ') 
        coach_ids = select_column_from_table('coach_id', coach_records) 
        coach_id = validate_choice_input(coach_id, coach_ids,
                                         'invalid coach id, enter a valid id: ')

        if inp == '1':
            # get and validate input
            rating = input('enter coach rating (from 1-5 points): ')
            rating = validate_choice_input(rating, ['1', '2', '3', '4', '5'],
                                           'invalid rating, enter a valid rating: ')

            # get indexes
            coach_id_index = get_column_index('rating_coach_id', rating_records)
            student_id_index = get_column_index('rating_student_id', rating_records)
            value_index = get_column_index('rating_value', rating_records)

            # old rating of student
            old_rating = 0.0
            # get student's previous rating
            for i in rating_records:
                if i[coach_id_index] == coach_id and i[student_id_index] == student_id:
                    old_rating = i[value_index]
                    break

            # current coach rating
            coach_rating = select_column_from_table_where('coach_rating', 'coach_id',
                                                          coach_id, coach_records)[0]
            # how many times coach has been rated
            rating_count = select_column_from_table_where('rating_id', 'rating_coach_id', coach_id, rating_records)
            rating_count = len(rating_count)
            
            # true if student has rated this coach before
            rated = validate_rating(coach_id, student_id, rating_records)

            # new rating
            new_rating = get_new_rating(float(rating), float(old_rating),
                                        float(coach_rating), rating_count, rated)

            # modify or append new record to rating.txt
            if rated:
                # WARNING: I added this just now. no clue if this will break 
                rating_id = 0
                for i in rating_records:
                    if i[coach_id_index] == coach_id and i[student_id_index] == student_id:
                        rating_id = i[0]
                new_rating_record = [rating_id, '', '', rating]
                new_rating_records = modify_record(new_rating_record, rating_records) 
                write_records(new_rating_records, 'data/rating.txt', interactive=False)
            else:
                rating_id = len(select_column_from_table('rating_id', rating_records))
                new_rating_record = [str(rating_id), coach_id, student_id, rating]
                write_record(new_rating_record, 'data/rating.txt', interactive=False)

            # modify coach records
            new_coach_record = [str(coach_id), '', '', '', '', '', '', '', str(new_rating)] 
            new_coach_records = modify_record(new_coach_record, coach_records)
            write_records(new_coach_records, 'data/coach.txt')
        else:
            feedback = input('enter your feedback: ').strip()

            if feedback == '':
                input('empty feedback, cancelling... press enter to continue: ')
            else:
                feedback = '"' + feedback + '"'
                record = [coach_id, feedback]
                write_record(record, 'data/feedback.txt')


def registered_student_manage_schedule(student_id):
    '''menu for students to book or cancel a class

    parameters:
    student_id (str): id of currently logged in student
    '''

    while True:
        os.system('clear')
        print('MANAGE SCHEDULE')
        print('1. Book Class\n2. Cancel Class\n3. Return to Student Menu')

        inp = input('enter choice: ')
        inp = validate_choice_input(inp, ['1', '2', '3'],
                                    'invalid choice, enter a valid choice: ')

        if inp == '3':
            return

        old_records = get_pretty_schedule(booked=True)
        index = get_column_index('schedule_booked_ids', old_records)

        schedule = [old_records[0][:-1] + ['booked']]
        for i in old_records[1:]:
            ids = i[index].split(':')
            if student_id in ids:
                schedule.append(i[:-1] + ['yes'])
            else:
                schedule.append(i[:-1] + ['no'])

        pretty_print(schedule, interactive=False)
        schedule_records = read_csv('data/schedule.txt')

        schedule_id = input('enter schedule id: ')
        valid_ids = select_column_from_table('schedule_id', schedule_records)
        schedule_id = validate_choice_input(schedule_id, valid_ids,
                                            'invalid id, enter a valid id: ')
        if inp == '1':
            if is_booked(schedule_id, schedule):
                input('class is already booked. press enter to continue: ')
            else:
                change_schedule('book', schedule_id, schedule_records, student_id)
        else:
            if not is_booked(schedule_id, schedule):
                input('you haven\'t booked that class. press enter to continue: ')
            else:
                change_schedule('cancel', schedule_id, schedule_records, student_id)
### STUDENT MENU FUNCTIONS

### MAIN FUNCTIONS
def init():
    '''initialize files needed by program to run correctly'''

    # create files if not exists
    files = ['coach.txt', 'student.txt', 'schedule.txt', 'sport.txt',
             'sport-centre.txt', 'feedback.txt', 'rating.txt']
    for i in files:
        if not os.path.exists('data/' + i):
            with open('data/' + i, 'w') as _:
                pass

    # add column headers if not exist
    with open('data/coach.txt', 'r+') as coach, \
         open('data/sport.txt', 'r+') as sport, \
         open('data/student.txt', 'r+') as student, \
         open('data/schedule.txt', 'r+') as schedule, \
         open('data/sport-centre.txt', 'r+') as sport_centre, \
         open('data/feedback.txt', 'r+') as feedback, \
         open('data/rating.txt', 'r+') as rating:
        if coach.read(1) == '':
            coach.write('coach_id,coach_name,coach_date_joined,coach_date_terminated,coach_phone,coach_address,coach_sport_id,coach_sport_centre_id,coach_rating\n')
        if sport.read(1) == '':
            sport.write('sport_id,sport_name,sport_hourly_fee\n')
        if student.read(1) == '':
            student.write('student_id,student_name,student_date_joined,student_email,student_password,student_phone,student_address\n')
        if schedule.read(1) == '':
            schedule.write('schedule_id,schedule_date,schedule_time,schedule_duration,schedule_coach_id,schedule_booked_ids\n')
        if sport_centre.read(1) == '':
            sport_centre.write('sport_centre_id,sport_centre_name,sport_centre_location\n')
        if feedback.read(1) == '':
            feedback.write('coach_id,feedback')
        if rating.read(1) == '':
            rating.write('rating_id,rating_coach_id,rating_student_id,rating_value')


def main():
    '''entrypoint to program'''

    while True:
        os.system('clear')
        print('REAL CHAMPIONS SPORT CENTRE\n1. Admin\n2. Student\n3. Quit')

        inp = input('enter choice: ')
        inp = validate_choice_input(inp, ['1', '2', '3'],
                                    'invalid choice, enter a valid choice: ')
            
        if inp == '1':
            success = admin_login()
            if success:
                admin_menu()
        elif inp == '2':
            unregistered_student_menu()
        else:
            print('quitting...')
            return
### MAIN FUNCTIONS


if __name__ == '__main__':
    init()
    main()

