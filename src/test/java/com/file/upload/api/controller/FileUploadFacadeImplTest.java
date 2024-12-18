package com.file.upload.api.controller;

import com.file.upload.api.service.FileProcessingService;
import com.file.upload.client.response.IpGeoLocationDto;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FileUploadFacadeImplTest {

    @Mock
    private FileProcessingService fileUploadService;

    @InjectMocks
    private FileUploadFacadeImpl underTest;

    @Test
    void shouldUploadFile() {
        // given
        IpGeoLocationDto ipGeoLocationDto = IpGeoLocationDto
                .builder()
                .query("1.0.0.0")
                .isp("google")
                .countryCode("USA")
                .build();
       HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
       MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "test.pdf", "text/plain", "test".getBytes());

        // when
        underTest.uploadFile(mockMultipartFile, httpServletRequest);

        // then
        verify(fileUploadService, times(1)).process(mockMultipartFile);
    }
}