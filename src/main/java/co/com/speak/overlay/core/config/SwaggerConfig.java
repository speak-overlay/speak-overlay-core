package co.com.speak.overlay.core.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de ejemplo con S3GW")
                        .version("1.0.0")
                        .description("Integración de almacenamiento S3 compatible usando s3gw")
                        .contact(new Contact()
                                .name("Tu Nombre")
                                .email("tu@correo.com")));
    }
}
