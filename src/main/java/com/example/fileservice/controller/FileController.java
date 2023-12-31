package com.example.fileservice.controller;

import com.example.fileservice.dto.FileDto;
import com.example.fileservice.dto.ResponseDto;
import com.example.fileservice.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@RestController
@RequestMapping("file")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;
    @PostMapping(value = "/upload")
    public ResponseDto<FileDto> upload(@RequestBody MultipartFile file) {
        return this.fileService.upload(file);
    }

    @GetMapping(value = "/download/{id}")
    public ResponseDto<FileDto> download(@PathVariable(value = "id") Integer fileId) {
        return this.fileService.download(fileId);
    }

    @PutMapping(value = "/update/{id}")
    public ResponseDto<FileDto> updateFile(@RequestBody FileDto dto,
                                           @PathVariable(value = "id") Integer fileId) {
        return this.fileService.updateFile(dto, fileId);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseDto<FileDto> deleteFile(@PathVariable(value = "id") Integer fileId) {
        return this.fileService.deleteFile(fileId);
    }

    @GetMapping("/get-files-by-user/{id}")
    public ResponseDto<Set<FileDto>> getFilesByUsersId(@PathVariable("id") Integer id) {
        return this.fileService.getFilesByUsersId(id);
    }

}