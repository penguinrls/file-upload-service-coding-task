package com.file.upload.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.file.upload.api.service.model.FileContent;
import com.file.upload.exception.FileProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import static java.util.function.Predicate.not;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileProcessingServiceImpl implements FileProcessingService {

    private final ObjectMapper objectMapper;

    @Override
    public byte[] process(MultipartFile file) {
        List<FileContent> fileContentList = readFile(file);
        return writeFile(fileContentList);
    }

    private byte[] writeFile(List<FileContent> fileContentList) {
        try {
            return objectMapper.writeValueAsBytes(fileContentList);
        }
        catch (IOException e) {
            throw new FileProcessingException(e.getMessage());
        }
    }

    private List<FileContent> readFile(MultipartFile multipartFile) {
        try(InputStream inputStream = multipartFile.getInputStream()) {
            return new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                    .lines()
                    .filter(not(line -> line.startsWith("\\n")))
                    .map(this::processLine)
                    .toList();
        }
        catch (IOException e) {
            throw new FileProcessingException(e.getMessage());
        }
    }

    private FileContent processLine(String line) {
        String []tokens = line.split("\\|");

        if (tokens.length != 7) {
            throw new FileProcessingException("Invalid file format");
        }

        return FileContent.builder()
                .uuid(UUID.fromString(tokens[0]))
                .id(tokens[1])
                .name(tokens[2])
                .transport(tokens[4])
                .topSpeed(new BigDecimal(tokens[5]))
                .averageSpeed(new BigDecimal(tokens[6]))
                .build();
    }

}
