package com.statscollector.gerrit.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.client.CredentialsProvider;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.statscollector.gerrit.authentication.GerritAuthenticationHelper;
import com.statscollector.gerrit.dao.GerritDao;
import com.statscollector.gerrit.model.GerritChange;
import com.statscollector.gerrit.model.GerritChangeDetails;

public class GerritServiceTest {

	private final GerritService gerritService = new GerritService();
	private final GerritDao gerritDao = Mockito.mock(GerritDao.class);
	private final GerritAuthenticationHelper authenticationHelper = Mockito.mock(GerritAuthenticationHelper.class);
	private final CredentialsProvider credentialsProvider = Mockito.mock(CredentialsProvider.class);
	private final String jsonResultsString = "[{\"id\":\"testservice~develop~Ie8489b0c9c58c0497aa7e67c916d7243afa46a06\",\"project\":\"testservice\",\"branch\":\"develop\",\"change_id\":\"Ie8489b0c9c58c0497aa7e67c916d7243afa46a06\",\"subject\":\"Fix for Jira PROJTESTDEV-3414 and PROJTESTDEV-3406 - Error when clicking Details link on LDB\",\"status\":\"NEW\",\"created\":\"2015-05-20 13:55:38.473000000\",\"updated\":\"2015-05-28 08:55:08.860000000\",\"mergeable\":true,\"insertions\":183,\"deletions\":124,\"_sortkey\":\"003566d700002574\",\"_number\":9588,\"owner\":{\"name\":\"Test Reviewer3\"}},{\"id\":\"testreferenceweb~develop~Id241bdbd9e695609097d532c11c65ef354cde2b4\",\"project\":\"testreferenceweb\",\"branch\":\"develop\",\"change_id\":\"Id241bdbd9e695609097d532c11c65ef354cde2b4\",\"subject\":\"Fixed typo in whitelist IPs\",\"status\":\"NEW\",\"created\":\"2015-05-28 08:36:10.389000000\",\"updated\":\"2015-05-28 08:36:10.389000000\",\"mergeable\":true,\"insertions\":1,\"deletions\":1,\"_sortkey\":\"003566c40000259c\",\"_number\":9628,\"owner\":{\"name\":\"Test Reviewer4\"}},{\"id\":\"testwebview~develop~I68235a0f3e7f3ca5b91ad833a81e2ec52a51e58c\",\"project\":\"testwebview\",\"branch\":\"develop\",\"change_id\":\"I68235a0f3e7f3ca5b91ad833a81e2ec52a51e58c\",\"subject\":\"PROJTESTSD4978: latest js; show delayInMinutes and fix earler/later links\",\"status\":\"NEW\",\"created\":\"2015-05-28 07:32:09.306000000\",\"updated\":\"2015-05-28 07:33:44.017000000\",\"mergeable\":true,\"insertions\":25,\"deletions\":18,\"_sortkey\":\"003566850000259b\",\"_number\":9627,\"owner\":{\"name\":\"Test Reviewer5\"}},{\"id\":\"testreferencedataengine~develop~I4ba6b44cb0e8661ec05bad6645cd5807768fed55\",\"project\":\"testreferencedataengine\",\"branch\":\"develop\",\"change_id\":\"I4ba6b44cb0e8661ec05bad6645cd5807768fed55\",\"subject\":\"Updated Ignore file, CSRF and better CSS.\",\"status\":\"NEW\",\"created\":\"2015-05-27 15:32:16.099000000\",\"updated\":\"2015-05-27 15:33:05.446000000\",\"mergeable\":true,\"insertions\":129,\"deletions\":10,\"_sortkey\":\"003562c50000259a\",\"_number\":9626,\"owner\":{\"name\":\"Test Reviewer1\"}},{\"id\":\"testwebview~develop~I7f38bc0508c339818b7aa4bc6fa978b4ed834550\",\"project\":\"testwebview\",\"branch\":\"develop\",\"change_id\":\"I7f38bc0508c339818b7aa4bc6fa978b4ed834550\",\"subject\":\"PROJTESTSD4978: show delayInMinutes value on the hover item\",\"status\":\"NEW\",\"created\":\"2015-05-27 14:02:12.606000000\",\"updated\":\"2015-05-27 14:30:07.669000000\",\"mergeable\":true,\"insertions\":5,\"deletions\":2,\"_sortkey\":\"0035628600002599\",\"_number\":9625,\"owner\":{\"name\":\"Test Reviewer5\"}},{\"id\":\"testweb~develop~I70b9cd6afebe17523b1da80fe19ffd2662f6429b\",\"project\":\"testweb\",\"branch\":\"develop\",\"change_id\":\"I70b9cd6afebe17523b1da80fe19ffd2662f6429b\",\"subject\":\"PROJTESTSD4978: show delayInMinutes value on the hover item\",\"status\":\"NEW\",\"created\":\"2015-05-27 13:45:54.322000000\",\"updated\":\"2015-05-27 14:29:55.331000000\",\"mergeable\":true,\"insertions\":126,\"deletions\":156,\"_sortkey\":\"0035628500002597\",\"_number\":9623,\"owner\":{\"name\":\"Test Reviewer5\"}},{\"id\":\"testservice~develop~I28e75a6cd5d285368d22a239543c34d8567bf636\",\"project\":\"testservice\",\"branch\":\"develop\",\"change_id\":\"I28e75a6cd5d285368d22a239543c34d8567bf636\",\"subject\":\"PROJTESTSD4978: show delayInMinutes value on the hover item\",\"status\":\"NEW\",\"created\":\"2015-05-27 13:46:34.542000000\",\"updated\":\"2015-05-27 14:29:37.717000000\",\"mergeable\":true,\"insertions\":58,\"deletions\":50,\"_sortkey\":\"0035628500002598\",\"_number\":9624,\"owner\":{\"name\":\"Test Reviewer5\"}},{\"id\":\"testweb~develop~Ib4347c1c51eb3f6cc81a0abebd6466b4dbfe5188\",\"project\":\"testweb\",\"branch\":\"develop\",\"change_id\":\"Ib4347c1c51eb3f6cc81a0abebd6466b4dbfe5188\",\"subject\":\"Unit Test for Jira PROJTESTDEV-3414 and PROJTESTDEV-3406 - Error when clicking Details link on LDB\",\"status\":\"NEW\",\"created\":\"2015-05-22 08:47:06.618000000\",\"updated\":\"2015-05-27 10:14:24.927000000\",\"mergeable\":true,\"insertions\":26,\"deletions\":0,\"_sortkey\":\"0035618600002587\",\"_number\":9607,\"owner\":{\"name\":\"Test Reviewer3\"}},{\"id\":\"testwebview~develop~I6146c4e9f27c03ff7a4ff3e3721f488c2911446f\",\"project\":\"testwebview\",\"branch\":\"develop\",\"change_id\":\"I6146c4e9f27c03ff7a4ff3e3721f488c2911446f\",\"subject\":\"Updated url for windows phone app download\",\"status\":\"NEW\",\"created\":\"2015-05-26 09:38:59.249000000\",\"updated\":\"2015-05-26 09:39:05.957000000\",\"mergeable\":true,\"insertions\":1,\"deletions\":1,\"_sortkey\":\"00355bc30000258e\",\"_number\":9614,\"owner\":{\"name\":\"Test Reviewer1\"}},{\"id\":\"testdesktopfunctionaltests~develop~I6de9dd5cd0dd56c8f4393cbcad91ac750f5db394\",\"project\":\"testdesktopfunctionaltests\",\"branch\":\"develop\",\"change_id\":\"I6de9dd5cd0dd56c8f4393cbcad91ac750f5db394\",\"subject\":\"Func tests for PROJTESTDEV-3414 and PROJTESTDEV-3406 - Error whwn clicking details link on LDB\",\"status\":\"NEW\",\"created\":\"2015-05-20 15:03:52.826000000\",\"updated\":\"2015-05-21 12:34:56.454000000\",\"mergeable\":true,\"insertions\":43,\"deletions\":2,\"_sortkey\":\"0035405200002578\",\"_number\":9592,\"owner\":{\"name\":\"Test Reviewer3\"}},{\"id\":\"testweb~develop~Idfe2a31b2107b63c9f28c55a00279359c386c1ff\",\"project\":\"testweb\",\"branch\":\"develop\",\"change_id\":\"Idfe2a31b2107b63c9f28c55a00279359c386c1ff\",\"subject\":\"PROJTESTTEST-4605 - Fix for update now not working on home page, requires update to template\",\"status\":\"NEW\",\"created\":\"2015-03-11 17:12:06.487000000\",\"updated\":\"2015-03-16 09:07:51.542000000\",\"mergeable\":false,\"insertions\":3,\"deletions\":2,\"_sortkey\":\"0033cc4300002397\",\"_number\":9111,\"owner\":{\"name\":\"Test Reviewer2\"}}]";
	private final String jsonDetailsString = "{\"id\":\"testwebview~develop~Ic69e4a99cbed45f3ec3e48c8151fa1dedc4043ac\",\"project\":\"testwebview\",\"branch\":\"develop\",\"change_id\":\"Ic69e4a99cbed45f3ec3e48c8151fa1dedc4043ac\",\"subject\":\"TESTTESTTEST-5032: fix to show correct disruption alert info\",\"status\":\"NEW\",\"created\":\"2015-05-28 13:54:59.736000000\",\"updated\":\"2015-05-28 13:55:07.720000000\",\"mergeable\":true,\"insertions\":3,\"deletions\":2,\"_sortkey\":\"003568030000259d\",\"_number\":9629,\"owner\":{\"_account_id\":1000027,\"name\":\"Test Reviewer5\",\"email\":\"test@gmail.com\",\"username\":\"testreviewer5\",\"avatars\":[{\"url\":\"http://test-test-wiki.co.uk/avatars/testreviewer5.png\",\"height\":26}]},\"labels\":{\"Code-Review\":{\"all\":[{\"value\":0,\"_account_id\":1000003,\"name\":\"Test Reviewer3\",\"email\":\"test@gmail.com\",\"username\":\"testreviewer3\",\"avatars\":[{\"url\":\"http://test-test-wiki.co.uk/avatars/testreviewer3.png\",\"height\":26}]},{\"value\":0,\"_account_id\":1000000,\"name\":\"Test Reviewer1\",\"email\":\"test2@googlemail.com\",\"username\":\"testuser1\",\"avatars\":[{\"url\":\"http://test-test-wiki.co.uk/avatars/testuser1.png\",\"height\":26}]},{\"_account_id\":1000004,\"name\":\"Jenkins Test\",\"email\":\"test.jenkins@gmail.com\",\"username\":\"jenkins\",\"avatars\":[{\"url\":\"http://test-test-wiki.co.uk/avatars/jenkins.png\",\"height\":26}]}],\"values\":{\"-2\":\"Do not submit\",\"-1\":\"I would prefer that you didn\u0027t submit this\",\" 0\":\"No score\",\"+1\":\"Looks good to me, but someone else must approve\",\"+2\":\"Looks good to me, approved\"},\"default_value\":0},\"Verified\":{\"approved\":{\"_account_id\":1000004,\"name\":\"Jenkins Test\",\"email\":\"test.jenkins@gmail.com\",\"username\":\"jenkins\",\"avatars\":[{\"url\":\"http://test-test-wiki.co.uk/avatars/jenkins.png\",\"height\":26}]},\"all\":[{\"value\":0,\"date\":\"2015-05-28 13:55:35.595000000\",\"_account_id\":1000003,\"name\":\"Test Reviewer3\",\"email\":\"test@gmail.com\",\"username\":\"testreviewer3\",\"avatars\":[{\"url\":\"http://test-test-wiki.co.uk/avatars/testreviewer3.png\",\"height\":26}]},{\"value\":0,\"date\":\"2015-05-28 13:55:26.220000000\",\"_account_id\":1000000,\"name\":\"Test Reviewer1\",\"email\":\"test2@googlemail.com\",\"username\":\"testuser1\",\"avatars\":[{\"url\":\"http://test-test-wiki.co.uk/avatars/testuser1.png\",\"height\":26}]},{\"value\":1,\"date\":\"2015-05-28 13:55:07.720000000\",\"_account_id\":1000004,\"name\":\"Jenkins Test\",\"email\":\"test.jenkins@gmail.com\",\"username\":\"jenkins\",\"avatars\":[{\"url\":\"http://test-test-wiki.co.uk/avatars/jenkins.png\",\"height\":26}]}],\"values\":{\"-1\":\"Fails\",\" 0\":\"No score\",\"+1\":\"Verified\"},\"default_value\":0}},\"permitted_labels\":{\"Code-Review\":[\"-2\",\"-1\",\" 0\",\"+1\",\"+2\"],\"Verified\":[\"-1\",\" 0\",\"+1\"]},\"removable_reviewers\":[{\"_account_id\":1000003,\"name\":\"Test Reviewer3\",\"email\":\"test@gmail.com\",\"username\":\"testreviewer3\",\"avatars\":[{\"url\":\"http://test-test-wiki.co.uk/avatars/testreviewer3.png\",\"height\":26}]},{\"_account_id\":1000004,\"name\":\"Jenkins Test\",\"email\":\"test.jenkins@gmail.com\",\"username\":\"jenkins\",\"avatars\":[{\"url\":\"http://test-test-wiki.co.uk/avatars/jenkins.png\",\"height\":26}]},{\"_account_id\":1000000,\"name\":\"Test Reviewer1\",\"email\":\"test2@googlemail.com\",\"username\":\"testuser1\",\"avatars\":[{\"url\":\"http://test-test-wiki.co.uk/avatars/testuser1.png\",\"height\":26}]}],\"messages\":[{\"id\":\"fa80f949_9225dc5d\",\"author\":{\"_account_id\":1000027,\"name\":\"Test Reviewer5\",\"email\":\"test@gmail.com\",\"username\":\"testreviewer5\",\"avatars\":[{\"url\":\"http://test-test-wiki.co.uk/avatars/testreviewer5.png\",\"height\":26}]},\"date\":\"2015-05-28 13:54:59.736000000\",\"message\":\"Uploaded patch set 1.\",\"_revision_number\":1},{\"id\":\"fa80f949_b22a602b\",\"author\":{\"_account_id\":1000004,\"name\":\"Jenkins Test\",\"email\":\"test.jenkins@gmail.com\",\"username\":\"jenkins\",\"avatars\":[{\"url\":\"http://test-test-wiki.co.uk/avatars/jenkins.png\",\"height\":26}]},\"date\":\"2015-05-28 13:55:04.730000000\",\"message\":\"Patch Set 1:\n\nBuild Started http://testjenkins:8085/job/Gerrit-TestWebView/22/\",\"_revision_number\":1},{\"id\":\"fa80f949_5220d449\",\"author\":{\"_account_id\":1000004,\"name\":\"Jenkins Test\",\"email\":\"test.jenkins@gmail.com\",\"username\":\"jenkins\",\"avatars\":[{\"url\":\"http://test-test-wiki.co.uk/avatars/jenkins.png\",\"height\":26}]},\"date\":\"2015-05-28 13:55:07.720000000\",\"message\":\"Patch Set 1: Verified+1\n\nBuild Successful \n\nhttp://testjenkins:8085/job/Gerrit-TestWebView/22/ : SUCCESS\",\"_revision_number\":1}]}";

