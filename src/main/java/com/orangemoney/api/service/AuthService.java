package com.orangemoney.api.service;

import com.orangemoney.api.dto.request.LoginRequest;
import com.orangemoney.api.dto.request.RegisterRequest;
import com.orangemoney.api.dto.response.AuthResponse;
import com.orangemoney.api.dto.response.UserResponse;
import org.springframework.transaction.annotation.Transactional;

public interface AuthService {

    UserResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    void logout(String token);

    @Transactional
    void verifyEmail(String token);

    @Transactional
    void resendVerificationEmail(Long userId);
}