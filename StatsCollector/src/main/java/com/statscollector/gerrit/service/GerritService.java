package com.statscollector.gerrit.service;

import java.io.StringReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gerrit.extensions.common.ChangeInfo;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.statscollector.gerrit.authentication.GerritAuthenticationHelper;
import com.statscollector.gerrit.dao.GerritDao;
import com.statscollector.gerrit.model.ConnectionTestResults;
import com.statscollector.gerrit.serialization.TimestampDeserializer;

/**
 * I'm a service that performs some business logic to access the information
 * requested from Gerrit.
 *
 * @author JCannon
 *
 */
@Service
public class GerritService {

    @Autowired
    private GerritDao gerritDao;

    @Autowired
    private GerritAuthenticationHelper authenticationHelper;

    final static Logger LOGGER = Logger.getLogger(GerritService.class);

    public ConnectionTestResults<ChangeInfo> testConnection() {
        StringBuilder connectionDetails = new StringBuilder();
        JsonParser jsonParser = new JsonParser();
        Credentials credentials = authenticationHelper.credentialsProvider().getCredentials(AuthScope.ANY);
        connectionDetails.append("Attempting Connection With Username: "
                + credentials.getUserPrincipal().getName() + " and Password: "
                + credentials.getPassword());
        String connectionResult = "";
        try {
            connectionResult = gerritDao.testConnection(authenticationHelper.credentialsProvider());
            JsonElement resultJson = jsonParser.parse(connectionResult);
            List<ChangeInfo> result = translateToChanges(resultJson);
            return new ConnectionTestResults<ChangeInfo>(connectionDetails.toString(), result, null);
        } catch(JsonSyntaxException e) {
            return new ConnectionTestResults<ChangeInfo>(connectionDetails.toString(), null, connectionResult);
        } catch(Exception e) {
            return new ConnectionTestResults<ChangeInfo>(connectionDetails.toString(), null, e.toString());
        }
    }

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
        if(rawJsonElement.isJsonArray()) {
            JsonArray rawJsonArray = rawJsonElement.getAsJsonArray();
            for(JsonElement jsonElement : rawJsonArray) {
                results.add(translateToChange(jsonElement));
            }
            return results;
        } else {
            LOGGER.error("Failed To Parse JSON: " + rawJsonElement.toString());
            return results;
        }
    }

    private ChangeInfo translateToChange(final JsonElement jsonElement) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Timestamp.class, new TimestampDeserializer())
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        ChangeInfo result = gson.fromJson(jsonElement, ChangeInfo.class);
        return result;
    }

    public void setStatisticsDao(final GerritDao statisticsDao) {
        this.gerritDao = statisticsDao;
    }

    public void setAuthenticationHelper(final GerritAuthenticationHelper authenticationHelper) {
        this.authenticationHelper = authenticationHelper;
    }

    public List<ChangeInfo> populateChangeReviewers(final List<ChangeInfo> changes) throws Exception {
        JsonParser jsonParser = new JsonParser();
        List<ChangeInfo> result = new ArrayList<>();
        for(ChangeInfo gerritChange : changes) {
            String changeId = gerritChange.changeId;
            JsonReader jsonReader = new JsonReader(new StringReader(gerritDao.getDetails(
                    authenticationHelper.credentialsProvider(), changeId)));
            jsonReader.setLenient(true);
            try {
                result.add(translateToChange(jsonParser.parse(jsonReader)));
            } catch(Exception e) {
                // LOGGER.error("Error found in processing changeId: " +
                // changeId, e);
            }
        }
        return result;
    }
}
