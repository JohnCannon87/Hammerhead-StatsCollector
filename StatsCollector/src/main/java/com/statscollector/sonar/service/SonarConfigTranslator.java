package com.statscollector.sonar.service;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.statscollector.sonar.config.SonarConfig;

@Component
public class SonarConfigTranslator {

    public SonarConfig getConfigFromFile(final MultipartFile file)
            throws JsonSyntaxException, JsonIOException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(file.getBytes(), SonarConfig.class);
    }

}
