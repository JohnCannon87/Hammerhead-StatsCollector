package com.statscollector.application;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.statscollector.application.config.GerritJsonConfigService;
import com.statscollector.application.config.SonarJsonConfigService;
import com.statscollector.gerrit.config.GerritConfig;
import com.statscollector.neo.sonar.config.SonarConfig;
import com.statscollector.neo.sonar.service.metrics.DistributionConverter;
import com.statscollector.neo.sonar.service.metrics.GenericConverter;
import com.statscollector.neo.sonar.service.metrics.NumberConverter;
import com.statscollector.neo.sonar.service.metrics.SonarMetricConverter;
import com.statscollector.neo.sonar.service.metrics.SonarMetricConverterService;

@Configuration
public class WebConfiguration extends WebMvcAutoConfigurationAdapter {

    @Autowired
    private GerritJsonConfigService gerritConfigService;

    @Autowired
    private SonarJsonConfigService sonarConfigService;

    private SonarMetricConverterService sonarMetricConverterService;

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

    @Bean
    public SonarMetricConverterService sonarMetricConverterService() {
        if(null == sonarMetricConverterService) {
            // @formatter:off
            Map<String, SonarMetricConverter> converters =  new HashMap<>();
            converters.put(SonarMetricConverterService.LINES_OF_CODE_KEY, new NumberConverter(SonarMetricConverterService.LINES_OF_CODE_NAME, SonarMetricConverterService.LINES_OF_CODE_KEY));
            converters.put(SonarMetricConverterService.COMPLEXITY_KEY , new NumberConverter(SonarMetricConverterService.COMPLEXITY_NAME, SonarMetricConverterService.COMPLEXITY_KEY));
            converters.put(SonarMetricConverterService.FILES_KEY , new NumberConverter(SonarMetricConverterService.FILES_NAME, SonarMetricConverterService.FILES_KEY));
            converters.put(SonarMetricConverterService.METHODS_KEY , new NumberConverter(SonarMetricConverterService.METHODS_NAME, SonarMetricConverterService.METHODS_KEY));
            converters.put(SonarMetricConverterService.BLOCKER_VIOLATIONS_KEY , new NumberConverter(SonarMetricConverterService.BLOCKER_VIOLATIONS_NAME, SonarMetricConverterService.BLOCKER_VIOLATIONS_KEY));
            converters.put(SonarMetricConverterService.CRITICAL_VIOLATIONS_KEY , new NumberConverter(SonarMetricConverterService.CRITICAL_VIOLATIONS_NAME, SonarMetricConverterService.CRITICAL_VIOLATIONS_KEY));
            converters.put(SonarMetricConverterService.MAJOR_VIOLATIONS_KEY , new NumberConverter(SonarMetricConverterService.MAJOR_VIOLATIONS_NAME, SonarMetricConverterService.MAJOR_VIOLATIONS_KEY));
            converters.put(SonarMetricConverterService.MINOR_VIOLATIONS_KEY , new NumberConverter(SonarMetricConverterService.MINOR_VIOLATIONS_NAME, SonarMetricConverterService.MINOR_VIOLATIONS_KEY));
            converters.put(SonarMetricConverterService.INFO_VIOLATIONS_KEY , new NumberConverter(SonarMetricConverterService.INFO_VIOLATIONS_NAME, SonarMetricConverterService.INFO_VIOLATIONS_KEY));
            converters.put(SonarMetricConverterService.LINES_TO_COVER_KEY , new NumberConverter(SonarMetricConverterService.LINES_TO_COVER_NAME, SonarMetricConverterService.LINES_TO_COVER_KEY));
            converters.put(SonarMetricConverterService.UNCOVERED_LINES_KEY , new NumberConverter(SonarMetricConverterService.UNCOVERED_LINES_NAME, SonarMetricConverterService.UNCOVERED_LINES_KEY));
            converters.put(SonarMetricConverterService.FUNCTION_COMPLEXITY_DISTRIBUTION_KEY , new DistributionConverter(SonarMetricConverterService.FUNCTION_COMPLEXITY_DISTRIBUTION_NAME, SonarMetricConverterService.FUNCTION_COMPLEXITY_DISTRIBUTION_KEY));
            converters.put(SonarMetricConverterService.FILE_COMPLEXITY_DISTRIBUTION_KEY , new DistributionConverter(SonarMetricConverterService.FILE_COMPLEXITY_DISTRIBUTION_NAME, SonarMetricConverterService.FILE_COMPLEXITY_DISTRIBUTION_KEY));
            converters.put(SonarMetricConverterService.DEFAULT_CONVERTER_KEY , new GenericConverter(SonarMetricConverterService.DEFAULT_CONVERTER_NAME, SonarMetricConverterService.DEFAULT_CONVERTER_KEY));
            // @formatter:on
            sonarMetricConverterService = new SonarMetricConverterService(converters);
        }
        return sonarMetricConverterService;

    }

}
