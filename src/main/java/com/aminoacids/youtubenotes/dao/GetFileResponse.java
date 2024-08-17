package com.aminoacids.youtubenotes.dao;

import com.aminoacids.youtubenotes.model.Note;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetFileResponse {
    private long id;
    private String name;
    private String owner;
    private boolean canEdit;
    private String videoURL;
    private List<NoteDAO> notes;
    private String jwtToken;
}
