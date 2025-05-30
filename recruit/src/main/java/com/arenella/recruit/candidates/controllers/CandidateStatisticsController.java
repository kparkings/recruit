package com.arenella.recruit.candidates.controllers;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.arenella.recruit.candidates.beans.RecruiterStats;
import com.arenella.recruit.candidates.controllers.NewCandidateSummaryAPIOutbound.NewCandidateSummaryAPIOutboundBuilder;
import com.arenella.recruit.candidates.controllers.NewCandidatesAPIOutbound.NewCandidatesAPIOutboundBuilder;
import com.arenella.recruit.candidates.enums.FUNCTION;
import com.arenella.recruit.candidates.services.CandidateStatisticsService;
import com.arenella.recruit.candidates.services.CandidateStatisticsService.NEW_STATS_TYPE;

/**
* Provides statistics relating to Candidates
* @author K Parkings
*/
@RestController
public class CandidateStatisticsController {
	
	public enum STAT_PERIOD {WEEK, DAY}
	
	@Autowired
	private CandidateStatisticsService candidateStatisticsService;
	
	/**
	* Returns the total number of candidates actively 
	* looking for a new role
	* @return number of available candidates
	*/
	@GetMapping(path="candidate/stats/total-active")
	public ResponseEntity<Long> fetchNumberOfCandidates() {
		return ResponseEntity.ok(candidateStatisticsService.fetchNumberOfAvailableCandidates());
	}
	
	/**
	* Returns statistics relating to search behavior 
	* by the recruiters
	* @return Search statistics
	*/
	@GetMapping(path="/candidate/public/search-history")
	public ResponseEntity<SearchStats> fetchSearchHistory(){
		return ResponseEntity.ok(new SearchStats(candidateStatisticsService.fetchCandidateSearchEvents(30)));
	}
	
	/**
	* Returns a breakdown of the number of available candidates
	* per Function
	* @return Stats about available Candidates by role
	*/
	@PostMapping(path="candidate/public/function-count")
	public ResponseEntity<List<CandidateRoleStatsAPIOutbound>> fetchCandidateRoleStats(@RequestBody RoleTotalsFiltersAPIInbound filters) throws Exception{
		return ResponseEntity.ok(candidateStatisticsService.fetchCandidateRoleStats(RoleTotalsFiltersAPIInbound.toDomain(filters)).stream().map(CandidateRoleStatsAPIOutbound::convertFromDomain).collect(Collectors.toCollection(LinkedList::new)));
	}
	
	/**
	* Endpoint for retrievens a breakdown of available/unavailable candidates by country
	* for a give function
	* @return
	*/
	@GetMapping(path="candidate/public/country-breakdown/{function}")
	public ResponseEntity<Set<RoleTotalsAPIOutbound>> fetchCountryAvailabilityBreakdownForFunction(@PathVariable("function")FUNCTION function) throws Exception{
		return ResponseEntity.ok(candidateStatisticsService.fetchCountryAvailabilityBreakdownForFunction(function).stream().map(RoleTotalsAPIOutbound::convertFromDomain).collect(Collectors.toCollection(LinkedHashSet::new)));
	}
	
