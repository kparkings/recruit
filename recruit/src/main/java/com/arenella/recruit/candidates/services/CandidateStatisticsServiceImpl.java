package com.arenella.recruit.candidates.services;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arenella.recruit.candidates.beans.CandidateRoleStats;
import com.arenella.recruit.candidates.dao.CandidateDao;
import com.arenella.recruit.candidates.entities.CandidateRoleStatsView;

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

	/**
	* Refer to StatisticsService for details
	*/
	@Override
	public List<CandidateRoleStats> fetchCandidateRoleStats() {
		
		return candidateDao.getCandidateRoleStats().stream().map(stat -> CandidateRoleStatsView.convertFromView(stat)).collect(Collectors.toCollection(LinkedList::new));
	}

}
