package com.file.upload.api.service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class FileContent {

    @JsonIgnore
    UUID uuid;

    @JsonIgnore
    String id;

    @JsonProperty("Name")
    String name;

    @JsonIgnore
    String likes;

    @JsonProperty("Transport")
    String transport;

    @JsonIgnore
    BigDecimal averageSpeed;

    @JsonProperty("Top Speed")
    BigDecimal topSpeed;
}
