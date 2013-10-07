What is this?
-------------

This is an attempt at a thread "histogram" for a particular thread dump.

Why?
---

The eventual idea is to send all this to a central threaddb 
server, hence the name, where all threads can be compared against an existing database
so that the following can be done

- Tag threads with a "thread ID" just like an SQL ID so that we can easily refer to a
  particular thread
- Ignore housekeeping threads
- Find known bad thread patterns
- Find new threads

Credit
------
 
Inspiration and some code from [TDA](https://java.net/projects/tda) hence the GPL license on this project
