package com.emz.protec.auth.service;

import com.emz.protec.auth.dto.LoginRequest;
import com.emz.protec.auth.dto.LoginResponse;

public interface AuthService {

	LoginResponse login(LoginRequest request);
}
