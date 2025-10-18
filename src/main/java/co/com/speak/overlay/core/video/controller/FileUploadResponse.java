package co.com.speak.overlay.core.video.controller;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FileUploadResponse {
    private String name;
    private String resource;
}
