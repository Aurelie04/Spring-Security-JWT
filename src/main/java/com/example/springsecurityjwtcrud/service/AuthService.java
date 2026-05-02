package com.example.springsecurityjwtcrud.service;

import com.example.springsecurityjwtcrud.dto.AuthResponse;
import com.example.springsecurityjwtcrud.dto.LoginRequest;
import com.example.springsecurityjwtcrud.dto.RegisterRequest;
import com.example.springsecurityjwtcrud.model.AppUser;
import com.example.springsecurityjwtcrud.repository.UserRepository;
import com.example.springsecurityjwtcrud.security.AuthUserDetails;
import com.example.springsecurityjwtcrud.security.JwtTokenProvider;
import java.util.Locale;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtTokenProvider jwtTokenProvider,
            AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String normalizedUser = normalizeUsername(request.username());
        if (normalizedUser.isBlank()) {
            throw requestValidation("username is invalid");
        }
        if (userRepository.existsByUsername(normalizedUser)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "username already taken");
        }
        if (userRepository.existsByEmail(request.email().trim().toLowerCase(Locale.ROOT))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "email already registered");
        }

        AppUser user = new AppUser();
        user.setUsername(normalizedUser);
        user.setEmail(request.email().trim().toLowerCase(Locale.ROOT));
        user.setPasswordHash(passwordEncoder.encode(request.password()));

        AppUser saved = userRepository.save(user);
        AuthUserDetails details = new AuthUserDetails(saved);
        String token = jwtTokenProvider.generateToken(details);
        return new AuthResponse(token, saved.getUsername(), saved.getEmail());
    }

    public AuthResponse login(LoginRequest request) {
        try {
            var auth =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    normalizeUsername(request.username()), request.password()));
            AuthUserDetails details = (AuthUserDetails) auth.getPrincipal();
            AppUser u = details.getUser();
            String token = jwtTokenProvider.generateToken(details);
            return new AuthResponse(token, u.getUsername(), u.getEmail());
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "invalid username or password");
        }
    }

    private static String normalizeUsername(String raw) {
        return raw.strip();
    }

    private static ResponseStatusException requestValidation(String message) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
    }
}
