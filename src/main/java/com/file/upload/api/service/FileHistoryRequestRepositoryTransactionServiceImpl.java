package com.file.upload.api.service;

import com.file.upload.api.service.model.FileUploadRequestHistory;
import com.file.upload.api.service.repository.FileUploadRequestHistoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component

public class FileHistoryRequestRepositoryTransactionServiceImpl implements FileHistoryRequestRepositoryTransactionService {

    private final FileUploadRequestHistoryRepository fileUploadRequestHistoryRepository;

    public FileHistoryRequestRepositoryTransactionServiceImpl(FileUploadRequestHistoryRepository fileUploadRequestHistoryRepository) {
        this.fileUploadRequestHistoryRepository = fileUploadRequestHistoryRepository;
    }

    @Transactional
    @Override
    public UUID save(FileUploadRequestHistory fileUploadHistory) {
        return fileUploadRequestHistoryRepository.save(fileUploadHistory).getRequestId();
    }

    @Transactional
    @Override
    public void update(UUID uuid, Long elapsedTimeInMilliseconds, Integer statusCode) {
        fileUploadRequestHistoryRepository.findById(uuid)
                .ifPresent(history -> {
                    history.setTimeLapsedOfRequest(elapsedTimeInMilliseconds);
                    history.setResponseCode(statusCode);
                    fileUploadRequestHistoryRepository.save(history);
                });
    }
}
