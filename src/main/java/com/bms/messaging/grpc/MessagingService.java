package com.bms.messaging.grpc;

import com.bms.messaging.ManagerService;
import com.bms.messaging.grpc.observer.SendMessageHandler;
import com.google.protobuf.Empty;
import com.message.v1.Message;
import com.message.v1.MessageServiceGrpc;
import com.message.v1.RegisterRequest;
import com.message.v1.SendMessageRequest;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class MessagingService extends MessageServiceGrpc.MessageServiceImplBase {
    private final ManagerService managerService;

    @Override
    public void register(RegisterRequest request, StreamObserver<Empty> responseObserver) {
        managerService.registerUser(request.getUserId());

        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<SendMessageRequest> sendMessage(StreamObserver<Message> responseObserver) {
        return new SendMessageHandler(managerService, responseObserver);
    }
}
