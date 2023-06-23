package com.arenella.recruit.authentication.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arenella.recruit.authentication.beans.AuthenticatedEvent;
import com.arenella.recruit.authentication.services.AuthenticationStatisticsService;

/**
* Controller / REST API for Authentication Statistics
* @author K Parkings
*/
@RestController
public class AuthenticationStatisticsController {
	
	@Autowired
	private AuthenticationStatisticsService authenticationStatisticsService;
	
	/**
	* Returns statisitc's of logins related to users
	* @return
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping(path="authentication/stats")
	public ResponseEntity<LoginTrendStats> fetchLoginTrendStats() {
		
		Set<AuthenticatedEvent> 	eventsYear 	= this.authenticationStatisticsService.fetchAuthenticatedEventsSince(LocalDateTime.of(LocalDate.now().minusYears(1), LocalTime.MIDNIGHT));
		LoginTrendStats 			stats 		= new LoginTrendStats(eventsYear);
		
		return ResponseEntity.ok().body(stats);
	}
	
	/**
	* Summary of Statistics relating to Logins of various periods
	* @author K Parkings
	*/
	public static class LoginTrendStats{
		
		Set<AuthenticatedEvent> eventsToday 		= new LinkedHashSet<>();
		Set<AuthenticatedEvent> eventsWeek 			= new LinkedHashSet<>();
		Set<DayStat> 			weekStats			= new LinkedHashSet<>();
		Set<DayStat> 			threeMonthStats		= new LinkedHashSet<>();	
		Set<DayStat> 			yearStats			= new LinkedHashSet<>();
		
		/**
		* 
		* @return
		*/
		public Set<AuthenticatedEvent> getEventsToday(){
			return this.eventsToday;
		}
		
		public Set<AuthenticatedEvent> getEventsWeek(){
			return this.eventsWeek;
		}
		
		/**
		* 
		* @return
		*/
		public Set<DayStat> getWeekStats(){
			return this.weekStats;
		}
		
		/**
		* 
		* @return
		*/
		public Set<DayStat> getThreeMonthStats(){
			return this.threeMonthStats;
		}	
		
		/**
		* 
		* @return
		*/
		public Set<DayStat> getYearStats(){
			return this.yearStats;
		}
		
		/**
		* 
		* @param eventsToday
		* @param eventsWeek
		* @param events3Months
		* @param eventsYear
		*/
		public LoginTrendStats(Set<AuthenticatedEvent> eventsYear) {
			
			this.eventsToday.addAll(eventsYear.stream().filter(s -> s.getLoggedInAt().isAfter(LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT))).collect(Collectors.toCollection(LinkedHashSet::new)));
			this.eventsWeek.addAll(eventsYear.stream().filter(s -> s.getLoggedInAt().isAfter(LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT).minusWeeks(1))).collect(Collectors.toCollection(LinkedHashSet::new)));
			
			this.weekStats 			= this.generateBuckets(LocalDate.now().minusDays(7), 	LocalDate.now().plusDays(1));
			this.threeMonthStats 	= this.generateBuckets(LocalDate.now().minusMonths(3), 	LocalDate.now().plusDays(1));
			this.yearStats 			= this.generateBuckets(LocalDate.now().minusYears(1), 	LocalDate.now().plusDays(1));
			
			fillBuckets(weekStats, 			eventsYear.stream().filter(s -> s.getLoggedInAt().toLocalDate().isAfter(LocalDate.now().minusDays(7))).collect(Collectors.toCollection(LinkedHashSet::new)));
			fillBuckets(threeMonthStats, 	eventsYear.stream().filter(s -> s.getLoggedInAt().toLocalDate().isAfter(LocalDate.now().minusDays(90))).collect(Collectors.toCollection(LinkedHashSet::new)));
			fillBuckets(yearStats, 			eventsYear.stream().filter(s -> s.getLoggedInAt().toLocalDate().isAfter(LocalDate.now().minusYears(1))).collect(Collectors.toCollection(LinkedHashSet::new)));
			
		}
		
		/**
		* A Single days statistics
		* @author K Parkings
		*/
		public static class DayStat{
			
			private LocalDate 		date;
			private List<String> 	userLogins = new LinkedList<>(); 
			
			/**
			* Constructor
			* @param date - date the statistics are for
			*/
			public DayStat(LocalDate date) {
				this.date = date;
			}
			
			/**
			* Returns the date the statistics are for
			* @return date the statistics are for
			*/
			public LocalDate getDate() {
				return this.date;
			}
			
			/**
			* Returns the individual users logins
			* @return
			*/
			public List<String> getLogins(){
				return this.userLogins;
			}
			
			/**
			* Adds an event to the Date
			* @param userId - UserId of the user that loggedIn on that day
			*/
			public void addUserLogin(String userId) {
				this.userLogins.add(userId);
			}
			
		}
		
		/**
		* Cycles over the events adding them to the correct day bucket
		* @param events - Events to add to buckets
		*/
		public void fillBuckets(Set<DayStat> buckets, Set<AuthenticatedEvent> events) {
			events.stream().forEach(e -> {
				DayStat bucket = buckets.stream().filter(b -> b.getDate().equals(e.getLoggedInAt().toLocalDate())).findAny().get();
				bucket.addUserLogin(e.getUserId());
			});
		}
		
		/**
		* Method for generating buckets to hold information between 
		* two dates
		* @param start 	- First Date
		* @param end	- Last Date
		* @return buckets
		*/
		private Set<DayStat> generateBuckets(LocalDate start, LocalDate end) {
			
			Set<DayStat> 	stats 		= new LinkedHashSet<>();
			LocalDate 		current 	= start;
			
			while (current.isBefore(end)) {
				stats.add(new DayStat(current));
				current = current.plusDays(1);
			}
			
			return stats;
			
		}
		
	}
	
}