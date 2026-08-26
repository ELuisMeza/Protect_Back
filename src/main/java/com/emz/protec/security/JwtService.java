package com.emz.protec.security;

import java.time.Instant;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

	private final JwtEncoder jwtEncoder;
	private final JwtProperties jwtProperties;

	public JwtService(JwtEncoder jwtEncoder, JwtProperties jwtProperties) {
		this.jwtEncoder = jwtEncoder;
		this.jwtProperties = jwtProperties;
	}

	public String generateToken(Authentication authentication) {
		Instant now = Instant.now();
		Instant expiresAt = now.plusMillis(jwtProperties.expirationMs());

		String role = authentication.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.map(auth -> auth.startsWith("ROLE_") ? auth.substring(5) : auth)
				.findFirst()
				.orElse("ADMIN");

		JwtClaimsSet claims = JwtClaimsSet.builder()
				.subject(authentication.getName())
				.issuedAt(now)
				.expiresAt(expiresAt)
				.claim("role", role)
				.claim("roles", List.of(role))
				.build();

		JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
		return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
	}

	public long getExpirationMs() {
		return jwtProperties.expirationMs();
	}
}
