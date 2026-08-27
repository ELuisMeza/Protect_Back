package com.emz.protec.seed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(0)
public class SeedCommand implements ApplicationRunner {

	private static final Logger log = LoggerFactory.getLogger(SeedCommand.class);

	private final AdminUserSeeder adminUserSeeder;
	private final CatalogSeeder catalogSeeder;
	private final ConfigurableApplicationContext context;

	public SeedCommand(
			AdminUserSeeder adminUserSeeder,
			CatalogSeeder catalogSeeder,
			ConfigurableApplicationContext context) {
		this.adminUserSeeder = adminUserSeeder;
		this.catalogSeeder = catalogSeeder;
		this.context = context;
	}

	@Override
	public void run(ApplicationArguments args) {
		if (!requested(args)) {
			return;
		}

		log.info("Ejecutando seeders (--seed)");
		adminUserSeeder.seed();
		catalogSeeder.seed();
		log.info("Seeders finalizados. Cerrando proceso.");
		System.exit(SpringApplication.exit(context, () -> 0));
	}

	private static boolean requested(ApplicationArguments args) {
		return args.containsOption("seed") || args.getNonOptionArgs().contains("seed");
	}
}
