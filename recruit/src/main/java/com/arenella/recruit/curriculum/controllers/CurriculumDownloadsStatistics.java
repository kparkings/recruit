package com.arenella.recruit.curriculum.controllers;

import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.arenella.recruit.curriculum.beans.CurriculumDownloadedEvent;

import liquibase.pro.packaged.e;

/**
* Contains stats of Curriculums downladed per day
* @author K Parkings
*/
public class CurriculumDownloadsStatistics {

	private Map<String,Integer> 	dailyDownloads 				= new TreeMap<>();
	private Map<String, Integer> 	recruiterDownloads 			= new TreeMap<>();
	
	/**
	* Constructor. Reads in a list of events and generates the 
	* statistics
	* @param events - All events related to curriculum downloads
	*/
	public CurriculumDownloadsStatistics(List<CurriculumDownloadedEvent> events){
		
		events.stream().forEach(event -> {
			
			this.processDailyDownloads(event);
			this.processRecruiterDownloads(event);
			
		});
		
		//recruiterDownloads.entrySet().stream()
        //.sorted(Map.Entry.<String, Integer>comparingByValue().reversed()) 
        //.limit(10) 
        //.forEach(System.out::println);
		
		this.recruiterDownloads = this.recruiterDownloads.entrySet()
									.stream()
									.sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
									.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
										    (oldValue, newValue) -> oldValue, LinkedHashMap::new));
								
		
	}
	
	private void processDailyDownloads(CurriculumDownloadedEvent event) {
		
		if (event.isAdminUser()) {
			return;
		}
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
		String date = formatter.format(event.getTimestamp());
		
		if (dailyDownloads.containsKey(date)) {
			dailyDownloads.put(date, dailyDownloads.get(date) + 1 );
		} else {
			dailyDownloads.put(date, 1);
		}
		
	}
	
	private void processRecruiterDownloads(CurriculumDownloadedEvent event) {
		
		if (event.isAdminUser()) {
			return;
		}
		
		
		if (recruiterDownloads.containsKey(event.getUserId())) {
			recruiterDownloads.put(event.getUserId(), recruiterDownloads.get(event.getUserId()) + 1 );
		} else {
			recruiterDownloads.put(event.getUserId(), 1);
		}
		
	}
	
	/**
	* Returns the stats for downloads per day
	* @return stats
	*/
	public Map<String,Integer> getDailyDownloads(){
		return dailyDownloads;
	}
	
	/**
	* Returns the stats for downloads per recruiter
	* @return stats
	*/
	public Map<String, Integer> getRecruiterDownloads() {
		return this.recruiterDownloads;
	}
	
}