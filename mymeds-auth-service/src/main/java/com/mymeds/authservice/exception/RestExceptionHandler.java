package com.mymeds.authservice.exception;

import com.mymeds.sharedutilities.exception.GeneralErrorCode;
import com.mymeds.sharedutilities.exception.MyMedsGeneralException;
import com.mymeds.sharedutilities.exception.RestErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler({MyMedsGeneralException.class})
  public ResponseEntity<RestErrorResponse> handleMyMedsGeneralException(MyMedsGeneralException ex) {
    final int statusCode = ex.getError().getHttpStatus();
    RestErrorResponse response;
    if (ex.getCause() != null) {
      response = new RestErrorResponse(ex.getError(), ex.getCause().getMessage());
    } else {
      response = new RestErrorResponse(ex.getError());
    }
    return ResponseEntity.status(statusCode)
        .body(response);
  }

  @ExceptionHandler({Exception.class})
  public ResponseEntity<RestErrorResponse> handleUnknownException(Exception ex) {
    final GeneralErrorCode errorCode = GeneralErrorCode.UNKNOWN_EXCEPTION;
    return ResponseEntity.status(errorCode.getHttpStatus())
        .body(
            new RestErrorResponse(
                ex.getMessage(),
                errorCode.getHttpStatus()
            )
        );
  }
}
