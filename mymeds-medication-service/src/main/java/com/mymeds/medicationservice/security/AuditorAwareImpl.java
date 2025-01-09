package com.mymeds.medicationservice.security;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;

public class AuditorAwareImpl implements AuditorAware<String> {

  @Override
  @NonNull
  public Optional<String> getCurrentAuditor() {
    return Optional.of(CurrentAuditorContextHolder.get()
        .map(UUID::toString)
        .orElse("system")
    );
  }
}
