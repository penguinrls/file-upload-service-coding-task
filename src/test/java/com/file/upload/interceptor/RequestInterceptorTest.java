package com.file.upload.interceptor;

import com.file.upload.api.service.FileHistoryRequestRepositoryTransactionServiceImpl;
import com.file.upload.api.service.model.FileUploadRequestHistory;
import com.file.upload.api.service.validator.FileUploadValidatorService;
import com.file.upload.client.IpApiClient;
import com.file.upload.client.response.IpGeoLocationDto;
import com.file.upload.exception.ValidationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RequestInterceptorTest {

    @Mock
    IpApiClient ipApiClient;

    @Mock
    private FileHistoryRequestRepositoryTransactionServiceImpl fileHistoryRequestRepositoryTransactionService;

    @Mock
    private FileUploadValidatorService fileUploadValidatorService;

    @InjectMocks
    private RequestInterceptor underTest;

    @Test
    void shouldPreHandleReturnTrueOnValidRequest() throws Exception {
        // given
        IpGeoLocationDto ipGeoLocationDto = IpGeoLocationDto
                .builder()
                .query("query")
                .isp("virgin")
                .countryCode("GB")
                .build();
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
        Object o = mock(Object.class);
        when(ipApiClient.getLocationMeta()).thenReturn(ipGeoLocationDto);

        // when
        boolean result = underTest.preHandle(httpServletRequest, httpServletResponse, o);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void shouldPreHandleReturnFalseOnInValidRequest() throws Exception {
        // given
        IpGeoLocationDto ipGeoLocationDto = IpGeoLocationDto
                .builder()
                .query("query")
                .isp("AWS")
                .countryCode("GB")
                .build();
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
        Object o = mock(Object.class);
        when(ipApiClient.getLocationMeta()).thenReturn(ipGeoLocationDto);
        doThrow(ValidationException.class).when(fileUploadValidatorService).validate(ipGeoLocationDto);

        // when
        boolean result = underTest.preHandle(httpServletRequest, httpServletResponse, o);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @Value("${feature.validation=true}")
    void shouldSkipValidationWhenFeatureFlagDisabled() throws Exception {
        IpGeoLocationDto ipGeoLocationDto = IpGeoLocationDto
                .builder()
                .query("query")
                .isp("AWS")
                .countryCode("GB")
                .build();
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
        Object o = mock(Object.class);
        when(ipApiClient.getLocationMeta()).thenReturn(ipGeoLocationDto);
        doThrow(ValidationException.class).when(fileUploadValidatorService).validate(ipGeoLocationDto);

        // when
        boolean result = underTest.preHandle(httpServletRequest, httpServletResponse, o);

        // then
        assertThat(result).isFalse();
    }

    @Test
    void shouldAfterCompletionOfRequestAndWriteToAudit() throws Exception {
        // given
        LocalDateTime key = LocalDateTime.now();
        ArgumentCaptor<FileUploadRequestHistory> fileUploadRequestHistoryArgumentCaptor = ArgumentCaptor.forClass(FileUploadRequestHistory.class);
        IpGeoLocationDto ipGeoLocationDto = IpGeoLocationDto
                .builder()
                .query("1.0.0.0")
                .isp("virgin")
                .countryCode("GB")
                .build();
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
        Object o = mock(Object.class);
        when(httpServletRequest.getAttribute("requestStart")).thenReturn(key);
        when(httpServletRequest.getAttribute("ipGeoLocationDtoMap")).thenReturn(Map.of(key, ipGeoLocationDto));
        when(httpServletRequest.getRequestURI()).thenReturn("/test");


        // when
        underTest.afterCompletion(httpServletRequest, httpServletResponse, o, null);

        // then
        verify(fileHistoryRequestRepositoryTransactionService).save(fileUploadRequestHistoryArgumentCaptor.capture());
        assertThat(fileUploadRequestHistoryArgumentCaptor.getValue().getTimeLapsedOfRequest()).isNotNull();
        assertThat(fileUploadRequestHistoryArgumentCaptor.getValue().getRequestUri()).isEqualTo("/test");
        assertThat(fileUploadRequestHistoryArgumentCaptor.getValue().getResponseCode()).isNotNull();
        assertThat(fileUploadRequestHistoryArgumentCaptor.getValue().getRequestTimeStamp()).isNotNull();
        assertThat(fileUploadRequestHistoryArgumentCaptor.getValue().getReqCountryCode()).isEqualTo("GB");
        assertThat(fileUploadRequestHistoryArgumentCaptor.getValue().getReqIpProvider()).isEqualTo("virgin");
        assertThat(fileUploadRequestHistoryArgumentCaptor.getValue().getReqIpAddress()).isEqualTo("1.0.0.0");
    }

}