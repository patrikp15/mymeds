package com.mymeds.authservice.controller;

import com.mymeds.authservice.rest.ChangeUserStatusRequest;
import com.mymeds.authservice.rest.CreateGuestRequest;
import com.mymeds.authservice.rest.GetUserDetailResponse;
import com.mymeds.authservice.rest.RegisterGuestRequest;
import com.mymeds.authservice.rest.UpdateUserRequest;
import com.mymeds.authservice.security.CurrentAuditorContextHolder;
import com.mymeds.authservice.service.AuthTokenService;
import com.mymeds.authservice.service.UserService;
import com.mymeds.sharedutilities.exception.MyMedsGeneralException;
import com.mymeds.sharedutilities.exception.UserErrorCode;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/guests")
public class AuthGuestController {

  private final UserService userService;
  private final AuthTokenService authTokenService;

  public AuthGuestController(UserService userService, AuthTokenService authTokenService) {
    this.userService = userService;
    this.authTokenService = authTokenService;
  }

  @PutMapping("/{guestId}/status")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void changeGuestStatus(@PathVariable UUID guestId, @RequestBody @Valid ChangeUserStatusRequest request) {
    userService.changeUserStatus(guestId, request);
  }

  @PutMapping("/{guestId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void updateGuest(@PathVariable UUID guestId, @RequestBody @Valid UpdateUserRequest request) {
    userService.updateUser(guestId, request);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void createGuest(@RequestBody @Valid CreateGuestRequest request) {
    final UUID userId = CurrentAuditorContextHolder.get()
        .orElseThrow(() -> new MyMedsGeneralException(UserErrorCode.USER_NOT_FOUND));
    userService.createGuest(userId, request);
  }

  @GetMapping("/detail")
  public GetUserDetailResponse getGuestDetailByToken(@RequestParam(name = "token") String token) {
    return authTokenService.getUserDetailByToken(token);
  }

  @PostMapping("/register")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void registerGuest(
      @RequestBody @Valid RegisterGuestRequest request,
      @RequestParam(name = "token") String token
  ) {
    authTokenService.registerGuest(request, token);
  }
}
