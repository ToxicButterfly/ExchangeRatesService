package org.example.exchangeratesservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Exchange Rates Service API")
                        .version("1.0.0")
                        .description("API for retrieving and managing currency exchange rates.")
                        .contact(new Contact()
                                .name("Toxic Bfly")
                                .email("Qwerty541683@gmail.com")));
    }
}
