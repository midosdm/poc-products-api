package com.api.pocproductsapi.service.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "poc-products-api")
public class AuthProperties {
    @NotBlank(message = "Jwt secret cannot be empty")
    private String jwtSecret;
}
