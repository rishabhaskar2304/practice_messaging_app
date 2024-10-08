package com.bms.messaging.grpc.observer;

import com.bms.messaging.service.ManagerService;
import com.bms.messaging.dto.ReceiveMessage;
import com.bms.messaging.grpc.GrpcStubRegistry;
import com.message.v1.Message;
import com.message.v1.SendMessageRequest;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SendMessageHandler implements StreamObserver<SendMessageRequest> {
    private final ManagerService managerService;
    private final StreamObserver<Message> responseObserver;
    private String userId;

    @Override
    public void onNext(SendMessageRequest value) {
        if (value.hasMessage()) {
            managerService.sendMessage(value.getMessage());
        } else {
            userId = value.getUserId();
            managerService.registerUser(userId);
            GrpcStubRegistry.OBSERVER_REGISTRY.put(userId, this);
        }
    }

    @Override
    public void onError(Throwable t) {
        if (userId != null)
            GrpcStubRegistry.OBSERVER_REGISTRY.remove(userId);
    }

    @Override
    public void onCompleted() {
        if (userId != null)
            GrpcStubRegistry.OBSERVER_REGISTRY.remove(userId);

        this.responseObserver.onCompleted();
    }

    public void sendMessage(ReceiveMessage message) {
        this.responseObserver.onNext(Message.newBuilder()
            .setFromUserId(message.getFrom())
            .setToUserId(message.getTo())
            .setContent(message.getContent())
            .build());
    }
}
