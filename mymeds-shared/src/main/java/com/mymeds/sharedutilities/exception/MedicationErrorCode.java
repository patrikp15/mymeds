package com.mymeds.sharedutilities.exception;

import java.net.HttpURLConnection;

public enum MedicationErrorCode implements MyMedsError {

  MEDICATION_NOT_FOUND(4000, "Medication not found", HttpURLConnection.HTTP_BAD_REQUEST),
  USER_MEDICATION_NOT_FOUND(4001, "User medication not found", HttpURLConnection.HTTP_BAD_REQUEST),
  DOSE_NOT_BELONG_TO_MEDICATION(4002, "Dose doesn't belong to medication", HttpURLConnection.HTTP_BAD_REQUEST);

  private final int code;
  private final String msg;
  private final int httpStatus;

  MedicationErrorCode(int code, String msg, int httpStatus) {
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
