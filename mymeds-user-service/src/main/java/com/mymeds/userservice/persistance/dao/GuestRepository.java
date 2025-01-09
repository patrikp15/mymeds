package com.mymeds.userservice.persistance.dao;

import com.mymeds.userservice.persistance.model.Guest;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestRepository extends JpaRepository<Guest, Long> {

  Optional<Guest> findOneByGuestId(UUID guestId);

  List<Guest> findAllByUserDetailUserId(UUID userId);
}
