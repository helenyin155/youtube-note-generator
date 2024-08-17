package com.aminoacids.youtubenotes.dao;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LoginResponse {
    private String jwtToken;
}
