package com.statscollector.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({ "com.statscollector.service", "com.statscollector.dao", "com.statscollector.controller",
		"com.statscollector.model", "com.statscollector.authentication", "com.statscollector.config" })
public class Application {

	public static void main(final String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
