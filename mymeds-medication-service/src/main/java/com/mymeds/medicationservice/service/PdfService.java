package com.mymeds.medicationservice.service;

import com.mymeds.medicationservice.enumeration.PdfFormat;

public interface PdfService {

  byte[] generatePDF(PdfFormat format, String text);
}
