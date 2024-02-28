package pi.actors;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.*;
import java.time.temporal.ChronoUnit;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import pi.App;
import pi.messages.CalculationRequestMessage;
import pi.messages.CalculationResponseMessage;
import pi.messages.IMessage;
import pi.messages.PIMessage;

public class PI extends AbstractBehavior<IMessage> {
    private long totalPoints;
    private long numActors;
    private long messageReceivedCounter = 0;
    private long isInsideCounter = 0;
    private long pointsPerActor = 0;

    public PI(ActorContext<IMessage> context) {
        super(context);
    }

    @Override
    public Receive<IMessage> createReceive() {
        return newReceiveBuilder()
            .onMessage(PIMessage.class, this::estimate)
            .onMessage(CalculationResponseMessage.class, this::receiver)
            .build();
    }
    
    private Behavior<IMessage> estimate(PIMessage message) {
        this.numActors = message.getNumActors();
        this.totalPoints = message.getTotalPoints();
        this.pointsPerActor = (long) Math.floor((double)(this.totalPoints / this.numActors));
        
        for (long i = 0; i < message.getNumActors(); i++) {
            String calculationActorName = "MonteCarloActor_" + i;
            ActorRef<CalculationRequestMessage> calculationActor = getContext().spawn(MonteCarlo.start(), calculationActorName);

            CalculationRequestMessage calculationMessage = new CalculationRequestMessage(this.pointsPerActor, getContext().getSelf());

            calculationActor.tell(calculationMessage);
        }
        return this;
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

            return Behaviors.stopped();
        }

        return this;
    }

    public static Behavior<IMessage> start() {
        return Behaviors.setup(PI::new);
    }
}
