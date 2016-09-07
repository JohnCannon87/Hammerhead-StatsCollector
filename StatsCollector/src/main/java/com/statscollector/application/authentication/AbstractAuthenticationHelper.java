package com.statscollector.application.authentication;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;

import com.statscollector.application.config.WebConfig;

/**
 * I am an abstract class that provides common authentication methods for an
 * extending class
 *
 * @author JCannon
 *
 */
public abstract class AbstractAuthenticationHelper {

    private CredentialsProvider credsProvider;
    private boolean credsProviderCreated = false;

    private static final Logger LOGGER = Logger.getLogger(AbstractAuthenticationHelper.class);

    @Bean
    public CredentialsProvider credentialsProvider() {
        try {
            if(!credsProviderCreated || credentialsHaveChanged(credsProvider)) {
                WebConfig config = getConfig();
                credsProvider = new BasicCredentialsProvider();
                credsProvider.setCredentials(AuthScope.ANY,
                        new UsernamePasswordCredentials(config.getUsername(), config.getActualPassword()));
                credsProviderCreated = true;
            }
        } catch(IllegalArgumentException e) {
            LOGGER.error(e);
        }
        return credsProvider;
    }

    private boolean credentialsHaveChanged(final CredentialsProvider credsProvider2) {
        if(null == credsProvider2) {
            return true;
        } else {
            WebConfig config = getConfig();
            Credentials credentials = credsProvider2.getCredentials(new AuthScope(config.getHost(), config
                    .getHostPort()));
            if(null == credentials) {
                // No credentials found auth scope must have
                // changed.
                return true;
            } else if(null == credentials.getPassword()) {
                return true;
            } else {
                boolean passwordSame = credentials.getPassword().equals(config.getActualPassword());
                boolean usernameSame = credentials.getUserPrincipal().getName().equals(config.getUsername());
                return !passwordSame || !usernameSame;
            }
        }
    }

    protected abstract WebConfig getConfig();

}
