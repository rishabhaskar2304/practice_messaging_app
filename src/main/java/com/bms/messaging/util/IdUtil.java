package com.bms.messaging.util;

import java.util.UUID;

public class IdUtil {

  public static String getId() {
    return UUID.randomUUID().toString();
  }
}
