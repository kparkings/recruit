package com.arenella.recruit.candidates.services;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.arenella.recruit.candidates.entities.CandidateEntity;
import com.arenella.recruit.candudates.beans.Candidate;
import com.arenella.recruit.candudates.beans.CandidateFilterOptions;
import com.arenella.recruit.candudates.dao.CandidateDao;

/**
* Provides services related to Candidates
* @author K Parkings
*/
@Service
public class CandidateServiceImpl implements CandidateService{
	
	@Autowired
	private CandidateDao candidateDao;
	
	/**
	* Refer to the CandidateService Interface for Details
	*/
	@Override
	public void persistCandidate(Candidate candidate) {
		
		CandidateEntity entity = CandidateEntity.convertToEntity(candidate);
		
		//TODO: Works with single country. Fails with multiple. Because we need to set up the composite key
		
		candidateDao.save(entity);
		
	}

	/**
	* Refer to the CandidateService Interface for Details
	*/
	@Override
	public Page<Candidate> getCandidates(CandidateFilterOptions filterOptions, Pageable pageable) {
		
		return candidateDao.findAll(filterOptions, pageable).map(candidate -> CandidateEntity.convertFromEntity(candidate));
		
	}
	
	/**
	* Refer to the CandidateService Interface for Details
	*/
	@Override
	public Set<Candidate> getCandidates(CandidateFilterOptions filterOptions) {
		return StreamSupport.stream(candidateDao.findAll(filterOptions).spliterator(), false)
								.map(candidate -> CandidateEntity.convertFromEntity(candidate)).collect(Collectors.toCollection(LinkedHashSet::new));
	}

	/**
	* Refer to the CandidateService Interface for Details
	*/
	@Override
	public Candidate getCandidate(String candidateId) {
		// TODO Auto-generated method stub
		return null;
	}
	
}