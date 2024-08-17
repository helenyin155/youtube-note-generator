package com.aminoacids.youtubenotes.dao;

import com.aminoacids.youtubenotes.model.Note;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class WriteFileRequest {
    private long id;
    private String name;
}
