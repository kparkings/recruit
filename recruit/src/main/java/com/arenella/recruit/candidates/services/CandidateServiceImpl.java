package com.arenella.recruit.candidates.services;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.arenella.recruit.adapters.events.CandidateNoLongerAvailableEvent;
import com.arenella.recruit.candidates.adapters.CandidateCreatedEvent;
import com.arenella.recruit.candidates.adapters.ExternalEventPublisher;
import com.arenella.recruit.candidates.beans.Candidate;
import com.arenella.recruit.candidates.beans.CandidateFilterOptions;
import com.arenella.recruit.candidates.beans.CandidateSearchAccuracyWrapper;
import com.arenella.recruit.candidates.beans.CandidateSearchAlert;
import com.arenella.recruit.candidates.beans.PendingCandidate;
import com.arenella.recruit.candidates.controllers.CandidateController.CANDIDATE_UPDATE_ACTIONS;
import com.arenella.recruit.candidates.dao.CandidateDao;
import com.arenella.recruit.candidates.dao.PendingCandidateDao;
import com.arenella.recruit.candidates.entities.CandidateEntity;
import com.arenella.recruit.candidates.entities.PendingCandidateEntity;
import com.arenella.recruit.candidates.utils.CandidateSuggestionUtil;
import com.arenella.recruit.candidates.utils.CandidateSuggestionUtil.suggestion_accuracy;
import com.arenella.recruit.candidates.utils.SkillsSynonymsUtil;
import com.arenella.recruit.curriculum.enums.FileType;
import com.arenella.recruit.candidates.dao.CandidateSearchAlertDao;

/**
* Provides services related to Candidates
* @author K Parkings
*/
@Service
public class CandidateServiceImpl implements CandidateService{
	
	@Autowired
	private CandidateDao 				candidateDao;

	@Autowired
	private PendingCandidateDao 		pendingCandidateDao;

	@Autowired
	private CandidateStatisticsService 	statisticsService;
	
	@Autowired
	private ExternalEventPublisher		externalEventPublisher;
	
	@Autowired
	private CandidateSuggestionUtil		suggestionUtil;
	
	@Autowired
	private SkillsSynonymsUtil			skillsSynonymsUtil;
	
	@Autowired
	private CandidateSearchAlertDao		skillAlertDao;
	
	/**
	* Refer to the CandidateService Interface for Details
	*/
	@Override
	public void persistCandidate(Candidate candidate) {
		
		CandidateEntity entity = CandidateEntity.convertToEntity(candidate);
		
		long candidateId = candidateDao.save(entity).getCandidateId();
		
		this.externalEventPublisher
			.publishCandidateCreatedEvent(CandidateCreatedEvent
					.builder()
						.candidate(candidate)
						.candidateId(String.valueOf(candidateId))
					.build());		
	}

	/**
	* Refer to the CandidateService Interface for Details
	*/
	@Override
	public Page<Candidate> getCandidates(CandidateFilterOptions filterOptions, Pageable pageable) {
		
		this.statisticsService.logCandidateSearchEvent(filterOptions);
		
		this.externalEventPublisher.publishSearchedSkillsEvent(filterOptions.getSkills());
		
		return candidateDao.findAll(filterOptions, pageable).map(candidate -> CandidateEntity.convertFromEntity(candidate));
	}
	
	/**
	* Refer to the CandidateService Interface for Details
	*/
	@Override
	public Set<Candidate> getCandidates(CandidateFilterOptions filterOptions) {
		
		this.statisticsService.logCandidateSearchEvent(filterOptions);
		
		this.externalEventPublisher.publishSearchedSkillsEvent(filterOptions.getSkills());
		
		return StreamSupport.stream(candidateDao.findAll(filterOptions).spliterator(), false)
								.map(candidate -> CandidateEntity.convertFromEntity(candidate)).collect(Collectors.toCollection(LinkedHashSet::new));
	}

	/**
	* Refer to the CandidateService Interface for Details
	*/
	@Override
	public void updateCandidate(String candidateId, CANDIDATE_UPDATE_ACTIONS updateAction) {
		
		Candidate candidate = this.candidateDao.findCandidateById(Long.valueOf(candidateId)).orElseThrow(() -> new RuntimeException("Cannot perform update on unknown Candidate: " + candidateId));
		
		switch (updateAction) {
			case enable: {
				candidate.makeAvailable();
				break;
			}
			case disable: {
				candidate.noLongerAvailable();
				this.externalEventPublisher.publishCandidateNoLongerAvailableEvent(new CandidateNoLongerAvailableEvent(Long.valueOf(candidate.getCandidateId())));
				break;
			}
			default: {
				throw new IllegalArgumentException("Unknown update action requested for Candidate");
			}
		}
		
		this.candidateDao.saveCandidate(candidate);
		
	}

