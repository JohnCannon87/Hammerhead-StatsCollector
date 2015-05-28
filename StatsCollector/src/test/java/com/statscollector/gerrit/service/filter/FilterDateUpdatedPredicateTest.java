package com.statscollector.gerrit.service.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.statscollector.gerrit.model.GerritChange;

public class FilterDateUpdatedPredicateTest {

	private FilterDateUpdatedPredicate filterDateUpdatedPredicate;
	private final DateTime beforeFilterDate = new DateTime(0).withYear(2014).withMonthOfYear(1).withDayOfMonth(1);
	private final DateTime duringFilterDate = new DateTime(0).withYear(2015).withMonthOfYear(6).withDayOfMonth(1);
	private final DateTime afterFilterDate = new DateTime(0).withYear(2017).withMonthOfYear(1).withDayOfMonth(1);

	@Before
	public void setUp() throws Exception {
		DateTime startDate = new DateTime(0).withYear(2015).withMonthOfYear(1).withDayOfMonth(1);
		DateTime endDate = new DateTime(0).withYear(2016).withMonthOfYear(1).withDayOfMonth(1);
		filterDateUpdatedPredicate = new FilterDateUpdatedPredicate(startDate, endDate);
	}

	@Test
	public void testApplyDuringFilter() {
		GerritChange testInput = Mockito.mock(GerritChange.class);
		Mockito.when(testInput.getUpdated()).thenReturn(duringFilterDate);
		boolean result = filterDateUpdatedPredicate.apply(testInput);
		assertTrue(result);

	}

	@Test
	public void testApplyBeforeFilter() {
		GerritChange testInput = Mockito.mock(GerritChange.class);
		Mockito.when(testInput.getUpdated()).thenReturn(beforeFilterDate);
		boolean result = filterDateUpdatedPredicate.apply(testInput);
		assertFalse(result);
	}

	@Test
	public void testApplyAfterFilter() {
		GerritChange testInput = Mockito.mock(GerritChange.class);
		Mockito.when(testInput.getUpdated()).thenReturn(afterFilterDate);
		boolean result = filterDateUpdatedPredicate.apply(testInput);
		assertFalse(result);
	}

	@Test
	public void testFilter() {
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
		List<GerritChange> filter = filterDateUpdatedPredicate.filter(filterList);
		assertTrue(1 == filter.size());
		GerritChange gerritChange = filter.get(0);
		assertEquals(duringInput, gerritChange);
	}

}
