package com.bms.messaging.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReceiveMessage implements Message {
    final String from;
    final String to;
    final String content;
}
