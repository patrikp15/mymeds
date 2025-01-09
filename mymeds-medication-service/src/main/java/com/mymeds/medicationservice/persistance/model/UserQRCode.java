package com.mymeds.medicationservice.persistance.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Getter
@Setter
@Table(name = "USER_QR_CODE")
@Entity
public class UserQRCode extends AuditableEntity {

  @Column(name = "USER_ID", columnDefinition = "UUID", updatable = false, nullable = false)
  private UUID userId;

  @Column(name = "QR_CODE", nullable = false)
  @Lob
  private String qrCode;

  @Column(name = "VALID_FROM", nullable = false)
  private LocalDate validFrom;

  @Column(name = "VALID_TO", nullable = false)
  private LocalDate validTo;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    final UserQRCode that = (UserQRCode) o;

    return new EqualsBuilder().appendSuper(super.equals(o))
        .append(getUserId(), that.getUserId()).append(getQrCode(), that.getQrCode())
        .append(getValidFrom(), that.getValidFrom()).append(getValidTo(), that.getValidTo()).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).appendSuper(super.hashCode()).append(getUserId()).append(getQrCode())
        .append(getValidFrom()).append(getValidTo()).toHashCode();
  }
}