	/**
	* Refer to the CandidateService Interface for Details
	*/
	@Override
	public void flagCandidateAvailability(long candidateId, boolean available) {
		
		CandidateEntity candidate = this.candidateDao.findById(candidateId).orElseThrow(() -> new IllegalArgumentException("Unknown candidate Id " + candidateId));
		
		candidate.setFlaggedAsUnavailable(available);
		
		this.candidateDao.save(candidate);
		
	}

	/**
	* Refer to the CandidateService Interface for Details
	*/
	@Override
	public void persistPendingCandidate(PendingCandidate pendingCandidate) {
		
		if (pendingCandidate.getPendingCandidateId() == null) {
			throw new IllegalArgumentException("Cannot create candidate with no Id ");
		}
		
		if (this.pendingCandidateDao.existsById(pendingCandidate.getPendingCandidateId())) {
			throw new IllegalArgumentException("Cannot create candidate with Id " + pendingCandidate.getPendingCandidateId());
		}
		
		this.pendingCandidateDao.save(PendingCandidateEntity.convertToEntity(pendingCandidate));
		
	}

	/**
	* Refer to the CandidateService Interface for Details
	*/
	@Override
	public Set<PendingCandidate> getPendingCandidates() {
		
		return StreamSupport.stream(this.pendingCandidateDao.findAll().spliterator(), false)
				.map(entity -> PendingCandidateEntity.convertFromEntity(entity)).collect(Collectors.toCollection(LinkedHashSet::new));
	}
	
	/**
	* Refer to the CandidateService Interface for Details
	*/
	@Override
	public suggestion_accuracy doTestCandidateAlert(long candidateId, CandidateFilterOptions filterOptions) {
		
		CandidateFilterOptions suggestionFilterOptions = CandidateFilterOptions
				.builder()
					.dutch(filterOptions.getDutch().isPresent() 		? filterOptions.getDutch().get() 	: null)
					.english(filterOptions.getEnglish().isPresent() 	? filterOptions.getEnglish().get() 	: null)
					.french(filterOptions.getFrench().isPresent() 		? filterOptions.getFrench().get() 	: null)
					.skills(filterOptions.getSkills())
				.build();

		filterOptions.getSkills().clear();
		filterOptions.setDutch(null);
		filterOptions.setEnglish(null);
		filterOptions.setFrench(null);
		
		Set<Candidate> results = this.candidateDao.findCandidates(filterOptions);
		
		if (results.isEmpty()) {
			return suggestion_accuracy.poor;
		}
		
		Candidate candidate = results.stream().findFirst().get();
		
		candidate.getSkills().addAll(this.skillsSynonymsUtil.addtSynonymsForSkills(candidate.getSkills()));
		
		CandidateSearchAccuracyWrapper 	wrappedCandidate 	= new CandidateSearchAccuracyWrapper(candidate);
		
		if (suggestionUtil.isPerfectMatch(wrappedCandidate, suggestionFilterOptions)) {
			return suggestion_accuracy.perfect;
		}
		
		if (suggestionUtil.isExcellentMatch(wrappedCandidate, suggestionFilterOptions)) {
			return suggestion_accuracy.excellent;
		}
		
		if (suggestionUtil.isGoodMatch(wrappedCandidate, suggestionFilterOptions)) {
			return suggestion_accuracy.good;
		}
		
		return suggestion_accuracy.poor;
		
	}
	
