package com.statscollector.application;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.statscollector.application.config.GerritJsonConfigService;
import com.statscollector.application.config.SonarJsonConfigService;
import com.statscollector.gerrit.config.GerritConfig;
import com.statscollector.sonar.config.SonarConfig;

@Configuration
public class WebConfiguration extends WebMvcAutoConfigurationAdapter {

    @Autowired
    private GerritJsonConfigService gerritConfigService;

    @Autowired
    private SonarJsonConfigService sonarConfigService;

    private static final Logger LOGGER = Logger.getLogger(WebConfiguration.class);

    @Bean
    public GerritJsonConfigService gerritConfigService(
            @Value("${config.gerrit.fileName}") final String gerritConfigFileName) {
        if(null == gerritConfigService) {
            gerritConfigService = new GerritJsonConfigService(gerritConfigFileName);
        }
        return gerritConfigService;
    }

    @Bean
    public SonarJsonConfigService sonarConfigService(
            @Value("${config.sonar.fileName}") final String sonarConfigFileName) {
        if(null == sonarConfigService) {
            sonarConfigService = new SonarJsonConfigService(sonarConfigFileName);
        }
        return sonarConfigService;
    }

    @Bean
    public GerritConfig gerritConfig() {
        try {
            GerritConfig configFile = gerritConfigService.getConfigFile();
            if(null == configFile) {
                configFile = new GerritConfig();
                gerritConfigService.saveConfigFile(configFile);
            }
            return configFile;
        } catch(IOException e) {
            LOGGER.error(e);
        }
        return null;
    }

    @Bean
    public SonarConfig sonarConfig() {
        try {
            SonarConfig configFile = sonarConfigService.getConfigFile();
            if(null == configFile) {
                configFile = new SonarConfig();
                sonarConfigService.saveConfigFile(configFile);
            }
            return configFile;
        } catch(IOException e) {
            LOGGER.error(e);
        }
        return null;
    }

}
