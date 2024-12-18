package com.file.upload.api.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;


public interface FileUploadFacade {

    byte[] uploadFile(MultipartFile file, HttpServletRequest request);

}
