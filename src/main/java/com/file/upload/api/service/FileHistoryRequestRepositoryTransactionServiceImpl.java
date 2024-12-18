package com.file.upload.api.service;

import com.file.upload.api.service.model.FileUploadRequestHistory;
import com.file.upload.api.service.repository.FileUploadRequestHistoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

@Component

public class FileHistoryRequestRepositoryTransactionServiceImpl implements FileHistoryRequestRepositoryTransactionService {

    private final FileUploadRequestHistoryRepository fileUploadRequestHistoryRepository;

    public FileHistoryRequestRepositoryTransactionServiceImpl(FileUploadRequestHistoryRepository fileUploadRequestHistoryRepository) {
        this.fileUploadRequestHistoryRepository = fileUploadRequestHistoryRepository;
    }

    @Transactional
    @Override
    public void save(FileUploadRequestHistory fileUploadHistory) {
         fileUploadRequestHistoryRepository.save(fileUploadHistory);
    }

}
