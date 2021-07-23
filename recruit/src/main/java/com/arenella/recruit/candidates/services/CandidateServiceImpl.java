package com.arenella.recruit.candidates.services;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.arenella.recruit.candidates.adapters.ExternalEventPublisher;
import com.arenella.recruit.candidates.beans.Candidate;
import com.arenella.recruit.candidates.beans.CandidateFilterOptions;
import com.arenella.recruit.candidates.controllers.CandidateController.CANDIDATE_UPDATE_ACTIONS;
import com.arenella.recruit.candidates.dao.CandidateDao;
import com.arenella.recruit.candidates.entities.CandidateEntity;

/**
* Provides services related to Candidates
* @author K Parkings
*/
@Service
public class CandidateServiceImpl implements CandidateService{
	
	@Autowired
	private CandidateDao 				candidateDao;
	
	@Autowired
	private CandidateStatisticsService 	statisticsService;
	
	@Autowired
	private ExternalEventPublisher		externalEventPublisher;
	
	/**
	* Refer to the CandidateService Interface for Details
	*/
	@Override
	public void persistCandidate(Candidate candidate) {
		
		CandidateEntity entity = CandidateEntity.convertToEntity(candidate);
		
		try {
		candidateDao.save(entity);
		}catch(Exception e) {
			e.printStackTrace();
		}
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
		
		CandidateEntity candidate = this.candidateDao.findById(Long.valueOf(candidateId)).orElseThrow(() -> new RuntimeException("Cannot perform update on unknown Candidate: " + candidateId));
		
		switch (updateAction) {
			case enable: {
				candidate.setAvailable(true);
				break;
			}
			case disable: {
				candidate.setAvailable(false);
				break;
			}
			default: {
				throw new IllegalArgumentException("Unknown update action requested for Candidate");
			}
		}
		
		this.candidateDao.save(candidate);
		
	}
	
}