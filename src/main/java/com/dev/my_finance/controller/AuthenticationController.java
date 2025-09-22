package com.dev.my_finance.controller;

import com.dev.my_finance.dto.AuthenticationRequest;
import com.dev.my_finance.dto.AuthenticationResponse;
import com.dev.my_finance.dto.RegisterRequest;
import com.dev.my_finance.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request, HttpServletResponse response) {
        return  ResponseEntity.ok(authenticationService.register(request, response));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request, HttpServletResponse response) {
        return  ResponseEntity.ok(authenticationService.authenticate(request, response));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(
            HttpServletRequest request, HttpServletResponse response) {
        return  ResponseEntity.ok(authenticationService.refreshToken(request, response));
    }

}
