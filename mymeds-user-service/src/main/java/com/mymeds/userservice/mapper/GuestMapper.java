package com.mymeds.userservice.mapper;

import com.mymeds.userservice.client.rest.AuthRegisterGuestClientRequest;
import com.mymeds.userservice.client.rest.AuthUserDetailClientResponse;
import com.mymeds.userservice.persistance.model.Guest;
import com.mymeds.userservice.rest.GetGuestResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GuestMapper {

  @Mapping(target = "userDetail", ignore = true)
  Guest toGuest(AuthRegisterGuestClientRequest request);

  GetGuestResponse toGetGuestResponse(Guest guest, AuthUserDetailClientResponse guestAuthDetail);
}
