package com.mymeds.authservice.security;

import java.util.Optional;
import java.util.UUID;

public class CurrentAuditorContextHolder {

  private static final ThreadLocal<String> currentAuditorContext = new ThreadLocal<>();

  public static void set(String id) {
    currentAuditorContext.set(id);
  }

  public static Optional<UUID> get() {
    if (currentAuditorContext.get() != null) {
      return Optional.of(UUID.fromString(currentAuditorContext.get()));
    }
    return Optional.empty();
  }

  public static void clear() {
    currentAuditorContext.remove();
  }
}
