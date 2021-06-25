package com.arenella.recruit.candidates.services;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.arenella.recruit.candidates.beans.CandidateFilterOptions;
import com.arenella.recruit.candidates.beans.CandidateRoleStats;
import com.arenella.recruit.candidates.beans.CandidateSearchEvent;
import com.arenella.recruit.candidates.dao.CandidateDao;
import com.arenella.recruit.candidates.entities.CandidateRoleStatsView;
import com.arenella.recruit.candidates.enums.COUNTRY;
import com.arenella.recruit.candidates.enums.FUNCTION;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
* Services for retrieving statistics relating to Candidates
* @author K Parkings
*/
@Service
public class CandidateStatisticsServiceImpl implements CandidateStatisticsService{

	@Autowired
	private CandidateDao candidateDao;
	
	@Autowired
	ObjectMapper om;
	
	/**
	* Refer to StatisticsService for details 
	*/
	@Override
	public Long fetchNumberOfAvailableCandidates() {
		return candidateDao.countByAvailable(true);
	}

	/**
	* Refer to StatisticsService for details
	*/
	@Override
	public List<CandidateRoleStats> fetchCandidateRoleStats() {
		
		return candidateDao.getCandidateRoleStats().stream().map(stat -> CandidateRoleStatsView.convertFromView(stat)).collect(Collectors.toCollection(LinkedList::new));
	}
	
