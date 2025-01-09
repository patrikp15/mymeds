package com.mymeds.userservice.mapper;

import com.mymeds.userservice.client.rest.AuthRegisterUserClientRequest;
import com.mymeds.userservice.client.rest.AuthUserDetailClientResponse;
import com.mymeds.userservice.persistance.model.Guest;
import com.mymeds.userservice.persistance.model.UserDetail;
import com.mymeds.userservice.rest.GetGuestResponse;
import com.mymeds.userservice.rest.GetUserResponse;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserDetailMapper {

  UserDetail toUserDetail(AuthRegisterUserClientRequest request);

  GetUserResponse toGetUserDetailResponse(
      UserDetail userDetail,
      AuthUserDetailClientResponse authUserClientResponse,
      List<GetGuestResponse> guests
  );

  GetGuestResponse toGetGuestResponse(Guest guest, AuthUserDetailClientResponse authUserClientResponse);
}
