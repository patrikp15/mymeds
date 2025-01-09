package com.mymeds.authservice.mapper;

import com.mymeds.authservice.client.rest.notification.AuthNotificationRequest;
import com.mymeds.authservice.persistance.model.AuthToken;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NotificationClientMapper {

  @Mapping(target = "tokenExpiration", source = "authToken.expireAt")
  AuthNotificationRequest toAuthNotificationRequest(String email, AuthToken authToken);
}
