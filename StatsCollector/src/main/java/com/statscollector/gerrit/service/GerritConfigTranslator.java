package com.statscollector.gerrit.service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.statscollector.gerrit.config.GerritConfig;

@Component
public class GerritConfigTranslator {

	private static final String VERSION_1_STRING = "1.0";
	private static final String HOST_KEY = "host";
	private static final String NO_PEER_REVIEW_TARGET_KEY = "noPeerReviewTarget";
	private static final String ONE_PEER_REVIEW_TARGET_KEY = "onePeerReviewTarget";
	private static final String TWO_PEER_REVIEW_TARGET_KEY = "twoPeerReviewTarget";
	private static final String COLLABORATIVE_REVIEW_TARGET_KEY = "collaborativeReviewTarget";
	private static final String PASSWORD_KEY = "password";
	private static final String USERNAME_KEY = "username";
	private static final String REVIEWERS_TO_IGNORE_KEY = "reviewersToIgnore";
	private static final String CONFIG_VERSION_KEY = "configVersion";

	public GerritConfig updateConfigFromFile(final MultipartFile file, final GerritConfig gerritConfig)
			throws IOException, ConfigurationException {
		StringWriter writer = new StringWriter();
		IOUtils.copy(file.getInputStream(), writer);
		String value = writer.toString();
		JsonElement root = new JsonParser().parse(value);
		JsonObject rootObject = root.getAsJsonObject();
		String versionString = rootObject.get(CONFIG_VERSION_KEY).getAsString();

		switch (versionString) {
		case VERSION_1_STRING:
			return translateV1ConfigFile(rootObject, gerritConfig);
		default:
			return null;
		}
	}

	private GerritConfig translateV1ConfigFile(final JsonObject rootObject, final GerritConfig gerritConfig)
			throws ConfigurationException {
		gerritConfig.setHost(rootObject.get(HOST_KEY).getAsString());
		gerritConfig.setReviewersToIgnore(convertArrayToList(rootObject.get(REVIEWERS_TO_IGNORE_KEY).getAsJsonArray()));
		gerritConfig.setTargets(Float.valueOf(rootObject.get(NO_PEER_REVIEW_TARGET_KEY).getAsString()),
				Float.valueOf(rootObject.get(ONE_PEER_REVIEW_TARGET_KEY).getAsString()),
				Float.valueOf(rootObject.get(TWO_PEER_REVIEW_TARGET_KEY).getAsString()),
				Float.valueOf(rootObject.get(COLLABORATIVE_REVIEW_TARGET_KEY).getAsString()));
		gerritConfig.setUsernameAndPassword(rootObject.get(USERNAME_KEY).getAsString(), rootObject.get(PASSWORD_KEY)
				.getAsString());
		return gerritConfig;
	}

	private List<String> convertArrayToList(final JsonArray jsonArray) {
		List<String> result = new ArrayList<>();
		for (JsonElement jsonElement : jsonArray) {
			result.add(jsonElement.getAsString());
		}
		return result;
	}

}
