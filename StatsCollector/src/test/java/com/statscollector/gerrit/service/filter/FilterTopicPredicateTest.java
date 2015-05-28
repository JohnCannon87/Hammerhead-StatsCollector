package com.statscollector.gerrit.service.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.statscollector.gerrit.model.GerritChange;

public class FilterTopicPredicateTest {

	private FilterTopicPredicate filterTopicPredicate;

	@Before
	public void setUp() throws Exception {
		String testRegex = ".*test.*";
		filterTopicPredicate = new FilterTopicPredicate(testRegex);
	}

	@Test
	public void testApplyMatches() {
		GerritChange inputMatches = Mockito.mock(GerritChange.class);
		Mockito.when(inputMatches.getTopic()).thenReturn("test");
		boolean result = filterTopicPredicate.apply(inputMatches);
		assertFalse(result);
	}

	@Test
	public void testApplyDoesntMatch() {
		GerritChange inputMatches = Mockito.mock(GerritChange.class);
		Mockito.when(inputMatches.getTopic()).thenReturn("fail");
		boolean result = filterTopicPredicate.apply(inputMatches);
		assertTrue(result);
	}

	@Test
	public void testFilter() {
		GerritChange doesMatchInput = Mockito.mock(GerritChange.class);
		GerritChange doesntMatchInput = Mockito.mock(GerritChange.class);
		Mockito.when(doesMatchInput.getTopic()).thenReturn("test");
		Mockito.when(doesntMatchInput.getTopic()).thenReturn("fail");
		List<GerritChange> filterList = new ArrayList<GerritChange>();
		filterList.add(doesMatchInput);
		filterList.add(doesntMatchInput);
		List<GerritChange> filter = filterTopicPredicate.filter(filterList);
		assertTrue(1 == filter.size());
		GerritChange gerritChange = filter.get(0);
		assertEquals(doesntMatchInput, gerritChange);
	}

}
