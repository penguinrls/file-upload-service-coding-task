package com.file.upload.api.service.repository;

import com.file.upload.api.service.model.FileUploadRequestHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FileUploadRequestHistoryRepository extends JpaRepository<FileUploadRequestHistory, UUID> {

}
