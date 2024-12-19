package com.file.upload.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@Tag(name = "Upload File API")
@RestController
@RequiredArgsConstructor
public class FileUploadApiController {

    private final FileUploadFacade fileUploadFacade;

    @Operation(description = "Upload file to document collection")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "File successfully uploaded"),
            @ApiResponse(responseCode = "400", description = "The request file was invalid")
    })
    @PostMapping(path = "v1/api/upload", consumes = MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(value = CREATED)
    public ResponseEntity<byte[]> upload(@RequestPart("file") MultipartFile file) {
            byte[] json = fileUploadFacade.uploadFile(file);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("application/json"))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=OutcomeFile.json")
                    .contentLength(json.length)
                    .body(json);
    }
}
