package com.file.upload.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.file.upload.api.service.FileHistoryRequestRepositoryTransactionServiceImpl;
import com.file.upload.api.service.validator.FileUploadValidatorServiceImpl;
import com.file.upload.client.IpApiClient;
import com.file.upload.client.response.IpGeoLocationDto;
import com.file.upload.exception.FileProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@AutoConfigureMockMvc
class FileUploadApiControllerTest {

    @MockBean
    private IpApiClient ipApiClient;

    @SpyBean
    private FileUploadValidatorServiceImpl fileUploadValidatorService;

    @MockBean
    private FileHistoryRequestRepositoryTransactionServiceImpl fileHistoryRepositoryTransactionService;

    @MockBean
    private FileUploadFacade fileUploadFacadeImpl;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnOk() throws Exception {
        // given
        IpGeoLocationDto ipGeoLocationDto = IpGeoLocationDto
                .builder()
                .query("query")
                .isp("virgin")
                .countryCode("GB")
                .build();
        MockMultipartFile multipartFile = new MockMultipartFile("file", "file".getBytes());
        when(fileUploadFacadeImpl.uploadFile(any(MultipartFile.class))).thenReturn("{}".getBytes());
        when(ipApiClient.getLocationMeta()).thenReturn(ipGeoLocationDto);

        // when
        mockMvc.perform(multipart("/v1/api/upload")
                        .file(multipartFile))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment;filename=OutcomeFile.json"));
    }

    @Test
    void shouldReturnBadRequest() throws Exception {
        // given
        IpGeoLocationDto ipGeoLocationDto = IpGeoLocationDto
                .builder()
                .query("query")
                .isp("virgin")
                .countryCode("GB")
                .build();
        MockMultipartFile multipartFile = new MockMultipartFile("file", "file".getBytes());
        when(fileUploadFacadeImpl.uploadFile(any(MultipartFile.class))).thenThrow(new FileProcessingException("error"));
        when(ipApiClient.getLocationMeta()).thenReturn(ipGeoLocationDto);

        // when
        mockMvc.perform(multipart("/v1/api/upload")
                        .file(multipartFile))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldBlockForBadIps() throws Exception {
        // given
        IpGeoLocationDto ipGeoLocationDto = IpGeoLocationDto
                .builder()
                .query("query")
                .isp("AWS")
                .countryCode("GB")
                .build();
        MockMultipartFile multipartFile = new MockMultipartFile("file", "file".getBytes());
        when(ipApiClient.getLocationMeta()).thenReturn(ipGeoLocationDto);

        // when
        mockMvc.perform(multipart("/v1/api/upload")
                        .file(multipartFile))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldBlockForBadCountryCode() throws Exception {
        // given
        IpGeoLocationDto ipGeoLocationDto = IpGeoLocationDto
                .builder()
                .query("query")
                .isp("virgin")
                .countryCode("Spain")
                .build();
        MockMultipartFile multipartFile = new MockMultipartFile("file", "file".getBytes());
        when(ipApiClient.getLocationMeta()).thenReturn(ipGeoLocationDto);

        // when
        mockMvc.perform(multipart("/v1/api/upload")
                        .file(multipartFile))
                .andExpect(status().isForbidden());
    }

}