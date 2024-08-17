package com.aminoacids.youtubenotes.dao;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddNoteResponse {
    private long id;
    private String jwtToken;
}
