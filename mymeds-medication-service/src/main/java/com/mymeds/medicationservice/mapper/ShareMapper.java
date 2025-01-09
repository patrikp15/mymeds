package com.mymeds.medicationservice.mapper;

import com.mymeds.medicationservice.persistance.model.UserQRCode;
import com.mymeds.medicationservice.rest.QRCodeResponse;
import java.time.LocalDate;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ShareMapper {

  UserQRCode toUserQRCode(UUID userId, String qrCode, LocalDate validFrom, LocalDate validTo);

  QRCodeResponse toQrCodeResponse(String qrCodeBase64);
}
