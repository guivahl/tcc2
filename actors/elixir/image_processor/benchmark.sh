mix compile

# (width, height)
dimensions=(
    "1 1" # 1 actor
    "2 1" # 2 actor
    "4 1" # 4 actor
    "3 2" # 6 actor
    "4 2" # 8 actor
    "5 2" # 10 actor
)

for dim in "${dimensions[@]}"; do
    width=$(echo "$dim" | cut -d' ' -f1)
    height=$(echo "$dim" | cut -d' ' -f2)
    
    for i in {1..20}; do
        echo "WIDTH: $width HEIGHT: $height ITERATION: $i"
        mix run lib/main.ex "$width" "$height"
    done
    
    sh clean-folders.sh
done


echo "$SECONDS seconds executing benchmark."