	/**
	* Refer to the StatisticsService for details 
	*/
	@Override
	public void logCandidateSearchEvent(CandidateFilterOptions filterOptions) {
		
		boolean isAdminUser	= SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().filter(role -> role.getAuthority().equals("ROLE_ADMIN")).findAny().isPresent();
		
		/**
		* We dont want to log these events for Admin users
		*/
		if (isAdminUser) {
			return;
		}
		
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		
		Set<CandidateSearchEvent> events = new HashSet<>();
		
		if (isAllOptionsAvailable(filterOptions)) {
			events = this.allOptionsAvailable(userId, filterOptions);
		} else if (isCountryAndFunctionAvailable(filterOptions)) {
			events = this.countryAndFunctionAvailable(userId, filterOptions);
		} else if (isCountryAndSkillAvailable(filterOptions)) {
			events = this.countryAndSkillAvailable(userId, filterOptions);
		} else if (isSkillAndFunctionAvailable(filterOptions)) {
			events = this.skillAndFunctionAvailable(userId, filterOptions);
		} else if (isOnlySkillAvailable(filterOptions)) {
			events = this.onlySkillAvailable(userId, filterOptions);
		} else if (isOnlyFunctionAvailable(filterOptions)) {
			events = this.onlyFunctionAvailable(userId, filterOptions);
		} else if (isOnlyCountryAvailable(filterOptions)) {
			events = this.onlyCountryAvailable(userId, filterOptions);
		}
				
		
		events.forEach(event -> {
			try {
				System.out.println("" + om.writeValueAsString(event));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		});
		
	}
	
	public boolean isAllOptionsAvailable(CandidateFilterOptions filterOptions) {
		return !filterOptions.getCountries().isEmpty() && !filterOptions.getFunctions().isEmpty() && !filterOptions.getSkills().isEmpty();
	}
	
	public boolean isCountryAndFunctionAvailable(CandidateFilterOptions filterOptions) {
		return !filterOptions.getCountries().isEmpty() && !filterOptions.getFunctions().isEmpty() && filterOptions.getSkills().isEmpty();
	}
	
	public boolean isCountryAndSkillAvailable(CandidateFilterOptions filterOptions) {
		return !filterOptions.getCountries().isEmpty() && filterOptions.getFunctions().isEmpty() && !filterOptions.getSkills().isEmpty();
	}
	
	public boolean isSkillAndFunctionAvailable(CandidateFilterOptions filterOptions) {
		return filterOptions.getCountries().isEmpty() && filterOptions.getFunctions().isEmpty() && !filterOptions.getSkills().isEmpty();
	}
	
	public boolean isOnlySkillAvailable(CandidateFilterOptions filterOptions) {
		return filterOptions.getCountries().isEmpty() && filterOptions.getFunctions().isEmpty() && !filterOptions.getSkills().isEmpty();
	}
	
	public boolean isOnlyFunctionAvailable(CandidateFilterOptions filterOptions) {
		return filterOptions.getCountries().isEmpty() && !filterOptions.getFunctions().isEmpty() && !filterOptions.getSkills().isEmpty();
	}
	public boolean isOnlyCountryAvailable(CandidateFilterOptions filterOptions) {
		return !filterOptions.getCountries().isEmpty() && filterOptions.getFunctions().isEmpty() && filterOptions.getSkills().isEmpty();
	};
	
	public Set<CandidateSearchEvent> allOptionsAvailable(String userId, CandidateFilterOptions filterOptions) {
		
		Set<CandidateSearchEvent> events = new HashSet<>();
		
		filterOptions.getSkills().stream().forEach(skill -> {
			
			filterOptions.getCountries().stream().forEach(country -> {
				
				filterOptions.getFunctions().stream().forEach(function -> {
					events.add(this.generateEvent(userId, skill, country, function, filterOptions));
				});
				
			});
			
		});
		
		return events;
		
	}
	
	
	public Set<CandidateSearchEvent> countryAndFunctionAvailable(String userId, CandidateFilterOptions filterOptions) {
		
		Set<CandidateSearchEvent> events = new HashSet<>();
			
		filterOptions.getCountries().stream().forEach(country -> {
			
			filterOptions.getFunctions().stream().forEach(function -> {
				events.add(this.generateEvent(userId, null, country, function, filterOptions));
			});
			
		});
		
		return events;
		
	}
	
	
	public Set<CandidateSearchEvent> countryAndSkillAvailable(String userId, CandidateFilterOptions filterOptions) {
		
		Set<CandidateSearchEvent> events = new HashSet<>();
		
		filterOptions.getSkills().stream().forEach(skill -> {
			
			filterOptions.getCountries().stream().forEach(country -> {
				
					events.add(this.generateEvent(userId, skill, country, null, filterOptions));
				
			});
			
		});
		
		return events;
		
	}
	
	public Set<CandidateSearchEvent> skillAndFunctionAvailable(String userId, CandidateFilterOptions filterOptions) {
		
		Set<CandidateSearchEvent> events = new HashSet<>();
		
		filterOptions.getSkills().stream().forEach(skill -> {
			
			filterOptions.getFunctions().stream().forEach(function -> {
				events.add(this.generateEvent(userId, skill, null, function, filterOptions));
			});
			
		});
		
		return events;
		
	}
	public Set<CandidateSearchEvent> onlySkillAvailable(String userId, CandidateFilterOptions filterOptions) {
		return filterOptions.getSkills().stream().map(skill -> (CandidateSearchEvent)this.generateEvent(userId, skill, null, null, filterOptions)).collect(Collectors.toSet());
	}
	public Set<CandidateSearchEvent> onlyFunctionAvailable(String userId, CandidateFilterOptions filterOptions) {
		return filterOptions.getFunctions().stream().map(function -> (CandidateSearchEvent)this.generateEvent(userId, null, null, function, filterOptions)).collect(Collectors.toSet());
	}
	
	public Set<CandidateSearchEvent> onlyCountryAvailable(String userId, CandidateFilterOptions filterOptions) {
		return filterOptions.getCountries().stream().map(country -> (CandidateSearchEvent)this.generateEvent(userId, null, country, null, filterOptions)).collect(Collectors.toSet());
	};
	
	private CandidateSearchEvent generateEvent(String userId, String skill, COUNTRY country, FUNCTION function, CandidateFilterOptions filterOptions) {
		
		return CandidateSearchEvent
							.builder()
								.country(country)
								.dutch(filterOptions.getDutch().orElse(null))
								.english(filterOptions.getEnglish().orElse(null))
								.freelance(filterOptions.isFreelance())
								.french(filterOptions.getFrench().orElse(null))
								.function(function)
								.perm(filterOptions.isPerm())
								.skill(skill)
								.userId(userId)
								.yearsExperienceGtEq(filterOptions.getYearsExperienceGtEq())
								.yearsExperienceLtEq(filterOptions.getYearsExperienceLtEq())
							.build();
		
	}
	

}
