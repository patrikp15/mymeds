package com.mymeds.medicationservice.config.share;

import com.mymeds.medicationservice.enumeration.PdfFormat;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PdfContent {

  private PdfFormat format;
  private int fontSize;
  private int qrSize;
  private float qrX;
  private float qrY;
}
