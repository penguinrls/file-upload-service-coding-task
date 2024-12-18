package com.file.upload.api.service;

import com.file.upload.api.service.model.FileUploadRequestHistory;
import com.file.upload.api.service.repository.FileUploadRequestHistoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FileHistoryRepositoryTransactionServiceImplTest {

    @Mock
    private FileUploadRequestHistoryRepository fileUploadRequestHistoryRepository;

    @InjectMocks
    private FileHistoryRequestRepositoryTransactionServiceImpl underTest;

    @Test
    void shouldSave() {
        // given
        UUID requestId = UUID.randomUUID();
        FileUploadRequestHistory fileUploadRequestHistory = mock(FileUploadRequestHistory.class);
        when(fileUploadRequestHistoryRepository.save(fileUploadRequestHistory)).thenReturn(fileUploadRequestHistory);

        // when
        underTest.save(fileUploadRequestHistory);

        // then
        verify(fileUploadRequestHistoryRepository).save(fileUploadRequestHistory);
    }

}