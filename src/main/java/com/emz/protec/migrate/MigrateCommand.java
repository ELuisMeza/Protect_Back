package com.emz.protec.migrate;

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
public class MigrateCommand implements ApplicationRunner {

	private static final Logger log = LoggerFactory.getLogger(MigrateCommand.class);

	private final DatabaseMigrator databaseMigrator;
	private final ConfigurableApplicationContext context;

	public MigrateCommand(DatabaseMigrator databaseMigrator, ConfigurableApplicationContext context) {
		this.databaseMigrator = databaseMigrator;
		this.context = context;
	}

	@Override
	public void run(ApplicationArguments args) {
		if (!requested(args)) {
			return;
		}

		log.info("Ejecutando migración (--migrate)");
		databaseMigrator.migrate();
		log.info("Migración finalizada.");
		System.exit(SpringApplication.exit(context, () -> 0));
	}

	private static boolean requested(ApplicationArguments args) {
		return args.containsOption("migrate") || args.getNonOptionArgs().contains("migrate");
	}
}
