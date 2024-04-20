package com.arenella.recruit.candidates.services;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.arenella.recruit.candidates.beans.Candidate;
import com.arenella.recruit.candidates.beans.CandidateFilterOptions;
import com.arenella.recruit.candidates.beans.CandidateRoleStats;
import com.arenella.recruit.candidates.beans.CandidateSearchEvent;
import com.arenella.recruit.candidates.beans.RecruiterStats;
import com.arenella.recruit.candidates.controllers.CandidateStatisticsController.STAT_PERIOD;
import com.arenella.recruit.candidates.dao.CandidateSearchStatisticsDao;
import com.arenella.recruit.candidates.dao.NewCandidateStatsTypeDao;
import com.arenella.recruit.candidates.entities.CandidateRoleStatsView;
import com.arenella.recruit.candidates.entities.CandidateSearchEventEntity;
import com.arenella.recruit.candidates.enums.COUNTRY;
import com.arenella.recruit.candidates.enums.FUNCTION;
import com.arenella.recruit.candidates.repos.CandidateRepository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;

/**
* Services for retrieving statistics relating to Candidates
* @author K Parkings
*/
@Service
public class CandidateStatisticsServiceImpl implements CandidateStatisticsService{

	@Autowired
	private ElasticsearchClient 				esClient;
	
	@Autowired
	private CandidateSearchStatisticsDao 		statisticsDao;
	
	@Autowired
	private NewCandidateStatsTypeDao			newCandidateStatsTypeDao;
	
	@Autowired
	private CandidateRepository					candidateRepo;
	
	/**
	* Refer to StatisticsService for details 
	*/
	@Override
	public Long fetchNumberOfAvailableCandidates() {
		return candidateRepo.getCountByAvailable(true);
	}

	/**
	* Refer to StatisticsService for details
	*/
	@Override
	public List<CandidateRoleStats> fetchCandidateRoleStats() throws Exception{
		
		return candidateRepo.getCandidateRoleStats(esClient).stream().map(CandidateRoleStatsView::convertFromView).collect(Collectors.toCollection(LinkedList::new));
	}

	/**
	* Refer to StatisticsService for details
	*/
	@Override
	public RecruiterStats fetchSearchStatsForRecruiter(String recruiterId, STAT_PERIOD period) {
		
		LocalDate since = period == STAT_PERIOD.DAY ? LocalDate.now() : LocalDate.now().minusWeeks(1);
		
		Set<CandidateSearchEvent> events =  this.statisticsDao.fetchEventForRecruiter(recruiterId, since);
		
		return new RecruiterStats(events);
		
	}
	
	/**
	* Refer to the StatisticsService for details 
	*/
	@Override
	public void logCandidateSearchEvent(CandidateFilterOptions filterOptions) {
		
		boolean isAdminUser	= SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().filter(role -> role.getAuthority().equals("ROLE_ADMIN")).findAny().isPresent();
		
		/**
		* We don't want to log these events for Admin users
		*/
		if (isAdminUser) {
			return;
		}
		
		String 						userId 		= SecurityContextHolder.getContext().getAuthentication().getName();
		UUID						searchId	= UUID.randomUUID();
		Set<CandidateSearchEvent> 	events 		= new HashSet<>();
		
		if (isAllOptionsAvailable(filterOptions)) {
			events = this.allOptionsAvailable(userId, searchId, filterOptions);
		} else if (isCountryAndFunctionAvailable(filterOptions)) {
			events = this.countryAndFunctionAvailable(userId, searchId, filterOptions);
		} else if (isCountryAndSkillAvailable(filterOptions)) {
			events = this.countryAndSkillAvailable(userId, searchId, filterOptions);
		} else if (isSkillAndFunctionAvailable(filterOptions)) {
			events = this.skillAndFunctionAvailable(userId, searchId, filterOptions);
		} else if (isOnlySkillAvailable(filterOptions)) {
			events = this.onlySkillAvailable(userId, searchId, filterOptions);
		} else if (isOnlyFunctionAvailable(filterOptions)) {
			events = this.onlyFunctionAvailable(userId, searchId, filterOptions);
		} else if (isOnlyCountryAvailable(filterOptions)) {
			events = this.onlyCountryAvailable(userId, searchId, filterOptions);
		} else {
			events.add(this.onlyGeneralValuesAvailable(userId, searchId, filterOptions));
		}
			
		statisticsDao.saveAll(events.stream().map(CandidateSearchEventEntity::toEntity).collect(Collectors.toSet()));
		
	}
	
