package com.mymeds.sharedutilities.exception;

import java.net.HttpURLConnection;

public enum GeneralErrorCode implements MyMedsError {

  UNKNOWN_EXCEPTION(9999, "Unspecified exception has occurred", HttpURLConnection.HTTP_BAD_REQUEST),
  VALID_QR_CODE_FOR_USER_ALREADY_EXISTS(1000, "Valid QR Code for user already exists", HttpURLConnection.HTTP_BAD_REQUEST),
  QR_CODE_NOT_FOUND(1002, "QR Code not found", HttpURLConnection.HTTP_BAD_REQUEST),
  UNABLE_GENERATE_QR_CODE(1003, "Unable to generate QR Code", HttpURLConnection.HTTP_BAD_REQUEST),
  FONT_NOT_FOUND(1004, "Font not found", HttpURLConnection.HTTP_BAD_REQUEST),
  PDF_CALCULATING_DIMENSION_FAILED(1005, "PDF calculating dimension has failed", HttpURLConnection.HTTP_BAD_REQUEST),
  CLIENT_REQUEST_FAILED(1006, "Client request failed", HttpURLConnection.HTTP_BAD_REQUEST),
  PDF_GENERATING_FAILED(1007, "Generating pdf failed", HttpURLConnection.HTTP_BAD_REQUEST),
  VALUE_MUST_NOT_BE_NULL_NOR_EMPTY(1008, "Value must not be null not empty", HttpURLConnection.HTTP_BAD_REQUEST),
  REQUEST_TO_CLIENT_SERVICE_FAILED(1009, "Request to client service failed", HttpURLConnection.HTTP_BAD_REQUEST),
  USER_STATUS_TRANSITION_NOT_ALLOWED(1010, "User status transition not allowed", HttpURLConnection.HTTP_BAD_REQUEST);

  private final int code;
  private final String msg;
  private final int httpStatus;

  GeneralErrorCode(int code, String msg, int httpStatus) {
    this.code = code;
    this.msg = msg;
    this.httpStatus = httpStatus;
  }

  @Override
  public int getCode() {
    return code;
  }

  @Override
  public String getMsg() {
    return msg;
  }

  @Override
  public int getHttpStatus() {
    return httpStatus;
  }
}
