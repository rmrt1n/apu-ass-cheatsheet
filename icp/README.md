# ICP

To compile, you'll need to have `make` on your system. If you're running linux
or osx, you probably already have it installed. To compile, run `make` which
would generate the `exec.out` executable. If you're on Windows, you can install
`make` from `chocolatey`. 

Unlike my ugly AF python code, the code here should be easier to understand if
you already know C. `char ***` is a pointer to a matrix of string (`char *`)
that is used to represent the tabular data stored in the text files. There might
be a simpler way to write this, but the me of that time couldn't think of it.
