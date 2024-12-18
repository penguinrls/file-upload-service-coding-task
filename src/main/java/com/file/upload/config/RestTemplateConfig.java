package com.file.upload.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.commons.httpclient.HttpClientConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
@Import(HttpClientConfiguration.class)
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder, JdkClientHttpRequestFactory jdkClientHttpRequestFactory) {
        return restTemplateBuilder
                .requestFactory(() -> jdkClientHttpRequestFactory)
                .build();
    }

    @Bean
    public JdkClientHttpRequestFactory jdkClientHttpRequestFactory() {
        return new JdkClientHttpRequestFactory();
    }

}
