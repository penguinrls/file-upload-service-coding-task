package com.file.upload.api.service;

import com.file.upload.api.service.model.FileUploadRequestHistory;
import com.file.upload.api.service.repository.FileUploadRequestHistoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
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
        when(fileUploadRequestHistory.getRequestId()).thenReturn(requestId);
        when(fileUploadRequestHistoryRepository.save(fileUploadRequestHistory)).thenReturn(fileUploadRequestHistory);

        // when
        UUID result = underTest.save(fileUploadRequestHistory);

        // then
        verify(fileUploadRequestHistoryRepository).save(fileUploadRequestHistory);
    }

    @Test
    void shouldUpdate() {
        // given
        ArgumentCaptor<FileUploadRequestHistory>fileUploadRequestHistoryArgumentCaptor = ArgumentCaptor.forClass(FileUploadRequestHistory.class);
        UUID requestId = UUID.randomUUID();
        FileUploadRequestHistory fileUploadRequestHistory = new FileUploadRequestHistory();
        when(fileUploadRequestHistoryRepository.findById(requestId)).thenReturn(Optional.of(fileUploadRequestHistory));

        // when
        underTest.update(requestId, 123L, HttpStatus.OK.value());

        // then
        verify(fileUploadRequestHistoryRepository).save(fileUploadRequestHistoryArgumentCaptor.capture());
        assertThat(fileUploadRequestHistoryArgumentCaptor.getValue().getTimeLapsedOfRequest()).isEqualTo(123L);
        assertThat(fileUploadRequestHistoryArgumentCaptor.getValue().getResponseCode()).isEqualTo(HttpStatus.OK.value());
    }

}