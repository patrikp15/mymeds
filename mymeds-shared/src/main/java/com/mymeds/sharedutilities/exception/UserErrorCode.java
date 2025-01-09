package com.mymeds.sharedutilities.exception;

import java.net.HttpURLConnection;

public enum UserErrorCode implements MyMedsError {

  USER_NOT_FOUND_IN_CONTEXT(2000, "User not found in the current context", HttpURLConnection.HTTP_BAD_REQUEST),
  USER_MUST_BE_ACTIVE(2001, "User must be active for this action", HttpURLConnection.HTTP_BAD_REQUEST),
  EMAIL_ALREADY_EXISTS(2002, "Email address already exists", HttpURLConnection.HTTP_BAD_REQUEST),
  PASSWORD_INVALID(2003, "Invalid password", HttpURLConnection.HTTP_BAD_REQUEST),
  PASSWORDS_NOT_MATCH(2004, "Password and re-password don't match", HttpURLConnection.HTTP_BAD_REQUEST),
  EMAIL_FORMAT_INVALID(2005, "Email format is invalid", HttpURLConnection.HTTP_BAD_REQUEST),
  USER_NOT_FOUND(2006, "User not found", HttpURLConnection.HTTP_BAD_REQUEST),
  USER_ID_NOT_FOUND(2007, "User ID not part of the context", HttpURLConnection.HTTP_BAD_REQUEST),
  USER_STATUS_TRANSITION_NOT_ALLOWED(2008, "User status transition not allowed", HttpURLConnection.HTTP_BAD_REQUEST),
  MAX_NUMBER_OF_GUESTS_EXCEEDED(2009, "Max number of guests has exceeded", HttpURLConnection.HTTP_BAD_REQUEST),
  USER_BY_TOKEN_NOT_FOUND(2010, "User by token not found", HttpURLConnection.HTTP_BAD_REQUEST);

  private final int code;
  private final String msg;
  private final int httpStatus;

  UserErrorCode(int code, String msg, int httpStatus) {
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
