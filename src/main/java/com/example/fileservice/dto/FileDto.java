package com.example.fileservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileDto {
    private Integer fileId;
    private Integer userId;
    private String fileName;
    private String filePath;
    private String ext;
    private Boolean status;
    private byte[] data;
    private String createdAt;
    private String updatedAt;
    private String deletedAt;
}