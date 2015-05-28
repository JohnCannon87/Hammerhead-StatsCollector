package com.statscollector.gerrit.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.apache.http.client.CredentialsProvider;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.statscollector.gerrit.authentication.GerritAuthenticationHelper;
import com.statscollector.gerrit.config.GerritConfig;
import com.statscollector.gerrit.dao.GerritDao;
import com.statscollector.gerrit.model.GerritChange;
import com.statscollector.gerrit.model.GerritReviewStatsResult;

public class GerritStatisticsHelperTest {

	private final GerritStatisticsHelper gerritStatisticsHelper = new GerritStatisticsHelper();
	private final GerritDao gerritDao = Mockito.mock(GerritDao.class);
	private final GerritAuthenticationHelper authenticationHelper = Mockito.mock(GerritAuthenticationHelper.class);
	private final CredentialsProvider credentialsProvider = Mockito.mock(CredentialsProvider.class);
	private final GerritConfig gerritConfig = Mockito.mock(GerritConfig.class);

	private final GerritService gerritService = new GerritService();

	private final String jsonDetailsString = "{\"id\":\"testwebview~develop~Ic69e4a99cbed45f3ec3e48c8151fa1dedc4043ac\",\"project\":\"testwebview\",\"branch\":\"develop\",\"change_id\":\"Ic69e4a99cbed45f3ec3e48c8151fa1dedc4043ac\",\"subject\":\"TESTTESTTEST-5032: fix to show correct disruption alert info\",\"status\":\"NEW\",\"created\":\"2015-05-28 13:54:59.736000000\",\"updated\":\"2015-05-28 13:55:07.720000000\",\"mergeable\":true,\"insertions\":3,\"deletions\":2,\"_sortkey\":\"003568030000259d\",\"_number\":9629,\"owner\":{\"_account_id\":1000027,\"name\":\"Test Reviewer5\",\"email\":\"test@gmail.com\",\"username\":\"testreviewer5\",\"avatars\":[{\"url\":\"http://test-test-wiki.co.uk/avatars/testreviewer5.png\",\"height\":26}]},\"labels\":{\"Code-Review\":{\"all\":[{\"value\":0,\"_account_id\":1000003,\"name\":\"Test Reviewer3\",\"email\":\"test@gmail.com\",\"username\":\"testreviewer3\",\"avatars\":[{\"url\":\"http://test-test-wiki.co.uk/avatars/testreviewer3.png\",\"height\":26}]},{\"value\":2,\"_account_id\":1000000,\"name\":\"Test Reviewer1\",\"email\":\"test2@googlemail.com\",\"username\":\"testuser1\",\"avatars\":[{\"url\":\"http://test-test-wiki.co.uk/avatars/testuser1.png\",\"height\":26}]},{\"_account_id\":1000004,\"name\":\"Jenkins Test\",\"email\":\"test.jenkins@gmail.com\",\"username\":\"jenkins\",\"avatars\":[{\"url\":\"http://test-test-wiki.co.uk/avatars/jenkins.png\",\"height\":26}]}],\"values\":{\"-2\":\"Do not submit\",\"-1\":\"I would prefer that you didn\u0027t submit this\",\" 0\":\"No score\",\"+1\":\"Looks good to me, but someone else must approve\",\"+2\":\"Looks good to me, approved\"},\"default_value\":0},\"Verified\":{\"approved\":{\"_account_id\":1000004,\"name\":\"Jenkins Test\",\"email\":\"test.jenkins@gmail.com\",\"username\":\"jenkins\",\"avatars\":[{\"url\":\"http://test-test-wiki.co.uk/avatars/jenkins.png\",\"height\":26}]},\"all\":[{\"value\":0,\"date\":\"2015-05-28 13:55:35.595000000\",\"_account_id\":1000003,\"name\":\"Test Reviewer3\",\"email\":\"test@gmail.com\",\"username\":\"testreviewer3\",\"avatars\":[{\"url\":\"http://test-test-wiki.co.uk/avatars/testreviewer3.png\",\"height\":26}]},{\"value\":0,\"date\":\"2015-05-28 13:55:26.220000000\",\"_account_id\":1000000,\"name\":\"Test Reviewer1\",\"email\":\"test2@googlemail.com\",\"username\":\"testuser1\",\"avatars\":[{\"url\":\"http://test-test-wiki.co.uk/avatars/testuser1.png\",\"height\":26}]},{\"value\":1,\"date\":\"2015-05-28 13:55:07.720000000\",\"_account_id\":1000004,\"name\":\"Jenkins Test\",\"email\":\"test.jenkins@gmail.com\",\"username\":\"jenkins\",\"avatars\":[{\"url\":\"http://test-test-wiki.co.uk/avatars/jenkins.png\",\"height\":26}]}],\"values\":{\"-1\":\"Fails\",\" 0\":\"No score\",\"+1\":\"Verified\"},\"default_value\":0}},\"permitted_labels\":{\"Code-Review\":[\"-2\",\"-1\",\" 0\",\"+1\",\"+2\"],\"Verified\":[\"-1\",\" 0\",\"+1\"]},\"removable_reviewers\":[{\"_account_id\":1000003,\"name\":\"Test Reviewer3\",\"email\":\"test@gmail.com\",\"username\":\"testreviewer3\",\"avatars\":[{\"url\":\"http://test-test-wiki.co.uk/avatars/testreviewer3.png\",\"height\":26}]},{\"_account_id\":1000004,\"name\":\"Jenkins Test\",\"email\":\"test.jenkins@gmail.com\",\"username\":\"jenkins\",\"avatars\":[{\"url\":\"http://test-test-wiki.co.uk/avatars/jenkins.png\",\"height\":26}]},{\"_account_id\":1000000,\"name\":\"Test Reviewer1\",\"email\":\"test2@googlemail.com\",\"username\":\"testuser1\",\"avatars\":[{\"url\":\"http://test-test-wiki.co.uk/avatars/testuser1.png\",\"height\":26}]}],\"messages\":[{\"id\":\"fa80f949_9225dc5d\",\"author\":{\"_account_id\":1000027,\"name\":\"Test Reviewer5\",\"email\":\"test@gmail.com\",\"username\":\"testreviewer5\",\"avatars\":[{\"url\":\"http://test-test-wiki.co.uk/avatars/testreviewer5.png\",\"height\":26}]},\"date\":\"2015-05-28 13:54:59.736000000\",\"message\":\"Uploaded patch set 1.\",\"_revision_number\":1},{\"id\":\"fa80f949_b22a602b\",\"author\":{\"_account_id\":1000004,\"name\":\"Jenkins Test\",\"email\":\"test.jenkins@gmail.com\",\"username\":\"jenkins\",\"avatars\":[{\"url\":\"http://test-test-wiki.co.uk/avatars/jenkins.png\",\"height\":26}]},\"date\":\"2015-05-28 13:55:04.730000000\",\"message\":\"Patch Set 1:\n\nBuild Started http://testjenkins:8085/job/Gerrit-TestWebView/22/\",\"_revision_number\":1},{\"id\":\"fa80f949_5220d449\",\"author\":{\"_account_id\":1000004,\"name\":\"Jenkins Test\",\"email\":\"test.jenkins@gmail.com\",\"username\":\"jenkins\",\"avatars\":[{\"url\":\"http://test-test-wiki.co.uk/avatars/jenkins.png\",\"height\":26}]},\"date\":\"2015-05-28 13:55:07.720000000\",\"message\":\"Patch Set 1: Verified+1\n\nBuild Successful \n\nhttp://testjenkins:8085/job/Gerrit-TestWebView/22/ : SUCCESS\",\"_revision_number\":1}]}";

