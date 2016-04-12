package com.statscollector.gerrit.controller;

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
import com.statscollector.gerrit.config.GerritConfig;
import com.statscollector.gerrit.config.TempGerritConfig;
import com.statscollector.gerrit.service.GerritConfigTranslator;

@RestController
@RequestMapping("/gerrit/config")
public class GerritConfigRestfulController {

	final static Logger LOGGER = Logger.getLogger(GerritConfigRestfulController.class);

	@Autowired
	private GerritConfig gerritConfig;

	@Autowired
	private GerritConfigTranslator gerritConfigTranslator;

    @RequestMapping(value = "/schema", produces = "application/json")
    @ResponseBody
    public JsonSchema gerritConfigSchema() throws JsonMappingException {
    	ObjectMapper m = new ObjectMapper();
    	SchemaFactoryWrapper visitor = new SchemaFactoryWrapper();
    	m.acceptJsonFormatVisitor(GerritConfig.class, visitor);
    	JsonSchema jsonSchema = visitor.finalSchema();
		return jsonSchema;    	
    }

	@RequestMapping(value = "/info", produces = "application/json")
	@ResponseBody
	public GerritConfig gerritInfo() {
		return gerritConfig;
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public @ResponseBody GerritConfig handleFileUpload(@RequestParam("file") final MultipartFile file)
			throws IOException, ConfigurationException {
		LOGGER.info("Uploaded File: " + file.getName());
		return gerritConfigTranslator.updateConfigFromFile(file, gerritConfig);
	}

    @RequestMapping(method = RequestMethod.POST, value = "/changeConfig", produces = "application/json", consumes = "application/json")
    @ResponseBody
    public GerritConfig changeConfig(@RequestBody final TempGerritConfig newGerritConfig) throws ConfigurationException {
    	gerritConfig.replaceWith(newGerritConfig);
    	gerritConfig.setUsernameAndPassword(newGerritConfig.getUsername(), newGerritConfig.getPassword());
        return gerritConfig;
    }

}
