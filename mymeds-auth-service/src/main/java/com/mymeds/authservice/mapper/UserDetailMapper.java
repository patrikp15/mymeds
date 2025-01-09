package com.mymeds.authservice.mapper;

import com.mymeds.authservice.persistance.model.UserDetail;
import com.mymeds.authservice.rest.AuthUserResponse;
import com.mymeds.authservice.rest.CreateGuestRequest;
import com.mymeds.authservice.rest.GetUserDetailResponse;
import com.mymeds.authservice.rest.RegisterUserRequest;
import com.mymeds.sharedutilities.enumeration.UserRole;
import com.mymeds.sharedutilities.enumeration.UserStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserDetailMapper {

  @Mapping(target = "password", ignore = true)
  UserDetail toUserDetail(RegisterUserRequest request, UserRole userRole, UserStatus userStatus);

  AuthUserResponse toAuthUserResponse(UserDetail userDetail);

  UserDetail toGuestUserDetail(CreateGuestRequest request, UserRole userRole, UserStatus userStatus);

  GetUserDetailResponse toGetUserDetailResponse(UserDetail userDetail);

  UserDetail toUserDetail(CreateGuestRequest request, UserRole userRole, UserStatus userStatus);
}
