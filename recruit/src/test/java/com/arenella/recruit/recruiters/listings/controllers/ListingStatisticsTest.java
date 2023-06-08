package com.arenella.recruit.recruiters.listings.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.listings.beans.ListingViewedEvent;
import com.arenella.recruit.listings.controllers.ListingStatistics;

/**
* Unit tests for the ListingStatistics class
* @author K Parkings
*/
public class ListingStatisticsTest {

	/**
	* Tests that if no events for the current week the week and day view
	* return 0
	* @throws Exception
	*/
	@Test
	public void testNoEventsForCurrentWeek() throws Exception {
		
		List<ListingViewedEvent> events = new LinkedList<>();
		
		ListingStatistics stats = new ListingStatistics(events);
		
		assertEquals(0L, stats.getViewsThisWeek());
		assertEquals(0L, stats.getViewsToday());
		
	}
	
	/**
	* Test that by default the viewsPerWeek Map is instantiated 
	* but empty. Defensive programming
	* @throws Exception
	*/
	@Test
	public void testDefaults() throws Exception {
		
		List<ListingViewedEvent> events = new LinkedList<>();
		
		ListingStatistics stats = new ListingStatistics(events);
		
		assertTrue(stats.getViewsPerWeek().isEmpty());
		
	}
	
	/**
	* Tests after processing that the day and week totals are correct and 
	* the viewsPerWeek has the correct keys and values
	* @throws Exception
	*/
	@Test
	public void testEventProcessing() throws Exception {
		
		//TODO: [KP] Class uses current day and current week. Need to decide how to test
		//		that
		
	}
	
}