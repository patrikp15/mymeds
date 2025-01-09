package com.mymeds.authservice.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;

public class MaskUtils {

  private static final String NOT_AVAILABLE = "N/A";
  public static final String REPLACEMENT_CHAR = "*";

  private MaskUtils(){}

  public static String mask(@Nullable String input) {
    String output = NOT_AVAILABLE;
    if (StringUtils.isNotEmpty(input)) {
      if (input.length() > 2) {
        output = input.replaceAll("(?<=.).(?=.)", REPLACEMENT_CHAR);
      } else {
        output = input.replaceAll("\\S", REPLACEMENT_CHAR);
      }
    }
    return output;
  }
}
