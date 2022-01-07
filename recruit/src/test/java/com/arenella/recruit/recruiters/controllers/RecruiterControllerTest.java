package com.arenella.recruit.recruiters.controllers;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.arenella.recruit.recruiters.beans.Recruiter;
import com.arenella.recruit.recruiters.beans.Recruiter.language;
import com.arenella.recruit.recruiters.beans.RecruiterAPIInbound;
import com.arenella.recruit.recruiters.beans.RecruiterAPIOutbound;
import com.arenella.recruit.recruiters.beans.RecruiterAccountRequestAPIInbound;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_action;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_type;
import com.arenella.recruit.recruiters.beans.SubscriptionAPIInbound;
import com.arenella.recruit.recruiters.services.RecruiterService;

/**
* Unit tests for the RecruiterController class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
public class RecruiterControllerTest {

	@InjectMocks
	private RecruiterController 	recruiterController;
	
	@Mock
	private RecruiterService 		mockRecruiterService;
	
	final String 		companyName 		= " aCompanyName ";
	final String 		email 				= " kparkings@gmail.com ";
	final String 		firstname 			= " kevin ";
	final language 		lang	 			= language.DUTCH;
	final String 		surname 			= " parkings ";
	final String 		userId 				= " kparkings01 ";
	final LocalDate		accountCreated		= LocalDate.of(2021, 10, 13);
	final boolean		active				= true;
	
	/**
	* Tests successful creation of a Recruiter
	* @throws Exception
	*/
	@Test
	public void testAddRecruiter() throws Exception{

		ArgumentCaptor<Recruiter> recruiterArg = ArgumentCaptor.forClass(Recruiter.class);
		
		RecruiterAPIInbound recruiter = RecruiterAPIInbound
													.builder()
														.companyName(companyName)
														.email(email)
														.firstName(firstname)
														.language(lang)
														.surname(surname)
														.userId(userId)
													.build();
		
		Mockito.doNothing().when(mockRecruiterService).addRecruiter(recruiterArg.capture());
		
		recruiterController.addRecruiter(recruiter);
		
		Mockito.verify(this.mockRecruiterService).addRecruiter(Mockito.any(Recruiter.class));
		
		Recruiter recruiterDomain = recruiterArg.getValue();
		
		assertEquals(companyName.toLowerCase().trim(), 	recruiterDomain.getCompanyName());
		assertEquals(email.toLowerCase().trim(), 		recruiterDomain.getEmail());
		assertEquals(firstname.toLowerCase().trim(), 	recruiterDomain.getFirstName());
		assertEquals(lang, 								recruiterDomain.getLanguage());
		assertEquals(surname.toLowerCase().trim(), 		recruiterDomain.getSurname());
		assertEquals(userId.toLowerCase().trim(), 		recruiterDomain.getUserId());
		
	}
	
	/**
	* Tests successful update of existing Recruiter
	* @throws Exception
	*/
	@Test
	public void testUpdateRecruiter() throws Exception{
	
		ArgumentCaptor<Recruiter> recruiterArg = ArgumentCaptor.forClass(Recruiter.class);
		
		RecruiterAPIInbound recruiter = RecruiterAPIInbound
													.builder()
														.companyName(companyName)
														.email(email)
														.firstName(firstname)
														.language(lang)
														.surname(surname)
														.userId(userId)
													.build();
		
		Mockito.doNothing().when(mockRecruiterService).updateRecruiter(recruiterArg.capture());
		
		recruiterController.updateRecruiter(recruiter);
		
		Mockito.verify(this.mockRecruiterService).updateRecruiter(Mockito.any(Recruiter.class));
		
		Recruiter recruiterDomain = recruiterArg.getValue();
		
		assertEquals(companyName.toLowerCase().trim(), 	recruiterDomain.getCompanyName());
		assertEquals(email.toLowerCase().trim(), 		recruiterDomain.getEmail());
		assertEquals(firstname.toLowerCase().trim(), 	recruiterDomain.getFirstName());
		assertEquals(lang, 								recruiterDomain.getLanguage());
		assertEquals(surname.toLowerCase().trim(), 		recruiterDomain.getSurname());
		assertEquals(userId.toLowerCase().trim(), 		recruiterDomain.getUserId());
		
	}
	
	/**
	* Tests retrieval of all available Recruiters
	* @throws Exception
	*/
	@Test
	public void testFetchRecruiters() throws Exception {
		
		Set<Recruiter> recruiters = Set.of(Recruiter
				.builder()
				.companyName(companyName)
				.email(email)
				.firstName(firstname)
				.language(lang)
				.surname(surname)
				.userId(userId)
				.active(active)
				.accountCreated(accountCreated)
			.build());
		
		Mockito.when(this.mockRecruiterService.fetchRecruiters()).thenReturn(recruiters);
		
		Set<RecruiterAPIOutbound> outboundRecruiters = this.recruiterController.fetchRecruiters();
		
		RecruiterAPIOutbound outboundRecruiter = outboundRecruiters.stream().findFirst().get();
		
		assertEquals(companyName.toLowerCase().trim(),	 	outboundRecruiter.getCompanyName());
		assertEquals(email.toLowerCase().trim(), 			outboundRecruiter.getEmail());
		assertEquals(firstname.toLowerCase().trim(), 		outboundRecruiter.getFirstName());
		assertEquals(lang, 									outboundRecruiter.getLanguage());
		assertEquals(surname.toLowerCase().trim(), 			outboundRecruiter.getSurname());
		assertEquals(userId.toLowerCase().trim(), 			outboundRecruiter.getUserId());
		assertEquals(active, 								outboundRecruiter.isActive());
		
	}
	
	/**
	* Tests successful retrieval of existing Recruiter
	* @throws Exception
	*/
	@Test
	public void testFetchRecruiter() throws Exception{
		
		Recruiter recruiter = Recruiter
								.builder()
									.companyName(companyName)
									.email(email)
									.firstName(firstname)
									.language(lang)
									.surname(surname)
									.userId(userId)
									.active(active)
									.accountCreated(accountCreated)
								.build();
							
		Mockito.when(this.mockRecruiterService.fetchRecruiter(userId)).thenReturn(recruiter);
		
		RecruiterAPIOutbound outboundRecruiter = this.recruiterController.fetchRecruiter(userId);
		
		assertEquals(companyName.toLowerCase().trim(),	 	outboundRecruiter.getCompanyName());
		assertEquals(email.toLowerCase().trim(), 			outboundRecruiter.getEmail());
		assertEquals(firstname.toLowerCase().trim(), 		outboundRecruiter.getFirstName());
		assertEquals(lang, 									outboundRecruiter.getLanguage());
		assertEquals(surname.toLowerCase().trim(), 			outboundRecruiter.getSurname());
		assertEquals(userId.toLowerCase().trim(), 			outboundRecruiter.getUserId());
		assertEquals(active, 								outboundRecruiter.isActive());
		
	}
	
	/**
	* Tests correct response is sent when request for Recruiter account is 
	* sent by the Recruiter
	* @throws Exception
	*/
	@Test
	public void testRequestRecruiterAccount() throws Exception {

		RecruiterAccountRequestAPIInbound request = RecruiterAccountRequestAPIInbound.builder()
																						.companyName(companyName)
																						.email(email)
																						.firstName(firstname)
																						.language(lang)
																						.surname(surname)
																					.build();
		
		//TODO: Test (ArgCapt) that request sent to service has all values but userId is empty once service call is done
		
		ResponseEntity<Void> response = this.recruiterController.requestRecruiterAccount(request);
		
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		
	}
	
	/**
	* Tests happy path of SubscriptonAction update
	* @throws Exception
	*/
	@Test
	public void testPerformSubscriptionAction() throws Exception {
		
		final String 				recruiterId 			= "kparkings";
		final UUID 					subscriptionId 			= UUID.randomUUID();
		final subscription_action 	action 					= subscription_action.ACTIVATE_SUBSCRIPTION; 	
	
		Mockito.doNothing().when(this.mockRecruiterService).performSubscriptionAction(recruiterId, subscriptionId, action);
		
		ResponseEntity<Void> response = this.recruiterController.performSubscriptionAction(recruiterId, subscriptionId, action);
		
		Mockito.verify(this.mockRecruiterService).performSubscriptionAction(recruiterId, subscriptionId, action);
		
		assertEquals(response.getStatusCode(), HttpStatus.OK);
	
	}
	
	/**
	* Tests happy path of add subscription
	* @throws Exception
	*/
	@Test
	public void testAddSubscription() throws Exception {
		
		final String 					recruiterId 	= "kparkings";
		final SubscriptionAPIInbound 	type		 	= SubscriptionAPIInbound.builder().type(subscription_type.YEAR_SUBSCRIPTION).build();
		
		Mockito.doNothing().when(this.mockRecruiterService).addSubscription(recruiterId, type.getType());
		
		ResponseEntity<Void> response = this.recruiterController.addSubscription(recruiterId, type);
		
		Mockito.verify(this.mockRecruiterService).addSubscription(recruiterId, type.getType());
		
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		
	}

}