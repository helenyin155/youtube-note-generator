package com.aminoacids.youtubenotes.services;

import com.aminoacids.youtubenotes.dao.AuthenticationRequest;
import com.aminoacids.youtubenotes.dao.LoginResponse;
import com.aminoacids.youtubenotes.dao.PasswordChangeRequest;
import com.aminoacids.youtubenotes.dao.StateResponse;
import com.aminoacids.youtubenotes.model.Roles;
import com.aminoacids.youtubenotes.model.User;
import com.aminoacids.youtubenotes.repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    public StateResponse register(AuthenticationRequest request) {
        if (userRepo.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists");
        }
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(true)
                .role(Roles.USER)
                .build();
        userRepo.save(user);
        return StateResponse.builder()
                .isSuccessful(true)
                .build();
    }

    public LoginResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword())
        );
        var user = userRepo.findByEmail(request.getEmail()).orElseThrow();
        var token = jwtService.generateToken(user);
        return LoginResponse.builder()
                .jwtToken(token)
                .build();
    }

//    public StateResponse logout() {
//    }

//    public StateResponse changePassword(PasswordChangeRequest request, String id) {
//    }
//
//    public StateResponse activate(String id) {
//    }
}
