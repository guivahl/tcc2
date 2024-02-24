Compile: mvn compile -q
Run: mvn exec:exec -q 

# Pi

## Run

### Benchmark

```sh
mvn package
```

```sh
chmod +x benchmark-1m.sh benchmark-100m.sh
```

```sh
./benchmark-1m.sh && ./benchmark-100m.sh
```

### One test case

```sh
java -cp target/pi-1.0-SNAPSHOT.jar pi.App 10000 300
```

1. First argument is the number of total points to be calculated.
2. Second argument is the number of actors to be used.
3. If none of the arguments are passed, both values will be 1000 by default.

