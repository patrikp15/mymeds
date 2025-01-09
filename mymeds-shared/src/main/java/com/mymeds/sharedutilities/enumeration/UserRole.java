package com.mymeds.sharedutilities.enumeration;

public enum UserRole {

  ADMIN, BASIC, GUEST;

  public String getAuthority() {
    return "ROLE_" + this.name();
  }
}
