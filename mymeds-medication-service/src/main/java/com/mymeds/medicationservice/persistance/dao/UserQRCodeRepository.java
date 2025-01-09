package com.mymeds.medicationservice.persistance.dao;

import com.mymeds.medicationservice.persistance.model.UserQRCode;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserQRCodeRepository extends JpaRepository<UserQRCode, Long> {

  List<UserQRCode> findAllByUserId(UUID userId);
}
