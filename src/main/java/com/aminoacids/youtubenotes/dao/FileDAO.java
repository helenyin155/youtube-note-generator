package com.aminoacids.youtubenotes.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileDAO {
    private long id;
    private String name;
    private String owner;
    private String videoURL;
    private boolean canEdit;
    private Date lastModified;
}
