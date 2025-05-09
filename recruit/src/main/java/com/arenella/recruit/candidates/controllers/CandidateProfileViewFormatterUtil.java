package com.arenella.recruit.candidates.controllers;

import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.arenella.recruit.candidates.beans.CandidateProfileViewedEvent;

/**
* Util to take a collection of events and split them 
* into buckets 
*/
public class CandidateProfileViewFormatterUtil {

	/**
	* Static methods only. Hide default constructor to 
	* avoid instantiation 
	*/
	private CandidateProfileViewFormatterUtil() {
		//Hide default constuctor
	}
	/**
	* Groups events into year week buckets returning a count per 
	* year-week number combination
	* @param events - To be processed
	* @return grouped into buckets
	*/
	public static Set<Bucket> byWeek(Set<CandidateProfileViewedEvent> events) {
		
		final Map<String, Bucket> buckets = new HashMap<>();
		
		events.stream().forEach(event -> {
			TemporalField 	weekOfYear 	= WeekFields.of(Locale.getDefault()).weekOfYear();
			int 			weekNumber 	= event.getViewed().get(weekOfYear);
			String 			key 		= event.getViewed().getYear() + " - " + weekNumber;
			
			if (!buckets.containsKey(key)) {
				buckets.put(key, new Bucket(key));
			}
	
			buckets.get(key).increment();
			
		});
		
		return buckets.values().stream().toList().stream().sorted(Comparator.comparing(Bucket::getBucketId)).collect(Collectors.toCollection(LinkedHashSet::new));
	}
	
	/**
	* A bucket represents a grouping of events. Such
	* as a specific week in time and the number of 
	* events in occurred in that grouping
	*/
	public static class Bucket {
		
		private final String 	bucketId;
		private long 			count;
		
		/**
		* Constructor for a new bucket
		* @param bucketId - Id of the bucket
		*/
		public Bucket(String bucketId) {
			this.bucketId = bucketId;
		}
		
		/**
		* Increments number of times a n event occured 
		* in the period represented by the bucket 
		*/
		public void increment() {
			this.count++;
		}
		
		/**
		* Returns the unique Id of the bucket
		* @return id of the bucket
		*/
		public String getBucketId() {
			return this.bucketId;
		}

		/**
		* Returns the number of events that occurred in the 
		* buckets
		* @return number of events
		*/
		public long getCount() {
			return this.count;
		}
		
	}
	
}