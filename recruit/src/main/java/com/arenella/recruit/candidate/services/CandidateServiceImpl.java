package com.arenella.recruit.candidate.services;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arenella.recruit.candidate.beans.Candidate;
import com.arenella.recruit.candidate.dao.CandidateDao;
import com.arenella.recruit.candidate.entities.CandidateEntity;
import com.arenella.recruit.candidate.entities.LanguageEntity;

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
		// TODO Auto-generated method stub
		return null;
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