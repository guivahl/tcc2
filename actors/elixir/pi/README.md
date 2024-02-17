# Pi

## Run

### Benchmark

```sh
mix compile
```

```sh
chmod +x benchmark-1m.sh benchmark-100m.sh
```

```sh
./benchmark-1m.sh && ./benchmark-100m.sh
```

### One test case

```sh
mix run lib/main.ex 10000 300
```

1. First argument is the number of total points to be calculated.
2. Second argument is the number of actors to be used.
3. If none of the arguments are passed, both values will be 1000 by default.

