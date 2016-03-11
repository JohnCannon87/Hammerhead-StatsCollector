package com.statscollector.gerrit.dao;

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
import org.apache.log4j.Logger;
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

    final static Logger LOGGER = Logger.getLogger(GerritDao.class);

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
        URIBuilder baseURIBuilder = setupBaseURI(ALL_CHANGES_REST_URL);
        baseURIBuilder.setParameter(QUERY, BASE_STATUS_STRING + changeStatus);
        URI uri = baseURIBuilder.build();

        return makeHttpRequest(credsProvider, uri);
    }

    public String getDetails(final CredentialsProvider credsProvider, final String changeId) throws Exception {
        URIBuilder baseURIBuilder = setupBaseURI(ALL_CHANGES_REST_URL + changeId + DETAIL_URL_END);
        URI uri = baseURIBuilder.build();

        return makeHttpRequest(credsProvider, uri);
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

    private String makeHttpRequest(final CredentialsProvider credsProvider, final URI uri) throws Exception {
        try (CloseableHttpClient httpclient = HttpClients.custom().build()) {

            HttpGet httpGet = new HttpGet(uri);
            try (CloseableHttpResponse response = httpclient.execute(httpGet)) {
                HttpEntity httpEntity = response.getEntity();
                String resultString = EntityUtils.toString(httpEntity);
                if(resultString.equals(UNAUTHORIZED)) {
                    resultString = processResponseWithDigestAuthentication(credsProvider, getConfig().getHost(),
                            getConfig().getHostPort(), response, uri);
                    // LOGGER.info("INFO: AUTH FAILED RETURN STRING WAS: " + resultString);
                }
                EntityUtils.consume(httpEntity);
                return resultString;
            } catch(Exception e) {
                LOGGER.error("ERROR THROWN PROCESSING HTTP REQUEST: " + e.getMessage());
                throw e;
            }
        } catch(Exception e) {
            LOGGER.error("ERROR THROWN PROCESSING HTTP REQUEST: " + e.getMessage());
            throw e;
        }
    }

    private String processResponseWithDigestAuthentication(final CredentialsProvider credsProvider, final String host,
            final int port, final CloseableHttpResponse notAuthorizedResponse, final URI uri) throws Exception {
        HttpHost httpHost = new HttpHost(host, port, "http");

        Map<String, String> authenticationElementsMap = getAuthenticationElements(notAuthorizedResponse);
        String nonceValue = authenticationElementsMap.get(NONCE_KEY);
        String realmValue = authenticationElementsMap.get(REALM_KEY);

        DigestScheme digestScheme = new DigestScheme();
        digestScheme.overrideParamter(REALM_KEY, realmValue);
        digestScheme.overrideParamter(NONCE_KEY, nonceValue);

        if(realmValue.isEmpty()) {
            LOGGER.error("WARNING NO REALM VALUE");
        }

        BasicAuthCache authCache = new BasicAuthCache();
        authCache.put(httpHost, digestScheme);

        HttpClientContext context = HttpClientContext.create();
        context.setCredentialsProvider(credsProvider);
        context.setAuthCache(authCache);

        HttpGet httpGet = new HttpGet(uri);

        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            try (CloseableHttpResponse response = httpclient.execute(httpHost, httpGet, context)) {
                HttpEntity httpEntity = response.getEntity();
                String resultString = EntityUtils.toString(httpEntity);
                if(resultString.equals(UNAUTHORIZED)) {
                    LOGGER.error("Failed To Authenticate With Digest Auth, Giving Up !, Result String Was: "
                            + resultString);
                    return "Failed to authenticate Digest";
                }
                EntityUtils.consume(httpEntity);
                return resultString;
            } catch(Exception e) {
                LOGGER.error("ERROR THROWN PROCESSING HTTP REQUEST: " + e.getMessage());
                throw e;
            }
        } catch(Exception e) {
            LOGGER.error("ERROR THROWN PROCESSING HTTP REQUEST: " + e.getMessage());
            throw e;
        }
    }

    private Map<String, String> getAuthenticationElements(final CloseableHttpResponse response) {
        Header[] authenticationHeaders = response.getHeaders(AUTHENTICATION_HEADER);
        if(authenticationHeaders.length != 1) {
            throw new RuntimeException("Cannot handle multiple http authentication headers!");
        }
        Header authenticationHeader = authenticationHeaders[0];
        HeaderElement[] authenticationElements = authenticationHeader.getElements();
        Map<String, String> authenticationElementsMap = new HashMap<>();
        for(HeaderElement headerElement : authenticationElements) {
            authenticationElementsMap.put(headerElement.getName(), headerElement.getValue());
        }
        return authenticationElementsMap;
    }

    public void setGerritConfig(final GerritConfig gerritConfig) {
        this.gerritConfig = gerritConfig;
    }

    @Override
    protected AbstractWebConfig getConfig() {
        return gerritConfig;
    }
}
