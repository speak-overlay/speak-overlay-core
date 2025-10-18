package co.com.speak.overlay.core.video.controller;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FileUploadRequest {
    private String filename;
    private byte[] bytes;
    private String contentType;
    private Long contentLength;
    private String objectName;
    private String transactionId;
}