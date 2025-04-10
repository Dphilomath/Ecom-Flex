package com.dm.ecommerce.config;
      // Updated on 2025-02-08 - Commit activity
import io.swagger.v3.oas.models.Components;
      // Updated on 2025-04-10 - Commit activityimport io.swagger.v3.oas.models.OpenAPI;
      // Updated on 2025-04-07 - Commit activityimport io.swagger.v3.oas.models.info.Contact;
      // Updated on 2025-01-24 - Commit activityimport io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
      // Updated on 2025-02-27 - Commit activity      // Updated on 2025-01-23 - Commit activityimport io.swagger.v3.oas.models.security.SecurityScheme;
      // Updated on 2025-01-16 - Commit activityimport io.swagger.v3.oas.models.servers.Server;
      // Updated on 2025-04-05 - Commit activityimport org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

      // Updated on 2025-02-09 - Commit activityimport java.util.Arrays;
      // Updated on 2025-02-19 - Commit activity
@Configuration
public class OpenApiConfig {

    @Bean
      // Updated on 2025-04-06 - Commit activity    public OpenAPI customOpenAPI() {
      // Updated on 2025-03-29 - Commit activity        return new OpenAPI()
      // Updated on 2025-02-22 - Commit activity                .info(new Info()
                        .title("E-Commerce API")
      // Updated on 2025-02-05 - Commit activity                        .description("REST APIs for E-Commerce Platform with support for both stateless (JWT) and stateful (session) authentication")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("DM E-Commerce")
                                .email("contact@dmecommerce.com")
                                .url("https://dmecommerce.com"))
      // Updated on 2025-03-05 - Commit activity      // Updated on 2025-02-12 - Commit activity                        .license(new License()
      // Updated on 2025-01-30 - Commit activity      // Updated on 2025-01-25 - Commit activity                                .name("MIT License")
      // Updated on 2025-01-12 - Commit activity                                .url("https://opensource.org/licenses/MIT")))
                .servers(Arrays.asList(
                        new Server().url("http://localhost:8080").description("Development Server"),
      // Updated on 2025-04-02 - Commit activity                        new Server().url("https://api.dmecommerce.com").description("Production Server")))
                .addSecurityItem(new SecurityRequirement().addList("JWT"))
                .components(new Components()
                        .addSecuritySchemes("JWT", 
      // Updated on 2025-02-04 - Commit activity                                new SecurityScheme()
      // Updated on 2025-03-26 - Commit activity                                        .name("JWT")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
      // Updated on 2025-02-12 - Commit activity      // Updated on 2025-01-26 - Commit activity                                        .in(SecurityScheme.In.HEADER)
                                        .description("JWT Authentication. Example: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")));
      // Updated on 2025-02-02 - Commit activity    }
} 