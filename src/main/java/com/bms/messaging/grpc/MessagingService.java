package com.bms.messaging.grpc;

import com.bms.messaging.service.ManagerService;
import com.bms.messaging.grpc.observer.SendMessageHandler;
import com.message.v1.Message;
import com.message.v1.MessageServiceGrpc;
import com.message.v1.SendMessageRequest;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.lognet.springboot.grpc.GRpcService;

@GRpcService
@RequiredArgsConstructor
public class MessagingService extends MessageServiceGrpc.MessageServiceImplBase {
    private final ManagerService managerService;

    @Override
    public StreamObserver<SendMessageRequest> sendMessage(StreamObserver<Message> responseObserver) {
        return new SendMessageHandler(managerService, responseObserver);
    }
}
