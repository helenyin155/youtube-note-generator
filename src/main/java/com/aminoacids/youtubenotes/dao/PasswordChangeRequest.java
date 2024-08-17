package com.aminoacids.youtubenotes.dao;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PasswordChangeRequest {
    private String password;
}
