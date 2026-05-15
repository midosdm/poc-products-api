package com.api.pocproductsapi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.*;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
        info =
                @Info(
                        title = "Products API poc",
                        description = "This documents all the products API calls. They are JWT secured.",
                        version = "v1.0.0",
                        contact = @Contact(name = "Mohamed SAIDEM", email = "mohamedsaidem01@gmail.com")),
        security = @SecurityRequirement(name = "bearerAuth"),
        servers = {@Server(url = "http://localhost:8080")})
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT")
@SpringBootApplication
public class PocProductsApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(PocProductsApiApplication.class, args);
    }
}
