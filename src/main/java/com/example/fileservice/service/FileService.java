package com.example.fileservice.service;

import com.example.fileservice.dto.FileDto;
import com.example.fileservice.dto.ResponseDto;
import com.example.fileservice.mapper.FileMapper;
import com.example.fileservice.module.File;
import com.example.fileservice.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;
    private final FileMapper fileMapper;

    public ResponseDto<FileDto> upload(MultipartFile file) {
        try {
            return ResponseDto.<FileDto>builder()
                    .success(true)
                    .message("OK")
                    .data(this.fileMapper.toDto(
                            this.fileRepository.save(
                                    File.builder()
                                            .fileName(Objects.requireNonNull(file.getOriginalFilename()).split("\\.")[0])
                                            .ext(Objects.requireNonNull(file.getOriginalFilename()).split("\\.")[1])
                                            .createdAt(LocalDateTime.now())
                                            .filePath(saveFile(file))
                                            .status(true)
                                            .build())))
                    .build();
        } catch (Exception e) {
            return ResponseDto.<FileDto>builder()
                    .code(-3)
                    .message("File while saving error message :: " + e.getMessage())
                    .build();
        }
    }

    public ResponseDto<FileDto> download(Integer fileId) {
        return this.fileRepository.findByFileIdAndDeletedAtIsNull(fileId)
                .map(fileModel -> {
                    try {
                        FileDto dto = this.fileMapper.toDto(fileModel);
                        dto.setData(Files.readAllBytes(Path.of(fileModel.getFilePath())));
                        return ResponseDto.<FileDto>builder()
                                .success(true)
                                .message("OK")
                                .data(dto)
                                .build();
                    } catch (Exception e) {
                        return ResponseDto.<FileDto>builder()
                                .code(-3)
                                .message("File while getting error message :: " + e.getMessage())
                                .build();
                    }
                })
                .orElse(ResponseDto.<FileDto>builder()
                        .code(-1)
                        .message(String.format("File with file id :: %d is not found!", fileId))
                        .build());
    }

    public ResponseDto<FileDto> updateFile(FileDto dto, Integer fileId) {
        Optional<File> optional = fileRepository.findByFileIdAndDeletedAtIsNull(fileId);
        if (optional.isEmpty()) {
            return ResponseDto.<FileDto>builder()
                    .message("File is not found!")
                    .code(-3)
                    .data(null)
                    .build();
        }
        try {
            File file = optional.get();
            fileMapper.updateFilesFromDto(dto, optional.get());
            file.setFileId(optional.get().getFileId());
            file.setUpdatedAt(LocalDateTime.now());
            fileRepository.save(file);
            return ResponseDto.<FileDto>builder()
                    .success(true)
                    .message("File successful updated!")
                    .data(fileMapper.toDto(file))
                    .build();
        } catch (Exception e) {
            return ResponseDto.<FileDto>builder()
                    .message("File while updating error :: " + e.getMessage())
                    .code(-1)
                    .build();
        }
    }

    public ResponseDto<FileDto> deleteFile(Integer fileId) {
        try {
            return this.fileRepository.findByFileIdAndDeletedAtIsNull(fileId)
                    .map(fileModel -> {
                        java.io.File file = new java.io.File(fileModel.getFilePath());
                        if (file.exists()) {
                            file.delete();
                        }
                        fileModel.setDeletedAt(LocalDateTime.now());
                        this.fileRepository.save(fileModel);
                        return ResponseDto.<FileDto>builder()
                                .success(true)
                                .message("OK")
                                .data(this.fileMapper.toDto(fileModel))
                                .build();
                    })
                    .orElse(ResponseDto.<FileDto>builder()
                            .code(-1)
                            .message(String.format("File with file id :: %d is not found!", fileId))
                            .build());
        } catch (Exception e) {
            return ResponseDto.<FileDto>builder()
                    .code(-3)
                    .message("File while deleting error message :: " + e.getMessage())
                    .build();
        }
    }

    private String saveFile(MultipartFile file) throws IOException {
        String folders = String.format("%s/%s", "upload", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
        java.io.File fileModel = new java.io.File(folders);
        if (!fileModel.exists()) {
            fileModel.mkdirs();
        }
        String fileName = String.format("%s/%s", folders, UUID.randomUUID());
        Files.copy(file.getInputStream(), Path.of(fileName));
        return fileName;
    }

    public ResponseDto<Set<FileDto>> getFilesByUsersId(Integer id) {
        Set<File> files = fileRepository.findAllByUserIdAndDeletedAtIsNull(id);
        if (files.isEmpty()) {
            return ResponseDto.<Set<FileDto>>builder()
                    .message("Files are not found!")
                    .code(-3)
                    .data(null)
                    .build();
        }
        return ResponseDto.<Set<FileDto>>builder()
                .success(true)
                .message("OK")
                .data(fileMapper.toSetDto(files))
                .build();
    }

}