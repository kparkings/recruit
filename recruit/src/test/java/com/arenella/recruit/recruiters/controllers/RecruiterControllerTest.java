package com.arenella.recruit.recruiters.controllers;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.arenella.recruit.recruiters.beans.Recruiter;
import com.arenella.recruit.recruiters.beans.Recruiter.language;
import com.arenella.recruit.recruiters.beans.RecruiterAPIInbound;
import com.arenella.recruit.recruiters.beans.RecruiterAPIOutbound;
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

}