package com.arenella.recruit.recruiters.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.arenella.recruit.recruiters.beans.Recruiter;
import com.arenella.recruit.recruiters.beans.Recruiter.language;
import com.arenella.recruit.recruiters.beans.RecruiterAPIInbound;
import com.arenella.recruit.recruiters.beans.RecruiterAPIOutbound;
import com.arenella.recruit.recruiters.beans.RecruiterAccountRequestAPIInbound;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.INVOICE_TYPE;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_action;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_type;
import com.arenella.recruit.recruiters.beans.SubscriptionAPIInbound;
import com.arenella.recruit.recruiters.beans.SubscriptionActionFeedback;
import com.arenella.recruit.recruiters.services.RecruiterService;

/**
* Unit tests for the RecruiterController class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
class RecruiterControllerTest {

	@InjectMocks
	private RecruiterController 	recruiterController;
	
	@Mock
	private RecruiterService 		mockRecruiterService;
	
	@Mock
	private Principal				mockPrincipal;
	
	final String 		companyName 		= " aCompanyName ";
	final String 		email 				= " admin@arenella-ict.com ";
	final String 		firstname 			= " kevin ";
	final language 		lang	 			= language.DUTCH;
	final String 		surname 			= " parkings ";
	final String 		userId 				= " kparkings01 ";
	final LocalDate		accountCreated		= LocalDate.of(2021, 10, 13);
	final boolean		active				= true;
	
	/**
	* Tests successful update of existing Recruiter
	* @throws Exception
	*/
	@Test
	void testUpdateRecruiter() throws Exception{
	
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
	void testFetchRecruiters() {
		
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
	void testFetchRecruiter() throws Exception{
		
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
	void testRequestRecruiterAccount() {

		RecruiterAccountRequestAPIInbound request = RecruiterAccountRequestAPIInbound.builder()
																						.companyName(companyName)
																						.email(email)
																						.firstName(firstname)
																						.language(lang)
																						.surname(surname)
																					.build();
		
		//TODO: Test (ArgCapt) that request sent to service has all values but userId is empty once service call is done
		
		ResponseEntity<Void> response = this.recruiterController.requestRecruiterAccount(request);
		
		assertEquals( HttpStatus.OK, response.getStatusCode());
		
	}
	
	/**
	* Tests happy path of SubscriptonAction update
	* @throws Exception
	*/
	@Test
	void testPerformSubscriptionAction() throws Exception {
		
		final String 				recruiterId 			= "kparkings";
		final UUID 					subscriptionId 			= UUID.randomUUID();
		final subscription_action 	action 					= subscription_action.ACTIVATE_SUBSCRIPTION; 	
	
		Mockito.when(this.mockRecruiterService.performSubscriptionAction(recruiterId, subscriptionId, action)).thenReturn(Optional.empty());
		
		ResponseEntity<Optional<SubscriptionActionFeedback>> response = this.recruiterController.performSubscriptionAction(recruiterId, subscriptionId, action);
		
		Mockito.verify(this.mockRecruiterService).performSubscriptionAction(recruiterId, subscriptionId, action);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
	
	}
	
	/**
	* Tests happy path of add subscription
	* @throws Exception
	*/
	@Test
	void testAddSubscription() throws Exception {
		
		final String 					recruiterId 	= "kparkings";
		final SubscriptionAPIInbound 	type		 	= SubscriptionAPIInbound.builder().type(subscription_type.YEAR_SUBSCRIPTION).invoiceType(INVOICE_TYPE.BUSINESS).build();
		
		Mockito.doNothing().when(this.mockRecruiterService).addSubscription(recruiterId, type.getType(), INVOICE_TYPE.BUSINESS);
		
		ResponseEntity<Void> response = this.recruiterController.addSubscription(recruiterId, type);
		
		Mockito.verify(this.mockRecruiterService).addSubscription(recruiterId, type.getType(), INVOICE_TYPE.BUSINESS);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
	}
	
	/**
	* Tests email reset request
	* @throws Exception
	*/
	@Test
	void testResetPassword() {
		
		final String email = "kparkings";
		
		Mockito.doNothing().when(this.mockRecruiterService).resetPassword(Mockito.anyString());
		
		ResponseEntity<Void> response = this.recruiterController.resetPassword(email);
		
		Mockito.verify(this.mockRecruiterService).resetPassword(email);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
	}
	
	/**
	* Tests endpoint to see if User has a paid subscription
	* @throws Exception
	*/
	@Test
	void testHasPaidSubscription() {
		
		Mockito.when(this.mockPrincipal.getName()).thenReturn("rec1");
		Mockito.when(this.mockRecruiterService.hasPaidSubscription(Mockito.anyString())).thenReturn(true);
		
		ResponseEntity<Boolean> response = this.recruiterController.hasPaidSubscription(mockPrincipal);
	
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody());
		
	}
	
	/**
	* Tests handling of recruiter deletions request
	* @throws Exception
	*/
	@Test
	void testDeleteRecruiter() {
		
		final String recruiterId = "kparkings";
		
		ResponseEntity<Void> response = this.recruiterController.deleteRecruiter(recruiterId);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertFalse(response.hasBody());
		
		Mockito.verify(this.mockRecruiterService).deleteRecruiter(recruiterId);
		
	}
	
	/**
	* Tests request to generate invoice for a Subscription
	* @throws Exception
	*/
	@Test
	void testGetInvoiceForRecruiterSubscription() throws Exception {
		
		final UUID 								subscriptionId 		= UUID.randomUUID();
		final String							invoiceNumber		= "2025-01-00001";
		final String							unitDescription		= "subscription part 3/12";
		final LocalDate							invoiceDate			= LocalDate.of(2024, 10, 13);
		final ByteArrayResource 				invoiceFile 		= new ByteArrayResource(new byte[] {});
		
		Mockito.when(this.mockRecruiterService.generateInvoiceForSubscription(subscriptionId, invoiceNumber, Optional.empty(), Optional.of(invoiceDate), Optional.of(unitDescription))).thenReturn(invoiceFile);
		
		byte[] response = this.recruiterController.getInvoiceForRecruiterSubscription(subscriptionId, invoiceNumber, unitDescription,Optional.empty(), invoiceDate);
		
		Mockito.verify(this.mockRecruiterService).generateInvoiceForSubscription(subscriptionId, invoiceNumber, Optional.empty(), Optional.of(invoiceDate), Optional.of(unitDescription));
		
		assertEquals(invoiceFile.getByteArray(), response);
		
	}

}