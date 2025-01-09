package com.mymeds.userservice.persistance.dao;

import com.mymeds.userservice.persistance.model.UserDetail;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailRepository extends JpaRepository<UserDetail, Long> {

  Optional<UserDetail> findOneByUserId(UUID userId);
}
