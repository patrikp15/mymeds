package com.mymeds.authservice.persistance.model;

import com.mymeds.authservice.enumeration.AuthTokenStatus;
import com.mymeds.sharedutilities.enumeration.AuthTokenType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(
    name = "AUTH_TOKEN",
    indexes = {
        @Index(name = "idx_token", columnList = "TOKEN"),
        @Index(name = "idx_user_id", columnList = "USER_ID")
    }
)
@Entity
public class AuthToken extends AuditableEntity {

  @Column(name = "USER_ID", columnDefinition = "UUID", updatable = false, nullable = false)
  private UUID userId;

  @Column(name = "TOKEN", nullable = false, unique = true)
  private String token;

  @Column(name = "EXPIRE_AT", nullable = false)
  private LocalDateTime expireAt;

  @Enumerated(EnumType.STRING)
  @Column(name = "TOKEN_TYPE", nullable = false)
  private AuthTokenType tokenType;

  @Enumerated(EnumType.STRING)
  @Column(name = "TOKEN_STATUS", nullable = false)
  private AuthTokenStatus tokenStatus = AuthTokenStatus.ACTIVE;
}
