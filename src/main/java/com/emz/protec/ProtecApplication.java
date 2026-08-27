package com.emz.protec;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProtecApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(ProtecApplication.class);
		if (isCliCommand(args)) {
			app.setWebApplicationType(WebApplicationType.NONE);
		}
		app.run(args);
	}

	static boolean isCliCommand(String[] args) {
		return Arrays.stream(args).anyMatch(arg ->
				"--migrate".equals(arg) || "migrate".equals(arg)
						|| "--seed".equals(arg) || "seed".equals(arg));
	}

}
