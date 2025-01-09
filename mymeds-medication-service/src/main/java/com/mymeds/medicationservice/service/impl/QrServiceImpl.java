package com.mymeds.medicationservice.service.impl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.mymeds.medicationservice.config.share.PdfConfig;
import com.mymeds.medicationservice.mapper.ShareMapper;
import com.mymeds.medicationservice.persistance.dao.UserQRCodeRepository;
import com.mymeds.medicationservice.persistance.model.UserQRCode;
import com.mymeds.medicationservice.rest.QRCodeResponse;
import com.mymeds.medicationservice.security.CurrentAuditorContextHolder;
import com.mymeds.medicationservice.service.QrService;
import com.mymeds.sharedutilities.exception.GeneralErrorCode;
import com.mymeds.sharedutilities.exception.MyMedsGeneralException;
import com.mymeds.sharedutilities.exception.UserErrorCode;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Comparator;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class QrServiceImpl implements QrService {

  private static final Logger LOG = LoggerFactory.getLogger(QrServiceImpl.class);

  private final UserQRCodeRepository userQRCodeRepository;
  private final int qrExpiration;
  private final ShareMapper shareMapper;
  private final String feQRBaseUrl;
  private final PdfConfig pdfConfig;

  public QrServiceImpl(
      UserQRCodeRepository userQRCodeRepository,
      ShareMapper shareMapper,
      @Value("${share.qr.expiration}") int qrExpiration,
      @Value("${mymeds.url.fe-qr-base-url}") String feQRBaseUrl,
      PdfConfig pdfConfig) {
    this.userQRCodeRepository = userQRCodeRepository;
    this.qrExpiration = qrExpiration;
    this.shareMapper = shareMapper;
    this.feQRBaseUrl = feQRBaseUrl;
    this.pdfConfig = pdfConfig;
  }

  @Override
  public QRCodeResponse generateQRCodeForUser(UUID userId) {
    LOG.info("action=generateQRCodeForUser, status=started, user={}", userId);
    userQRCodeRepository.findAllByUserId(userId).stream()
        .filter(userQRCode -> LocalDate.now().isBefore(userQRCode.getValidTo()))
        .findAny()
        .ifPresent(userQRCode -> {
          throw new MyMedsGeneralException(GeneralErrorCode.VALID_QR_CODE_FOR_USER_ALREADY_EXISTS);
        });

    final String qrCode = generateQRCodeBase64(userId)
        .orElseThrow(() -> new MyMedsGeneralException(GeneralErrorCode.UNABLE_GENERATE_QR_CODE));

    final LocalDate validFrom = LocalDate.now();
    final LocalDate validTo = LocalDate.from(validFrom).plus(qrExpiration, ChronoUnit.DAYS);
    userQRCodeRepository.save(shareMapper.toUserQRCode(userId, qrCode, validFrom, validTo));

    LOG.info("action=generateQRCodeForUser, status=finished");
    return shareMapper.toQrCodeResponse(qrCode);
  }

  @Override
  public QRCodeResponse getQRCodeByUserId() {
    final UUID userId = getUserIdOrException();
    LOG.info("action=getQRCodeByUserId, status=started, user={}", userId);

    final UserQRCode userQRCode = userQRCodeRepository.findAllByUserId(userId).stream()
        .filter(qrCode -> LocalDate.now().isBefore(qrCode.getValidTo()))
        .min(Comparator.comparing(UserQRCode::getValidTo))
        .orElseThrow(() -> new MyMedsGeneralException(GeneralErrorCode.QR_CODE_NOT_FOUND));

    LOG.info("action=getQRCodeByUserId, status=finished");
    return shareMapper.toQrCodeResponse(userQRCode.getQrCode());
  }

  private Optional<String> generateQRCodeBase64(UUID userId) {
    try (final ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
      final QRCodeWriter qrCodeWriter = new QRCodeWriter();
      final BitMatrix bitMatrix = qrCodeWriter.encode(
          composeUrlForQRCode(userId),
          BarcodeFormat.QR_CODE,
          pdfConfig.getQrDefaultSize(),
          pdfConfig.getQrDefaultSize()
      );

      MatrixToImageWriter.writeToStream(bitMatrix, "PNG", baos);
      return Optional.of(Base64.getEncoder().encodeToString(baos.toByteArray()));
    } catch (Exception e) {
      LOG.error("action=generateQRCodeBase64, status=failed, message=unable to generate QR Code", e);
      return Optional.empty();
    }
  }

  private UUID getUserIdOrException() {
    return CurrentAuditorContextHolder.get()
        .orElseThrow(() -> new MyMedsGeneralException(UserErrorCode.USER_NOT_FOUND_IN_CONTEXT));
  }

  private String composeUrlForQRCode(UUID userId) {
    return UriComponentsBuilder.fromHttpUrl(feQRBaseUrl)
        .queryParam("uid", userId)
        .build()
        .toUriString();
  }
}
