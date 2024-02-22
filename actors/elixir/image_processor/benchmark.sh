mix compile

for i in {1..20}; do
    echo "ITERATION: $i" 
    mix run lib/main.ex
done

echo "$SECONDS seconds executing benchmark."
