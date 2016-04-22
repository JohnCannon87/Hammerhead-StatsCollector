package com.statscollector.gerrit.service;

import java.io.IOException;

import org.apache.commons.configuration.ConfigurationException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.statscollector.gerrit.config.GerritConfig;

@Component
public class GerritConfigTranslator {

    public GerritConfig getConfigFromFile(final MultipartFile file)
            throws IOException, ConfigurationException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(file.getBytes(), GerritConfig.class);
    }

}
