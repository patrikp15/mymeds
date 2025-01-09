package com.mymeds.userservice.controller;

import com.mymeds.sharedutilities.exception.MyMedsGeneralException;
import com.mymeds.sharedutilities.exception.UserErrorCode;
import com.mymeds.userservice.client.rest.AuthRegisterGuestClientRequest;
import com.mymeds.userservice.rest.GetGuestResponse;
import com.mymeds.userservice.rest.UpdateGuestRequest;
import com.mymeds.userservice.security.CurrentAuditorContextHolder;
import com.mymeds.userservice.service.GuestService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/guests")
public class GuestController {

  private final GuestService guestService;

  public GuestController(GuestService guestService) {
    this.guestService = guestService;
  }

  @GetMapping
  public ResponseEntity<List<GetGuestResponse>> getAllGuestsForUser() {
    final UUID userId = CurrentAuditorContextHolder.get()
        .orElseThrow(() -> new MyMedsGeneralException(UserErrorCode.USER_ID_NOT_FOUND));
    return ResponseEntity.ok(guestService.getAllGuests(userId));
  }

  @PostMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void createGuest(@RequestBody @Valid AuthRegisterGuestClientRequest request) {
    guestService.createGuest(request);
  }

  @PutMapping("/{guestId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void updateGuest(
      @PathVariable UUID guestId,
      @RequestBody @Valid UpdateGuestRequest request
  ) {
    guestService.updateGuest(guestId, request);
  }
}
