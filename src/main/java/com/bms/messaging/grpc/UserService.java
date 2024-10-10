package com.bms.messaging.grpc;

import com.bms.messaging.persistence.UserEntity;
//import com.bms.messaging.persistence.UserRepository;
import com.bms.messaging.service.ManagerService;
import com.bms.messaging.util.IdUtil;
import com.message.v1.FindUserRequest;
import com.message.v1.RegisterRequest;
import com.message.v1.RegisterResponse;
import com.message.v1.User;
import com.message.v1.UserServiceGrpc.UserServiceImplBase;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.lognet.springboot.grpc.GRpcService;

import java.util.Optional;

@GRpcService
@RequiredArgsConstructor
public class UserService extends UserServiceImplBase {

  private final ManagerService managerService;
//  private final UserRepository userRepository;

  @Override
  public void register(RegisterRequest request, StreamObserver<RegisterResponse> responseObserver) {
    String userId = IdUtil.getId();
    managerService.registerUser(userId);

//    userRepository.save(UserEntity.builder()
//            .username(request.getName())
//            .emailId(request.getEmailId())
//            .userId(userId)
//            .build());

    responseObserver.onNext(RegisterResponse.newBuilder()
            .setUserId(userId).build());
    responseObserver.onCompleted();
  }

  @Override
  public void findUser(FindUserRequest request, StreamObserver<User> responseObserver) {
//    Optional<UserEntity> optionalUser = userRepository.findById(request.getEmailId());
//
//    if (optionalUser.isPresent()) {
//      UserEntity userEntity = optionalUser.get();
//
//      responseObserver.onNext(User.newBuilder().setUserId(userEntity.getUserId())
//              .setEmailId(userEntity.getEmailId())
//              .setName(userEntity.getUsername())
//              .build());
//    } else {
//      responseObserver.onNext(User.getDefaultInstance());
//    }
//
//    responseObserver.onCompleted();
  }
}
