package com.bms.messaging.grpc;

import com.message.v1.SendMessageRequest;
import io.grpc.stub.StreamObserver;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GrpcStubRegistry {
    public static final Map<String, StreamObserver<SendMessageRequest>> OBSERVER_REGISTRY = new ConcurrentHashMap<>();
}
