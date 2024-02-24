for i in {1..20}; do
    echo "WIDTH: 1 HEIGHT: 1 ITERATION: $i" 
    java -cp target/imageProcessor-1.0-SNAPSHOT.jar imageProcessor.App 1 1
done

for i in {1..20}; do
    echo "WIDTH: 5 HEIGHT: 2 ITERATION: $i" 
    java -cp target/imageProcessor-1.0-SNAPSHOT.jar imageProcessor.App 5 2
done

echo "$SECONDS seconds executing benchmark."
