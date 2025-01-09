package com.mymeds.authservice.persistance.model;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class AuditableEntity extends BaseEntity {

  @CreatedDate
  private LocalDateTime createdTs;

  @LastModifiedDate
  private LocalDateTime modifiedTs;

  @CreatedBy
  private String createdBy;

  @LastModifiedBy
  private String modifiedBy;
}
