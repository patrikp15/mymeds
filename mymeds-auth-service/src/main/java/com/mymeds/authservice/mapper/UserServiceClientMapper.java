package com.mymeds.authservice.mapper;

import com.mymeds.authservice.client.rest.user.RegisterGustClientRequest;
import com.mymeds.authservice.client.rest.user.RegisterUserClientRequest;
import com.mymeds.authservice.client.rest.user.UpdateUserClientRequest;
import com.mymeds.authservice.rest.CreateGuestRequest;
import com.mymeds.authservice.rest.RegisterUserRequest;
import com.mymeds.authservice.rest.UpdateUserRequest;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserServiceClientMapper {

  @Mapping(target = "guestRequest", expression = "java(toRegisterGustClientRequest(request.getGuestRequest(), guestId, null))")
  RegisterUserClientRequest toRegisterUserClientRequest(RegisterUserRequest request, UUID userId, UUID guestId);

  RegisterGustClientRequest toRegisterGustClientRequest(CreateGuestRequest request, UUID guestId, UUID userId);

  UpdateUserClientRequest toUpdateUserClientRequest(UpdateUserRequest request);
}
