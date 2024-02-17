POINTS=1000000000 # 100 million points

ARRAY_ACTORS=(1 2 4 6 8 10 12 14 16 18 20 50 100 500 1000 5000 10000 50000 100000)

for ACTORS in ${ARRAY_ACTORS[@]}
do
    for i in {1..20}; do
        echo "Running POINTS: $POINTS and ACTORS: $ACTORS ITERATION: $i" 
        mix run lib/main.ex $POINTS $ACTORS
    done
done

echo "$SECONDS seconds executing benchmark."
