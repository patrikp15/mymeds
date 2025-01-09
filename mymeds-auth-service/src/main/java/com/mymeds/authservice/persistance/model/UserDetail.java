package com.mymeds.authservice.persistance.model;

import com.mymeds.sharedutilities.enumeration.UserRole;
import com.mymeds.sharedutilities.enumeration.UserStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@Table(
    name = "USER_DETAIL",
    indexes = {
        @Index(name = "idx_user_id", columnList = "USER_ID"),
        @Index(name = "idx_email", columnList = "EMAIL")
    }
)
@Entity
public class UserDetail extends AuditableEntity implements UserDetails {

  @Column(name = "USER_ID", columnDefinition = "UUID", updatable = false, nullable = false)
  private UUID userId = UUID.randomUUID();

  @Column(name = "PASSWORD")
  private String password;

  @Email
  @Column(name = "EMAIL", nullable = false, unique = true)
  private String email;

  @Enumerated(EnumType.STRING)
  @Column(name = "USER_ROLE", nullable = false)
  private UserRole userRole;

  @Enumerated(EnumType.STRING)
  @Column(name = "USER_STATUS", nullable = false)
  private UserStatus userStatus;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.singleton(new SimpleGrantedAuthority(userRole.getAuthority()));
  }

  @Override
  public String getUsername() {
    return userId.toString();
  }

  @Override
  public boolean isAccountNonExpired() {
    return false;
  }

  @Override
  public boolean isAccountNonLocked() {
    return false;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return false;
  }
}
