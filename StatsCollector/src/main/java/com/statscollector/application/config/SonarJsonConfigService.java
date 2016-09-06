package com.statscollector.application.config;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.statscollector.neo.sonar.config.SonarConfig;

/**
 * I manage the creation, saving and loading of a config file to/from a raw JSON file.
 *
 * @author JCannon
 *
 */
public class SonarJsonConfigService {

    private final String fileName;
    private final ObjectMapper mapper = new ObjectMapper();

    public SonarJsonConfigService(final String fileName) {
        super();
        this.fileName = fileName;
    }

    public SonarConfig getConfigFile() throws JsonParseException, JsonMappingException, IOException {
        File file = new File(fileName);
        if(!file.exists()) {
            return null;
        } else {
            return mapper.readValue(file, SonarConfig.class);
        }
    }

    public void saveConfigFile(final SonarConfig config)
            throws JsonGenerationException, JsonMappingException, IOException {
        mapper.writeValue(new File(fileName), config);
    }

}
