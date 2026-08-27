package com.emz.protec.seed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emz.protec.user.domain.AppUser;
import com.emz.protec.user.domain.Role;
import com.emz.protec.user.repository.UserRepository;

@Service
public class AdminUserSeeder {

	private static final Logger log = LoggerFactory.getLogger(AdminUserSeeder.class);

	private static final String USERNAME = "admin";
	private static final String PASSWORD = "admin123";

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public AdminUserSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional
	public void seed() {
		if (userRepository.existsByUsername(USERNAME)) {
			log.info("Usuario admin seed omitido (ya existe): {}", USERNAME);
			return;
		}

		userRepository.save(AppUser.builder()
				.username(USERNAME)
				.password(passwordEncoder.encode(PASSWORD))
				.role(Role.ADMIN)
				.build());
		log.info("Usuario admin seed creado: {}", USERNAME);
	}
}
