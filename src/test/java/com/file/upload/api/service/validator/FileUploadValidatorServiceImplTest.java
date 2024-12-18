package com.file.upload.api.service.validator;

import com.file.upload.client.response.IpGeoLocationDto;
import com.file.upload.exception.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class FileUploadValidatorServiceImplTest {

    @InjectMocks
    private final FileUploadValidatorService underTest = new FileUploadValidatorServiceImpl(List.of("China","Spain","USA"), List.of("AWS","GCP","Azure"));

    @Test
    void shouldThrowNoValidationException() {
        // given
        IpGeoLocationDto ipGeoLocationDto = IpGeoLocationDto
                .builder()
                .query("query")
                .isp("virgin")
                .countryCode("GB")
                .build();

        // when then
        assertDoesNotThrow(() -> underTest.validate(ipGeoLocationDto));
    }

    @ParameterizedTest
    @ValueSource(strings = {"China","Spain","USA"})
    void shouldThrowValidationExceptionWhenCountryCodeIsInvalid(String countryCode) {
        // given
        IpGeoLocationDto ipGeoLocationDto = IpGeoLocationDto
                .builder()
                .query("query")
                .isp("virgin")
                .countryCode(countryCode)
                .build();

        // when then
        assertThrows(ValidationException.class, () -> underTest.validate(ipGeoLocationDto));
    }

    @ParameterizedTest
    @ValueSource(strings = {"AWS","GCP","Azure"})
    void shouldThrowValidationExceptionWhenIspIsInvalid(String isp) {
        // given
        IpGeoLocationDto ipGeoLocationDto = IpGeoLocationDto
                .builder()
                .query("query")
                .isp(isp)
                .countryCode("GB")
                .build();

        // when then
        assertThrows(ValidationException.class, () -> underTest.validate(ipGeoLocationDto));
    }

}