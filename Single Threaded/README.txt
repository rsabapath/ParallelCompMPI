We started by calculating the single threaded instance.

The code singleThreaded.cc will calculate the distance from all nodes to
every other node. This of course is done on one system in a single process.

The data is read from "in.txt"
the file is contains data in the following format

<number of nodes> <number of edges>
<node of source> <node of dest> <node weight>
...
...
