package com.aminoacids.youtubenotes.dao;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class StateResponse {
    private boolean isSuccessful;
    private String jwtToken;
}