	/**
	* Refer to StatisticsService for details 
	*/
	@Override
	public Set<Candidate> fetchNewCandidates(LocalDate since) {
		return this.candidateRepo.findNewSinceLastDate(since);
	}

	/**
	* Refer to StatisticsService for details 
	*/
	@Override
	public LocalDate getLastRunDateNewCandidateStats(NEW_STATS_TYPE statsType) {
		return this.newCandidateStatsTypeDao.fetchAndSetLastRequested(statsType);
	}
	
	/**
	* Whether or not all values present in Search (And general search attributes)
	* @param filterOptions - Contains all search values
	* @return Whether or not all values available
	*/
	public boolean isAllOptionsAvailable(CandidateFilterOptions filterOptions) {
		return !filterOptions.getCountries().isEmpty() && !filterOptions.getFunctions().isEmpty() && !filterOptions.getSkills().isEmpty();
	}
	
	/**
	* Whether or not only Country and Function values present in Search (And general search attributes)
	* @param filterOptions - Contains all search values
	* @return Whether or not only Country and Function values available
	*/
	public boolean isCountryAndFunctionAvailable(CandidateFilterOptions filterOptions) {
		return !filterOptions.getCountries().isEmpty() && !filterOptions.getFunctions().isEmpty() && filterOptions.getSkills().isEmpty();
	}
	
	/**
	* Whether or not only Skill and Country values present in Search (And general search attributes)
	* @param filterOptions - Contains all search values
	* @return Whether or not only Skill and Country values available
	*/
	public boolean isCountryAndSkillAvailable(CandidateFilterOptions filterOptions) {
		return !filterOptions.getCountries().isEmpty() && filterOptions.getFunctions().isEmpty() && !filterOptions.getSkills().isEmpty();
	}
	
	/**
	* Whether or not only Skill and Function values present in Search (And general search attributes)
	* @param filterOptions - Contains all search values
	* @return Whether or not only Skill and Function values available
	*/
	public boolean isSkillAndFunctionAvailable(CandidateFilterOptions filterOptions) {
		return filterOptions.getCountries().isEmpty() && filterOptions.getFunctions().isEmpty() && !filterOptions.getSkills().isEmpty();
	}
	
	/**
	* Whether or not only Skill values present in Search (And general search attributes)
	* @param filterOptions - Contains all search values
	* @return Whether or not only Skill values available
	*/
	public boolean isOnlySkillAvailable(CandidateFilterOptions filterOptions) {
		return filterOptions.getCountries().isEmpty() && filterOptions.getFunctions().isEmpty() && !filterOptions.getSkills().isEmpty();
	}
	
	/**
	* Whether or not only Function values present in Search (And general search attributes)
	* @param filterOptions - Contains all search values
	* @return Whether or not only Function values available
	*/
	public boolean isOnlyFunctionAvailable(CandidateFilterOptions filterOptions) {
		return filterOptions.getCountries().isEmpty() && !filterOptions.getFunctions().isEmpty() && !filterOptions.getSkills().isEmpty();
	}
	
	/**
	* Whether or not only Country values present in Search (And general search attributes)
	* @param filterOptions - Contains all search values
	* @return Whether or not only county values available
	*/
	public boolean isOnlyCountryAvailable(CandidateFilterOptions filterOptions) {
		return !filterOptions.getCountries().isEmpty() && filterOptions.getFunctions().isEmpty() && filterOptions.getSkills().isEmpty();
	}
	
	/**
	* Process Event for Search where only Options selected (And general search attributes)
	* @param userId			- Unique Id of user who performed the Search
	* @param filterOptions	- General search attributes
	* @return Events
	*/
	public Set<CandidateSearchEvent> allOptionsAvailable(String userId, UUID searchId, CandidateFilterOptions filterOptions) {
		
		Set<CandidateSearchEvent> events = new HashSet<>();
		
		filterOptions.getSkills().stream().forEach(skill -> 
			filterOptions.getCountries().stream().forEach(country -> 
				filterOptions.getFunctions().stream().forEach(function -> 
					events.add(this.generateEvent(userId, searchId ,skill, country, function, filterOptions))
				)
			)
		);
		
		return events;
		
	}
	
	/**
	* Process Event for Search where Country and Function selected (And general search attributes)
	* @param userId			- Unique Id of user who performed the Search
	* @param filterOptions	- General search attributes
	* @return Events
	*/
	public Set<CandidateSearchEvent> countryAndFunctionAvailable(String userId, UUID searchId, CandidateFilterOptions filterOptions) {
		
		Set<CandidateSearchEvent> events = new HashSet<>();
			
		filterOptions.getCountries().stream().forEach(country -> 
			filterOptions.getFunctions().stream().forEach(function -> 
				events.add(this.generateEvent(userId, searchId, null, country, function, filterOptions))
			)
		);
		
		return events;
		
	}
	
