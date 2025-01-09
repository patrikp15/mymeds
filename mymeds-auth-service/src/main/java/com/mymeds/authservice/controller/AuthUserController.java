package com.mymeds.authservice.controller;

import com.mymeds.authservice.rest.ChangeUserStatusRequest;
import com.mymeds.authservice.rest.GetUserDetailResponse;
import com.mymeds.authservice.rest.UpdateUserRequest;
import com.mymeds.authservice.security.CurrentAuditorContextHolder;
import com.mymeds.authservice.service.UserService;
import com.mymeds.sharedutilities.exception.MyMedsGeneralException;
import com.mymeds.sharedutilities.exception.UserErrorCode;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/users")
public class AuthUserController {

  private final UserService userService;

  public AuthUserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/{userId}")
  public ResponseEntity<GetUserDetailResponse> getUserDetailById(@PathVariable UUID userId) {
    return ResponseEntity.ok(userService.getUserDetailById(userId));
  }

  @PutMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void updateUser(@RequestBody @Valid UpdateUserRequest request) {
    userService.updateUser(getUserIdOrThrowException(), request);
  }

  @PutMapping("/status")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void changeUserStatus(@RequestBody @Valid ChangeUserStatusRequest request) {
    userService.changeUserStatus(getUserIdOrThrowException(), request);
  }

  private UUID getUserIdOrThrowException() {
    return CurrentAuditorContextHolder.get()
        .orElseThrow(() -> new MyMedsGeneralException(UserErrorCode.USER_NOT_FOUND));
  }
}
