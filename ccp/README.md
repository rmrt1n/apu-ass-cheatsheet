# CCP

To build and run this program, you can either import the files to a new project
in your IDE, or you can simply run this in the terminal: `cd src && javac * &&
java Airport`.

This code is a mess, but it works. If you're sharing this with others, a
decent(?) way to differentiate your code is to change the locking mechanism for
the fuel truck. This one uses a semaphore, but you can replace it with a 
reentrant lock or an intrinsic lock using the `synchronized` keyword.
