package com.file.upload.interceptor;

import com.file.upload.api.service.FileHistoryRequestRepositoryTransactionService;
import com.file.upload.api.service.model.FileUploadRequestHistory;
import com.file.upload.api.service.validator.FileUploadValidatorService;
import com.file.upload.client.IpApiClient;
import com.file.upload.client.response.IpGeoLocationDto;
import com.file.upload.exception.ValidationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;
import java.util.Map;

import static jakarta.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static java.time.temporal.ChronoUnit.MILLIS;

@Component
@RequiredArgsConstructor
public class RequestInterceptor implements HandlerInterceptor {

    private final IpApiClient apiClient;
    private final FileHistoryRequestRepositoryTransactionService fileHistoryRepositoryTransactionService;
    private final FileUploadValidatorService fileUploadValidatorService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        LocalDateTime key = LocalDateTime.now();
        request.setAttribute("requestStart", key);
        IpGeoLocationDto ipGeoLocationDto = apiClient.getLocationMeta();
        request.setAttribute("ipGeoLocationDtoMap", Map.of(key, ipGeoLocationDto));

        if (fileUploadValidatorService != null) {
            try {
                fileUploadValidatorService.validate(ipGeoLocationDto);
            }
            catch(ValidationException validationException) {
                response.sendError(SC_FORBIDDEN, validationException.getMessage());
                return false;
            }
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        LocalDateTime requestStart = (LocalDateTime) request.getAttribute("requestStart");
        Map<LocalDateTime, IpGeoLocationDto> ipGeoLocationDtoMap = (Map<LocalDateTime, IpGeoLocationDto>) request.getAttribute("ipGeoLocationDtoMap");

        if (ipGeoLocationDtoMap != null && ipGeoLocationDtoMap.containsKey(requestStart)) {
            writeAudit(request, response, requestStart, ipGeoLocationDtoMap.get(requestStart));
        }
    }

    private void writeAudit(HttpServletRequest request, HttpServletResponse response, LocalDateTime startRequest, IpGeoLocationDto ipGeoLocationDto) {
        FileUploadRequestHistory fileUploadHistory = new FileUploadRequestHistory();
        fileUploadHistory.setRequestTimeStamp(startRequest);
        fileUploadHistory.setTimeLapsedOfRequest(MILLIS.between(startRequest, LocalDateTime.now()));
        fileUploadHistory.setReqIpAddress(ipGeoLocationDto.getQuery());
        fileUploadHistory.setReqCountryCode(ipGeoLocationDto.getCountryCode());
        fileUploadHistory.setReqIpProvider(ipGeoLocationDto.getIsp());
        fileUploadHistory.setRequestUri(request.getRequestURI());
        fileUploadHistory.setResponseCode(response.getStatus());
        fileHistoryRepositoryTransactionService.save(fileUploadHistory);
    }
}
