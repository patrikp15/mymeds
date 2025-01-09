package com.mymeds.sharedutilities.exception;

public class RestErrorResponse {

  private final String internalMsg;
  private final int httpStatusCode;
  private final int code;
  private final String causeMsg;

  public RestErrorResponse(MyMedsError myMedsError, String causeMsg) {
    this.internalMsg = myMedsError.getMsg();
    this.httpStatusCode = myMedsError.getHttpStatus();
    this.code = myMedsError.getCode();
    this.causeMsg = causeMsg;
  }

  public RestErrorResponse(String causeMsg, int httpStatusCode) {
    this.internalMsg = null;
    this.httpStatusCode = httpStatusCode;
    this.code = GeneralErrorCode.UNKNOWN_EXCEPTION.getCode();
    this.causeMsg = causeMsg;
  }

  public RestErrorResponse(MyMedsError myMedsError) {
    this(myMedsError, "");
  }

  public String getInternalMsg() {
    return internalMsg;
  }

  public int getHttpStatusCode() {
    return httpStatusCode;
  }

  public int getCode() {
    return code;
  }

  public String getCauseMsg() {
    return causeMsg;
  }
}
