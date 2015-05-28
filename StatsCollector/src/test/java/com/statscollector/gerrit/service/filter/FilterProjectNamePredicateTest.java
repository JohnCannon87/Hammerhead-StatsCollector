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

public class FilterProjectNamePredicateTest {

	private FilterProjectNamePredicate filterProjectNamePredicate;

	@Before
	public void setUp() throws Exception {
		String testRegex = ".*test.*";
		filterProjectNamePredicate = new FilterProjectNamePredicate(testRegex);
	}

	@Test
	public void testApplyMatches() {
		GerritChange inputMatches = Mockito.mock(GerritChange.class);
		Mockito.when(inputMatches.getProject()).thenReturn("test");
		boolean result = filterProjectNamePredicate.apply(inputMatches);
		assertTrue(result);
	}

	@Test
	public void testApplyDoesntMatch() {
		GerritChange inputMatches = Mockito.mock(GerritChange.class);
		Mockito.when(inputMatches.getProject()).thenReturn("fail");
		boolean result = filterProjectNamePredicate.apply(inputMatches);
		assertFalse(result);
	}

	@Test
	public void testFilter() {
		GerritChange doesMatchInput = Mockito.mock(GerritChange.class);
		GerritChange doesntMatchInput = Mockito.mock(GerritChange.class);
		Mockito.when(doesMatchInput.getProject()).thenReturn("test");
		Mockito.when(doesntMatchInput.getProject()).thenReturn("fail");
		List<GerritChange> filterList = new ArrayList<GerritChange>();
		filterList.add(doesMatchInput);
		filterList.add(doesntMatchInput);
		List<GerritChange> filter = filterProjectNamePredicate.filter(filterList);
		assertTrue(1 == filter.size());
		GerritChange gerritChange = filter.get(0);
		assertEquals(doesMatchInput, gerritChange);
	}

}
