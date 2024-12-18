package com.file.upload.api.controller;

import org.springframework.web.multipart.MultipartFile;


public interface FileUploadFacade {

    byte[] uploadFile(MultipartFile file);

}
