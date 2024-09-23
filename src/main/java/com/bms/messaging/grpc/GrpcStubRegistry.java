package com.bms.messaging.grpc;

import com.bms.messaging.grpc.observer.SendMessageHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GrpcStubRegistry {
    public static final Map<String, SendMessageHandler> OBSERVER_REGISTRY = new ConcurrentHashMap<>();
}
