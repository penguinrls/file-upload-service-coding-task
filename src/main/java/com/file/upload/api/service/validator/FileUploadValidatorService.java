package com.file.upload.api.service.validator;

import com.file.upload.client.response.IpGeoLocationDto;

public interface FileUploadValidatorService {

    void validate(IpGeoLocationDto ipGeoLocationDto);
}
