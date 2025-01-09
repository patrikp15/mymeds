package com.mymeds.sharedutilities.exception;

public interface MyMedsError {

  int getCode();

  String getMsg();

  int getHttpStatus();
}
