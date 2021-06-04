package com.arenella.recruit.candidate.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.arenella.recruit.candidates.dao.CandidateDao;
import com.arenella.recruit.candidates.services.CandidateStatisticsServiceImpl;

/**
* Unit tests for the CandidateStatisticsServiceImpl class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
public class CandidateStatisticsServiceImplTest {

	@Mock
	private CandidateDao 					mockCandidateDao;
	
	@InjectMocks
	private CandidateStatisticsServiceImpl 	service 			= new CandidateStatisticsServiceImpl();
	
	/**
	* Tests retrieval of available Candidate cound
	* @throws Exception
	*/
	@Test
	public void testFetchNumberOfAvailableCandidates() throws Exception {
		
		final Long availableCandidates = 787L;
		
		Mockito.when(mockCandidateDao.countByAvailable(true)).thenReturn(availableCandidates);
		
		assertEquals(availableCandidates, service.fetchNumberOfAvailableCandidates());
		
	}
	
}