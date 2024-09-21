package com.bms.messaging.actor;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import com.bms.messaging.entity.Message;
import com.bms.messaging.entity.RegisterUser;

public class ManagerActor extends AbstractBehavior<Message> {
    public ManagerActor(ActorContext<Message> context) {
        super(context);
    }

    public static Behavior<Message> create() {
        return Behaviors.setup(ManagerActor::new);
    }

    @Override
    public Receive<Message> createReceive() {
        return newReceiveBuilder()
                .onMessage(RegisterUser.class, this::registerUser)
                .build();
    }

    private Behavior<Message> registerUser(RegisterUser request) {
        getContext().spawn(UserActor.create(request.getUserId()), request.getUserId());
        return this;
    }
}
