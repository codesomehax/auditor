package com.example.auditor.service.auth;

import com.example.auditor.domain.appUser.AppUser;
import com.example.auditor.repository.appUser.AppUserRepository;
import com.example.auditor.requests.AuthenticationRequest;
import com.example.auditor.responses.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final AppUserRepository appUserRepository;
    private final JwtService jwtService;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        AppUser user = appUserRepository
                .findByEmail(request.getEmail())
                .orElseThrow();

        List<String> authorities = user
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        Map<String, Object> extraClaims = Map.of(
                "authorities", authorities
        );

        String jwtToken = jwtService.generateToken(user, extraClaims);
        return new AuthenticationResponse(jwtToken);
    }
}
