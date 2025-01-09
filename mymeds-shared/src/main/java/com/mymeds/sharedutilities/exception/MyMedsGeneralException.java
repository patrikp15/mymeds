package com.mymeds.sharedutilities.exception;

public class MyMedsGeneralException extends RuntimeException implements MyMedsException {

  private final MyMedsError errorCode;

  public MyMedsGeneralException(MyMedsError errorCode) {
    super(errorCode.getMsg());
    this.errorCode = errorCode;
  }

  public MyMedsGeneralException(MyMedsError errorCode, Throwable cause) {
    super(errorCode.getMsg(), cause);
    this.errorCode = errorCode;
  }

  @Override
  public MyMedsError getError() {
    return errorCode;
  }
}
