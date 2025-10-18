package co.com.speak.overlay.core.video.controller;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@RequiredArgsConstructor
@Component
public class StorageRepositoryMinIO {

    private final static String FILE_NAME = "filename";

    private final MinioClient minioClient;


    @Value("${files.bucket-name}")
    private String bucketName;

    public void upload(String fileName, FileUploadRequest request) {
      try {
            Map<String, String> metadata = new HashMap<>();
            metadata.put(FILE_NAME, fileName);


            InputStream inputStream = new ByteArrayInputStream(request.getBytes());
            PutObjectArgs build1 = PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(request.getObjectName())
                    .stream(inputStream, request.getBytes().length, -1)
                    .contentType(request.getContentType())
                    .userMetadata(metadata)
                    .build();

            minioClient.putObject(build1);
            log.info("file uploaded successfully on MinIO [{}] - [{}]", request.getFilename(), request.getObjectName());


            return FileUploadResponse
                    .builder()
                    .name(sanitizedFileName)
                    .resourse(request.getObjectName())
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error sending file to MinIO [{}] - [{}] ", request.getFilename(), request.getObjectName());
            throw new GenericConflictException(FileManagerError.UPLOAD_FILE_ERROR);
        }
    }
/*
    public FileUploadResponse uploadObject(String keyName, MultipartFile file) {

        try {
            FileUploadRequest request = FileUploadRequest.builder()
                    .filename(file.getOriginalFilename())
                    .bytes(file.getBytes())
                    .contentType(file.getContentType())
                    .contentLength(file.getSize())
                    .objectName(keyName)
                    .build();

            return upload(request);
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("Error building  FileUploadRequest file to MinIO [{}] - [{}]", file.getOriginalFilename(), keyName);
            throw new GenericConflictException(FileManagerError.UPLOAD_FILE_ERROR);
        }
    }

    public String uploadFileUploadDTO(String keyName, FileUploadDTO fileDTO) {
        log.info("uploading FileDTO [{}]", keyName);
        byte[] contentBytes = Base64.getDecoder().decode(fileDTO.getData());
        FileUploadRequest request = FileUploadRequest.builder()
                .filename(fileDTO.getFileName())
                .bytes(contentBytes)
                .contentType(fileDTO.getContentType())
                .contentLength((long) contentBytes.length)
                .objectName(keyName)
                .build();
        upload(request);
        return fileNameUtils.getDownloadUrl(keyName, fileDTO.getFileName());
    }

    @Override
    public String getBucketName() {
        return useDefaultBucket ? defaultBucket : TenantContext.getCurrentTenant();
    }


    public FileDetails downloadObject(String keyName) {
        log.info("downloading object from MinIO [{}]", keyName);
        try {
            StatObjectArgs statObjectArgs = StatObjectArgs.builder()
                    .bucket(getBucketName())
                    .object(keyName)
                    .build();
            StatObjectResponse statObjectResponse = minioClient.statObject(statObjectArgs);


            GetObjectArgs getObjectArgs = GetObjectArgs.builder()
                    .bucket(getBucketName())
                    .object(keyName)
                    .build();
            GetObjectResponse response = minioClient.getObject(getObjectArgs);

            byte[] byteArray = response.readAllBytes();
            return FileDetails.builder()
                    .contentType(statObjectResponse.contentType())
                    .file(byteArray)
                    .fileLength(statObjectResponse.size())
                    .filename(S3Utils.getCustomName(statObjectResponse))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("error reading data from sMinIO {}", e);
            throw new GenericConflictException(FileManagerError.DOWNLOAD_FILE_ERROR);
        }
    }

    @Override
    public void deleteObjects(List<String> keyNames) {
        String bucketName = getBucketName();
        try {
            for (String keyName : keyNames) {

                RemoveObjectArgs build = RemoveObjectArgs.builder()
                        .bucket(bucketName)
                        .object(keyName)
                        .build();

                log.info("try to delete Object : [{}] ", keyName);
                minioClient.removeObject(build);
                log.info("Object : [{}] deleted ", keyName);

            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new GenericConflictException(FileManagerError.DELETE_FILE_ERROR);
        }
    }*/

}
