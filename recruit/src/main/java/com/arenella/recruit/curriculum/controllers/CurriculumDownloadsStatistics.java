package com.arenella.recruit.curriculum.controllers;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.arenella.recruit.curriculum.beans.CurriculumDownloadedEvent;

/**
* Contains stats of Curriculums downladed per day
* @author K Parkings
*/
public class CurriculumDownloadsStatistics {

	Map<String,Integer> dailyDownloads = new TreeMap<>();
	
	/**
	* Constructor. Reads in a list of events and generates the 
	* statistics
	* @param events - All events related to curriculum downloads
	*/
	public CurriculumDownloadsStatistics(List<CurriculumDownloadedEvent> events){
		
		events.stream().forEach(event -> {
			
			this.processDailyDownloads(event);
			
		});
		
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
	
	/**
	* Returns the stats
	* @return stats
	*/
	public Map<String,Integer> getDailyDownloads(){
		return dailyDownloads;
	}
	
}
