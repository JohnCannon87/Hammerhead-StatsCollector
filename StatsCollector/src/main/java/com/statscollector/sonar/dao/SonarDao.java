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
	private static final BasicNameValuePair METRICS_PAIR = new BasicNameValuePair("metrics",
			"function_complexity,file_complexity,ncloc,coverage,violations_density");

	@Autowired
	private SonarConfig sonarConfig;

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
	public String getAllChanges(final CredentialsProvider credsProvider) throws IOException, URISyntaxException {
		String resultString;

		try (CloseableHttpClient httpclient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build()) {
			URIBuilder baseURIBuilder = setupBaseURI(ALL_METRICS_URL);
			baseURIBuilder.addParameters(getAllChangesParameters());
			URI uri = baseURIBuilder.build();
			HttpGet httpGet = new HttpGet(uri);

			try (CloseableHttpResponse response = httpclient.execute(httpGet)) {
				HttpEntity httpEntity = response.getEntity();
				resultString = EntityUtils.toString(httpEntity);
				EntityUtils.consume(httpEntity);
			} catch (Exception e) {
				System.out.println("ERROR THROWN PROCESSING HTTP REQUEST: " + e.getMessage());
				throw e;
			}
		} catch (Exception e) {
			System.out.println("ERROR THROWN PROCESSING HTTP REQUEST: " + e.getMessage());
			throw e;
		}
		return resultString;
	}

	private List<NameValuePair> getAllChangesParameters() {
		List<NameValuePair> result = new ArrayList<>();
		result.add(METRICS_PAIR);
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

	@Override
	protected AbstractWebConfig getConfig() {
		return sonarConfig;
	}

	public void setConfig(final SonarConfig sonarConfig) {
		this.sonarConfig = sonarConfig;
	}

}

// http://ojpjenkins:9000/api/resources?metrics=function_complexity,file_complexity,ncloc,coverage,violations_density