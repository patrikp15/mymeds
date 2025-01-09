package com.mymeds.authservice.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateUtils {

  private DateUtils(){}

  public static LocalDateTime fromDateToLocalDateTime(Date date) {
    return LocalDateTime.ofInstant(
        date.toInstant(),
        ZoneId.systemDefault()
    );
  }
}
