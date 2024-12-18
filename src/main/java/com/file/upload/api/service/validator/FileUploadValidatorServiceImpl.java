package com.file.upload.api.service.validator;

import com.file.upload.client.response.IpGeoLocationDto;
import com.file.upload.exception.ValidationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.lang.String.format;

@Component
@ConditionalOnProperty(name="feature.validation", havingValue="true")
public class FileUploadValidatorServiceImpl implements FileUploadValidatorService {

    private final List<String> invalidIps;
    private final List<String> invalidCountries;

    public FileUploadValidatorServiceImpl(@Value("${api.validation.invalid.country.codes}") List<String> invalidCountries,
                                          @Value("${api.validation.invalid.ips}") List<String> invalidIps) {
        this.invalidIps = invalidIps;
        this.invalidCountries = invalidCountries;
    }

    public void validate(IpGeoLocationDto ipGeoLocationDto) {

        if (!ipGeoLocationDto.isValidCountryCode(invalidCountries)) {
            throw new ValidationException(format("Invalid Country Code %s", ipGeoLocationDto.getCountryCode()));
        }

        if (!ipGeoLocationDto.isValidIps(invalidIps)) {
            throw new ValidationException(format("Invalid Ips %s", ipGeoLocationDto.getIsp()));
        }
    }

}

