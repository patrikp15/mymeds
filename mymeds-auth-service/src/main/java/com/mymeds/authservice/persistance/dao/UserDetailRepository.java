package com.mymeds.authservice.persistance.dao;

import com.mymeds.authservice.persistance.model.UserDetail;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailRepository extends JpaRepository<UserDetail, Long> {

  Optional<UserDetail> findOneByUserId(UUID userId);

  Optional<UserDetail> findOneByEmail(String email);

  boolean existsByEmail(String email);

  boolean existsByUserId(UUID userId);
}
