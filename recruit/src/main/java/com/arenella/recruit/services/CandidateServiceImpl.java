package com.arenella.recruit.services;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arenella.recruit.beans.Candidate;
import com.arenella.recruit.dao.CandidateDao;
import com.arenella.recruit.entities.CandidateEntity;
import com.arenella.recruit.entities.LanguageEntity;

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
	public Set<Candidate> getCandidates() {
		return StreamSupport.stream(candidateDao.findAll().spliterator(), false)
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