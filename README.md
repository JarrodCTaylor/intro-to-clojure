# Intro to Clojure

This repository contains a video poker implementation. That is used as an implementation exercise for the live introductory to clojure workshop.

## Running the application

To start the application from the command line:

```shell
clojure -M:run
```

To start the application from a repl:

Eval the comment block at the bottom of `src/intro_to_cloure/core.clj`

## Completing the implementation 

The functions that evaluate a given poker hand in `src/intro_to_clojure/game-logic.clj` need to be implemented by the user.

Once the functions are implemented and all the tests pass. The game should be fully functional.

## Running the tests

Execute the following shell command to run the tests.

```shell
bin/kaocha test

# Optional rerun on file change
bin/kaocha test --watch
```