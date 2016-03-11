package com.statscollector.sonar.functional;

import org.junit.Before;
import org.mockito.Mockito;

import com.statscollector.sonar.authentication.SonarAuthenticationHelper;
import com.statscollector.sonar.config.SonarConfig;
import com.statscollector.sonar.dao.SonarDao;
import com.statscollector.sonar.service.SonarStatisticsService;

public class SonarTest {

    private SonarConfig sonarConfig;
    private final SonarDao sonarDao = new SonarDao();
    private final SonarStatisticsService sonarStatisticsService = new SonarStatisticsService();
    private final SonarAuthenticationHelper authenticationHelper = new SonarAuthenticationHelper();

    @Before
    public void setup() {
        SonarConfig sonarConfig = Mockito.mock(SonarConfig.class);
        Mockito.when(sonarConfig.getHost()).thenReturn("sonar.ojp.gtsdevlan.com");
        Mockito.when(sonarConfig.getHostPort()).thenReturn(9000);
        Mockito.when(sonarConfig.getUsername()).thenReturn("jcannon");
        Mockito.when(sonarConfig.getPassword()).thenReturn("testpassword");
        sonarDao.setConfig(sonarConfig);
        authenticationHelper.setSonarConfig(sonarConfig);
        sonarStatisticsService.setAuthenticationHelper(authenticationHelper);
        sonarStatisticsService.setSonarDao(sonarDao);
    }
    //
    // @Test
    // public void testSonar() throws IOException, URISyntaxException {
    // List<SonarProject> statisticsForPeriod = sonarStatisticsService.getStatisticsForPeriod("2015-01-01",
    // "2015-12-25", ".*Ojp.*");
    // System.out.println(statisticsForPeriod);
    // }
}
