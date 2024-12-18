package com.file.upload.api.controller;

import com.file.upload.api.service.FileProcessingService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileUploadFacadeImpl implements FileUploadFacade {

    private final FileProcessingService fileProcessingService;

    @Override
    public byte[] uploadFile(MultipartFile file, HttpServletRequest request) {
        return fileProcessingService.process(file);
    }
    
}
