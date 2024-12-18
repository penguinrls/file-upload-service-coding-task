package com.file.upload.api.service;

import com.file.upload.api.service.model.FileUploadRequestHistory;
import jakarta.transaction.Transactional;

public interface FileHistoryRequestRepositoryTransactionService {
    @Transactional
    void save(FileUploadRequestHistory fileUploadHistory);
}
