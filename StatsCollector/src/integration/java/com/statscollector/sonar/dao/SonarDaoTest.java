package com.statscollector.sonar.dao;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.joda.time.DateMidnight;
import org.joda.time.Interval;
import org.joda.time.ReadableInstant;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.statscollector.sonar.config.SonarConfig;

public class SonarDaoTest {

    private static final ReadableInstant JAN_01_2015 = new DateMidnight().withYear(2015).withMonthOfYear(1)
            .withDayOfMonth(1);
    private static final ReadableInstant JAN_31_2015 = new DateMidnight().withYear(2015).withMonthOfYear(1)
            .withDayOfMonth(31);
    private final SonarDao sonarDao = new SonarDao();
    private CredentialsProvider credsProvider;

    @Before
    public void setUp() throws Exception {
        credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(new AuthScope("sonar.ojp.gtsdevlan.com", 9000), new UsernamePasswordCredentials(
                "jcannon",
                "testpassword"));
        SonarConfig sonarConfig = Mockito.mock(SonarConfig.class);
        Mockito.when(sonarConfig.getHost()).thenReturn("sonar.ojp.gtsdevlan.com");
        Mockito.when(sonarConfig.getHostPort()).thenReturn(9000);
        sonarDao.setConfig(sonarConfig);
    }

    @Test
    public void testGetAllChanges() throws IOException, URISyntaxException {
        String allChanges = sonarDao.getLatestStats(credsProvider);
        System.out.println(allChanges);
    }

    @Test
    public void testGetStatsForDateWindow() throws IOException, URISyntaxException {
        Interval interval = new Interval(JAN_01_2015, JAN_31_2015);
        String statsForDateWindow = sonarDao.getStatsForDateWindow(credsProvider, interval, "sonar:OjpAlertsEngine");
        System.out.println(statsForDateWindow);
    }

}
