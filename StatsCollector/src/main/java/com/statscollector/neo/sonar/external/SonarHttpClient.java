package com.statscollector.neo.sonar.external;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.statscollector.application.config.AbstractWebConfig;
import com.statscollector.application.dao.AbstractWebDao;
import com.statscollector.neo.sonar.authentication.SonarAuthenticationHelper;
import com.statscollector.neo.sonar.config.SonarConfig;
import com.statscollector.neo.sonar.model.RawSonarMetrics;
import com.statscollector.neo.sonar.model.RawSonarProject;

/**
 * I am a service for accessing Sonar information.
 */
@Service
public class SonarHttpClient extends AbstractWebDao {

    private static final Logger LOGGER = Logger.getLogger(SonarHttpClient.class);

    private static final String HTTP_SCHEME = "http";
    private static final BasicNameValuePair METRICS_PAIR = new BasicNameValuePair(
            "metrics",
            "ncloc,complexity,files,functions,blocker_violations,critical_violations,major_violations,minor_violations,info_violations,lines_to_cover,uncovered_lines,function_complexity_distribution,file_complexity_distribution");
    private static final String PROJECTS_URL = "/api/projects/index";
    private static final String TIME_MACHINE_URL = "/api/timemachine";
    private static final String FROM_DATE_KEY = "fromDateTime";
    private static final String END_DATE_KEY = "toDateTime";
    private static final DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendYear(4, 4)
            .appendLiteral("-").appendMonthOfYear(2).appendLiteral("-").appendDayOfMonth(2).toFormatter();
    private static final String RESOURCE_KEY = "resource";

    @Autowired
    private SonarConfig sonarConfig;

    @Autowired
    private SonarAuthenticationHelper sonarAuthenticationHelper;

    /**
     * I return a list of available project names from sonar.
     *
     * @return
     */
    public List<RawSonarProject> getProjectNames() {
        CredentialsProvider credsProvider = sonarAuthenticationHelper.credentialsProvider();
        String rawProjectValuesString = null;
        try (CloseableHttpClient httpclient = HttpClients.custom()
                .setDefaultCredentialsProvider(credsProvider)
                .build()) {
            HttpHost httpHost = new HttpHost(sonarConfig.getHost(), sonarConfig.getHostPort(), "http");
            BasicScheme basicScheme = new BasicScheme();
            BasicAuthCache authCache = new BasicAuthCache();
            authCache.put(httpHost, basicScheme);

            HttpClientContext context = HttpClientContext.create();
            context.setCredentialsProvider(credsProvider);
            context.setAuthCache(authCache);

            URIBuilder baseURIBuilder = setupBaseURI(PROJECTS_URL);
            URI uri = baseURIBuilder.build();
            HttpGet httpGet = new HttpGet(uri);
            rawProjectValuesString = processHttpGet(httpclient, context, httpGet);
        } catch(IOException | URISyntaxException e) {
            LOGGER.error("Error thrown refreshing Sonar Stats", e);
        }
        Type listType = new TypeToken<ArrayList<RawSonarProject>>() {}.getType();
        List<RawSonarProject> sonarProjects = new Gson().fromJson(rawProjectValuesString, listType);
        return sonarProjects;
    }

    /**
     * I return the raw string for all metrics for the provided project name.
     *
     * @param credsProvider
     * @param projectName
     * @return rawString
     * @throws Exception
     */
    public RawSonarMetrics getProjectValuesFromStartOfTimeToNow(final String projectName) {
        CredentialsProvider credsProvider = sonarAuthenticationHelper.credentialsProvider();
        String rawProjectValuesString = null;
        try (CloseableHttpClient httpclient = HttpClients.custom()
                .setDefaultCredentialsProvider(credsProvider)
                .build()) {
            HttpHost httpHost = new HttpHost(sonarConfig.getHost(), sonarConfig.getHostPort(), "http");
            BasicScheme basicScheme = new BasicScheme();
            BasicAuthCache authCache = new BasicAuthCache();
            authCache.put(httpHost, basicScheme);

            HttpClientContext context = HttpClientContext.create();
            context.setCredentialsProvider(credsProvider);
            context.setAuthCache(authCache);

            URIBuilder baseURIBuilder = setupBaseURI(TIME_MACHINE_URL);
            baseURIBuilder.addParameters(getDateWindowParameters(projectName));
            URI uri = baseURIBuilder.build();
            HttpGet httpGet = new HttpGet(uri);
            rawProjectValuesString = processHttpGet(httpclient, context, httpGet);
        } catch(IOException | URISyntaxException e) {
            LOGGER.error("Error thrown refreshing Sonar Stats", e);
        }

        Type listType = new TypeToken<ArrayList<RawSonarMetrics>>() {}.getType();
        List<RawSonarMetrics> rawSonarMetrics = new Gson().fromJson(rawProjectValuesString, listType);
        return rawSonarMetrics.get(0);
    }

    /**
     * @param httpclient
     * @param context
     * @param httpGet
     * @throws Exception
     */
    private String processHttpGet(final CloseableHttpClient httpclient, final HttpClientContext context,
            final HttpGet httpGet) {
        String rawProjectValuesString = null;
        try (CloseableHttpResponse response = httpclient.execute(httpGet, context)) {
            HttpEntity httpEntity = response.getEntity();
            rawProjectValuesString = EntityUtils.toString(httpEntity);
            EntityUtils.consume(httpEntity);
        } catch(IOException e) {
            LOGGER.error("Error thrown refreshing Sonar Stats", e);
        }
        return rawProjectValuesString;
    }

    private List<NameValuePair> getDateWindowParameters(final String resourceKey) {
        BasicNameValuePair fromDateParam = new BasicNameValuePair(FROM_DATE_KEY, formatDateOnly(new DateTime(0)));
        BasicNameValuePair endDateParam = new BasicNameValuePair(END_DATE_KEY, formatDateOnly(DateTime.now()));
        BasicNameValuePair resourceParam = new BasicNameValuePair(RESOURCE_KEY, resourceKey);
        List<NameValuePair> result = new ArrayList<>();
        result.add(METRICS_PAIR);
        result.add(fromDateParam);
        result.add(endDateParam);
        result.add(resourceParam);
        return result;
    }

    /**
     * I return a URIBuilder with the default http scheme and host and the
     * provided request url
     *
     * @param status
     * @param requestUrl
     * @return
     * @throws URISyntaxException
     */
    private URIBuilder setupBaseURI(final String requestUrl) throws URISyntaxException {
        return new URIBuilder().setScheme(HTTP_SCHEME).setHost(getTargetHost()).setPath(requestUrl);

    }

    private String formatDateOnly(final DateTime date) {
        return formatter.print(date.getMillis());
    }

    @Override
    protected AbstractWebConfig getConfig() {
        return sonarConfig;
    }

}
