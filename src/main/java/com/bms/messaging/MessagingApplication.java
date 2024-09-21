package com.bms.messaging;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Props;
import com.bms.messaging.actor.UserActor;
import com.bms.messaging.entity.Message;
import com.bms.messaging.entity.SendMessage;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootApplication
public class MessagingApplication {

	public static void main(String[] args) {
//		SpringApplication.run(MessagingApplication.class, args);
		ActorSystem<Message> system = ActorSystem.create(UserActor.create("system"), "system");

		ActorRef<Message> actor1 = system.systemActorOf(UserActor.create("actor1"), "actor1", Props.empty());
		system.systemActorOf(UserActor.create("actor2"), "actor2", Props.empty());

		SendMessage message = new SendMessage("actor1", "actor2", "Hello there, actor2");
		actor1.tell(message);
	}

}
