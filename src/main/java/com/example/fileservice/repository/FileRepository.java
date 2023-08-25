package com.example.fileservice.repository;


import com.example.fileservice.module.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface FileRepository extends JpaRepository<File, Integer> {
    Optional<File> findByFileIdAndDeletedAtIsNull(Integer fileId);

    Set<File> findAllByFileId(Integer id);
}