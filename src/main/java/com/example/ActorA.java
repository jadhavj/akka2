package com.example;

import akka.actor.OneForOneStrategy;
import akka.actor.SupervisorStrategy;
import akka.actor.UntypedActor;
import akka.japi.pf.DeciderBuilder;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;
import static akka.actor.SupervisorStrategy.resume;
import static akka.actor.SupervisorStrategy.restart;
import static akka.actor.SupervisorStrategy.stop;
import static akka.actor.SupervisorStrategy.escalate;
/**
 * Created by M1039838 on 11/15/2017.
 */
public class ActorA extends UntypedActor {

    private static Config config = ConfigFactory.load();

    private static SupervisorStrategy strategy =
            new OneForOneStrategy(5, Duration.create(1, TimeUnit.SECONDS), DeciderBuilder.
                    match(ArithmeticException.class, e -> resume()).
                    match(NullPointerException.class, e -> restart()).
                    match(IllegalArgumentException.class, e -> stop()).
                    matchAny(o -> escalate()).build());

    @Override
    public SupervisorStrategy supervisorStrategy() {
        return strategy;
    }

    @Override
    public void preStart() {
        System.out.println("Before start");
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof  MyMessage) {
            MyMessage message1 = (MyMessage) message;
            System.out.println("Hello " + message1.getVal() + " I am " + getSelf());

        } else {
            unhandled(message);
        }
    }
}
