package com.statscollector.gerrit.dao;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.auth.DigestScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.statscollector.application.config.AbstractWebConfig;
import com.statscollector.application.dao.AbstractWebDao;
import com.statscollector.gerrit.config.GerritConfig;

/**
 * I'm a DAO (actually more of a http client) that accesses Gerrit data using
 * simple http requests.
 *
 * @author JCannon
 *
 */
@Repository
public class GerritDao extends AbstractWebDao {

	private static final String HTTP_SCHEME = "http";
	private static final String ALL_CHANGES_REST_URL = "/a/changes/";
	private final static String QUERY = "q";
	private static final String DETAIL_URL_END = "/detail";
	private static final String BASE_STATUS_STRING = "status:";
	private static final Object UNAUTHORIZED = "Unauthorized";
	private static final String AUTHENTICATION_HEADER = "WWW-Authenticate";
	private static final String NONCE_KEY = "nonce";
	private static final String REALM_KEY = "Digest realm";

	@Autowired
	private GerritConfig gerritConfig;

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
	 * @param changeStatus
	 * @return result
	 * @throws Exception
	 */
	public String getAllChanges(final CredentialsProvider credsProvider, final String changeStatus) throws Exception {
		String resultString;

		try (CloseableHttpClient httpclient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build()) {
			URIBuilder baseURIBuilder = setupBaseURI(ALL_CHANGES_REST_URL);
			baseURIBuilder.setParameter(QUERY, BASE_STATUS_STRING + changeStatus);
			URI uri = baseURIBuilder.build();
			HttpGet httpGet = new HttpGet(uri);
			try (CloseableHttpResponse response = httpclient.execute(httpGet)) {
				HttpEntity httpEntity = response.getEntity();
				resultString = EntityUtils.toString(httpEntity);
				if (resultString.equals(UNAUTHORIZED)) {
					resultString = processResponseWithDigestAuthentication(credsProvider, getConfig().getHost(),
							getConfig().getHostPort(), response, changeStatus);
				}
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

	private String processResponseWithDigestAuthentication(final CredentialsProvider credsProvider, final String host,
			final int port, final CloseableHttpResponse notAuthorizedResponse, final String changeStatus)
					throws Exception {
		String resultString;
		HttpHost httpHost = new HttpHost(host, port, "http");

		Map<String, String> authenticationElementsMap = getAuthenticationElements(notAuthorizedResponse);
		String nonceValue = authenticationElementsMap.get(NONCE_KEY);
		String realmValue = authenticationElementsMap.get(REALM_KEY);

		DigestScheme digestScheme = new DigestScheme();
		digestScheme.overrideParamter(REALM_KEY, realmValue);
		digestScheme.overrideParamter(NONCE_KEY, nonceValue);

		BasicAuthCache authCache = new BasicAuthCache();
		authCache.put(httpHost, digestScheme);

		HttpClientContext context = HttpClientContext.create();
		context.setCredentialsProvider(credsProvider);
		context.setAuthCache(authCache);

		URIBuilder baseURIBuilder = setupBaseURI(ALL_CHANGES_REST_URL);
		baseURIBuilder.setParameter(QUERY, BASE_STATUS_STRING + changeStatus);
		URI uri = baseURIBuilder.build();
		HttpGet httpGet = new HttpGet(uri);

		System.out.println("Http Get: " + httpGet.getRequestLine());

		try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
			try (CloseableHttpResponse response = httpclient.execute(httpHost, httpGet, context)) {
				HttpEntity httpEntity = response.getEntity();
				resultString = EntityUtils.toString(httpEntity);
				if (resultString.equals(UNAUTHORIZED)) {
					return "Failed to authenticate Digest";
				}
				EntityUtils.consume(httpEntity);
			} catch (Exception e) {
				System.out.println("ERROR THROWN PROCESSING HTTP REQUEST: " + e.getMessage());
				throw e;
			}
		} catch (Exception e) {
			System.out.println("ERROR THROWN PROCESSING HTTP REQUEST: " + e.getMessage());
			throw e;
		}

		return null;
	}

	private Map<String, String> getAuthenticationElements(final CloseableHttpResponse response) {
		Header[] authenticationHeaders = response.getHeaders(AUTHENTICATION_HEADER);
		if (authenticationHeaders.length != 1) {
			throw new RuntimeException("Cannot handle multiple http authentication headers!");
		}
		Header authenticationHeader = authenticationHeaders[0];
		HeaderElement[] authenticationElements = authenticationHeader.getElements();
		Map<String, String> authenticationElementsMap = new HashMap<>();
		for (HeaderElement headerElement : authenticationElements) {
			authenticationElementsMap.put(headerElement.getName(), headerElement.getValue());
		}
		return authenticationElementsMap;
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

	public String getDetails(final CredentialsProvider credsProvider, final String changeId) throws IOException,
	URISyntaxException {
		String resultString;

		try (CloseableHttpClient httpclient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build()) {
			URIBuilder baseURIBuilder = setupBaseURI(ALL_CHANGES_REST_URL + changeId + DETAIL_URL_END);
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

	public void setGerritConfig(final GerritConfig gerritConfig) {
		this.gerritConfig = gerritConfig;
	}

	@Override
	protected AbstractWebConfig getConfig() {
		return gerritConfig;
	}
}
