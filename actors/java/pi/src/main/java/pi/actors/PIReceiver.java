package pi.actors;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.*;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import pi.App;
import pi.messages.CalculationRequestMessage;
import pi.messages.CalculationResponseMessage;
import pi.messages.FinishMessage;
import pi.messages.IMessage;

public class PIReceiver extends AbstractBehavior<IMessage> {
    private long totalPoints;
    private long numActors;
    private long messageReceivedCounter = 0;
    private long isInsideCounter = 0;
    private long pointsPerActor = 0;

    public PIReceiver(ActorContext<IMessage> context, long numActors, long totalPoints, long pointsPerActor) {
        super(context);
        this.numActors = numActors;
        this.totalPoints = totalPoints;
        this.pointsPerActor = pointsPerActor;
    }

    @Override
    public Receive<IMessage> createReceive() {
        return newReceiveBuilder()
            .onMessage(CalculationResponseMessage.class, this::receiver)
            .build();
    }

    private Behavior<IMessage> receiver(CalculationResponseMessage message) {
        this.messageReceivedCounter++;
        this.isInsideCounter += message.getIsInsideCounter();
        
        if (this.messageReceivedCounter == this.numActors) {
            double estimatedPi = (4.0 * this.isInsideCounter) / this.totalPoints;
            double piDiff = Math.abs(estimatedPi - Math.PI);
            Instant stopTime = Instant.now();

            long timeExecutionMiliseconds = Duration.between(App.startTime, stopTime).toMillis();

            String csvString = String.format(
                "%d,%d,%d,%d,%f,%f,%f,%d\n", 
                this.totalPoints, 
                this.numActors,
                this.pointsPerActor, 
                this.isInsideCounter,
                estimatedPi,
                Math.PI,
                piDiff,
                timeExecutionMiliseconds
            );

            try{
                Path path = Paths.get("results.csv");
                Files.write(path, csvString.getBytes(), StandardOpenOption.APPEND);
            } catch(Exception e) {
                System.out.println("Error: " + e.toString());
            }

            FinishMessage finishMessage = new FinishMessage();

            message.getSender().tell(finishMessage);
    
            return Behaviors.stopped();
        }

        return this;
    }

    public static Behavior<IMessage> start(long numActors, long totalPoints, long pointsPerActor) {
        return Behaviors.setup(context -> new PIReceiver(context, numActors, totalPoints, pointsPerActor));
    }
}
