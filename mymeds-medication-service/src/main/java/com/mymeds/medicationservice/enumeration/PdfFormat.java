package com.mymeds.medicationservice.enumeration;

public enum PdfFormat {

  A4(28),
  MOBILE(10);

  private final float nextLineHeight;

  PdfFormat(float nextLineHeight) {
    this.nextLineHeight = nextLineHeight;
  }

  public float getNextLineHeight() {
    return nextLineHeight;
  }
}