	/**
	* Process Event for Search where only Skill and Country selected (And general search attributes)
	* @param userId			- Unique Id of user who performed the Search
	* @param filterOptions	- General search attributes
	* @return Events
	*/
	public Set<CandidateSearchEvent> countryAndSkillAvailable(String userId, UUID searchId, CandidateFilterOptions filterOptions) {
		
		Set<CandidateSearchEvent> events = new HashSet<>();
		
		filterOptions.getSkills().stream().forEach(skill -> 
			filterOptions.getCountries().stream().forEach(country -> 
				events.add(this.generateEvent(userId, searchId, skill, country, null, filterOptions))
			)
		);
		
		return events;
		
	}
	
	/**
	* Process Event for Search where only Skill and Function selected (And general search attributes)
	* @param userId			- Unique Id of user who performed the Search
	* @param filterOptions	- General search attributes
	* @return Events
	*/
	public Set<CandidateSearchEvent> skillAndFunctionAvailable(String userId, UUID searchId, CandidateFilterOptions filterOptions) {
		
		Set<CandidateSearchEvent> events = new HashSet<>();
		
		filterOptions.getSkills().stream().forEach(skill -> 
			filterOptions.getFunctions().stream().forEach(function -> 
				events.add(this.generateEvent(userId, searchId, skill, null, function, filterOptions))
			)			
		);
		
		return events;
		
	}
	
	/**
	* Process Event for Search where only Skill was selected (And general search attributes)
	* @param userId			- Unique Id of user who performed the Search
	* @param filterOptions	- General search attributes
	* @return Events
	*/
	public Set<CandidateSearchEvent> onlySkillAvailable(String userId, UUID searchId, CandidateFilterOptions filterOptions) {
		return filterOptions.getSkills().stream().map(skill -> this.generateEvent(userId, searchId, skill, null, null, filterOptions)).collect(Collectors.toSet());
	}
	
	/**
	* Process Event for Search where only Function was selected (And general search attributes)
	* @param userId			- Unique Id of user who performed the Search
	* @param filterOptions	- General search attributes
	* @return Events
	*/
	public Set<CandidateSearchEvent> onlyFunctionAvailable(String userId, UUID searchId, CandidateFilterOptions filterOptions) {
		return filterOptions.getFunctions().stream().map(function -> this.generateEvent(userId, searchId, null, null, function, filterOptions)).collect(Collectors.toSet());
	}
	
	/**
	* Process Event for Search where only Country was selected (And general search attributes)
	* @param userId			- Unique Id of user who performed the Search
	* @param filterOptions	- General search attributes
	* @return Events
	*/
	public Set<CandidateSearchEvent> onlyCountryAvailable(String userId, UUID searchId, CandidateFilterOptions filterOptions) {
		return filterOptions.getCountries().stream().map(country -> this.generateEvent(userId, searchId, null, country, null, filterOptions)).collect(Collectors.toSet());
	}
	
	/**
	* Generates an event where no multiple search values where selected
	* @param userId			- Id of the User who performed the Search
	* @param filterOptions	- Other filter options
	* @return
	*/
	public CandidateSearchEvent onlyGeneralValuesAvailable(String userId, UUID searchId, CandidateFilterOptions filterOptions) {
		return generateEvent(userId, searchId, null, null, null, filterOptions);
	}
	
	/**
	* Produces an event based upon the provided attribute values
	* @param userId			- Id of the User who performed the Search
	* @param skill			- Skill Searched for
	* @param country		- Country searched on
	* @param function		- Function searched on
	* @param filterOptions	- Other filter options
	* @return Event
	*/
	private CandidateSearchEvent generateEvent(String userId, UUID searchId, String skill, COUNTRY country, FUNCTION function, CandidateFilterOptions filterOptions) {
		
		return CandidateSearchEvent
							.builder()
								.searchId(searchId)
								.country(country)
								.dutch(filterOptions.getDutch().orElse(null))
								.english(filterOptions.getEnglish().orElse(null))
								.freelance(filterOptions.isFreelance().orElse(true))
								.french(filterOptions.getFrench().orElse(null))
								.function(function)
								.perm(filterOptions.isPerm().orElse(true))
								.skill(skill)
								.userId(userId)
								.yearsExperienceGtEq(filterOptions.getYearsExperienceGtEq())
								.yearsExperienceLtEq(filterOptions.getYearsExperienceLtEq())
							.build();
		
	}

}