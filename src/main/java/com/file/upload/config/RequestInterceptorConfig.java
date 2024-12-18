package com.file.upload.config;

import com.file.upload.api.service.FileHistoryRequestRepositoryTransactionService;
import com.file.upload.api.service.FileHistoryRequestRepositoryTransactionServiceImpl;
import com.file.upload.api.service.validator.FileUploadValidatorService;
import com.file.upload.client.IpApiClient;
import com.file.upload.interceptor.RequestInterceptor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class RequestInterceptorConfig implements WebMvcConfigurer {

    private IpApiClient ipApiClient;
    private FileHistoryRequestRepositoryTransactionService fileHistoryRepositoryTransactionService;
    private FileUploadValidatorService fileUploadValidatorService;

    public RequestInterceptorConfig(@Lazy FileHistoryRequestRepositoryTransactionServiceImpl fileHistoryRepositoryTransactionService,
                                    @Lazy IpApiClient ipApiClient,
                                    FileUploadValidatorService fileUploadValidatorService) {
        this.fileHistoryRepositoryTransactionService = fileHistoryRepositoryTransactionService;
        this.ipApiClient = ipApiClient;
        this.fileUploadValidatorService = fileUploadValidatorService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestInterceptor(ipApiClient, fileHistoryRepositoryTransactionService, fileUploadValidatorService));
    }

}
