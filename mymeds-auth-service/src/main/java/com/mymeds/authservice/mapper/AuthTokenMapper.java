package com.mymeds.authservice.mapper;

import com.mymeds.authservice.persistance.model.AuthToken;
import com.mymeds.sharedutilities.enumeration.AuthTokenType;
import java.time.LocalDateTime;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuthTokenMapper {

  AuthToken toAuthToken(String token, UUID userId, LocalDateTime expireAt, AuthTokenType tokenType);
}
