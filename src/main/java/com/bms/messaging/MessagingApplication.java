package com.bms.messaging;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.javadsl.AskPattern;
import akka.actor.typed.receptionist.Receptionist;
import akka.actor.typed.receptionist.ServiceKey;
import com.bms.messaging.actor.ManagerActor;
import com.bms.messaging.entity.Message;
import com.bms.messaging.entity.RegisterUser;
import com.bms.messaging.entity.SendMessage;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.time.Duration;

import java.util.concurrent.CompletionStage;

//@SpringBootApplication
public class MessagingApplication {

	public static void main(String[] args) throws InterruptedException {
//		SpringApplication.run(MessagingApplication.class, args);
		ActorSystem<Message> system = ActorSystem.create(ManagerActor.create(), "Manager");

		system.tell(new RegisterUser("actor1"));
		system.tell(new RegisterUser("actor2"));

		Thread.sleep(1000);

		ServiceKey<Message> userServiceKey = ServiceKey.create(Message.class, "actor1");

		CompletionStage<Receptionist.Listing> listingFuture = AskPattern.ask(
				system.receptionist(),
				replyTo -> Receptionist.find(userServiceKey, replyTo),
				Duration.ofSeconds(3),
				system.scheduler()
		);

		listingFuture.thenAccept(listing -> {
			if (!listing.getServiceInstances(userServiceKey).isEmpty()) {
				ActorRef<Message> actor1 = listing.getServiceInstances(userServiceKey).iterator().next();

				SendMessage message = new SendMessage("actor2", "actor1", "Hello there, actor1");
				actor1.tell(message);
			} else {
				System.out.println("actor1 not found");
			}
		});
	}

}
