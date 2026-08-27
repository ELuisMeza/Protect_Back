package com.emz.protec.migrate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Service
public class DatabaseMigrator {

	private static final Logger log = LoggerFactory.getLogger(DatabaseMigrator.class);
	private static final String SCRIPT = "db/migration.sql";

	private final DataSource dataSource;

	public DatabaseMigrator(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void migrate() {
		log.info("Ejecutando migración {}", SCRIPT);
		ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
		populator.addScript(new ClassPathResource(SCRIPT));
		populator.setSeparator(";");
		populator.execute(dataSource);
		log.info("Migración finalizada");
	}
}
