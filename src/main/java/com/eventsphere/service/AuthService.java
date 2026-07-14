package com.eventsphere.service;

import com.eventsphere.dto.AuthResponse;
import com.eventsphere.dto.LoginRequest;
import com.eventsphere.dto.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
