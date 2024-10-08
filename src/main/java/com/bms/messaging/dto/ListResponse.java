package com.bms.messaging.dto;

import akka.actor.typed.receptionist.Receptionist;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ListResponse implements Message {
    final Receptionist.Listing listing;
}
