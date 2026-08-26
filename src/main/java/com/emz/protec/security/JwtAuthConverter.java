package com.emz.protec.security;

import java.util.Collection;
import java.util.List;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

	@Override
	public AbstractAuthenticationToken convert(Jwt jwt) {
		Collection<GrantedAuthority> authorities = extractAuthorities(jwt);
		return new JwtAuthenticationToken(jwt, authorities, jwt.getSubject());
	}

	private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
		String role = jwt.getClaimAsString("role");
		if (role == null || role.isBlank()) {
			List<String> roles = jwt.getClaimAsStringList("roles");
			if (roles != null && !roles.isEmpty()) {
				role = roles.getFirst();
			}
		}
		if (role == null || role.isBlank()) {
			return List.of();
		}
		String normalized = role.startsWith("ROLE_") ? role : "ROLE_" + role;
		return List.of(new SimpleGrantedAuthority(normalized));
	}
}
