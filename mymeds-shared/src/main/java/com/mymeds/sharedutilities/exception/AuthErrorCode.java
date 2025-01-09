package com.mymeds.sharedutilities.exception;

import java.net.HttpURLConnection;

public enum AuthErrorCode implements MyMedsError {

  AUTH_FAILED(3000, "Authentication failed", HttpURLConnection.HTTP_UNAUTHORIZED),
  JWT_TOKEN_EXPIRED(3001, "JWT Token expired", HttpURLConnection.HTTP_UNAUTHORIZED),
  AUTH_TOKEN_NOT_FOUND(3002, "Token not found", HttpURLConnection.HTTP_BAD_REQUEST),
  AUTH_TOKEN_ALREADY_USED(3003, "Token has been already used", HttpURLConnection.HTTP_BAD_REQUEST),
  AUTH_TOKEN_EXPIRED(3004, "Token expired", HttpURLConnection.HTTP_BAD_REQUEST);

  private final int code;
  private final String msg;
  private final int httpStatus;

  AuthErrorCode(int code, String msg, int httpStatus) {
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
