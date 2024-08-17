package com.aminoacids.youtubenotes.controllers;

import com.aminoacids.youtubenotes.services.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {
    private final JWTService jwtService;

    @GetMapping("/email")
    public String decodeJWT(@RequestHeader("Authorization") String token) {
        return jwtService.extractBodyEmail(token);
    }

    @GetMapping("/refresh")
    public String refreshJWT(@RequestHeader("Authorization") String token) {
        return jwtService.refreshToken(token);
    }
}
