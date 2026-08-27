package com.emz.protec.auth.service;

import com.emz.protec.auth.dto.LoginRequest;
import com.emz.protec.auth.dto.LoginResult;

public interface AuthService {

	LoginResult login(LoginRequest request);
}
