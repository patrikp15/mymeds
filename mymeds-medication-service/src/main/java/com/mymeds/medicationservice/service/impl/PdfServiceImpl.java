package com.mymeds.medicationservice.service.impl;

import com.mymeds.medicationservice.client.UserClient;
import com.mymeds.medicationservice.client.rest.GuestClientResponse;
import com.mymeds.medicationservice.client.rest.UserClientResponse;
import com.mymeds.medicationservice.config.share.PdfConfig;
import com.mymeds.medicationservice.config.share.PdfContent;
import com.mymeds.medicationservice.enumeration.PdfFontType;
import com.mymeds.medicationservice.enumeration.PdfFormat;
import com.mymeds.medicationservice.persistance.dao.UserQRCodeRepository;
import com.mymeds.medicationservice.persistance.model.UserQRCode;
import com.mymeds.medicationservice.security.CurrentAuditorContextHolder;
import com.mymeds.medicationservice.service.PdfService;
import com.mymeds.sharedutilities.enumeration.RelationshipType;
import com.mymeds.sharedutilities.exception.GeneralErrorCode;
import com.mymeds.sharedutilities.exception.MyMedsGeneralException;
import com.mymeds.sharedutilities.exception.UserErrorCode;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Comparator;
import java.util.UUID;
import javax.imageio.ImageIO;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PdfServiceImpl implements PdfService {

  private static final Logger LOG = LoggerFactory.getLogger(PdfServiceImpl.class);

  private static final float[] DEFAULT_COLOR = new float[]{51/255f,51/255f,51/255f};
  private static final float[] RED_COLOR = new float[]{249/255f,77/255f,91/255f};

  private final UserQRCodeRepository userQRCodeRepository;
  private final PdfConfig pdfConfig;
  private final UserClient userClient;

  public PdfServiceImpl(
      UserQRCodeRepository userQRCodeRepository,
      PdfConfig pdfConfig,
      UserClient userClient
  ) {
    this.userQRCodeRepository = userQRCodeRepository;
    this.pdfConfig = pdfConfig;
    this.userClient = userClient;
  }

  @Override
  public byte[] generatePDF(PdfFormat format, String text) {
    final UUID userId = getUserIdOrException();
    LOG.info("action=generatePDF, status=started, user={}, format={}", userId, format);

    try (final PDDocument document = new PDDocument()) {
      final String qrCodeBase64Format = getQRCodeBase64Format(userId);
      final byte[] qrCodeBytes = Base64.getDecoder().decode(qrCodeBase64Format);
      final BufferedImage qrImage = ImageIO.read(new ByteArrayInputStream(qrCodeBytes));

      final PDPage page = new PDPage(PDRectangle.A4);
      document.addPage(page);

      final ByteArrayOutputStream tempImageOutput = new ByteArrayOutputStream();
      ImageIO.write(qrImage, "png", tempImageOutput);

      final PDImageXObject qrCodeImage = PDImageXObject.createFromByteArray(document, tempImageOutput.toByteArray(), "qrcode");

      final PdfContent content = pdfConfig.getByPdfFormat(format);

      final PDPageContentStream contentStream = new PDPageContentStream(document, page);
      contentStream.drawImage(qrCodeImage, content.getQrX(), content.getQrY(), content.getQrSize(), content.getQrSize());

      final PDType0Font fontRegular = PDType0Font.load(document, loadFontOrException(PdfFontType.REGULAR));
      final PDType0Font fontBold = PDType0Font.load(document, loadFontOrException(PdfFontType.BOLD));

      // Initial text
      float textY = content.getQrY() + content.getFormat().getNextLineHeight();
      textY = addTextAndReturnTextY(contentStream, fontBold, content, pdfConfig.getPdfTextOrDefault(text), textY, DEFAULT_COLOR);

      // User and guests info
      final UserClientResponse userClientResponse = userClient.getUserDetail(userId);
      // User info
//      textY = addTextAndReturnTextY(contentStream, fontRegular, content, buildNameText(
//          userClientResponse.getFirstName(),
//          userClientResponse.getMiddleName(),
//          userClientResponse.getLastName(),
//          null,
//          userClientResponse.getDateOfBirth()
//      ), textY - content.getFormat().getNextLineHeight(), DEFAULT_COLOR);
//      textY = addTextAndReturnTextY(contentStream, fontRegular, content, userClientResponse.getEmail(), textY, DEFAULT_COLOR);
//      textY = addTextAndReturnTextY(contentStream, fontRegular, content, userClientResponse.getMobileNumber(), textY, DEFAULT_COLOR);

      textY = addTextAndReturnTextY(contentStream, fontBold, content, "Kontaktná osoba", textY, RED_COLOR);
      for (GuestClientResponse guest : userClientResponse.getGuests()) {
        textY = addTextAndReturnTextY(contentStream, fontRegular, content, buildNameText(
            guest.getFirstName(),
            guest.getMiddleName(),
            guest.getLastName(),
            guest.getRelationshipType(),
            null
        ), textY, RED_COLOR);
        textY = addTextAndReturnTextY(contentStream, fontRegular, content, guest.getEmail(), textY, RED_COLOR);
        textY = addTextAndReturnTextY(contentStream, fontRegular, content, guest.getMobileNumber(), textY, RED_COLOR);
      }

      contentStream.close();

      final ByteArrayOutputStream pdfOutput = new ByteArrayOutputStream();
      document.save(pdfOutput);

      LOG.info("action=generatePDF, status=finished");
      return pdfOutput.toByteArray();
    } catch (IOException e) {
      throw new MyMedsGeneralException(GeneralErrorCode.PDF_GENERATING_FAILED, e);
    }
  }

  private float addTextAndReturnTextY(
      PDPageContentStream contentStream,
      PDType0Font font,
      PdfContent content,
      String text,
      float textY,
      float[] color
  )
      throws IOException {
    float textX = pdfConfig.getCenterTextX(font, content, text);
    float nextTextY = pdfConfig.getNextTextY(content.getFormat(), textY);

    contentStream.beginText();
    contentStream.setNonStrokingColor(color[0], color[1], color[2]);
    contentStream.setFont(font, content.getFontSize());
    contentStream.newLineAtOffset(textX, nextTextY);
    contentStream.showText(text);
    contentStream.endText();
    return nextTextY;
  }

  private String getQRCodeBase64Format(UUID userId) {
    return userQRCodeRepository.findAllByUserId(userId).stream()
        .filter(qrCode -> LocalDate.now().isBefore(qrCode.getValidTo()))
        .min(Comparator.comparing(UserQRCode::getValidTo))
        .map(UserQRCode::getQrCode)
        .orElseThrow(() -> new MyMedsGeneralException(GeneralErrorCode.QR_CODE_NOT_FOUND));
  }

  private UUID getUserIdOrException() {
    return CurrentAuditorContextHolder.get()
        .orElseThrow(() -> new MyMedsGeneralException(UserErrorCode.USER_NOT_FOUND_IN_CONTEXT));
  }

  private String buildNameText(
      String firstName,
      String middleName,
      String lastName,
      RelationshipType relationshipType,
      LocalDate dateOfBirth
  ) {
    String name = firstName;
    if (StringUtils.isNotBlank(middleName)) {
      name += " " + middleName;
    }
    name += " " + lastName;
    if (relationshipType != null) {
      name += " (" + relationshipType.name() + ")";
    } else if (dateOfBirth != null) {
      name += " (" + dateOfBirth.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + ")";
    }
    return name;
  }

  private InputStream loadFontOrException(PdfFontType type) {
    final InputStream fontStream = getClass().getClassLoader().getResourceAsStream("font/" + type.getName());
    if (fontStream == null) {
      throw new MyMedsGeneralException(GeneralErrorCode.FONT_NOT_FOUND);
    }
    return fontStream;
  }
}
