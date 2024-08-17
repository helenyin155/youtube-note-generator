package com.aminoacids.youtubenotes.controllers;

import com.aminoacids.youtubenotes.dao.AuthenticationRequest;
import com.aminoacids.youtubenotes.dao.LoginResponse;
import com.aminoacids.youtubenotes.dao.PasswordChangeRequest;
import com.aminoacids.youtubenotes.dao.StateResponse;
import com.aminoacids.youtubenotes.services.AuthenticationService;
import com.aminoacids.youtubenotes.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<StateResponse> signUp(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

//    @GetMapping("/logout")
//    public ResponseEntity<StateResponse> logout(@RequestHeader("Authorization") String token) {
//        return ResponseEntity.ok(authenticationService.logout());
//    }
//
//    @PostMapping("/password/{id}")
//    public ResponseEntity<StateResponse> changePassword(@RequestBody PasswordChangeRequest request, @PathVariable String id) {
//        return ResponseEntity.ok(authenticationService.changePassword(request, id));
//    }
//
//    @GetMapping("/activate/{id}")
//    public ResponseEntity<StateResponse> isActivated(@PathVariable String id) {
//        return ResponseEntity.ok(authenticationService.activate(id));
//    }
}
