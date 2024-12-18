package com.file.upload.api.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileProcessingService {

   byte[] process(MultipartFile file);
}
