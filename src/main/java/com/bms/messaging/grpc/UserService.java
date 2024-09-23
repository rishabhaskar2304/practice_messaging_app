package com.bms.messaging.grpc;

import com.bms.messaging.service.ManagerService;
import com.bms.messaging.util.IdUtil;
import com.message.v1.RegisterRequest;
import com.message.v1.RegisterResponse;
import com.message.v1.UserServiceGrpc.UserServiceImplBase;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class UserService extends UserServiceImplBase {

  private final ManagerService managerService;

  @Override
  public void register(RegisterRequest request, StreamObserver<RegisterResponse> responseObserver) {
    String userId = IdUtil.getId();
    managerService.registerUser(userId);

    responseObserver.onNext(RegisterResponse.newBuilder()
        .setUserId(userId).build());
    responseObserver.onCompleted();
  }
}
