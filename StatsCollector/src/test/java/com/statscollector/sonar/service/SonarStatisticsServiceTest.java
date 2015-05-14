package com.statscollector.sonar.service;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.statscollector.sonar.authentication.SonarAuthenticationHelper;
import com.statscollector.sonar.dao.SonarDao;
import com.statscollector.sonar.model.SonarProject;

public class SonarStatisticsServiceTest {

	private SonarDao sonarDao;
	private CredentialsProvider credsProvider;
	private SonarAuthenticationHelper authenticationHelper;
	private final SonarStatisticsService service = new SonarStatisticsService();

	private final static String TEST_RESULTS = "[{\"id\":13043,\"key\":\"national-rail-enquiries-android-app:app\",\"name\":\"Nre Android Peak/Off Peak\",\"scope\":\"PRJ\",\"qualifier\":\"TRK\",\"date\":\"2015-01-28T09:28:55+0000\",\"creationDate\":\"2015-01-27T14:21:48+0000\",\"lname\":\"Nre Android Peak/Off Peak\",\"lang\":\"java\",\"version\":\"unspecified\",\"description\":\"\",\"msr\":[{\"key\":\"ncloc\",\"val\":43639.0,\"frmt_val\":\"43,639\"},{\"key\":\"file_complexity\",\"val\":32.9,\"frmt_val\":\"32.9\"},{\"key\":\"function_complexity\",\"val\":4.6,\"frmt_val\":\"4.6\"},{\"key\":\"violations_density\",\"val\":79.7,\"frmt_val\":\"79.7%\"}]},{\"id\":11985,\"key\":\"sonar:OjpBindings\",\"name\":\"OjpBindings\",\"scope\":\"PRJ\",\"qualifier\":\"TRK\",\"date\":\"2015-02-03T16:19:23+0000\",\"creationDate\":\"2014-09-16T09:42:17+0100\",\"lname\":\"OjpBindings\",\"lang\":\"java\",\"version\":\"unspecified\",\"description\":\"\"},{\"id\":8056,\"key\":\"sonar:OjpAlertsDataModel\",\"name\":\"OjpAlertsDataModel\",\"scope\":\"PRJ\",\"qualifier\":\"TRK\",\"date\":\"2015-05-12T08:36:53+0100\",\"creationDate\":\"2014-03-17T10:17:31+0000\",\"lname\":\"OjpAlertsDataModel\",\"lang\":\"java\",\"version\":\"unspecified\",\"description\":\"\",\"msr\":[{\"key\":\"ncloc\",\"val\":3889.0,\"frmt_val\":\"3,889\"},{\"key\":\"file_complexity\",\"val\":21.7,\"frmt_val\":\"21.7\"},{\"key\":\"function_complexity\",\"val\":1.9,\"frmt_val\":\"1.9\"},{\"key\":\"coverage\",\"val\":58.7,\"frmt_val\":\"58.7%\"},{\"key\":\"violations_density\",\"val\":89.6,\"frmt_val\":\"89.6%\"}]},{\"id\":8111,\"key\":\"sonar:OjpAlertsEngine\",\"name\":\"OjpAlertsEngine\",\"scope\":\"PRJ\",\"qualifier\":\"TRK\",\"date\":\"2015-05-12T08:40:58+0100\",\"creationDate\":\"2014-03-17T10:24:40+0000\",\"lname\":\"OjpAlertsEngine\",\"lang\":\"java\",\"version\":\"unspecified\",\"description\":\"\",\"msr\":[{\"key\":\"ncloc\",\"val\":9910.0,\"frmt_val\":\"9,910\"},{\"key\":\"file_complexity\",\"val\":14.1,\"frmt_val\":\"14.1\"},{\"key\":\"function_complexity\",\"val\":3.0,\"frmt_val\":\"3.0\"},{\"key\":\"coverage\",\"val\":45.4,\"frmt_val\":\"45.4%\"},{\"key\":\"violations_density\",\"val\":90.8,\"frmt_val\":\"90.8%\"}]},{\"id\":8306,\"key\":\"sonar:OjpAlertsExternalInterfaces\",\"name\":\"OjpAlertsExternalInterfaces\",\"scope\":\"PRJ\",\"qualifier\":\"TRK\",\"date\":\"2015-05-12T08:42:03+0100\",\"creationDate\":\"2014-03-17T10:26:10+0000\",\"lname\":\"OjpAlertsExternalInterfaces\",\"lang\":\"java\",\"version\":\"unspecified\",\"description\":\"\",\"msr\":[{\"key\":\"ncloc\",\"val\":3753.0,\"frmt_val\":\"3,753\"},{\"key\":\"file_complexity\",\"val\":23.8,\"frmt_val\":\"23.8\"},{\"key\":\"function_complexity\",\"val\":3.2,\"frmt_val\":\"3.2\"},{\"key\":\"coverage\",\"val\":0.1,\"frmt_val\":\"0.1%\"},{\"key\":\"violations_density\",\"val\":89.0,\"frmt_val\":\"89.0%\"}]},{\"id\":8345,\"key\":\"sonar:OjpDataModel\",\"name\":\"OjpDataModel\",\"scope\":\"PRJ\",\"qualifier\":\"TRK\",\"date\":\"2015-05-12T08:48:38+0100\",\"creationDate\":\"2014-03-17T10:38:11+0000\",\"lname\":\"OjpDataModel\",\"lang\":\"java\",\"version\":\"unspecified\",\"description\":\"\",\"msr\":[{\"key\":\"ncloc\",\"val\":45908.0,\"frmt_val\":\"45,908\"},{\"key\":\"file_complexity\",\"val\":11.7,\"frmt_val\":\"11.7\"},{\"key\":\"function_complexity\",\"val\":1.4,\"frmt_val\":\"1.4\"},{\"key\":\"coverage\",\"val\":32.2,\"frmt_val\":\"32.2%\"},{\"key\":\"violations_density\",\"val\":90.1,\"frmt_val\":\"90.1%\"}]},{\"id\":9120,\"key\":\"sonar:OjpDesktop\",\"name\":\"OjpDesktop\",\"scope\":\"PRJ\",\"qualifier\":\"TRK\",\"date\":\"2015-05-12T08:52:37+0100\",\"creationDate\":\"2014-03-17T10:44:40+0000\",\"lname\":\"OjpDesktop\",\"lang\":\"java\",\"version\":\"unspecified\",\"description\":\"\",\"msr\":[{\"key\":\"ncloc\",\"val\":2364.0,\"frmt_val\":\"2,364\"},{\"key\":\"file_complexity\",\"val\":27.2,\"frmt_val\":\"27.2\"},{\"key\":\"function_complexity\",\"val\":5.2,\"frmt_val\":\"5.2\"},{\"key\":\"coverage\",\"val\":76.5,\"frmt_val\":\"76.5%\"},{\"key\":\"violations_density\",\"val\":96.2,\"frmt_val\":\"96.2%\"}]},{\"id\":9126,\"key\":\"sonar:OjpExternalInterfaces\",\"name\":\"OjpExternalInterfaces\",\"scope\":\"PRJ\",\"qualifier\":\"TRK\",\"date\":\"2015-05-12T08:53:04+0100\",\"creationDate\":\"2014-03-17T10:45:15+0000\",\"lname\":\"OjpExternalInterfaces\",\"lang\":\"java\",\"version\":\"unspecified\",\"description\":\"\",\"msr\":[{\"key\":\"ncloc\",\"val\":1601.0,\"frmt_val\":\"1,601\"},{\"key\":\"file_complexity\",\"val\":4.0,\"frmt_val\":\"4.0\"},{\"key\":\"function_complexity\",\"val\":1.4,\"frmt_val\":\"1.4\"},{\"key\":\"coverage\",\"val\":23.1,\"frmt_val\":\"23.1%\"},{\"key\":\"violations_density\",\"val\":97.4,\"frmt_val\":\"97.4%\"}]},{\"id\":9174,\"key\":\"sonar:OjpLegacy\",\"name\":\"OjpLegacy\",\"scope\":\"PRJ\",\"qualifier\":\"TRK\",\"date\":\"2015-05-12T08:54:09+0100\",\"creationDate\":\"2014-03-17T10:55:32+0000\",\"lname\":\"OjpLegacy\",\"lang\":\"java\",\"version\":\"unspecified\",\"description\":\"\",\"msr\":[{\"key\":\"ncloc\",\"val\":12511.0,\"frmt_val\":\"12,511\"},{\"key\":\"file_complexity\",\"val\":18.6,\"frmt_val\":\"18.6\"},{\"key\":\"function_complexity\",\"val\":3.5,\"frmt_val\":\"3.5\"},{\"key\":\"coverage\",\"val\":37.7,\"frmt_val\":\"37.7%\"},{\"key\":\"violations_density\",\"val\":91.2,\"frmt_val\":\"91.2%\"}]},{\"id\":9461,\"key\":\"sonar:OjpMobile\",\"name\":\"OjpMobile\",\"scope\":\"PRJ\",\"qualifier\":\"TRK\",\"date\":\"2015-05-12T08:56:00+0100\",\"creationDate\":\"2014-03-17T10:58:43+0000\",\"lname\":\"OjpMobile\",\"lang\":\"java\",\"version\":\"unspecified\",\"description\":\"\",\"msr\":[{\"key\":\"ncloc\",\"val\":11123.0,\"frmt_val\":\"11,123\"},{\"key\":\"file_complexity\",\"val\":22.8,\"frmt_val\":\"22.8\"},{\"key\":\"function_complexity\",\"val\":3.5,\"frmt_val\":\"3.5\"},{\"key\":\"coverage\",\"val\":20.7,\"frmt_val\":\"20.7%\"},{\"key\":\"violations_density\",\"val\":94.0,\"frmt_val\":\"94.0%\"}]},{\"id\":9592,\"key\":\"sonar:OjpService\",\"name\":\"OjpService\",\"scope\":\"PRJ\",\"qualifier\":\"TRK\",\"date\":\"2015-05-12T09:01:35+0100\",\"creationDate\":\"2014-03-17T11:17:35+0000\",\"lname\":\"OjpService\",\"lang\":\"java\",\"version\":\"unspecified\",\"description\":\"\",\"msr\":[{\"key\":\"ncloc\",\"val\":66839.0,\"frmt_val\":\"66,839\"},{\"key\":\"file_complexity\",\"val\":21.0,\"frmt_val\":\"21.0\"},{\"key\":\"function_complexity\",\"val\":3.2,\"frmt_val\":\"3.2\"},{\"key\":\"coverage\",\"val\":34.8,\"frmt_val\":\"34.8%\"},{\"key\":\"violations_density\",\"val\":94.1,\"frmt_val\":\"94.1%\"}]},{\"id\":10340,\"key\":\"sonar:OjpUtil\",\"name\":\"OjpUtil\",\"scope\":\"PRJ\",\"qualifier\":\"TRK\",\"date\":\"2015-05-12T09:06:17+0100\",\"creationDate\":\"2014-03-17T11:28:34+0000\",\"lname\":\"OjpUtil\",\"lang\":\"java\",\"version\":\"unspecified\",\"description\":\"\",\"msr\":[{\"key\":\"ncloc\",\"val\":3831.0,\"frmt_val\":\"3,831\"},{\"key\":\"file_complexity\",\"val\":11.5,\"frmt_val\":\"11.5\"},{\"key\":\"function_complexity\",\"val\":2.0,\"frmt_val\":\"2.0\"},{\"key\":\"coverage\",\"val\":48.9,\"frmt_val\":\"48.9%\"},{\"key\":\"violations_density\",\"val\":90.0,\"frmt_val\":\"90.0%\"}]},{\"id\":10463,\"key\":\"sonar:OjpWeb\",\"name\":\"OjpWeb\",\"scope\":\"PRJ\",\"qualifier\":\"TRK\",\"date\":\"2015-05-12T09:07:38+0100\",\"creationDate\":\"2014-03-17T11:32:57+0000\",\"lname\":\"OjpWeb\",\"lang\":\"java\",\"version\":\"unspecified\",\"description\":\"\",\"msr\":[{\"key\":\"ncloc\",\"val\":32846.0,\"frmt_val\":\"32,846\"},{\"key\":\"file_complexity\",\"val\":25.5,\"frmt_val\":\"25.5\"},{\"key\":\"function_complexity\",\"val\":4.2,\"frmt_val\":\"4.2\"},{\"key\":\"coverage\",\"val\":20.3,\"frmt_val\":\"20.3%\"},{\"key\":\"violations_density\",\"val\":92.6,\"frmt_val\":\"92.6%\"}]},{\"id\":10792,\"key\":\"sonar:OjpWebServices\",\"name\":\"OjpWebServices\",\"scope\":\"PRJ\",\"qualifier\":\"TRK\",\"date\":\"2015-05-12T09:11:12+0100\",\"creationDate\":\"2014-03-17T11:44:59+0000\",\"lname\":\"OjpWebServices\",\"lang\":\"java\",\"version\":\"unspecified\",\"description\":\"\",\"msr\":[{\"key\":\"ncloc\",\"val\":21861.0,\"frmt_val\":\"21,861\"},{\"key\":\"file_complexity\",\"val\":13.6,\"frmt_val\":\"13.6\"},{\"key\":\"function_complexity\",\"val\":2.7,\"frmt_val\":\"2.7\"},{\"key\":\"coverage\",\"val\":37.2,\"frmt_val\":\"37.2%\"},{\"key\":\"violations_density\",\"val\":93.2,\"frmt_val\":\"93.2%\"}]},{\"id\":11152,\"key\":\"com.thales.aurora.rstlauroraweb\",\"name\":\"RstlAuroraWeb\",\"scope\":\"PRJ\",\"qualifier\":\"TRK\",\"date\":\"2015-05-13T15:57:50+0100\",\"creationDate\":\"2014-03-27T13:02:12+0000\",\"lname\":\"RstlAuroraWeb\",\"lang\":\"grvy\",\"version\":\"1.2\",\"description\":\"\",\"msr\":[{\"key\":\"ncloc\",\"val\":27052.0,\"frmt_val\":\"27,052\"},{\"key\":\"file_complexity\",\"val\":50.1,\"frmt_val\":\"50.1\"},{\"key\":\"function_complexity\",\"val\":6.0,\"frmt_val\":\"6.0\"},{\"key\":\"coverage\",\"val\":11.5,\"frmt_val\":\"11.5%\"},{\"key\":\"violations_density\",\"val\":90.3,\"frmt_val\":\"90.3%\"}]}]";

