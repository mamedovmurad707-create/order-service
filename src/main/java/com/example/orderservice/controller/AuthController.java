package com.example.orderservice.controller;

import com.example.orderservice.model.request.LoginRequest;
import com.example.orderservice.model.response.TokenResponse;
import com.example.orderservice.service.impl.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth")
public class AuthController {
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @PostMapping("/login")
    public TokenResponse login(@RequestBody LoginRequest request) {

        UserDetails user;

        try {
            user = userDetailsService.loadUserByUsername(
                    request.username()
            );
        } catch (Exception ex) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Username or password is incorrect"
            );
        }

        if (!passwordEncoder.matches(
                request.password(),
                user.getPassword())) {

            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Username or password is incorrect"
            );
        }

        List<String> roles = user.getAuthorities()
                .stream()
                .map(authority -> authority.getAuthority()
                        .replace("ROLE_", ""))
                .toList();

        String token = jwtService.generateToken(
                user.getUsername(),
                roles
        );

        return new TokenResponse(token, "Bearer");
    }
}
