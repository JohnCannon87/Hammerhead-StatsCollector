package com.statscollector.application;

import java.io.FileNotFoundException;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.ResourceUtils;

@SpringBootApplication
@EnableScheduling
@EnableAsync
@ComponentScan({ "com.statscollector.application",
        "com.statscollector.information.controller",
        "com.statscollector.gerrit.service",
        "com.statscollector.gerrit.dao",
        "com.statscollector.gerrit.controller",
        "com.statscollector.gerrit.model",
        "com.statscollector.gerrit.authentication",
        "com.statscollector.sonar.service",
        "com.statscollector.sonar.dao",
        "com.statscollector.sonar.controller",
        "com.statscollector.sonar.model",
        "com.statscollector.sonar.authentication" })
public class Application extends SpringBootServletInitializer {

    @Value("${keystore.file}")
    private String keystoreFile;

    @Value("${keystore.pass}")
    private String keystorePass;

    final static Logger LOGGER = Logger.getLogger(Application.class);
    private static final String SERVER_PORT_KEY = "server.port";
    private static final Integer DEFAULT_PORT = 8443;

    @Override
    protected SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }

    private int getPortNumber() {
        String serverPortString = System.getProperty(SERVER_PORT_KEY);
        if(serverPortString == null) {
            LOGGER.info("No Port Provided Setting To Default Of: " + DEFAULT_PORT);
            return DEFAULT_PORT;
        }
        try {
            Integer serverPort = Integer.valueOf(serverPortString);
            LOGGER.info("Setting Port To: " + serverPort);
            return serverPort;
        } catch(NumberFormatException e) {
            LOGGER.error("Couldn't Parse Number For Port, String " + serverPortString
                    + " Is Not A Number, Returning Default Port: " + DEFAULT_PORT);
            return DEFAULT_PORT;
        }
    }

    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() throws FileNotFoundException {
        final String absoluteKeystoreFile = ResourceUtils.getFile(keystoreFile).getAbsolutePath();

        return new EmbeddedServletContainerCustomizer() {

            @Override
            public void customize(final ConfigurableEmbeddedServletContainer factory) {
                if(factory instanceof TomcatEmbeddedServletContainerFactory) {
                    TomcatEmbeddedServletContainerFactory containerFactory = (TomcatEmbeddedServletContainerFactory) factory;
                    containerFactory.addConnectorCustomizers(new TomcatConnectorCustomizer() {

                        @Override
                        public void customize(final Connector connector) {
                            connector.setPort(getPortNumber());
                            connector.setSecure(true);
                            connector.setScheme("https");
                            Http11NioProtocol proto = (Http11NioProtocol) connector.getProtocolHandler();
                            proto.setSSLEnabled(true);
                            proto.setKeystoreFile(absoluteKeystoreFile);
                            proto.setKeystorePass(keystorePass);
                            proto.setKeystoreType("PKCS12");
                        }
                    });
                }
            }
        };
    }
}
