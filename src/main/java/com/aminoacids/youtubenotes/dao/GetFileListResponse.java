package com.aminoacids.youtubenotes.dao;

import com.aminoacids.youtubenotes.model.File;
import lombok.*;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetFileListResponse {
    public List<FileDAO> fileList;
    private String jwtToken;

}
