package com.mymeds.medicationservice.controller;

import com.mymeds.medicationservice.enumeration.PdfFormat;
import com.mymeds.medicationservice.rest.QRCodeResponse;
import com.mymeds.medicationservice.service.PdfService;
import com.mymeds.medicationservice.service.QrService;
import java.util.UUID;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/share")
public class ShareController {

  private final PdfService pdfService;
  private final QrService qrService;

  public ShareController(PdfService pdfService, QrService qrService) {
    this.pdfService = pdfService;
    this.qrService = qrService;
  }

  @GetMapping("/qr/{userId}/generate")
  public ResponseEntity<QRCodeResponse> generateQRCode(@PathVariable UUID userId) {
    return ResponseEntity.ok(qrService.generateQRCodeForUser(userId));
  }

  @GetMapping("/qr")
  public ResponseEntity<QRCodeResponse> getQRCode() {
    return ResponseEntity.ok(qrService.getQRCodeByUserId());
  }

  @GetMapping("/pdf")
  public ResponseEntity<byte[]> generatePDF(
      @RequestParam PdfFormat format,
      @RequestParam(required = false) String text
  ) {
    final HttpHeaders headers = new HttpHeaders();
    final String fileName = "qr-" + format.name() + ".pdf";
    headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
    headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");
    return ResponseEntity
        .status(HttpStatus.OK)
        .headers(headers)
        .body(pdfService.generatePDF(format, text));
  }
}