	/**
	* Returns statistics for new Candidates. New candidates refer to Candidates added since
	* the endpoint was last called
	* @return statistics for new candidates
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping(path="candidate/stat/new-addtions", produces="application/json")
	public ResponseEntity<NewCandidatesAPIOutbound> fetchNewCandidates(){
		
		LocalDate 						lastRunDate = this.candidateStatisticsService.getLastRunDateNewCandidateStats(NEW_STATS_TYPE.NEW_CANDIDATES);
		NewCandidatesAPIOutboundBuilder builder 	= NewCandidatesAPIOutbound.builder();
		
		this.candidateStatisticsService.fetchNewCandidates(lastRunDate).forEach(builder::addCandidate);
		
		return new ResponseEntity<>(builder.build(), HttpStatus.OK);
	}
	
	/**
	* Returns a summary of new candidates since the last time the endpoint was called.
	* @return summary statistics for new candidates
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping(path="candidate/stat/new-candidate-summary", produces="application/json")
	public ResponseEntity<NewCandidateSummaryAPIOutbound> fetchNewCandidatesBreakdown(){
		
		LocalDate 								lastRunDate = this.candidateStatisticsService.getLastRunDateNewCandidateStats(NEW_STATS_TYPE.NEW_CANDIDATE_BREAKDOWN);
		NewCandidateSummaryAPIOutboundBuilder 	builder 	= NewCandidateSummaryAPIOutbound.builder();
		
		this.candidateStatisticsService.fetchNewCandidates(lastRunDate).forEach(builder::addCandidate);
		
		return new ResponseEntity<>(builder.build(), HttpStatus.OK);
	}
	
	/**
	* Returns Stat's relating to Searches performed
	* by a Recruiter
	* @return Recruiters login history
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping(path="candidate/stat/search/{recruiterId}")
	public ResponseEntity<RecruiterStats> fetchSearchStatsForRecruiter(@PathVariable("recruiterId") String recruiterId, @RequestParam("period") STAT_PERIOD period){
		return new ResponseEntity<>(this.candidateStatisticsService.fetchSearchStatsForRecruiter(recruiterId, period), HttpStatus.OK);
	}
	
	/**
	* Returns all profile viewed events for a given Candidate
	* @param candidateId - Id of Candidate to filter views on
	* @return Events
	* @throws IllegalAccessException
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_CANDIDATE')")
	@GetMapping(path="candidate/stat/candidate-views/{candidateId}")
	public ResponseEntity<Set<BucketAPIOutbound>> fetchCandidateProfileViewedEventForCandidate(@PathVariable("candidateId") String candidateId) throws IllegalAccessException {
		
		Set<BucketAPIOutbound> buckets = CandidateProfileViewFormatterUtil
				.byWeek(this.candidateStatisticsService.fetchCandidateProfileViewedEventForCandidate(candidateId))
				.stream().map(BucketAPIOutbound::fromDomain).collect(Collectors.toCollection(LinkedHashSet::new));
		
		return new ResponseEntity<>(buckets, HttpStatus.OK);
	}
	
	/**
	* Returns all profile viewed events generated by a specific Recruiter
	* viewing Candidate profiles
	* @param recruiterId - Id of Recruiter to filter views on
	* @return Events
	* @throws IllegalAccessException
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_RECRUITER')")
	@GetMapping(path="candidate/stat/recruiter-views/{recruiterId}")
	public ResponseEntity<Set<BucketAPIOutbound>> fetchCandidateProfileViewedEventForRecruiter(@PathVariable("recruiterId") String recruiterId) throws IllegalAccessException {
		
		Set<BucketAPIOutbound> buckets = CandidateProfileViewFormatterUtil
				.byWeek(this.candidateStatisticsService.fetchCandidateProfileViewedEventForRecruiter(recruiterId))
				.stream().map(BucketAPIOutbound::fromDomain).collect(Collectors.toCollection(LinkedHashSet::new));
		
		return new ResponseEntity<>(buckets, HttpStatus.OK);
	}
	
	/**
	* Returns all Candidate profile viewed events that took place up to and including
	* the past maxYears
	* @return Events
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping(path="candidate/stat/views/years/{maxYears}")
	public ResponseEntity<Set<BucketAPIOutbound>> fetchCandidateProfileViewedEvents(@PathVariable("maxYears") int maxYears) {
		
		Set<BucketAPIOutbound> buckets = CandidateProfileViewFormatterUtil
				.byWeek(this.candidateStatisticsService.fetchCandidateProfileViewedEventNewerThat(LocalDateTime.now().minusYears(maxYears)))
				.stream().map(BucketAPIOutbound::fromDomain).collect(Collectors.toCollection(LinkedHashSet::new));
		
		return new ResponseEntity<>(buckets, HttpStatus.OK);
	}
	
	/**
	* Registers an event where the authenticated Recruiter has viewed a candidates profile
	* @return Status
	*/
	@PreAuthorize("hasRole('ROLE_RECRUITER')")
	@PostMapping(path="candidate/stat/views/{candidateId}")
	public ResponseEntity<Void> registerCandidateProfileView(@PathVariable("candidateId") String candidateId, Principal principal) {
		this.candidateStatisticsService.registerCandidateProfileView(candidateId, principal.getName());
		return  ResponseEntity.ok().build();
	}
	
}