package com.aminoacids.youtubenotes.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShareFileRequest {
    private String email;
    private long fileId;
    private boolean canEdit;
}
