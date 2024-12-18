package com.file.upload;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FileUploadService {

	private static final Logger LOG = LoggerFactory.getLogger(FileUploadService.class);

	public static void main(String[] args) {
		SpringApplication.run(FileUploadService.class, args);
	}
}
