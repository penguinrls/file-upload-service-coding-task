package com.file.upload.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.file.upload.client.response.IpGeoLocationDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@ExtendWith(SpringExtension.class)
@RestClientTest(IpApiClientImpl.class)
@Import({JacksonAutoConfiguration.class, RestTemplate.class})
@TestPropertySource(properties = {"ip.service.url=http://ip-api.com/json"})
class IpApiClientImplTest {

    @Autowired
    private IpApiClient underTest;

    @Autowired
    private MockRestServiceServer mockServer;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnIpMetaData() throws JsonProcessingException {
        // given
        IpGeoLocationDto ipGeoLocationDto = IpGeoLocationDto
                .builder()
                .query("query")
                .isp("google")
                .countryCode("USA")
                .build();
        // when
        mockServer
                .expect(requestTo("http://ip-api.com/json"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .body(objectMapper.writeValueAsString(ipGeoLocationDto))
                        .contentType(APPLICATION_JSON));
        // when
        IpGeoLocationDto response = underTest.getLocationMeta();

        // then
        assertThat(response).isEqualTo(ipGeoLocationDto);
        mockServer.verify();
    }
}