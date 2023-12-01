package com.arenella.recruit.listings.controllers;

import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

import com.arenella.recruit.listings.beans.ListingViewedEvent;

/**
* Statistics relating to Listings 
* @author K Parkings
*/
public class ListingStatistics {

	private Map<String, Long> 	viewsPerWeek 		= new LinkedHashMap<>();
	private long 				viewsThisWeek;
	private long 				viewsToday;
	
	/**
	* Returns view events info grouped by week
	* @return
	*/
	public Set<ViewItem> getViewsPerWeek() {
		
		Set<ViewItem> items = new LinkedHashSet<>();
		
		this.viewsPerWeek.entrySet().forEach(entry ->{
			items.add(new ViewItem(entry.getKey(), entry.getValue()));
		});
		
		return items;
	}

	/**
	* Returns the total number of vies events there 
	* were for listings this week
	* @return views this week
	*/
	public long getViewsThisWeek() {
		return this.viewsThisWeek;
	}
	
	/**
	* Returns the total number of vies events there 
	* were for listings today
	* @return views today
	*/
	public long getViewsToday() {
		return this.viewsToday;
	}
	
	/**
	* Processes the raw events to useful status
	* @param events
	*/
	public ListingStatistics(List<ListingViewedEvent> events) {
		
		events.stream().forEach( 
			this::processCreatedByWeek
		);
	
		this.viewsPerWeek = this.viewsPerWeek.entrySet()
				.stream()
				.sorted(Map.Entry.<String, Long>comparingByKey())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
					    (oldValue, newValue) -> oldValue, LinkedHashMap::new));
		
		TemporalField 	weekOfYear = WeekFields.of(Locale.getDefault()).weekOfYear();
		int 			weekNumber = LocalDate.now().get(weekOfYear);
		String 			key = LocalDate.now().getYear() + " - " + String.format("%02d", weekNumber);
		
		if (this.viewsPerWeek.containsKey(key)) {
			this.viewsThisWeek = this.viewsPerWeek.get(key);
		}
		
	}
	
	/**
	* Processes the event so that it is grouped by week
	* @param event - event to process
	*/
	private void processCreatedByWeek(ListingViewedEvent event) {
		
		TemporalField 	weekOfYear = WeekFields.of(Locale.getDefault()).weekOfYear();
		int 			weekNumber = event.getCreated().get(weekOfYear);
		
		String 			key = event.getCreated().getYear() + " - " + String.format("%02d", weekNumber);
		
		if (viewsPerWeek.containsKey(key)) {
			viewsPerWeek.put(key, viewsPerWeek.get(key) + 1 );
		} else {
			viewsPerWeek.put(key, 1L);
		}
		
		if (LocalDate.now().equals(event.getCreated().toLocalDate())) {
			this.viewsToday = this.viewsToday + 1;
		}
		
	}
	
	public static class ViewItem{
		
		private final String 	bucketName;
		private final long		count;
		
		public ViewItem(String bucketName, long count) {
			this.bucketName = bucketName;
			this.count = count;
		}
		
		public String getBucketName() {
			return this.bucketName;
		}
		
		public long getCount() {
			return this.count;
		}
	}
	
}