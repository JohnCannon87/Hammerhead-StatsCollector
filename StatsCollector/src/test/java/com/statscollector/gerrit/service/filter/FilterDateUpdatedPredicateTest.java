package com.statscollector.gerrit.service.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import com.google.gerrit.extensions.common.ChangeInfo;

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
		ChangeInfo testInput = new ChangeInfo();
		testInput.updated = new Timestamp(duringFilterDate.getMillis());
		boolean result = filterDateUpdatedPredicate.apply(testInput);
		assertTrue(result);

	}

	@Test
	public void testApplyBeforeFilter() {
		ChangeInfo testInput = new ChangeInfo();
		testInput.updated = new Timestamp(beforeFilterDate.getMillis());
		boolean result = filterDateUpdatedPredicate.apply(testInput);
		assertFalse(result);
	}

	@Test
	public void testApplyAfterFilter() {
		ChangeInfo testInput = new ChangeInfo();
		testInput.updated = new Timestamp(afterFilterDate.getMillis());
		boolean result = filterDateUpdatedPredicate.apply(testInput);
		assertFalse(result);
	}

	@Test
	public void testFilter() {
		ChangeInfo afterInput = new ChangeInfo();
		ChangeInfo beforeInput = new ChangeInfo();
		ChangeInfo duringInput = new ChangeInfo();
		afterInput.updated = new Timestamp(afterFilterDate.getMillis());
		beforeInput.updated = new Timestamp(beforeFilterDate.getMillis());
		duringInput.updated = new Timestamp(duringFilterDate.getMillis());
		List<ChangeInfo> filterList = new ArrayList<ChangeInfo>();
		filterList.add(afterInput);
		filterList.add(beforeInput);
		filterList.add(duringInput);
		List<ChangeInfo> filter = filterDateUpdatedPredicate.filter(filterList);
		assertTrue(1 == filter.size());
		ChangeInfo gerritChange = filter.get(0);
		assertEquals(duringInput, gerritChange);
	}

}
