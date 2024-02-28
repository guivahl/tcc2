mix compile

for i in {1..20}; do
    echo "WIDTH: 1 HEIGHT: 1 ITERATION: $i" 
    mix run lib/main.ex 1 1
done

for i in {1..20}; do
    echo "WIDTH: 5 HEIGHT: 2 ITERATION: $i" 
    mix run lib/main.ex 5 2
done

echo "$SECONDS seconds executing benchmark."
