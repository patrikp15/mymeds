package com.mymeds.userservice.controller;

import com.mymeds.sharedutilities.exception.MyMedsGeneralException;
import com.mymeds.sharedutilities.exception.UserErrorCode;
import com.mymeds.userservice.rest.GetUserResponse;
import com.mymeds.userservice.rest.UpdateUserRequest;
import com.mymeds.userservice.security.CurrentAuditorContextHolder;
import com.mymeds.userservice.service.UserDetailService;
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
@RequestMapping("/users")
public class UserDetailController {

  private final UserDetailService userDetailService;

  public UserDetailController(UserDetailService userDetailService) {
    this.userDetailService = userDetailService;
  }

  @GetMapping("/{userId}")
  public ResponseEntity<GetUserResponse> getUserDetailWithGuests(@PathVariable UUID userId) {
    return ResponseEntity.ok(userDetailService.getUserWithGuestsById(userId));
  }

  @GetMapping
  public ResponseEntity<GetUserResponse> getUserDetail() {
    final UUID userId = CurrentAuditorContextHolder.get()
        .orElseThrow(() -> new MyMedsGeneralException(UserErrorCode.USER_ID_NOT_FOUND));
    return ResponseEntity.ok(userDetailService.getUserById(userId));
  }

  @PutMapping("/{userId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void updateUser(@PathVariable UUID userId, @RequestBody @Valid UpdateUserRequest request) {
    userDetailService.updateUser(userId, request);
  }
}
