package com.file.upload;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.file.upload.client.response.IpGeoLocationDto;
import com.github.tomakehurst.wiremock.WireMockServer;
//import org.apache.http.HttpStatus;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
//import static org.apache.http.HttpStatus.SC_OK;

public class IpStub extends ServiceStub {

    public IpStub(ObjectMapper objectMapper, WireMockServer wireMockServer) {
        super(objectMapper, wireMockServer);
    }

    public void stubGetIpMetaData() throws JsonProcessingException {
        IpGeoLocationDto ipGeoLocationDto = IpGeoLocationDto
                .builder()
                .query("123.0.0.0")
                .isp("virgin")
                .countryCode("GB")
                .build();

        stubRequest(get("ip-api.com/json"), ipGeoLocationDto, 0);
    }

}
