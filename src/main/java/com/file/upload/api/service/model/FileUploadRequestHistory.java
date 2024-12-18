package com.file.upload.api.service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@DynamicUpdate
@Table(name = "FILE_UPLOAD_HISTORY")
@Getter
@Setter
@ToString
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@Entity
public class FileUploadRequestHistory {

    @Column(name = "REQUEST_ID")
    @Id
    @GeneratedValue(strategy= GenerationType.UUID)
    private UUID requestId;

    @Column(name = "REQUEST_URI")
    private String requestUri;

    @Column(name = "REQUEST_TIME_STAMP")
    private LocalDateTime requestTimeStamp;

    @Column(name = "RESPONSE_CODE")
    private Integer responseCode;

    @Column(name = "REQUEST_IP_ADDRESS")
    private String reqIpAddress;

    @Column(name = "REQUEST_COUNTRY_CODE")
    private String reqCountryCode;

    @Column(name = "REQUEST_IP_PROVIDER")
    private String reqIpProvider;

    @Column(name = "TIME_LAPSED_OF_REQUEST")
    private Long timeLapsedOfRequest;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileUploadRequestHistory that = (FileUploadRequestHistory) o;
        return Objects.equals(requestId, that.requestId) && Objects.equals(requestUri, that.requestUri) && Objects.equals(requestTimeStamp, that.requestTimeStamp) && Objects.equals(responseCode, that.responseCode) && Objects.equals(reqIpAddress, that.reqIpAddress) && Objects.equals(reqCountryCode, that.reqCountryCode) && Objects.equals(reqIpProvider, that.reqIpProvider) && Objects.equals(timeLapsedOfRequest, that.timeLapsedOfRequest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestId, requestUri, requestTimeStamp, responseCode, reqIpAddress, reqCountryCode, reqIpProvider, timeLapsedOfRequest);
    }
}
