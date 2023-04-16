package com.example.deansoffice.controller;

import com.example.deansoffice.model.AuthenticationRequest;
import com.example.deansoffice.model.AuthenticationResponse;
import com.example.deansoffice.model.RegisterStudent;
import com.example.deansoffice.service.LoginAuthenticationJWT.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService service;

  @PostMapping("/register")
  public ResponseEntity<?> register(
      @RequestBody RegisterStudent request
  ) {
    return service.register(request);
  }
  @PostMapping("/login")
  public ResponseEntity<AuthenticationResponse> authenticate(
      @RequestBody AuthenticationRequest request
  ) {
    return ResponseEntity.ok(service.authenticate(request));
  }


}