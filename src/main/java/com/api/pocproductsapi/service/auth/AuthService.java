package com.api.pocproductsapi.service.auth;

import com.api.pocproductsapi.dto.auth.CreateAccountRequest;
import com.api.pocproductsapi.dto.auth.LoginRequest;
import com.api.pocproductsapi.dto.auth.LoginResponse;
import com.api.pocproductsapi.entity.User;
import com.api.pocproductsapi.exception.InvalidCredentialsException;
import com.api.pocproductsapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public void register(CreateAccountRequest request) {
        log.info("Registering user with email: {}", request.getEmail());
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request) {

        log.info("Logging in user with email: {}", request.getEmail());
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.error("Invalid credentials for user: {}", request.getEmail());
            throw new InvalidCredentialsException();
        }

        String token = jwtService.generateToken(user.getEmail());

        return new LoginResponse(token);
    }
}
