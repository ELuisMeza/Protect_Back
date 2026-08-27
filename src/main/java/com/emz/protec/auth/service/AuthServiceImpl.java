package com.emz.protec.auth.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.emz.protec.auth.dto.LoginRequest;
import com.emz.protec.auth.dto.LoginResponse;
import com.emz.protec.auth.dto.LoginResult;
import com.emz.protec.security.JwtService;

@Service
public class AuthServiceImpl implements AuthService {

	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;

	public AuthServiceImpl(AuthenticationManager authenticationManager, JwtService jwtService) {
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
	}

	@Override
	public LoginResult login(LoginRequest request) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.username(), request.password()));

		String token = jwtService.generateToken(authentication);
		String role = authentication.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.map(authority -> authority.startsWith("ROLE_") ? authority.substring(5) : authority)
				.findFirst()
				.orElse("ADMIN");

		return new LoginResult(token, new LoginResponse(authentication.getName(), role));
	}
}
