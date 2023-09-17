# RTS

This project contains the same program written in 2 languages, Java and Clojure.
To run both, you'll need a rabbitmq server running. I used a rabbitmq container,
which can be run using the `run.sh` file if you have podman. If you use docker,
just replace the podman command with docker.

To build and run the Java project, look at the build instructions from either
CPP or OODJ. The Clojure project is a leiningen project, so you'll need that and
clojure installed on your system first. Read the getting started guide on the
[official site](https://clojure.org/guides/getting_started).

Normally, you'll only need the Java project. It's probably better to just
compare different concurrency methods in Java than to compare implementations in
different languages. This module was my excuse to learn a new language
(Clojure).
