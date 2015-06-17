package com.statscollector.gerrit.service.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.gerrit.extensions.common.ChangeInfo;

public class FilterTopicPredicateTest {

	private FilterTopicPredicate filterTopicPredicate;

	@Before
	public void setUp() throws Exception {
		String testRegex = ".*test.*";
		filterTopicPredicate = new FilterTopicPredicate(testRegex);
	}

	@Test
	public void testApplyMatches() {
		ChangeInfo inputMatches = new ChangeInfo();
		inputMatches.topic = "test";
		boolean result = filterTopicPredicate.apply(inputMatches);
		assertFalse(result);
	}

	@Test
	public void testApplyDoesntMatch() {
		ChangeInfo inputMatches = new ChangeInfo();
		inputMatches.topic = "fail";
		boolean result = filterTopicPredicate.apply(inputMatches);
		assertTrue(result);
	}

	@Test
	public void testFilter() {
		ChangeInfo doesMatchInput = new ChangeInfo();
		ChangeInfo doesntMatchInput = new ChangeInfo();
		doesMatchInput.topic = "test";
		doesntMatchInput.topic = "fail";
		List<ChangeInfo> filterList = new ArrayList<ChangeInfo>();
		filterList.add(doesMatchInput);
		filterList.add(doesntMatchInput);
		List<ChangeInfo> filter = filterTopicPredicate.filter(filterList);
		assertTrue(1 == filter.size());
		ChangeInfo gerritChange = filter.get(0);
		assertEquals(doesntMatchInput, gerritChange);
	}

}
