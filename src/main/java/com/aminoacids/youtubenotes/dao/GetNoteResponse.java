package com.aminoacids.youtubenotes.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GetNoteResponse {
    private Long id;
    private String body;
    private double time;
    private String jwtToken;
}