	@Before
	public void setUp() throws Exception {
		credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(new AuthScope("sonar.ojp", 9000), new UsernamePasswordCredentials("jcannon",
				"testpassword"));
		sonarDao = Mockito.mock(SonarDao.class);
		Mockito.when(sonarDao.getLatestStats(Mockito.any(CredentialsProvider.class))).thenReturn(TEST_RESULTS);
		authenticationHelper = Mockito.mock(SonarAuthenticationHelper.class);
		Mockito.when(authenticationHelper.createAuthenticationCredentials()).thenReturn(credsProvider);
		service.setAuthenticationHelper(authenticationHelper);
		service.setSonarDao(sonarDao);
	}

	@Test
	public void testGetAllSonarProjects() throws IOException, URISyntaxException {
		List<SonarProject> allSonarProjects = service.getAllSonarProjects();
		assertEquals("Error incorrect number of projects", 15, allSonarProjects.size());
	}

	@Test
	public void testGetFilteredSonarProjects() throws IOException, URISyntaxException {
		List<SonarProject> ojpSonarProjects = service.getProjectsFilteredByName(".*Ojp.*");
		assertEquals("Error incorrect number of projects", 13, ojpSonarProjects.size());

		List<SonarProject> rstlSonarProjects = service.getProjectsFilteredByName(".*Rstl.*");
		assertEquals("Error incorrect number of projects", 1, rstlSonarProjects.size());
	}

}
