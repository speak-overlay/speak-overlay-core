package co.com.speak.overlay.core.video.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/video")
@RequiredArgsConstructor
public class VideoUploadController {

    private final StorageRepositoryMinIO repository;
    private static final String UPLOAD_DIR = "speak-overlay/original/videos/";

    @Operation(
            summary = "Subir un video a S3 compatible (s3gw o MinIO)",
            description = "Permite subir un archivo de video y opcionalmente un JSON con metadatos."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Archivo subido exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<FileUploadResponse> uploadFile(
            @RequestPart("file")
            @Parameter(description = "Archivo de video a subir", required = true,
                    content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE,
                            schema = @Schema(type = "string", format = "binary")))
            MultipartFile file,

            @RequestPart(value = "metadata", required = false)
            @Parameter(description = "JSON con metadatos del video (opcional)",
                    content = @Content(schema = @Schema(implementation = Object.class)))
            Object metadata
    ) throws IOException {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        String objectName = UPLOAD_DIR + UUID.randomUUID();

        FileUploadRequest fileUploadRequest = FileUploadRequest.builder()
                .filename(file.getOriginalFilename())
                .bytes(file.getBytes())
                .contentType(file.getContentType())
                .contentLength(file.getSize())
                .objectName(objectName)
                .build();

        FileUploadResponse upload = repository.upload(fileUploadRequest);

        return ResponseEntity.ok(upload);
    }
}