	@Before
	public void setUp() throws Exception {
		gerritService.setStatisticsDao(gerritDao);
		gerritService.setAuthenticationHelper(authenticationHelper);
		Mockito.when(authenticationHelper.credentialsProvider()).thenReturn(credentialsProvider);
	}

	@Test
	public void testGetAllChanges() throws Exception {
		String changeStatus = "status:open";
		Mockito.when(gerritDao.getAllChanges(credentialsProvider, changeStatus)).thenReturn(jsonResultsString);
		List<GerritChange> allChanges = gerritService.getAllChanges(changeStatus);
		assertEquals(11, allChanges.size());
	}

	@Test
	public void testPopulateChangeReviewers() throws Exception {
		Mockito.when(gerritDao.getDetails(credentialsProvider, "testChangeId")).thenReturn(jsonDetailsString);
		List<GerritChange> changes = createChanges();
		gerritService.populateChangeReviewers(changes);
		for (GerritChange gerritChange : changes) {
			Map<String, Integer> reviewers = gerritChange.getReviewers();
			assertNotNull(reviewers);
			assertEquals(3, reviewers.size());
		}
	}

	@Test
	public void testGetGerritChangeDetails() throws Exception {
		Mockito.when(gerritDao.getDetails(credentialsProvider, "testChangeId")).thenReturn(jsonDetailsString);
		List<GerritChange> changes = createChanges();
		Map<String, GerritChangeDetails> gerritChangeDetails = gerritService.getGerritChangeDetails(changes);
		assertNotNull(gerritChangeDetails);
		assertEquals(1, gerritChangeDetails.size());
	}

	private List<GerritChange> createChanges() {
		List<GerritChange> result = new ArrayList<>();
		GerritChange gerritChange = new GerritChange("testId", "testChangeId", "testProj", "testOwner",
				new DateTime(0), new DateTime(0), "", "develop");
		result.add(gerritChange);
		return result;
	}
}
