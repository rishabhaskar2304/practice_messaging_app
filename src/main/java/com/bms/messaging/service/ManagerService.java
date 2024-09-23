package com.bms.messaging.service;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.javadsl.AskPattern;
import akka.actor.typed.receptionist.Receptionist;
import akka.actor.typed.receptionist.ServiceKey;
import com.bms.messaging.actor.ManagerActor;
import com.bms.messaging.entity.Message;
import com.bms.messaging.entity.RegisterUser;
import com.bms.messaging.entity.SendMessage;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.CompletionStage;

@Service
public class ManagerService {
    public final ActorSystem<Message> system = ActorSystem.create(ManagerActor.create(), "Manager");

    public void registerUser(String userId) {
        system.tell(new RegisterUser(userId));
    }

    public void sendMessage(com.message.v1.Message messageProto) {
        ServiceKey<Message> userServiceKey = ServiceKey.create(Message.class, messageProto.getFromUserId());

        CompletionStage<Receptionist.Listing> listingFuture = AskPattern.ask(
                system.receptionist(),
                replyTo -> Receptionist.find(userServiceKey, replyTo),
                Duration.ofSeconds(3),
                system.scheduler()
        );

        listingFuture.thenAccept(listing -> {
            if (!listing.getServiceInstances(userServiceKey).isEmpty()) {
                ActorRef<Message> senderActor = listing.getServiceInstances(userServiceKey).iterator().next();

                SendMessage message = new SendMessage(messageProto.getFromUserId(), messageProto.getToUserId(), messageProto.getContent());
                senderActor.tell(message);
            } else {
                System.out.println("Sender actor not found");
            }
        });
    }
}
