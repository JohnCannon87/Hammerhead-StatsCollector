package com.statscollector.sonar.dao;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.statscollector.application.config.AbstractWebConfig;
import com.statscollector.application.dao.AbstractWebDao;
import com.statscollector.sonar.config.SonarConfig;

/**
 * I'm a DAO (actually more of a http client) that accesses Sonar data using
 * simple http requests.
 *
 * @author JCannon
 *
 */
@Repository
public class SonarDao extends AbstractWebDao {

	private static final String HTTP_SCHEME = "http";
	private static final String ALL_METRICS_URL = "/api/resources";
	private static final BasicNameValuePair METRICS_PAIR = new BasicNameValuePair(
			"metrics",
			"function_complexity,file_complexity,ncloc,coverage,violations_density,files,functions,blocker_violations,critical_violations,major_violations,minor_violations");
	private static final String TIME_MACHINE_URL = "/api/timemachine";
	private static final String FROM_DATE_KEY = "fromDateTime";
	private static final String END_DATE_KEY = "toDateTime";
	private static final DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendYear(4, 4)
			.appendLiteral("-").appendMonthOfYear(2).appendLiteral("-").appendDayOfMonth(2).toFormatter();
	private static final String RESOURCE_KEY = "resource";

	@Autowired
	private SonarConfig sonarConfig;

	final static Logger LOGGER = Logger.getLogger(SonarDao.class);

	/**
	 * I Return a String containing all changes for all projects, that have the
	 * provided status, I require a username and password passed in as a
	 * credentials provider, see:
	 *
	 * https://hc.apache.org/httpcomponents-client-ga/httpclient
	 * /examples/org/apache/http/examples/client/ClientAuthentication.java
	 *
	 * For an example
	 *
	 * @param credsProvider
	 * @return result
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public String getLatestStats(final CredentialsProvider credsProvider) throws IOException, URISyntaxException {
		String resultString;

		try (CloseableHttpClient httpclient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build()) {
			URIBuilder baseURIBuilder = setupBaseURI(ALL_METRICS_URL);
			baseURIBuilder.addParameters(getLatestStatsParameters());
			URI uri = baseURIBuilder.build();
			HttpGet httpGet = new HttpGet(uri);

			try (CloseableHttpResponse response = httpclient.execute(httpGet)) {
				HttpEntity httpEntity = response.getEntity();
				resultString = EntityUtils.toString(httpEntity);
				EntityUtils.consume(httpEntity);
			} catch (Exception e) {
				LOGGER.error("ERROR THROWN PROCESSING HTTP REQUEST: " + e.getMessage());
				throw e;
			}
		} catch (Exception e) {
			LOGGER.error("ERROR THROWN PROCESSING HTTP REQUEST: " + e.getMessage());
			throw e;
		}
		return resultString;
	}

	private List<NameValuePair> getLatestStatsParameters() {
		List<NameValuePair> result = new ArrayList<>();
		result.add(METRICS_PAIR);
		return result;
	}

	/**
	 * I Return a String containing all changes for all projects, that have the
	 * provided status, I require a username and password passed in as a
	 * credentials provider, see:
	 *
	 * https://hc.apache.org/httpcomponents-client-ga/httpclient
	 * /examples/org/apache/http/examples/client/ClientAuthentication.java
	 *
	 * For an example
	 *
	 * @param credsProvider
	 * @return result
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public String getStatsForDateWindow(final CredentialsProvider credsProvider, final Interval interval,
			final String resourceKey) throws IOException, URISyntaxException {
		String resultString;

		try (CloseableHttpClient httpclient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build()) {
			URIBuilder baseURIBuilder = setupBaseURI(TIME_MACHINE_URL);
			baseURIBuilder.addParameters(getDateWindowParameters(interval, resourceKey));
			URI uri = baseURIBuilder.build();
			HttpGet httpGet = new HttpGet(uri);

			try (CloseableHttpResponse response = httpclient.execute(httpGet)) {
				HttpEntity httpEntity = response.getEntity();
				resultString = EntityUtils.toString(httpEntity);
				EntityUtils.consume(httpEntity);
			} catch (Exception e) {
				LOGGER.error("ERROR THROWN PROCESSING HTTP REQUEST: " + e.getMessage());
				throw e;
			}
		} catch (Exception e) {
			LOGGER.error("ERROR THROWN PROCESSING HTTP REQUEST: " + e.getMessage());
			throw e;
		}
		return resultString;
	}

	private List<NameValuePair> getDateWindowParameters(final Interval interval, final String resourceKey) {
		BasicNameValuePair fromDateParam = new BasicNameValuePair(FROM_DATE_KEY, formatDateOnly(interval.getStart()));
		BasicNameValuePair endDateParam = new BasicNameValuePair(END_DATE_KEY, formatDateOnly(interval.getEnd()));
		BasicNameValuePair resourceParam = new BasicNameValuePair(RESOURCE_KEY, resourceKey);
		List<NameValuePair> result = new ArrayList<>();
		result.add(METRICS_PAIR);
		result.add(fromDateParam);
		result.add(endDateParam);
		result.add(resourceParam);
		return result;
	}

	private String formatDateOnly(final DateTime date) {
		return formatter.print(date.getMillis());
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

	@Override
	protected AbstractWebConfig getConfig() {
		return sonarConfig;
	}

	public void setConfig(final SonarConfig sonarConfig) {
		this.sonarConfig = sonarConfig;
	}

}

// http://ojpjenkins:9000/api/resources?metrics=function_complexity,file_complexity,ncloc,coverage,violations_density