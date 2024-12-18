package com.file.upload.client;

import com.file.upload.client.response.IpGeoLocationDto;

public interface IpApiClient {

    IpGeoLocationDto getLocationMeta();
}
