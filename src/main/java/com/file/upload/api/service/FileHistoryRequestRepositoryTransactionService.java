package com.file.upload.api.service;

import com.file.upload.api.service.model.FileUploadRequestHistory;
import jakarta.transaction.Transactional;

import java.util.UUID;

public interface FileHistoryRequestRepositoryTransactionService {
    @Transactional
    UUID save(FileUploadRequestHistory fileUploadHistory);

    @Transactional
    void update(UUID uuid, Long elaspedTimeInMilliseconds, Integer statusCode);
}
