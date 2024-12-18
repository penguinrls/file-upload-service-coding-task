package com.file.upload.exception;

public class FileProcessingException extends RuntimeException {

    public FileProcessingException(String message) {
        super(message);
    }

    public FileProcessingException(String message, Exception e) {
        super(message, e);
    }
}
