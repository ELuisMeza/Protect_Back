package com.emz.protec.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emz.protec.auth.dto.LoginRequest;
import com.emz.protec.auth.dto.LoginResponse;
import com.emz.protec.auth.dto.LoginResult;
import com.emz.protec.auth.service.AuthService;
import com.emz.protec.security.AuthCookieService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {

	private final AuthService authService;
	private final AuthCookieService authCookieService;

	public AuthController(AuthService authService, AuthCookieService authCookieService) {
		this.authService = authService;
		this.authCookieService = authCookieService;
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(
			@Valid @RequestBody LoginRequest request,
			HttpServletResponse response) {
		LoginResult result = authService.login(request);
		authCookieService.setAuthCookie(response, result.token());
		return ResponseEntity.ok(result.user());
	}

	@PostMapping("/logout")
	public ResponseEntity<Void> logout(HttpServletResponse response) {
		authCookieService.clearAuthCookie(response);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/me")
	public ResponseEntity<LoginResponse> me(Authentication authentication) {
		String role = authentication.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.map(authority -> authority.startsWith("ROLE_") ? authority.substring(5) : authority)
				.findFirst()
				.orElse("ADMIN");
		return ResponseEntity.ok(new LoginResponse(authentication.getName(), role));
	}
}
