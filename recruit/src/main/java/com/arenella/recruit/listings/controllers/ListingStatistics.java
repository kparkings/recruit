package com.arenella.recruit.listings.controllers;

import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

import com.arenella.recruit.listings.beans.ListingContactRequestEvent;
import com.arenella.recruit.listings.beans.ListingEvent;
import com.arenella.recruit.listings.beans.ListingViewedEvent;

/**
* Statistics relating to Listings 
* @author K Parkings
*/
public class ListingStatistics {

	private Map<String, Long> 	viewsPerWeek 				= new LinkedHashMap<>();
	private Map<String, Long> 	contactRequestsPerWeek 		= new LinkedHashMap<>();
	private AtomicLong 			viewsThisWeek				= new AtomicLong();
	private AtomicLong 			viewsToday 					= new AtomicLong();
	private AtomicLong 			contactRequestsThisWeek		= new AtomicLong();
	private AtomicLong 			contactRequestsToday		= new AtomicLong();
	
	/**
	* Returns contact request events info grouped by week
	* @return
	*/
	public Set<ViewItem> getContactRequestsPerWeek() {
		
		Set<ViewItem> items = new LinkedHashSet<>();
		
		this.contactRequestsPerWeek.entrySet().forEach(entry ->{
			items.add(new ViewItem(entry.getKey(), entry.getValue()));
		});
		
		return items;
	}
	
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
	* Returns the total number of contact request events there 
	* were for listings this week
	* @return views this week
	*/
	public long getContactRequestsThisWeek() {
		return this.contactRequestsThisWeek.longValue();
	}
	
	/**
	* Returns the total number of contact request events there 
	* were for listings today
	* @return views today
	*/
	public long getContactRequestsToday() {
		return this.contactRequestsToday.longValue();
	}
	
	/**
	* Returns the total number of vies events there 
	* were for listings this week
	* @return views this week
	*/
	public long getViewsThisWeek() {
		return this.viewsThisWeek.longValue();
	}
	
	/**
	* Returns the total number of vies events there 
	* were for listings today
	* @return views today
	*/
	public long getViewsToday() {
		return this.viewsToday.longValue();
	}
	
	/**
	* Processes the raw events to useful status
	* @param events
	*/
	public ListingStatistics(List<ListingViewedEvent> events, List<ListingContactRequestEvent> contactRequestEvents) {
		
		events.stream().forEach( evt ->
			this.processCreatedByWeek(evt, viewsPerWeek, viewsToday, viewsThisWeek)
		);
		
		this.viewsPerWeek.keySet().stream().sorted().forEach(s -> 
			this.contactRequestsPerWeek.put(s, 0L)
		);
		
		contactRequestEvents.stream().forEach( evt ->
				this.processCreatedByWeek(evt, contactRequestsPerWeek, contactRequestsToday, contactRequestsThisWeek)
		);
		
		this.contactRequestsPerWeek.keySet().stream().sorted().forEach(s -> {
			if (!this.viewsPerWeek.keySet().contains(s)) {
				this.viewsPerWeek.put(s, 0L);
			}
		});
		
		this.viewsPerWeek = this.viewsPerWeek.entrySet()
				.stream()
				.sorted(Map.Entry.<String, Long>comparingByKey())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
					    (oldValue, newValue) -> oldValue, LinkedHashMap::new));
		
		TemporalField 	weekOfYear = WeekFields.of(Locale.getDefault()).weekOfYear();
		int 			weekNumber = LocalDate.now().get(weekOfYear);
		String 			key = LocalDate.now().getYear() + " - " + String.format("%02d", weekNumber);
		
		if (this.viewsPerWeek.containsKey(key)) {
			this.viewsThisWeek.set(this.viewsPerWeek.get(key).longValue());
		}
		
		if (this.contactRequestsPerWeek.containsKey(key)) {
			this.contactRequestsThisWeek.set(this.contactRequestsPerWeek.get(key).longValue());
		}
		
	}

	/**
	* Processes the event so that it is grouped by week
	* @param event - event to process
	*/
	private void processCreatedByWeek(ListingEvent event, Map<String, Long> eventsCountByWeekContainer, AtomicLong dayCount, AtomicLong weekCount ) {
		
		TemporalField 	weekOfYear = WeekFields.of(Locale.getDefault()).weekOfYear();
		int 			weekNumber = event.getCreated().get(weekOfYear);
		
		String 			key = event.getCreated().getYear() + " - " + String.format("%02d", weekNumber);
		
		if (eventsCountByWeekContainer.containsKey(key)) {
			eventsCountByWeekContainer.put(key, eventsCountByWeekContainer.get(key) + 1 );
		} else {
			eventsCountByWeekContainer.put(key, 1L);
		}
		
		if (LocalDate.now().equals(event.getCreated().toLocalDate())) {
			dayCount.set(dayCount.get());
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