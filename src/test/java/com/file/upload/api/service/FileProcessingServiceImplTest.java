package com.file.upload.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.file.upload.exception.FileProcessingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FileProcessingServiceImplTest {

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private FileProcessingServiceImpl underTest;

    @Test
    void shouldProcessFile() throws Exception {
        // given
        byte[] data = Files.readAllBytes(Paths.get("src/test/resources/EntryFile.txt"));
        MockMultipartFile multipartFile = new MockMultipartFile("file", data);
        when(objectMapper.writeValueAsBytes(ArgumentMatchers.any())).thenReturn(data);

        // when
        byte[] jsonFile = underTest.process(multipartFile);

        // then
        assertThat(jsonFile).isNotEmpty();
    }

    @Test
    void shouldNotProcessFileWhenInWrongFormat() throws Exception {
        // given
        byte[] data = Files.readAllBytes(Paths.get("src/test/resources/EntryFile.txt"));
        MockMultipartFile multipartFile = new MockMultipartFile("file", "{}".getBytes());

        // when then
         assertThatThrownBy(() -> underTest.process(multipartFile))
                 .isInstanceOf(FileProcessingException.class);
    }

}