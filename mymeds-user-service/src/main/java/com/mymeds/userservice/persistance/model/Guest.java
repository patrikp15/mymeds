package com.mymeds.userservice.persistance.model;

import com.mymeds.sharedutilities.enumeration.RelationshipType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Getter
@Setter
@Table(name = "GUEST")
@Entity
public class Guest extends AuditableEntity {

  @Column(name = "GUEST_ID", columnDefinition = "UUID", updatable = false, nullable = false)
  private UUID guestId;

  @Column(name = "FIRST_NAME", nullable = false)
  private String firstName;

  @Column(name = "MIDDLE_NAME")
  private String middleName;

  @Column(name = "LAST_NAME", nullable = false)
  private String lastName;

  @Column(name = "MOBILE_NUMBER", nullable = false)
  private String mobileNumber;

  @Enumerated(EnumType.STRING)
  @Column(name = "RELATIONSHIP_TYPE", nullable = false, updatable = false)
  private RelationshipType relationshipType;

  @ManyToOne
  @JoinColumn(name = "USER_DETAIL", nullable = false)
  private UserDetail userDetail;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    final Guest guest = (Guest) o;

    return new EqualsBuilder().appendSuper(super.equals(o))
        .append(getGuestId(), guest.getGuestId()).append(getFirstName(), guest.getFirstName())
        .append(getMiddleName(), guest.getMiddleName()).append(getLastName(), guest.getLastName())
        .append(getMobileNumber(), guest.getMobileNumber()).append(getRelationshipType(), guest.getRelationshipType())
        .append(getUserDetail(), guest.getUserDetail()).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).appendSuper(super.hashCode()).append(getGuestId()).append(getFirstName())
        .append(getMiddleName()).append(getLastName()).append(getMobileNumber()).append(getRelationshipType())
        .append(getUserDetail()).toHashCode();
  }
}
