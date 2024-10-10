package com.bms.messaging.actor;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import akka.actor.typed.receptionist.Receptionist;
import akka.actor.typed.receptionist.ServiceKey;
import com.bms.messaging.dto.ListResponse;
import com.bms.messaging.dto.Message;
import com.bms.messaging.dto.ReceiveMessage;
import com.bms.messaging.dto.SendMessage;
import com.bms.messaging.grpc.GrpcStubRegistry;
import com.bms.messaging.grpc.observer.SendMessageHandler;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

public class UserActor extends AbstractBehavior<Message> {

  private final String actorId;
  private final ActorRef<Receptionist.Listing> listingResponseAdapter;
  private final Queue<ReceiveMessage> queue = new LinkedList<>();
  private Optional<SendMessage> pendingMessage = Optional.empty();

  public UserActor(ActorContext<Message> context, String actorId) {
    super(context);
    context.getLog().info("Actor with userId {} has been registered", actorId);
    this.actorId = actorId;
    this.listingResponseAdapter = context.messageAdapter(Receptionist.Listing.class,
        ListResponse::new);
  }

  public static Behavior<Message> create(String actorId) {
    return Behaviors.setup(context -> {
          ServiceKey<Message> serviceKey = ServiceKey.create(Message.class, actorId);

          context.getSystem().receptionist().tell(Receptionist.register(serviceKey, context.getSelf()));

          return new UserActor(context, actorId);
        }
    );
  }

  @Override
  public Receive<Message> createReceive() {
    return newReceiveBuilder()
        .onMessage(SendMessage.class, this::onSendMessage)
        .onMessage(ListResponse.class, this::onFind)
        .onMessage(ReceiveMessage.class, this::onReceiveMessage)
        .build();
  }

  private Behavior<Message> onSendMessage(SendMessage msg) {
    ServiceKey<Message> targetKey = ServiceKey.create(Message.class, msg.getTo());
    pendingMessage = Optional.of(msg);
//    getContext().getLog().info("{} received send message request", actorId);
    getContext().getSystem().receptionist()
        .tell(Receptionist.find(targetKey, listingResponseAdapter));

    return Behaviors.same();
  }

  private Behavior<Message> onReceiveMessage(ReceiveMessage msg) {
//    getContext().getLog()
//        .info("{} received message [{}], from {}", msg.getTo(), msg.getContent(), msg.getFrom());
    SendMessageHandler streamObserver = GrpcStubRegistry.OBSERVER_REGISTRY.get(
        actorId);
    queue.add(msg);
    if (streamObserver != null) {
      try {
        while (!queue.isEmpty()) {
          streamObserver.sendMessage(queue.poll());
        }
      } catch (Exception ignored) {}
    }
    return this;
  }

  private Behavior<Message> onFind(ListResponse listingResponse) {
//    getContext().getLog().info("{} received discovery response", actorId);
    Receptionist.Listing listing = listingResponse.getListing();
    if (pendingMessage.isPresent()) {
      SendMessage sendMessage = pendingMessage.get();
      ReceiveMessage receiveMessage = new ReceiveMessage(sendMessage.getFrom(),
          sendMessage.getTo(), sendMessage.getContent());
      ServiceKey<Message> targetKey = ServiceKey.create(Message.class, sendMessage.getTo());
      for (ActorRef<Message> serviceInstance : listing.getServiceInstances(targetKey)) {
//        getContext().getLog().info("{} sent message to receiver actor", actorId);
        serviceInstance.tell(receiveMessage);
      }

      pendingMessage = Optional.empty();
    }

    return this;
  }
}
