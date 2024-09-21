package com.bms.messaging.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RegisterUser implements Message {
    final String userId;
}
