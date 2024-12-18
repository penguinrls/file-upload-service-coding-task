package com.file.upload;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.http.HttpHeader;
import com.github.tomakehurst.wiremock.http.HttpHeaders;
import lombok.RequiredArgsConstructor;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


public abstract class ServiceStub {
    public ServiceStub(ObjectMapper mapper, WireMockServer wireMockServer) {
        this.mapper = mapper;
        this.wireMockServer = wireMockServer;
    }

    private final ObjectMapper mapper;
    private final WireMockServer wireMockServer;

    protected void stubRequest(MappingBuilder mappingBuilder, Object responseBody, int responseStatus) throws JsonProcessingException {
        stubRequest(mappingBuilder,
                mapper.writeValueAsString(responseBody),
                responseStatus,
                new HttpHeaders(new HttpHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)));
    }

    private void stubRequest(MappingBuilder mappingBuilder, String responseBody, int responseStatus, HttpHeaders responseHeaders) {
        mappingBuilder
                .willReturn(aResponse()
                        .withBody(responseBody)
                        .withHeaders(responseHeaders)
                        .withStatus(responseStatus));
        wireMockServer.stubFor(mappingBuilder);
    }
}
