package com.arenella.recruit.candidates.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arenella.recruit.candidates.dao.CandidateDao;

/**
* Services for retrieving statistics relating to Candidates
* @author K Parkings
*/
@Service
public class CandidateStatisticsServiceImpl implements CandidateStatisticsService{

	@Autowired
	private CandidateDao candidateDao;
	
	/**
	* Refer to StatisticsService for details 
	*/
	@Override
	public Long fetchNumberOfAvailableCandidates() {
		return candidateDao.countByAvailable(true);
	}

}
