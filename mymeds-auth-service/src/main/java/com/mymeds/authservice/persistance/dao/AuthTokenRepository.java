package com.mymeds.authservice.persistance.dao;

import com.mymeds.authservice.persistance.model.AuthToken;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthTokenRepository extends JpaRepository<AuthToken, Long> {

  Optional<AuthToken> findOneByToken(String token);

  List<AuthToken> findAllByUserId(UUID userId);
}
