package com.mymeds.authservice.utils;

import com.mymeds.sharedutilities.exception.GeneralErrorCode;
import com.mymeds.sharedutilities.exception.MyMedsGeneralException;
import com.mymeds.sharedutilities.exception.UserErrorCode;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Validation {
  
  private static final Logger LOG = LoggerFactory.getLogger(Validation.class);

  @Value("${regex.password}")
  private String passwordRegex;

  @Value("${regex.email}")
  private String emailRegex;

  public void validatePassword(String password, String passwordRecap) {
    guardStringNotBlank("password", password);
    guardStringNotBlank("re-password", passwordRecap);

    final Pattern pattern = Pattern.compile(passwordRegex);
    if (!pattern.matcher(password).matches()) {
      throw new MyMedsGeneralException(UserErrorCode.PASSWORD_INVALID);
    }

    if (!password.equals(passwordRecap)) {
      throw new MyMedsGeneralException(UserErrorCode.PASSWORDS_NOT_MATCH);
    }
  }

  public void validateEmailAddress(String email) {
    guardStringNotBlank("email", email);

    final Pattern pattern = Pattern.compile(emailRegex);
    if (!pattern.matcher(email).matches()) {
      throw new MyMedsGeneralException(UserErrorCode.EMAIL_FORMAT_INVALID);
    }
  }

  public void guardStringNotBlank(String field, String input) {
    if (StringUtils.isEmpty(input)) {
      LOG.error("action=guardStringNotBlank, status=failed, field={}, message=\"Value must not be blank\"", field);
      throw new MyMedsGeneralException(GeneralErrorCode.VALUE_MUST_NOT_BE_NULL_NOR_EMPTY);
    }
  }

  public void guardEmailDoesNotExist(Supplier<Boolean> validationSupplier) {
    if (validationSupplier.get()) {
      LOG.error("action=guardEmailDoesNotExist, status=failed, message=\"Email address already exists\"");
      throw new MyMedsGeneralException(UserErrorCode.EMAIL_ALREADY_EXISTS);
    }
  }
}
