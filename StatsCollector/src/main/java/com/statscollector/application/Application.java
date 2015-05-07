package com.statscollector.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({ "com.statscollector.gerrit.service", "com.statscollector.gerrit.dao",
		"com.statscollector.gerrit.controller", "com.statscollector.gerrit.model",
		"com.statscollector.gerrit.authentication", "com.statscollector.gerrit.config",
		"com.statscollector.information.controller" })
public class Application extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}

	public static void main(final String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
