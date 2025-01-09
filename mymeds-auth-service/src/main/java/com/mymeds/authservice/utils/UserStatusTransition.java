package com.mymeds.authservice.utils;

import com.mymeds.sharedutilities.enumeration.UserStatus;
import com.mymeds.sharedutilities.exception.MyMedsGeneralException;
import com.mymeds.sharedutilities.exception.UserErrorCode;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserStatusTransition {

  private static final Logger LOG = LoggerFactory.getLogger(UserStatusTransition.class);

  private static final Map<UserStatus, Set<UserStatus>> transitionRules;
  private final UserStatus from;
  private final UserStatus to;

  static {
    transitionRules = new HashMap<>();
    transitionRules.put(UserStatus.NOT_VERIFIED, Set.of(UserStatus.ACTIVE));
    transitionRules.put(UserStatus.ACTIVE, Set.of(UserStatus.NOT_ACTIVE));
    transitionRules.put(UserStatus.NOT_ACTIVE, Set.of());
  }

  private UserStatusTransition(UserStatus from, UserStatus to){
    this.from = from;
    this.to = to;
  }

  public static UserStatusTransition build(UserStatus from, UserStatus to) {
    return new UserStatusTransition(from, to);
  }

  public void validate() {
    if (!transitionRules.get(this.from).contains(this.to)) {
      LOG.error("action=validate, status=failed, message=User status transition not allowed");
      throw new MyMedsGeneralException(UserErrorCode.USER_STATUS_TRANSITION_NOT_ALLOWED);
    }
  }
}
