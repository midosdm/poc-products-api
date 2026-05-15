package com.api.pocproductsapi.service.auth;

import com.api.pocproductsapi.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthorizationService {
    public boolean isAdmin(User user) {
        log.info("Checking if user is admin");

        return "admin@admin.com".equalsIgnoreCase(user.getEmail());
    }
}
