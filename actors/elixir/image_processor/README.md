# Pi

## Run

### Benchmark

```sh
mix deps.get && mix compile
```

```sh
chmod +x benchmark.sh
```

```sh
./benchmark.sh
```

### One test case

```sh
mix run lib/main.ex 5 2
```

1. First argument is the number which the image width will be splitted.
2. Second argumentis the number which the image height will be splitted.
3. If none of the arguments are passed, both values will be 4 by default.

