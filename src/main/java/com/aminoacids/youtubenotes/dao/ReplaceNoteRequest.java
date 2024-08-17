package com.aminoacids.youtubenotes.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReplaceNoteRequest {
    private long id;
    private double time;
    private String body;
}
