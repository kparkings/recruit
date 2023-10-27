package com.arenella.recruit.recruiters.adapters;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.arenella.recruit.adapters.actions.GrantCreditCommand;
import com.arenella.recruit.candidates.adapters.CandidateExternalEventListener;

/**
* Unit tests for the RecruitersMonolithExternalEventPublisher class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
public class RecruitersMonolithExternalEventPublisherTest {

	@InjectMocks
	private RecruitersMonolithExternalEventPublisher		publisher;
	
	@Mock
	private ListingsExternalEventListener 					mockListingExternalEventListener;
	
	@Mock
	private RecruitersExternalEventListener					mockRecruitersInternalEventListener;
	
	@Mock
	private CandidateExternalEventListener					MockCandidateExternalEventListener;
	
	/**
	* Tests command sent to correct services
	* @throws Exception
	*/
	@Test
	public void testPublishGrantCreditCommand() throws Exception{
		
		this.publisher.publishGrantCreditCommand(new GrantCreditCommand());
		
		Mockito.verify(this.mockListingExternalEventListener).listenForGrantCreditCommand(Mockito.any(GrantCreditCommand.class));
		Mockito.verify(this.mockRecruitersInternalEventListener).listenForGrantCreditCommand(Mockito.any(GrantCreditCommand.class));
		Mockito.verify(this.MockCandidateExternalEventListener).listenForGrantCreditCommand(Mockito.any(GrantCreditCommand.class));
		
	}
}