	@Before
	public void setUp() throws Exception {
		gerritStatisticsHelper.setGerritService(gerritService);
		gerritStatisticsHelper.setGerritConfig(gerritConfig);
		gerritService.setStatisticsDao(gerritDao);
		gerritService.setAuthenticationHelper(authenticationHelper);

		Mockito.when(authenticationHelper.credentialsProvider()).thenReturn(credentialsProvider);

		List<String> reviewersToIgnore = new ArrayList();
		reviewersToIgnore.add("Jenkins Test");
		Mockito.when(gerritConfig.getReviewersToIgnore()).thenReturn(reviewersToIgnore);
	}

	@Test
	public void testFilterChangesBasedOnProjectName() {
		GerritChange doesMatchInput = Mockito.mock(GerritChange.class);
		GerritChange doesntMatchInput = Mockito.mock(GerritChange.class);
		Mockito.when(doesMatchInput.getProject()).thenReturn("test");
		Mockito.when(doesntMatchInput.getProject()).thenReturn("fail");
		List<GerritChange> filterList = new ArrayList<GerritChange>();
		filterList.add(doesMatchInput);
		filterList.add(doesntMatchInput);
		String testRegex = ".*test.*";
		List<GerritChange> filterChangesBasedOnProjectName = gerritStatisticsHelper.filterChangesBasedOnProjectName(
				filterList, testRegex);
		assertTrue(1 == filterChangesBasedOnProjectName.size());
		GerritChange gerritChange = filterChangesBasedOnProjectName.get(0);
		assertEquals(doesMatchInput, gerritChange);
	}

