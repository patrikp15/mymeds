package com.mymeds.medicationservice.config.share;

import com.mymeds.medicationservice.enumeration.PdfFormat;
import com.mymeds.sharedutilities.exception.GeneralErrorCode;
import com.mymeds.sharedutilities.exception.MyMedsGeneralException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PdfConfig {

  private final int pdfTextMaxLimit;
  private final String pdfDefaultText;
  private final int pdfA4FontSize;
  private final int pdfMobileFontSize;
  private final int qrDefaultSize;
  private final int qrMobileSize;

  private final Map<PdfFormat, PdfContent> pdfContentMap;

  public PdfConfig(
      @Value("${share.pdf.text-max-limit}") int pdfTextMaxLimit,
      @Value("${share.pdf.default-text}") String pdfDefaultText,
      @Value("${share.pdf.a4-font-size}") int pdfA4FontSize,
      @Value("${share.pdf.mobile-font-size}") int pdfMobileFontSize,
      @Value("${share.qr.default-size}") int qrDefaultSize,
      @Value("${share.qr.mobile-size}") int qrMobileSize
  ) {
    this.pdfTextMaxLimit = pdfTextMaxLimit;
    this.pdfDefaultText = pdfDefaultText;
    this.pdfA4FontSize = pdfA4FontSize;
    this.pdfMobileFontSize = pdfMobileFontSize;
    this.qrDefaultSize = qrDefaultSize;
    this.qrMobileSize = qrMobileSize;

    pdfContentMap = new HashMap<>(2);
    pdfContentMap.put(PdfFormat.A4, buildA4PdfContent());
    pdfContentMap.put(PdfFormat.MOBILE, buildMobilePdfContent());
  }

  public int getQrDefaultSize() {
    return qrDefaultSize;
  }

  public PdfContent getByPdfFormat(PdfFormat format) {
    return pdfContentMap.get(format);
  }

  public String getPdfTextOrDefault(String text) {
    if (StringUtils.isNotBlank(text)) {
      return text.length() >= pdfTextMaxLimit ? text.substring(0, pdfTextMaxLimit - 1) : text;
    }
    return pdfDefaultText.length() >= pdfTextMaxLimit ? pdfDefaultText.substring(0, pdfTextMaxLimit - 1) : pdfDefaultText;
  }

  public float getCenterTextX(PDType0Font font, PdfContent pdfContent, String text) {
    try {
      float textWidth = font.getStringWidth(text) / 1000 * pdfContent.getFontSize();
      return (PDRectangle.A4.getWidth() - textWidth) / 2;
    } catch (IOException e) {
      throw new MyMedsGeneralException(GeneralErrorCode.PDF_CALCULATING_DIMENSION_FAILED);
    }
  }

  public float getNextTextY(PdfFormat format, float nextTextY) {
    return nextTextY - format.getNextLineHeight();
  }

  private PdfContent buildA4PdfContent() {
    return PdfContent.builder()
        .format(PdfFormat.A4)
        .fontSize(pdfA4FontSize)
        .qrSize(qrDefaultSize)
        .qrX((PDRectangle.A4.getWidth() - qrDefaultSize) / 2)
        .qrY(PDRectangle.A4.getHeight() - qrDefaultSize - PdfFormat.A4.getNextLineHeight())
        .build();
  }

  private PdfContent buildMobilePdfContent() {
    return PdfContent.builder()
        .format(PdfFormat.MOBILE)
        .fontSize(pdfMobileFontSize)
        .qrSize(qrMobileSize)
        .qrX((PDRectangle.A4.getWidth() - qrMobileSize) / 2)
        .qrY(PDRectangle.A4.getHeight() - qrMobileSize - PdfFormat.MOBILE.getNextLineHeight())
        .build();
  }
}
