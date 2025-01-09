package com.mymeds.userservice.persistance.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Getter
@Setter
@Table(name = "USER_DETAIL")
@Entity
public class UserDetail extends AuditableEntity {

  @Column(name = "USER_ID", columnDefinition = "UUID", updatable = false, nullable = false)
  private UUID userId;

  @Column(name = "FIRST_NAME", nullable = false)
  private String firstName;

  @Column(name = "MIDDLE_NAME")
  private String middleName;

  @Column(name = "LAST_NAME")
  private String lastName;

  @Column(name = "MOBILE_NUMBER", nullable = false)
  private String mobileNumber;

  @Column(name = "DATE_OF_BIRTH", nullable = false, updatable = false)
  private LocalDate dateOfBirth;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    final UserDetail that = (UserDetail) o;

    return new EqualsBuilder().appendSuper(super.equals(o))
        .append(getUserId(), that.getUserId()).append(getFirstName(), that.getFirstName())
        .append(getMiddleName(), that.getMiddleName()).append(getLastName(), that.getLastName())
        .append(getMobileNumber(), that.getMobileNumber()).append(getDateOfBirth(), that.getDateOfBirth()).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).appendSuper(super.hashCode()).append(getUserId()).append(getFirstName())
        .append(getMiddleName()).append(getLastName()).append(getMobileNumber()).append(getDateOfBirth()).toHashCode();
  }
}
