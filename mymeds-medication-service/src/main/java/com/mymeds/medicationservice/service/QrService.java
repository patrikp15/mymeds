package com.mymeds.medicationservice.service;

import com.mymeds.medicationservice.rest.QRCodeResponse;
import java.util.UUID;

public interface QrService {

  QRCodeResponse generateQRCodeForUser(UUID userId);

  QRCodeResponse getQRCodeByUserId();
}