	/**
	* Refer to the CandidateService Interface for Details
	*/
	@Override
	public Page<CandidateSearchAccuracyWrapper> getCandidateSuggestions(CandidateFilterOptions filterOptions, Integer maxSuggestions) {
		
		final Set<CandidateSearchAccuracyWrapper> 	suggestions 		= new LinkedHashSet<>();
		
		int 										pageCounter 		= 0;
		Set<String> 								suggestionIds 		= new HashSet<>();
		AtomicReference<suggestion_accuracy> 		accuracy 			=  new AtomicReference<>(suggestion_accuracy.perfect);
		Pageable 									pageable 			= PageRequest.of(0,100);
		
		CandidateFilterOptions 						suggestionFilterOptions = CandidateFilterOptions
																							.builder()
																								.dutch(filterOptions.getDutch().isPresent() 		? filterOptions.getDutch().get() 	: null)
																								.english(filterOptions.getEnglish().isPresent() 	? filterOptions.getEnglish().get() 	: null)
																								.french(filterOptions.getFrench().isPresent() 		? filterOptions.getFrench().get() 	: null)
																								.skills(filterOptions.getSkills())
																							.build();
							
		filterOptions.getSkills().clear();
		filterOptions.setDutch(null);
		filterOptions.setEnglish(null);
		filterOptions.setFrench(null);
		
		while (true) {
		
			Page<Candidate> candidates = candidateDao.findAll(filterOptions, pageable).map(candidate -> CandidateEntity.convertFromEntity(candidate));
			
			candidates.getContent().stream().filter(c -> !suggestionIds.contains(c.getCandidateId())).forEach(candidate -> {
		
				candidate.getSkills().addAll(this.skillsSynonymsUtil.addtSynonymsForSkills(candidate.getSkills()));
				
				CandidateSearchAccuracyWrapper 	wrappedCandidate 	= new CandidateSearchAccuracyWrapper(candidate);
				boolean 						isMatch 			= false;
				
				switch(accuracy.get()) {
					case perfect:{
						isMatch = suggestionUtil.isPerfectMatch(wrappedCandidate, suggestionFilterOptions);
						break;
					}
					case excellent:{
						isMatch = suggestionUtil.isExcellentMatch(wrappedCandidate, suggestionFilterOptions);
						break;
					}
					case good:{
						isMatch = suggestionUtil.isGoodMatch(wrappedCandidate, suggestionFilterOptions);
						break;
					}
					case average:{
						isMatch = suggestionUtil.isAverageMatch(wrappedCandidate, suggestionFilterOptions);
						break;
					}
					case poor:{
						isMatch = suggestionUtil.isPoorMatch(wrappedCandidate, suggestionFilterOptions);
						break;
					}
				}
			
				if (isMatch) {
					suggestions.add(wrappedCandidate);
					suggestionIds.add(candidate.getCandidateId());
				}
				
			});
			
			pageCounter = pageCounter + 1;
			
			if (suggestions.size() >= maxSuggestions) {
				return new PageImpl<CandidateSearchAccuracyWrapper>(suggestions.stream().limit(maxSuggestions).collect(Collectors.toCollection(LinkedList::new)));
			} else if (!(candidates.getTotalPages() >= pageCounter)) {
				
				switch(accuracy.get()) {
					case perfect:{
						accuracy.set(suggestion_accuracy.excellent);
						break;
					}
					case excellent:{
						accuracy.set(suggestion_accuracy.good);
						break;
					}
					case good:{
						accuracy.set(suggestion_accuracy.average);
						break;
					}
					case average:{
						accuracy.set(suggestion_accuracy.poor);
						break;
					}
					case poor:{
						return new PageImpl<CandidateSearchAccuracyWrapper>(suggestions.stream().limit(maxSuggestions).collect(Collectors.toCollection(LinkedList::new)));
					}
					
				} 
				
				pageCounter = 0;
				pageable = PageRequest.of(pageCounter,100);
					
			}
			
			
		}

	}

	/**
	* Refer to the CandidateService for details 
	*/
	@Override
	public void updateCandidatesLastAvailabilityCheck(long candidateId) {
		
		CandidateEntity candidate = this.candidateDao.findById(candidateId).orElseThrow(() -> new IllegalArgumentException("Unknown candidate Id " + candidateId));
		
		candidate.setCandidateAvailabilityChecked();
		
		this.candidateDao.save(candidate);
		
	}

	/**
	* Refer to the CandidateService for details 
	*/
	@Override
	public void addSearchAlert(CandidateSearchAlert alert) {
		
		alert.initAsNewAlert(this.getAuthenticatedRecruiterId());
		
		this.skillAlertDao.saveAlert(alert);
		
	}
	
	/**
	* Refer to the CandidateService for details 
	*/
	@Override
	public void deleteSearchAlert(UUID id) {
		
		Optional<CandidateSearchAlert> alertOpt = this.skillAlertDao.getchAlertById(id);
		
		CandidateSearchAlert alert = alertOpt.orElseThrow(() -> new IllegalArgumentException("Unknown SearchAlert Id " + id));
		
		if (!this.getAuthenticatedRecruiterId().equals(alert.getRecruiterId())) {
			throw new IllegalArgumentException("Unable to delete SearchAlert");
		}
		
		this.skillAlertDao.deleteById(id);
		
	}

	/**
	* Refer to the CandidateService for details 
	*/
	@Override
	public Set<CandidateSearchAlert> getAlertsForCurrentUser() {
		return this.skillAlertDao.fetchAlertsByRecruiterId(getAuthenticatedRecruiterId());
	}
	
	/**
	* Retrieves the Id of the current Recruiter
	* @return id from security context
	*/
	private String getAuthenticatedRecruiterId() {
		return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
	}

	/**
	* Refer to the CandidateService for details 
	*/
	@Override
	public void extractFiltersFromDocument(FileType fileType, byte[] fileBytes) throws IOException {
		
		try {
			
			Set<String> skills = new HashSet<>();
			
			// CurriculumDetailsExtractionFactory.getInstance(fileType).extract(skills, curriculumId, curriculumFileBytes);
				
		}catch(Exception e) {
			//return CurriculumUpdloadDetails.builder().id(curriculumId).build();
		}
		
	}
	
}