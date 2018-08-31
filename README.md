# java do notation example

## quick class descriptions

* `Either`, typical monad.
* `MOption`, the _Java 8_ `Option` monad wrapper to support `BindTo`.
* `MStream`, the _Java 8_ `Stream` monad wrapper to support `BindTo`.
* `Parser`, utility methods.
* `Tuple`, for anonymous data types.
* `main`, the example.

## example

The example read lazily one (potentially big) file with quadratic equations like:

```
A x^2 + B x + C
```

Multiple equations for each line is valid separating equations by `;`.

The process compute only real equation roots.

The process write the valid result or one error with line and equation number if parsing or complex number as result is given.

The process consume a constant amount of memory (no matter how big is the input file).

## configuration

On `build.gradle` file you should configure the path to the `lombok` with `java-do-notation`.

## example runs

I created seven equations files, the file `equations.7z` contains random instances for `10^1`, `10^2`, ... and `10^5` lines (each line with 5 average equations).

```
$ wc -l t[1-7].equations
        10 t1.equations
       100 t2.equations
      1000 t3.equations
     10000 t4.equations
    100000 t5.equations
   1000000 t6.equations
  10000000 t7.equations
```

```
$ for i in `seq 1 7`; do echo -n "$i: "; time -f "%E, %M" java -Xms40m -Xmx40m -jar ./build/libs/do-notation-example.jar ~/tmp/t$i.equations ~/tmp/t$i.results; done
1: 0:00.20, 36252
2: 0:00.33, 42508
3: 0:00.57, 65704
4: 0:01.37, 91480
5: 0:05.65, 96380
6: 0:46.65, 103404
7: 7:36.11, 106860
```