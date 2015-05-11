package com.statscollector.gerrit.service;

import java.io.IOException;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.statscollector.gerrit.authentication.AuthenticationHelper;
import com.statscollector.gerrit.dao.GerritDao;
import com.statscollector.gerrit.model.GerritChange;
import com.statscollector.gerrit.model.GerritChangeDetails;

/**
 * I'm a service that performs some business logic to access the information
 * requested from Gerrit.
 *
 * @author JCannon
 *
 */
@Service
public class GerritService {

	private static final String ID_REF = "id";

	private static final String CHANGE_ID_REF = "change_id";

	private static final String PROJECT_REF = "project";

	private static final String OWNER_REF = "owner";

	private static final String OWNER_NAME_REF = "name";

	private static final String CREATED_REF = "created";

	private static final String UPDATED_REF = "updated";

	private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

	private static final String LABELS_REF = "labels";

	private static final String CODE_REVIEW_REF = "Code-Review";

	private static final String ALL_REF = "all";

	private static final String USERNAME_REF = "username";

	private static final String VALUE_REF = "value";

	private static final String NUMBER_REF = "_number";

	private static final String TOPIC_REF = "topic";

	private static final String BRANCH_REF = "branch";

	@Autowired
	private GerritDao gerritDao;

	@Autowired
	private AuthenticationHelper authenticationHelper;

	final static Logger LOGGER = Logger.getLogger(GerritService.class);

	public List<GerritChange> getAllChanges(final String changeStatus) throws IOException, URISyntaxException {
		JsonParser jsonParser = new JsonParser();
		String allOpenChangesUnparsed = gerritDao.getAllChanges(authenticationHelper.createAuthenticationCredentials(),
				changeStatus);
		JsonElement allOpenChanges = jsonParser.parse(allOpenChangesUnparsed);
		return translateToChanges(allOpenChanges);
	}

	private List<GerritChange> translateToChanges(final JsonElement rawJsonElement) {
		if (rawJsonElement.isJsonArray()) {
			List<GerritChange> results = new ArrayList<GerritChange>();
			JsonArray rawJsonArray = rawJsonElement.getAsJsonArray();
			for (JsonElement jsonElement : rawJsonArray) {
				results.add(translateToChange(jsonElement));
			}
			return results;
		} else {
			throw new NotImplementedException();
		}
	}

	private GerritChange translateToChange(final JsonElement jsonElement) {
		if (jsonElement.isJsonObject()) {
			JsonObject rawJsonObject = jsonElement.getAsJsonObject();
			String id = rawJsonObject.get(ID_REF).getAsString();
			String changeId = rawJsonObject.get(CHANGE_ID_REF).getAsString();
			String project = rawJsonObject.get(PROJECT_REF).getAsString();
			String owner = rawJsonObject.get(OWNER_REF).getAsJsonObject().get(OWNER_NAME_REF).getAsString();
			String topic = "";
			try {
				topic = rawJsonObject.get(TOPIC_REF).getAsString();
			} catch (Exception e) {
				// No Topic Found, Ignore
			}
			String branch = rawJsonObject.get(BRANCH_REF).getAsString();
			DateTime created = parseDateTime(rawJsonObject.get(CREATED_REF).getAsString());
			DateTime updated = parseDateTime(rawJsonObject.get(UPDATED_REF).getAsString());
			return new GerritChange(id, changeId, project, owner, created, updated, topic, branch);
		}
		return null;
	}

	private DateTime parseDateTime(final String dateTimeString) {
		DateTimeFormatter pattern = DateTimeFormat.forPattern(DATE_TIME_PATTERN);
		return pattern.parseDateTime(dateTimeString.split("\\.")[0]);
	}

	public void setStatisticsDao(final GerritDao statisticsDao) {
		this.gerritDao = statisticsDao;
	}

	public void setAuthenticationHelper(final AuthenticationHelper authenticationHelper) {
		this.authenticationHelper = authenticationHelper;
	}

	public void populateChangeReviewers(final List<GerritChange> changes) throws IOException, URISyntaxException {
		Map<String, GerritChangeDetails> gerritChangeDetails = getGerritChangeDetails(changes);
		for (GerritChange gerritChange : changes) {
			GerritChangeDetails changeDetail = gerritChangeDetails.get(gerritChange.getChangeId());
			if (null != changeDetail) {
				gerritChange.setReviewers(changeDetail.getReviewers());
				gerritChange.setChangeNumber(changeDetail.getChangeNumber());
			}
		}
	}

	public Map<String, GerritChangeDetails> getGerritChangeDetails(final List<GerritChange> changes)
			throws JsonSyntaxException, IOException, URISyntaxException {
		JsonParser jsonParser = new JsonParser();
		HashMap<String, GerritChangeDetails> result = new HashMap<>();
		for (GerritChange gerritChange : changes) {
			String changeId = gerritChange.getChangeId();
			JsonReader jsonReader = new JsonReader(new StringReader(gerritDao.getDetails(
					authenticationHelper.createAuthenticationCredentials(), changeId)));
			jsonReader.setLenient(true);
			try {
				result.put(changeId, translateToDetails(jsonParser.parse(jsonReader)));
			} catch (Exception e) {
				LOGGER.error("Error found in processing changeId: " + changeId, e);
			}
		}
		return result;
	}

	private GerritChangeDetails translateToDetails(final JsonElement detailsJson) {
		GerritChangeDetails gerritChangeDetails = new GerritChangeDetails();
		JsonElement labelsElement = detailsJson.getAsJsonObject().get(LABELS_REF);
		JsonElement codeReviewElement = labelsElement.getAsJsonObject().get(CODE_REVIEW_REF);
		JsonElement allReference = codeReviewElement.getAsJsonObject().get(ALL_REF);
		if (allReference != null) {
			JsonArray allArray = allReference.getAsJsonArray();
			for (JsonElement jsonElement : allArray) {
				JsonObject review = jsonElement.getAsJsonObject();
				if (null != review) {
					String username = review.get(USERNAME_REF).getAsString();
					Integer reviewValue = null;
					if (review.get(VALUE_REF) != null) {
						reviewValue = review.get(VALUE_REF).getAsInt();
					}
					gerritChangeDetails.addReviewer(username, reviewValue);
				}
			}
		}
		gerritChangeDetails.setChangeNumber(detailsJson.getAsJsonObject().get(NUMBER_REF).getAsInt());
		return gerritChangeDetails;
	}

}
