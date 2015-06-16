package com.statscollector.gerrit.service;

import java.io.StringReader;
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

import com.google.gerrit.extensions.common.ChangeInfo;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.statscollector.gerrit.authentication.GerritAuthenticationHelper;
import com.statscollector.gerrit.dao.GerritDao;

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
	private GerritAuthenticationHelper authenticationHelper;

	final static Logger LOGGER = Logger.getLogger(GerritService.class);

	public List<ChangeInfo> getAllChanges(final String changeStatus) throws Exception {
		JsonParser jsonParser = new JsonParser();
		String allOpenChangesUnparsed = gerritDao.getAllChanges(authenticationHelper.credentialsProvider(),
				changeStatus);
		// LOGGER.info(allOpenChangesUnparsed);
		JsonElement allOpenChanges = jsonParser.parse(allOpenChangesUnparsed);
		return translateToChanges(allOpenChanges);
	}

	private List<ChangeInfo> translateToChanges(final JsonElement rawJsonElement) {
		List<ChangeInfo> results = new ArrayList<ChangeInfo>();
		if (rawJsonElement.isJsonArray()) {
			JsonArray rawJsonArray = rawJsonElement.getAsJsonArray();
			for (JsonElement jsonElement : rawJsonArray) {
				results.add(translateToChange(jsonElement));
			}
			return results;
		} else {
			LOGGER.error("Failed To Parse JSON: " + rawJsonElement.toString());
			return results;
		}
	}

	private ChangeInfo translateToChange(final JsonElement jsonElement) {
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
				.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
		ChangeInfo result = gson.fromJson(jsonElement, ChangeInfo.class);
		return result;
		// if (jsonElement.isJsonObject()) {
		// JsonObject rawJsonObject = jsonElement.getAsJsonObject();
		// String id = rawJsonObject.get(ID_REF).getAsString();
		// String changeId = rawJsonObject.get(CHANGE_ID_REF).getAsString();
		// String project = rawJsonObject.get(PROJECT_REF).getAsString();
		// String owner =
		// rawJsonObject.get(OWNER_REF).getAsJsonObject().get(OWNER_NAME_REF).getAsString();
		// String topic = "";
		// try {
		// topic = rawJsonObject.get(TOPIC_REF).getAsString();
		// } catch (Exception e) {
		// // No Topic Found, Ignore
		// }
		// String branch = rawJsonObject.get(BRANCH_REF).getAsString();
		// DateTime created =
		// parseDateTime(rawJsonObject.get(CREATED_REF).getAsString());
		// DateTime updated =
		// parseDateTime(rawJsonObject.get(UPDATED_REF).getAsString());
		// return new GerritChange(id, changeId, project, owner, created,
		// updated, topic, branch);
		// }
		// return null;
	}

	private DateTime parseDateTime(final String dateTimeString) {
		DateTimeFormatter pattern = DateTimeFormat.forPattern(DATE_TIME_PATTERN);
		return pattern.parseDateTime(dateTimeString.split("\\.")[0]);
	}

	public void setStatisticsDao(final GerritDao statisticsDao) {
		this.gerritDao = statisticsDao;
	}

	public void setAuthenticationHelper(final GerritAuthenticationHelper authenticationHelper) {
		this.authenticationHelper = authenticationHelper;
	}

	public void populateChangeReviewers(final List<ChangeInfo> changes) throws Exception {
		Map<String, ChangeInfo> gerritChangeDetails = getGerritChangeDetails(changes);
	}

	public Map<String, ChangeInfo> getGerritChangeDetails(final List<ChangeInfo> changes) throws Exception {
		JsonParser jsonParser = new JsonParser();
		HashMap<String, ChangeInfo> result = new HashMap<>();
		for (ChangeInfo gerritChange : changes) {
			String changeId = gerritChange.changeId;
			JsonReader jsonReader = new JsonReader(new StringReader(gerritDao.getDetails(
					authenticationHelper.credentialsProvider(), changeId)));
			jsonReader.setLenient(true);
			try {
				result.put(changeId, translateToChange(jsonParser.parse(jsonReader)));
			} catch (Exception e) {
				// LOGGER.error("Error found in processing changeId: " +
				// changeId, e);
			}
		}
		return result;
	}

}
