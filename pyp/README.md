# PYP
It's just a single file python program with no dependencies, so you can just run
it with `python3 main.py`. The password for admin login is just `password`, and 
the credentials for the student login is email: `a`, password: `n`. The mock 
data is located in `data/`.

For some reason the lecturer requested the program to be written in a
single file, which resulted in this monstrosity. If you're curently doing this
assignment, and are also required to use write it in a single file, split up the
code into manageable modules (in separate files) before putting it all into a
single file. It's easier to manage this way (IMO).

**WARNING:** I wrote this 3 years ago, and I made some questionable choices in code
that I wouldn't do today. I also found and fixed(?) some errors found by my
static analyzer, but I haven't (and won't) test it again. So if anyone wants to
use this code, test it on your own.
