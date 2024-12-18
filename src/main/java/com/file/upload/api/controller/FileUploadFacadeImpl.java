package com.file.upload.api.controller;

import com.file.upload.api.service.FileProcessingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileUploadFacadeImpl implements FileUploadFacade {

    private final FileProcessingService fileProcessingService;

    @Override
    public byte[] uploadFile(MultipartFile file) {
        return fileProcessingService.process(file);
    }
    
}
