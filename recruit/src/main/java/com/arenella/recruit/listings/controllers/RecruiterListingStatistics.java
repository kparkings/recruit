package com.arenella.recruit.listings.controllers;

import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.util.Pair;

import com.arenella.recruit.listings.beans.ListingViewedEvent;

/**
* Container for Statistics relating to Listings viewed
* for a given recruiter
* @author K Parkings
*/
public class RecruiterListingStatistics {

	private Set<WeekStatBucket> 	yearByWeekAllListing 	= new LinkedHashSet<>();
	private Set<ListingStat>		listingStats 			= new LinkedHashSet<>();
	
	/**
	* Constructor - Populated Object based upon data in the events
	* @param events - To be processed to produce the statistics
	*/
	public RecruiterListingStatistics(Set<ListingViewedEvent> events) {
		
		this.yearByWeekAllListing.addAll(this.calculateWeekNumberRange());
		this.listingStats.addAll(this.produceStats(events));
		
		events.stream().filter(e -> e.getCreated().toLocalDate().isAfter(LocalDate.now().minusYears(1))).forEach(evt -> {
			this.yearByWeekAllListing.stream().filter(wb -> wb.getWeekNumber() == getWeekNumber(evt.getCreated().toLocalDate())).findFirst().get().incrementCount();
			
			
			Optional<ListingStat> stat = this.listingStats.stream().filter(ls -> ls.listingId.toString().equals(evt.getListingId().toString())).findFirst();
			
			if (stat.isPresent()) { //TODO: [KP] Currently pulling all events out DB, even for deleted Listings. Could cause performance issue. 
				stat.get().addToBucket(evt);
			} 
			
		});
		
	}
	
	/**
	* Produces the Stats for the individual Listings
	* @param events - Events to produce stats for
	* @return Stats
	*/
	private Set<ListingStat> produceStats(Set<ListingViewedEvent> events){
		
		Set<ListingStat> stats = new LinkedHashSet<>();
	
		events.stream().map(e -> Pair.of(e.getListingId(),e.getTitleOrLisingId())).collect(Collectors.toSet()).stream().forEach(e -> {
		
			ListingStat stat = new ListingStat(e.getFirst(), e.getSecond());
			LocalDate currentDay = events.stream().filter(le -> le.getListingId() == stat.getListingId()).map(l -> l.getCreated().toLocalDate()).sorted().findFirst().get();
		
			do{
				stat.addBucket(new DayStatBucket(currentDay));
				currentDay = currentDay.plusDays(1);
			}while(currentDay.isBefore(LocalDate.now().plusDays(1)));
				//Todo: [KP] Dynamic swith. If under 3 months days. If over 3 months week buckets??
			stats.add(stat);
			
		});
		
		return stats;
	}
	
	/**
	* Returns the overal Stats for a year by 
	* week number
	* @return Year Stats
	*/
	public Set<WeekStatBucket> getYearByWeekAllListing(){
		return this.yearByWeekAllListing;
	}
	
	/**
	* Returns the Stat's for individual Listings
	* @return
	*/
	public Set<ListingStat> getListingStats(){
		return this.listingStats;
	}
	
	/**
	* Container for Stats related to an individual Listing
	* @author K Parkings
	*/
	public static class ListingStat{
		
		private final UUID 					listingId;
		private final String 				title;
		private final Set<DayStatBucket> 	stats 		= new LinkedHashSet<>();
		
		/**
		* Constructor
		* @param listingId 	- Unique Id of the Listing
		* @param title		- Human readable name for the Listing 
		*/
		public ListingStat(UUID listingId, String title) {
			
			this.listingId 	= listingId;
			this.title 		= title;
			
			this.stats.addAll(stats);
			
		}
		
		/**
		* Adds the Event to the correct bucket
		* @param evt - Event to add to the bucket
		*/
		public void addToBucket(ListingViewedEvent evt) {
			
			Optional<DayStatBucket> bucket = this.stats.stream().filter(s -> s.getBucket().toString().equals(evt.getCreated().toLocalDate().toString())).findFirst();
			
			if (bucket.isPresent()) {
				bucket.get().incrementCount();
			}
		}
		
		/**
		* Returns the unique Id of the Listing
		* @return unique id of the Listing
		*/
		public UUID getListingId() {
			return this.listingId;
		}
		
		/**
		* Returns the Title of the Listing
		* @return Human readable Title
		*/
		public String getTitle() {
			return this.title;
		}
		
		/**
		* Returns the Stat's relating to 
		* the Listing views
		* @return
		*/
		public Set<DayStatBucket> getStats(){
			return this.stats;
		}
		
		/**
		* Adds a bucket for a specific days stat's
		* @param bucket - day stat's bucket
		*/
		public void addBucket(DayStatBucket bucket) {
			this.stats.add(bucket);
		}
		
	}
	
	/**
	* Represents Statis for a Specific Day
	* @author K Parkings
	*/
	public static class DayStatBucket{
		private LocalDate 	bucket;
		private int 		count;
		
		/**
		* Constructor
		* @param bucket - Day for the Stat's
		*/
		public DayStatBucket(LocalDate bucket) {
			this.bucket = bucket;
		}
		
		/**
		* Returns the bucket for the Date's stat's 
		* @return bucket
		*/
		public LocalDate getBucket() {
			return this.bucket;
		}
		
		/**
		* Returns the number of Listing views for the Date
		* @return number of views
		*/
		public int getCount() {
			return this.count;
		}
		
		/**
		* Increments the number of view for the day bucket stat's 
		*/
		public void incrementCount() {
			this.count++;
		}
	}
	
	/**
	* Represents Stat's for a specific Week number
	* @author K Parkings
	*/
	public static class WeekStatBucket{
		private int		 	weekNumber;
		private int 		count;
		
		/**
		* Constructor for Buckets for week of year
		* @param weekNumber - Weeknumber for Bucket
		*/
		public WeekStatBucket(int weekNumber) {
			this.weekNumber = weekNumber;
		}
		
		/**
		* Returns the Buckets Weeknumber
		* @return weeknumber
		*/
		public int getWeekNumber() {
			return this.weekNumber;
		}
		
		/**
		* Increases the number of event that occurred in the week 
		* by 1 
		*/
		public void incrementCount() {
			this.count++;
		}
		
		/**
		* Returns the number of events that occurred in the week
		* @return
		*/
		public int getCount() {
			return this.count;
		}
		
	}
	
	/**
	* Returns the WeekNumber for a specic Date
	* @param ld - Date to obtain WeekNumber for
	* @return WeekNumber
	*/
	private int getWeekNumber(LocalDate ld) {
		
		TemporalField 	woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
		
		return ld.get(woy);
		
	}
	
	/**
	* Creates a years worth of WeekNumbers for the past year based upon the 
	* current date
	* @return Week Stat's buckets
	*/
	private Set<WeekStatBucket> calculateWeekNumberRange(){
		
		Set<WeekStatBucket> weekNumbers = new LinkedHashSet<>();
		
		LocalDate 		ld 	= LocalDate.now().minusYears(1);
		
		int startWeekNum 	= getWeekNumber(ld) + 1;
		int currentWeekNum 	= startWeekNum;
		
		do {
			weekNumbers.add(new WeekStatBucket(currentWeekNum));
			currentWeekNum = (currentWeekNum == 52 ? 1 : currentWeekNum + 1);
		}
		while(currentWeekNum != startWeekNum);
		
		return weekNumbers;
		
	}
	
}
