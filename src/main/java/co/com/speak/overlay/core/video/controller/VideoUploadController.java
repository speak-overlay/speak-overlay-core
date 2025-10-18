package co.com.speak.overlay.core.video.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping(value = "video")
@RequiredArgsConstructor
public class VideoUploadController {

    private final StorageRepositoryMinIO repository;
    private static String uploadDir = "speak-overlay/original/videos/";

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        String uuidFile = uploadDir + UUID.randomUUID().toString();
        FileUploadRequest fileUploadRequest = FileUploadRequest.builder()
                .filename(file.getOriginalFilename())
                .bytes(file.getBytes())
                .contentType(file.getContentType())
                .contentLength(file.getSize())
                .objectName(uuidFile)
                .build();

      //  repository.upload();

        return ResponseEntity.ok("Archivo subido exitosamente: " + fileUploadRequest.getObjectName());

    }

}
