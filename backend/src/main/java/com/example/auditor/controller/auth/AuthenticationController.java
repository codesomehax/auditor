package com.example.auditor.controller.auth;

import com.example.auditor.requests.AuthenticationRequest;
import com.example.auditor.responses.AuthenticationResponse;
import com.example.auditor.service.auth.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
        return ResponseEntity
                .ok(authenticationService.authenticate(authenticationRequest));
    }
}
