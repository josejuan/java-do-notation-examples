# java do notation example

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