package com.file.upload.client.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IpGeoLocationDto {

    String query;
    String countryCode;
    String isp;

    public boolean isValidIps(List<String> invalidIps) {
        return invalidIps.stream().noneMatch(invalidIsp -> isp.equals(invalidIsp));
    }

    public boolean isValidCountryCode(List<String> invalidCountryCodes) {
        return invalidCountryCodes.stream().noneMatch(invalidCountryCode -> countryCode.equals(invalidCountryCode));
    }
}
