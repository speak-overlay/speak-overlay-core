package co.com.speak.overlay.core.video.config;


import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.util.Objects.nonNull;

@Configuration
public class MinIOConfig {

    private MinioClient minioClient;
    @Value("${min-io.service}")
    private String MINIO_SERVICE;

    @Value("${min-io.credentials.client}")
    private String MINIO_SERVICE_CLIENT;

    @Value("${min-io.credentials.client-secret}")
    private String MINIO_SERVICE_CLIENT_SECRET;


    @Bean(name = "minioClientBean")
    public MinioClient getMinioClient() {
        if (nonNull(this.minioClient)) {
            return this.minioClient;
        }
        this.minioClient = MinioClient.builder()
                .endpoint(MINIO_SERVICE)
                .credentials(MINIO_SERVICE_CLIENT, MINIO_SERVICE_CLIENT_SECRET)
                .build();

        return this.minioClient;
    }


}
