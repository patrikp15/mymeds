package com.mymeds.authservice.client;

import com.mymeds.authservice.client.rest.user.RegisterGustClientRequest;
import com.mymeds.authservice.client.rest.user.RegisterUserClientRequest;
import com.mymeds.authservice.client.rest.user.UpdateUserClientRequest;
import com.mymeds.authservice.mapper.UserServiceClientMapper;
import com.mymeds.authservice.persistance.model.UserDetail;
import com.mymeds.authservice.rest.CreateGuestRequest;
import com.mymeds.authservice.rest.RegisterUserRequest;
import com.mymeds.authservice.rest.UpdateUserRequest;
import com.mymeds.sharedutilities.enumeration.UserRole;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UserClient extends BaseClient {

  private final UserServiceClientMapper userServiceClientMapper;

  public UserClient(
      RestTemplate restTemplate,
      @Value("${mymeds.url.user-service-base-url}") String baseUrl,
      UserServiceClientMapper userServiceClientMapper) {
    super(restTemplate, baseUrl);
    this.userServiceClientMapper = userServiceClientMapper;
  }

  public void sendUserUpdateUserToUserService(UUID userId, UserRole userRole, UpdateUserRequest request) {
    final UpdateUserClientRequest updateUserClientRequest = userServiceClientMapper.toUpdateUserClientRequest(
        request
    );
    if (UserRole.BASIC.equals(userRole)) {
      requestPut(updateUserClientRequest, "/users/" + userId, Void.class);
    } else {
      requestPut(updateUserClientRequest, "/guests/" + userId, Void.class);
    }
  }

  public void sendUserRegistrationDetailsToUserService(RegisterUserRequest request, UserDetail user, UserDetail guest) {
    final RegisterUserClientRequest registerUserClientRequest = userServiceClientMapper.toRegisterUserClientRequest(
        request,
        user.getUserId(),
        guest.getUserId()
    );
    requestPost(registerUserClientRequest, "/auth/register", Void.class);
  }

  public void sendCreateGuestDetailsToUserService(UUID userId, UUID guestId, CreateGuestRequest request) {
    final RegisterGustClientRequest registerGustClientRequest = userServiceClientMapper.toRegisterGustClientRequest(
        request,
        guestId,
        userId
    );
    requestPost(registerGustClientRequest, "/guests", Void.class);
  }
}
