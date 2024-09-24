package com.bms.messaging.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RegisterUser implements Message {
    final String userId;
}
