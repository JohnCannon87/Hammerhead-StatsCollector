package com.statscollector.application;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAsync
@ComponentScan({ "com.statscollector.gerrit.service", "com.statscollector.gerrit.dao",
	"com.statscollector.gerrit.controller", "com.statscollector.gerrit.model",
	"com.statscollector.gerrit.authentication", "com.statscollector.gerrit.config",
"com.statscollector.information.controller" })
public class Application extends SpringBootServletInitializer {

	final static Logger LOGGER = Logger.getLogger(Application.class);
	private static final String SERVER_PORT_KEY = "server.port";

	@Override
	protected SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}

	public static void main(final String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public EmbeddedServletContainerFactory servletContainer() {
		TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
		factory.setPort(80);
		return factory;
	}
}
