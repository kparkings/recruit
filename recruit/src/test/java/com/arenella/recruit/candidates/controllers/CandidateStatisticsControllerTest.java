package com.arenella.recruit.candidates.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.arenella.recruit.candidates.services.CandidateStatisticsService;

/**
* Unit tests for the CandidateStatisticsController class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
public class CandidateStatisticsControllerTest {

	@Mock
	private CandidateStatisticsService			mockCandidateStatisticsService;
	
	@InjectMocks
	CandidateStatisticsController controller = new CandidateStatisticsController();
	
	/**
	* Tests return of total active users
	* @throws Exception
	*/
	@Test
	public void testFetchNumberOfCandidates() throws Exception{
		
		final Long numberOfAvailableCandidates = 88L;
		
		Mockito.when(mockCandidateStatisticsService.fetchNumberOfAvailableCandidates()).thenReturn(numberOfAvailableCandidates);
		
		ResponseEntity<Long> response = controller.fetchNumberOfCandidates();
		
		assertEquals(HttpStatus.OK, 				response.getStatusCode());
		assertEquals(numberOfAvailableCandidates, 	response.getBody());
		
	}
	
	/**
	* Tests logging of email requested event
	* @throws Exception
	*/
	//@Test
	//public void testLogEventEmailRequestedEvent() throws Exception{
		
	//	Mockito.doNothing().when(this.mockCandidateStatisticsService).logEventEmailRequested(Mockito.anyLong());
		
	//	ResponseEntity<Void> response = controller.logEventEmailRequestedEvent(1L);
		
	//	Mockito.verify(mockCandidateStatisticsService).logEventEmailRequested(Mockito.anyLong());
		
	//	assertEquals(HttpStatus.OK, response.getStatusCode());
		
	//}
	
}