	@Test
	public void testFilterChangesBasedOnDateRange() {
		DateTime beforeFilterDate = new DateTime(0).withYear(2014).withMonthOfYear(1).withDayOfMonth(1);
		DateTime duringFilterDate = new DateTime(0).withYear(2015).withMonthOfYear(6).withDayOfMonth(1);
		DateTime afterFilterDate = new DateTime(0).withYear(2017).withMonthOfYear(1).withDayOfMonth(1);
		DateTime startDate = new DateTime(0).withYear(2015).withMonthOfYear(1).withDayOfMonth(1);
		DateTime endDate = new DateTime(0).withYear(2016).withMonthOfYear(1).withDayOfMonth(1);
		GerritChange afterInput = Mockito.mock(GerritChange.class);
		GerritChange beforeInput = Mockito.mock(GerritChange.class);
		GerritChange duringInput = Mockito.mock(GerritChange.class);
		Mockito.when(afterInput.getUpdated()).thenReturn(afterFilterDate);
		Mockito.when(beforeInput.getUpdated()).thenReturn(beforeFilterDate);
		Mockito.when(duringInput.getUpdated()).thenReturn(duringFilterDate);
		List<GerritChange> filterList = new ArrayList<GerritChange>();
		filterList.add(afterInput);
		filterList.add(beforeInput);
		filterList.add(duringInput);
		List<GerritChange> filterChangesBasedOnDateRange = gerritStatisticsHelper.filterChangesBasedOnDateRange(
				filterList, startDate, endDate);
		assertTrue(1 == filterChangesBasedOnDateRange.size());
		GerritChange gerritChange = filterChangesBasedOnDateRange.get(0);
		assertEquals(duringInput, gerritChange);
	}

	@Test
	public void testPopulateReviewStatsAsync() throws Exception {
		List<GerritChange> noPeerReviewList = new ArrayList<>();
		List<GerritChange> onePeerReviewList = new ArrayList<>();
		List<GerritChange> twoPlusPeerReviewList = new ArrayList<>();
		List<GerritChange> collabrativeDevelopmentList = new ArrayList<>();
		List<GerritChange> changes = createChanges();
		Mockito.when(gerritDao.getDetails(credentialsProvider, "testChangeId")).thenReturn(jsonDetailsString);
		Future<GerritReviewStatsResult> populateReviewStatsAsync = gerritStatisticsHelper.populateReviewStatsAsync(
				"testStatus", noPeerReviewList, onePeerReviewList, twoPlusPeerReviewList, collabrativeDevelopmentList,
				changes);
		while (!populateReviewStatsAsync.isDone()) {
			System.out.println("Waiting for Thread to finish");
		}
		GerritReviewStatsResult gerritReviewStatsResult = populateReviewStatsAsync.get();
		assertTrue(gerritReviewStatsResult.getSuccess());
		assertEquals(1, gerritReviewStatsResult.getChanges().size());
		assertEquals(0, noPeerReviewList.size());
		assertEquals(1, onePeerReviewList.size());
		assertEquals(0, twoPlusPeerReviewList.size());
		assertEquals(0, collabrativeDevelopmentList.size());
	}

	private List<GerritChange> createChanges() {
		List<GerritChange> result = new ArrayList<>();
		GerritChange gerritChange = new GerritChange("testId", "testChangeId", "testProj", "testOwner",
				new DateTime(0), new DateTime(0), "", "develop");
		result.add(gerritChange);
		return result;
	}

}
