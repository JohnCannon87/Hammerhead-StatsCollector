package com.statscollector.sonar.controller;

import java.io.IOException;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.factories.SchemaFactoryWrapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.statscollector.sonar.config.SonarConfig;
import com.statscollector.sonar.config.TempSonarConfig;
import com.statscollector.sonar.service.SonarConfigTranslator;

@RestController
@RequestMapping("/sonar/config")
public class SonarConfigRestfulController {

    final static Logger LOGGER = Logger.getLogger(SonarConfigRestfulController.class);

    @Autowired
    private SonarConfig sonarConfig;

    @Autowired
    private SonarConfigTranslator sonarConfigTranslator;

    @RequestMapping(value = "/schema", produces = "application/json")
    @ResponseBody
    public JsonSchema sonarConfigSchema() throws JsonMappingException {
    	ObjectMapper m = new ObjectMapper();
    	SchemaFactoryWrapper visitor = new SchemaFactoryWrapper();
    	m.acceptJsonFormatVisitor(SonarConfig.class, visitor);
    	JsonSchema jsonSchema = visitor.finalSchema();
		return jsonSchema;    	
    }
    
    @RequestMapping(value = "/info", produces = "application/json")
    @ResponseBody
    public SonarConfig sonarInfo() {
        return sonarConfig;
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public @ResponseBody SonarConfig handleFileUpload(@RequestParam("file") final MultipartFile file)
            throws IOException, ConfigurationException {
        LOGGER.info("Uploaded File: " + file.getName());
        return sonarConfigTranslator.updateConfigFromFile(file, sonarConfig);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/changeConfig", produces = "application/json", consumes = "application/json")
    @ResponseBody
    public SonarConfig changeConfig(@RequestBody final TempSonarConfig newSonarConfig) throws ConfigurationException {
        sonarConfig.replaceWith(newSonarConfig);
        sonarConfig.setUsernameAndPassword(newSonarConfig.getUsername(), newSonarConfig.getPassword());
        return sonarConfig;
    }

}
