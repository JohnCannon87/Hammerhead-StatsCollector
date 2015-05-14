package com.statscollector.sonar.service;

import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.statscollector.sonar.config.SonarConfig;
import com.statscollector.sonar.config.TempSonarConfig;

@Component
public class SonarConfigTranslator {

	public SonarConfig updateConfigFromFile(final MultipartFile file, final SonarConfig sonarConfig)
			throws JsonSyntaxException, JsonIOException, IOException {
		Gson gson = new GsonBuilder().create();
		TempSonarConfig newSonarConfig = gson.fromJson(new InputStreamReader(file.getInputStream()),
				TempSonarConfig.class);
		sonarConfig.replaceWith(newSonarConfig);
		return sonarConfig;
	}

}
