package com.statscollector.gerrit.service.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.gerrit.extensions.common.ChangeInfo;

public class FilterProjectNamePredicateTest {

	private FilterProjectNamePredicate filterProjectNamePredicate;

	@Before
	public void setUp() throws Exception {
		String testRegex = ".*test.*";
		filterProjectNamePredicate = new FilterProjectNamePredicate(testRegex);
	}

	@Test
	public void testApplyMatches() {
		ChangeInfo inputMatches = new ChangeInfo();
		inputMatches.project = "test";
		boolean result = filterProjectNamePredicate.apply(inputMatches);
		assertTrue(result);
	}

	@Test
	public void testApplyDoesntMatch() {
		ChangeInfo inputMatches = new ChangeInfo();
		inputMatches.project = "fail";
		boolean result = filterProjectNamePredicate.apply(inputMatches);
		assertFalse(result);
	}

	@Test
	public void testFilter() {
		ChangeInfo doesMatchInput = new ChangeInfo();
		ChangeInfo doesntMatchInput = new ChangeInfo();
		doesMatchInput.project = "test";
		doesntMatchInput.project = "fail";
		List<ChangeInfo> filterList = new ArrayList<ChangeInfo>();
		filterList.add(doesMatchInput);
		filterList.add(doesntMatchInput);
		List<ChangeInfo> filter = filterProjectNamePredicate.filter(filterList);
		assertTrue(1 == filter.size());
		ChangeInfo gerritChange = filter.get(0);
		assertEquals(doesMatchInput, gerritChange);
	}

}
