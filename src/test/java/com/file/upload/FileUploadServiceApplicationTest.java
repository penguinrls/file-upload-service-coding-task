package com.file.upload;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.file.upload.api.service.repository.FileUploadRequestHistoryRepository;
import com.file.upload.client.response.IpGeoLocationDto;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Paths;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DataTestContainerComponentTest
@WireMockTest(httpPort = 8080)
@TestPropertySource(properties = {
		"ip.service.url=http://localhost:${wiremock.server.port}/ip-api.com/json"
})
@DBRider
@DBUnit(cacheConnection = false)
class FileUploadServiceApplicationTest {

	@Autowired
	private FileUploadRequestHistoryRepository fileUploadRequestHistoryRepository;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DataSet(value = "datasets/file-upload-request-history.yml", cleanBefore = true)
	void shouldUpLoadFileAndReturnJson() throws Exception {
		// given
		stubFor(WireMock.get("/ip-api.com/json").willReturn(aResponse()
				.withHeader("Content-Type", "application/json")
				.withBody(objectMapper.writeValueAsString(IpGeoLocationDto
						.builder()
						.query("123.0.0.1")
						.isp("virgin")
						.countryCode("GB")
						.build()))));
		byte[] data = Files.readAllBytes(Paths.get("src/test/resources/EntryFile.txt"));
		MockMultipartFile multipartFile = new MockMultipartFile("file", data);

		// when
		mockMvc.perform(multipart("/v1/api/upload")
						.file(multipartFile))
				.andExpect(status().isOk())
				.andExpect(header().string("Content-Disposition", "attachment;filename=OutcomeFile.json"));
	}

	@Test
	@DataSet(value = "datasets/file-upload-request-history.yml", cleanBefore = true)
	void shouldUpLoadFileAndReturnForbiddenForInvalidCountry() throws Exception {
		// given
		stubFor(WireMock.get("/ip-api.com/json").willReturn(aResponse()
				.withHeader("Content-Type", "application/json")
				.withBody(objectMapper.writeValueAsString(IpGeoLocationDto
						.builder()
						.query("123.0.0.2")
						.isp("virgin")
						.countryCode("USA")
						.build()))));
		byte[] data = Files.readAllBytes(Paths.get("src/test/resources/EntryFile.txt"));
		MockMultipartFile multipartFile = new MockMultipartFile("file", data);

		// when
		mockMvc.perform(multipart("/v1/api/upload")
						.file(multipartFile))
				.andExpect(status().isForbidden());
	}

	@Test
	@DataSet(value = "datasets/file-upload-request-history.yml", cleanBefore = true)
	void shouldUpLoadFileAndReturnForbiddenForInvalidIps() throws Exception {
		// given
		stubFor(WireMock.get("/ip-api.com/json").willReturn(aResponse()
				.withHeader("Content-Type", "application/json")
				.withBody(objectMapper.writeValueAsString(IpGeoLocationDto
						.builder()
						.query("123.0.0.3")
						.isp("AWS")
						.countryCode("GB")
						.build()))));
		byte[] data = Files.readAllBytes(Paths.get("src/test/resources/EntryFile.txt"));
		MockMultipartFile multipartFile = new MockMultipartFile("file", data);

		// when
		mockMvc.perform(multipart("/v1/api/upload")
						.file(multipartFile))
				.andExpect(status().isForbidden());
	}

}
