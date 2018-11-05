# miniprob

An evaluator for a tiny probabilistic language.

## Usage

Install Leiningen per the [official directions](https://leiningen.org/#install).

Then, run `lein repl` from a terminal window to start a Clojure REPL in the
`miniprob.core` namespace:

```
$ lein repl
nREPL server started on port 51041 on host 127.0.0.1 - nrepl://127.0.0.1:51041
REPL-y 0.3.7, nREPL 0.2.12
Clojure 1.8.0
Java HotSpot(TM) 64-Bit Server VM 1.8.0_181-b13
    Docs: (doc function-name-here)
          (find-doc "part-of-name-here")
  Source: (source function-name-here)
 Javadoc: (javadoc java-object-or-class-here)
    Exit: Control+D or (exit) or (quit)
 Results: Stored in vars *1, *2, *3, an exception in *e

miniprob.core=> (eval '(+ (flip :a) (flip :b)) {:a 2 :b 3})
```

## License

Copyright © 2018 kf Fellows

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
