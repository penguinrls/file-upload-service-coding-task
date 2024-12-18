package com.file.upload.client;

import com.file.upload.client.response.IpGeoLocationDto;
import com.file.upload.exception.UnexpectedResponseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import static java.lang.String.format;
import static java.util.Objects.isNull;
import static org.springframework.http.HttpMethod.GET;

@Component
public class IpApiClientImpl implements IpApiClient {

    private final String ipServiceUrl;
    private final RestTemplate restTemplate;

    public IpApiClientImpl(@Value("${ip.service.url}") String ipServiceUrl, RestTemplateBuilder restTemplateBuilder) {
        this.ipServiceUrl = ipServiceUrl;
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    public IpGeoLocationDto getLocationMeta() {
        ResponseEntity<IpGeoLocationDto> response = restTemplate.exchange(
                format("%s", ipServiceUrl),
                GET,
                null,
                new ParameterizedTypeReference<>() {});
        return handleResponse(response);
    }

    private <K> K handleResponse(ResponseEntity<K> response) {
        if (!isNull(response) && !isNull(response.getBody())) {
            return response.getBody();
        }
        throw new UnexpectedResponseException("Unexpected response format from Ip Geolocation");
    }
}
