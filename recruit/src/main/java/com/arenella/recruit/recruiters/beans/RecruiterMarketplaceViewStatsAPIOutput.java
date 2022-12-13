package com.arenella.recruit.recruiters.beans;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

/**
* Statistics for current weeks views of Open Poistions by recruiters
* @author K Parkings
*/
public class RecruiterMarketplaceViewStatsAPIOutput {
	
	private final Set<RecruiterStat> 		stats 		= new LinkedHashSet<>();
	private final LocalDateTime 			startOfDay 	= LocalDateTime
																	.now()
																	.withHour(0)
																	.withMinute(0)
																	.withSecond(0)
																	.withNano(0);
	
	/**
	* Constructor - Processes events to generate statistics
	* @param events
	*/
	public RecruiterMarketplaceViewStatsAPIOutput(Set<SupplyAndDemandEvent> events) {
		
		events.stream().forEach(event -> {
			
			Optional<RecruiterStat> stat = stats.stream().filter(s -> s.recruiterId.equals(event.getRecruiterId())).findAny();
		
			if (stat.isPresent()) {
				stat.get().processEvent(event);
			} else {
				RecruiterStat recruiterStat = new RecruiterStat(event.getRecruiterId(),startOfDay);
				recruiterStat.processEvent(event);
				this.stats.add(recruiterStat);
			}
			
		});
		
		
	}
	
	/**
	* Returns the stats of each Recruiter
	* @return stats
	*/
	public Set<RecruiterStat> getStats(){
		return this.stats;
	}
	
	/**
	* Represents view statistics of individual Recruiters
	* @author K Parkings
	*/
	public static class RecruiterStat {
		
		private String 			recruiterId;
		private LocalDateTime 	startOfDay;
		private long 			viewsToday;
		private long			viewsThisWeek;
	
		/**
		* Constructor
		* @param recruiterId 	- Unique identifier of the Recruiter
		*/
		public RecruiterStat(String recruiterId, LocalDateTime startOfDay) {
			this.recruiterId 		= recruiterId;
			this.startOfDay			= startOfDay;
		}
		
		/**
		* Processes Event to update the statistics
		* @param event - View statistic relating to the Recruiter
		*/
		public void processEvent(SupplyAndDemandEvent event) {
			
			this.viewsThisWeek = this.viewsThisWeek+1;
			
			if (event.getCreated().isAfter(this.startOfDay)) {
				this.viewsToday = this.viewsToday + 1;
			}
			
		}
			
		/**
		* Returns the unique Identifier of the Recruiter
		* @return Unique identifier of the Recruiter
		*/
		public String getRecruiterId() {
			return this.recruiterId;
		}
		
		/**
		* Returns the number of times the Recruiter views today
		* @return number of views today
		*/
		public long getViewsToday() {
			return this.viewsToday;
		}
		
		/**
		* Returns the number of times the Recruiter viewes this week
		* @return number of views this week
		*/
		public long getViewsThisWeek() {
			return this.viewsThisWeek;
		}
	}
}