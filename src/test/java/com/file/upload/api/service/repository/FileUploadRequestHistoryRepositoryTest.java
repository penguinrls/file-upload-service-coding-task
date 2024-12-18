package com.file.upload.api.service.repository;

import com.file.upload.DataJpaTestContainerTest;
import com.file.upload.api.service.model.FileUploadRequestHistory;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.DBUnitExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTestContainerTest
@ExtendWith(DBUnitExtension.class)
class FileUploadRequestHistoryRepositoryTest {

    @Autowired
    private FileUploadRequestHistoryRepository underTest;

    @DataSet(value = "datasets/file-upload-request-history.yml", cleanBefore = true)
    @Test
    void shouldReturnFileUploadRequestHistory() {
        // given
        // when
        Optional<FileUploadRequestHistory> fileUploadRequestHistoryOptional = underTest.findById(UUID.fromString("40df918a-6646-4d44-aa62-4850e8a0bc01"));

        // then
        assertThat(fileUploadRequestHistoryOptional).isPresent()
                .get()
                .extracting("requestId").isEqualTo(UUID.fromString("40df918a-6646-4d44-aa62-4850e8a0bc01"));
    }

    @Test
    void shouldNotReturnFileUploadRequestHistory() {
        // given
        // when
        Optional<FileUploadRequestHistory> fileUploadRequestHistoryOptional = underTest.findById(UUID.fromString("40df918a-6646-4d44-aa62-4850e8a0bc0"));

        // then
        assertThat(fileUploadRequestHistoryOptional).isNotPresent();
    }
}