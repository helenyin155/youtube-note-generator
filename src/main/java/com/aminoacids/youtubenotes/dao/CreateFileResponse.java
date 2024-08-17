package com.aminoacids.youtubenotes.dao;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateFileResponse {
    private long id;
    private String jwtToken;
}
