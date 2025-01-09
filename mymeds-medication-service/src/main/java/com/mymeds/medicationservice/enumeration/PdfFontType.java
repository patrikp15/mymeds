package com.mymeds.medicationservice.enumeration;

public enum PdfFontType {

  REGULAR("Nunito-Regular.ttf"),
  BOLD("Nunito-Bold.ttf"),
  ITALIC("Nunito-Italic.ttf");

  private final String name;

  PdfFontType(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
