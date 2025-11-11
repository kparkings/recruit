package com.arenella.recruit.recruiters.listings.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.arenella.recruit.adapters.actions.GrantCreditCommand;
import com.arenella.recruit.listings.adapters.ExternalEventPublisher;
import com.arenella.recruit.listings.adapters.RequestListingContactEmailCommand;
import com.arenella.recruit.listings.beans.Listing;
import com.arenella.recruit.listings.beans.ListingContactRequestEvent;
import com.arenella.recruit.listings.beans.ListingContactRequestEvent.CONTACT_USER_TYPE;
import com.arenella.recruit.listings.beans.ListingStatContactRequests;
import com.arenella.recruit.listings.beans.ListingViewedEvent;
import com.arenella.recruit.listings.controllers.ListingContactRequest;
import com.arenella.recruit.listings.exceptions.ListingValidationException;
import com.arenella.recruit.listings.repos.ListingRepository;
import com.arenella.recruit.listings.services.FileSecurityParser;
import com.arenella.recruit.listings.services.FileSecurityParser.FileType;
import com.arenella.recruit.listings.beans.RecruiterCredit;
import com.arenella.recruit.listings.dao.ListingAlertSentEventDao;
import com.arenella.recruit.listings.dao.ListingContactRequestEventDao;
import com.arenella.recruit.listings.dao.ListingRecruiterCreditDao;
import com.arenella.recruit.listings.services.ListingServiceImpl;
import com.arenella.recruit.listings.utils.ListingFunctionSynonymUtil;

import co.elastic.clients.elasticsearch.ElasticsearchClient;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

/**
* Unit tests for the ListingService class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
class ListingServiceImplTest {

	@InjectMocks
	private ListingServiceImpl 	service = new ListingServiceImpl();
	
	@Mock
	private ElasticsearchClient				mockEsClient;
	
	@Mock
	private ListingRepository 				mockListingRepository;
	
	@Mock
	private	Authentication					mockAuthentication;
	
	@Mock
	private FileSecurityParser 				mockFileSecurityParser;
	
	@Mock
	private ExternalEventPublisher 			mockExternalEventPublisher;
	
	@Mock
	private ListingRecruiterCreditDao 		mockCreditDao;
	
	@Mock
	private ListingFunctionSynonymUtil		mockListingFunctionSynonymUtil;
	
	@Mock
	private ListingContactRequestEventDao	mockListingContactRequestEventDao;
	
	@Mock
	private ListingAlertSentEventDao 		listingAlertSentEventDao;
	
	/**
	* Sets up test environment
	* @throws Exception
	*/
	@BeforeEach
	void init() {
		SecurityContextHolder.getContext().setAuthentication(mockAuthentication);
	}
	
	/**
	* Tests happy path for Listing creation
	* @throws Exception
	*/
	@Test
	void testAddListing() throws Exception {
		
		final Listing listing = Listing
									.builder()
										.title("aTitle")
										.description("aDesc")
										.ownerName("anOwnerName")
										.ownerEmail("anEmail")
										.ownerCompany("aCompany")
									.build();
		
		UUID listingId = service.addListing(listing, true);
		
		if (!(listingId instanceof UUID)) {
			throw new RuntimeException();
		}
		
		verify(mockListingRepository).saveListings(anySet());
		
	}
	
	/**
	* Asserts Exception is thrown if the Listing being updated does not exist
	* @throws Exception
	*/
	@Test
	void testUpdateListing_unknownListing() {
		
		final UUID listingId = UUID.randomUUID();
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.service.updateListing(listingId, null);
		});
		
	}
	
	/**
	* Asserts Exception is thrown if the Listing being updated is not owned 
	* by the user performing the update
	* @throws Exception
	*/
	@Test
	void testUpdateListing_notOwnerOfListing() {
		
		final UUID 				listingId 		= UUID.randomUUID();
		final Listing			listing			= Listing.builder().ownerId("kevin").title("a").description("b").ownerCompany("c").ownerName("d").ownerEmail("e").build();
		final Listing 			existingListing = Listing.builder().ownerId("kevin").title("a").description("b").ownerCompany("c").ownerName("d").ownerEmail("e").build();
		
		when(this.mockListingRepository.findListingById(listingId)).thenReturn(Optional.of(existingListing));
		when(mockAuthentication.getPrincipal()).thenReturn("notKevin");
		
		Assertions.assertThrows(AccessDeniedException.class, () -> {
			this.service.updateListing(listingId, listing);
		});
		
	}
	
	/**
	* Asserts Exception is thrown if the original owner of the listing tries
	* to update the owner to someone else
	* @throws Exception
	*/
	@Test
	void testUpdateListing_changeOwnerOfListing() {
		
		final UUID 				listingId 		= UUID.randomUUID();
		final Listing			listing			= Listing.builder().ownerId("kevin2").title("a").description("b").ownerCompany("c").ownerName("d").ownerEmail("e").build();
		final Listing 			existingListing = Listing.builder().ownerId("kevin2").title("a").description("b").ownerCompany("c").ownerName("d").ownerEmail("e").build();
		
		when(this.mockListingRepository.findListingById(listingId)).thenReturn(Optional.of(existingListing));
		when(mockAuthentication.getPrincipal()).thenReturn("kevin");
		
		Assertions.assertThrows(AccessDeniedException.class, () -> {
			this.service.updateListing(listingId, listing);
		});
		
	}

	/**
	* Asserts Exception is thrown if the Listing being Updates does not exist
	* @throws Exception
	*/
	@Test
	void testDeleteListing_unknownListing() {
		
		final UUID listingId = UUID.randomUUID();
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.service.deleteListing(listingId);
		});
		 
	}

	/**
	* Asserts Exception is thrown if the Listing being Deleted is not owned 
	* by the user performing the update
	* @throws Exception
	*/
	@Test
	void testDeleteListing_notOwnerOfListing() {
		
		final UUID 				listingId 		= UUID.randomUUID();
		final Listing 			listing 		= Listing.builder().ownerId("kevin").title("a").description("b").ownerCompany("c").ownerName("d").ownerEmail("e").build();
		
		when(this.mockListingRepository.findListingById(listingId)).thenReturn(Optional.of(listing));
		when(mockAuthentication.getPrincipal()).thenReturn("notKevin");
		
		Assertions.assertThrows(AccessDeniedException.class, () -> {
			this.service.deleteListing(listingId);
		});
		 
	}
	
	/**
	* Tests happy path
	* @throws Exception
	*/
	@Test
	void testUpdateListing() {
		
		final UUID 				listingId 			= UUID.randomUUID();
		final String			ownerId				= "kevin";
		final Listing			listing				= Listing.builder().ownerId("kevin").title("a").description("b").ownerCompany("c").ownerName("d").ownerEmail("e").build();
		final Listing 			existingListing 	= Listing.builder().ownerId(ownerId).title("a").description("b").ownerCompany("c").ownerName("d").ownerEmail("e").build();
		
		when(this.mockListingRepository.findListingById(listingId)).thenReturn(Optional.of(existingListing));
		when(mockAuthentication.getPrincipal()).thenReturn(ownerId);
		
		this.service.updateListing(listingId, listing);
		
		verify(this.mockListingRepository).saveListings(anySet());
	}
	
	/**
	* Tests happy path
	* @throws Exception
	*/
	@Test
	void testDeleteListing() {
		
		final UUID 		listingId 		= UUID.randomUUID();
		final String	ownerId			= "kevin";
		final Listing 	existingListing = Listing.builder().ownerId(ownerId).title("a").description("b").ownerCompany("c").ownerName("d").ownerEmail("e").build();
		
		when(this.mockListingRepository.findListingById(listingId)).thenReturn(Optional.of(existingListing));
		when(mockAuthentication.getPrincipal()).thenReturn(ownerId);
		
		this.service.deleteListing(listingId);
		
		verify(this.mockListingRepository).deleteById(listingId);
		
	}
	
	/**
	* Tests Exception thrown if one of the mandatory fields 
	* has no value provided
	* @throws Exception
	*/
	@Test
	void testAddListingValidation_missingValue() {
		
		Listing listing1 = Listing
				.builder()
					.title(null)
					.description("aDesc")
					.ownerName("anOwnerName")
					.ownerEmail("anEmail")
					.ownerCompany("aCompany")
				.build();
		
		Assertions.assertThrows(ListingValidationException.class, () -> 
			service.addListing(listing1, true)
		);
		

		Listing listing2 = Listing
				.builder()
					.title("aTitle")
					.description(null)
					.ownerName("anOwnerName")
					.ownerEmail("anEmail")
					.ownerCompany("aCompany")
				.build();
		
		Assertions.assertThrows(ListingValidationException.class, () -> 
			service.addListing(listing2, true)
		);

		Listing listing3 = Listing
				.builder()
					.title("aTitle")
					.description("aDesc")
					.ownerName(null)
					.ownerEmail("anEmail")
					.ownerCompany("aCompany")
				.build();
		
		Assertions.assertThrows(ListingValidationException.class, () -> 
			service.addListing(listing3, true)
		);
		
		Listing listing4 = Listing
				.builder()
					.title("aTitle")
					.description("aDesc")
					.ownerName("anOwnerName")
					.ownerEmail(null)
					.ownerCompany("aCompany")
				.build();
		
		Assertions.assertThrows(ListingValidationException.class, () -> 
			service.addListing(listing4, true)
		);

		Listing listing5 = Listing
				.builder()
					.title(null)
					.description("aDesc")
					.ownerName("anOwnerName")
					.ownerEmail("anEmail")
					.ownerCompany(null)
				.build();
		
		Assertions.assertThrows(ListingValidationException.class, () -> 
			service.addListing(listing5, true)
		);

	}
	
	/**
	* Tests Exception thrown if one of the mandatory fields 
	* have empty values provided
	* @throws Exception
	*/
	@Test
	void testAddListingValidation_emptyStringValue() {

		Listing listing1 = Listing
				.builder()
					.title("  ")
					.description("aDesc")
					.ownerName("anOwnerName")
					.ownerEmail("anEmail")
					.ownerCompany("aCompany")
				.build();
		
		assertThrows(ListingValidationException.class, () -> 
			service.addListing(listing1, true)
		);

		Listing listing2 = Listing
				.builder()
					.title("aTitle")
					.description("  ")
					.ownerName("anOwnerName")
					.ownerEmail("anEmail")
					.ownerCompany("aCompany")
				.build();
		
		assertThrows(ListingValidationException.class, () -> 
			service.addListing(listing2, true)
		);

		Listing listing3 = Listing
				.builder()
					.title("aTitle")
					.description("aDesc")
					.ownerName("  ")
					.ownerEmail("anEmail")
					.ownerCompany("aCompany")
				.build();
		
		assertThrows(ListingValidationException.class, () -> 
			service.addListing(listing3, true)
		);
		
		Listing listing4 = Listing
				.builder()
					.title("aTitle")
					.description("aDesc")
					.ownerName("anOwnerName")
					.ownerEmail("  ")
					.ownerCompany("aCompany")
				.build();
		
		assertThrows(ListingValidationException.class, () -> 
			service.addListing(listing4, true)
		);

		Listing listing5 = Listing
				.builder()
					.title(null)
					.description("aDesc")
					.ownerName("anOwnerName")
					.ownerEmail("anEmail")
					.ownerCompany("  ")
				.build();
		
		assertThrows(ListingValidationException.class, () -> 
			service.addListing(listing5, true)
		);

	}
	
	/**
	* Tests exception is thrown if attempt is made to log an event
	* for viewing a Listing that does not exist
	* @throws Exception
	*/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testRegisterListingViewedEvent_unknownListing() {
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_RECRUITER"));

		when(mockAuthentication.getAuthorities()).thenReturn(authorities);
		
		final UUID 			eventId 	= UUID.randomUUID();
		final UUID 			listingId 	= UUID.randomUUID();
		final LocalDateTime created 	= LocalDateTime.of(2022, 1, 14, 10, 11, 12);
		ListingViewedEvent 	event 		= ListingViewedEvent.builder().eventId(eventId).created(created).listingId(listingId).build();
		
		assertThrows(IllegalArgumentException.class, () -> {
			this.service.registerListingViewedEvent(event);
		});
		
	}
	
	/**
	* Tests logging of event to show Listing was viewed
	* @throws Exception
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	void testRegisterListingViewedEvent() {
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_RECRUITER"));

		when(mockAuthentication.getAuthorities()).thenReturn(authorities);
		
		final UUID eventId 		= UUID.randomUUID();
		final UUID listingId 	= UUID.randomUUID();
		final LocalDateTime created = LocalDateTime.of(2022, 1, 14, 10, 11, 12);
		
		ListingViewedEvent 	event 		= ListingViewedEvent.builder().eventId(eventId).created(created).listingId(listingId).build();
		Listing 			listingObj  = Listing.builder().created(created).listingId(listingId).build();
				
		when(this.mockListingRepository.findListingById(event.getListingId())).thenReturn(Optional.of(listingObj));
		
		this.service.registerListingViewedEvent(event);
		
		verify(this.mockListingRepository, times(1)).saveListings(anySet());
		
	}
	
	/**
	* Tests if Admin user views event it is not logged
	* @throws Exception
	*/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testRegisterListingViewedEvent_admin() {
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

		when(mockAuthentication.getAuthorities()).thenReturn(authorities);
		
		final UUID eventId 		= UUID.randomUUID();
		final UUID listingId 	= UUID.randomUUID();
		final LocalDateTime created = LocalDateTime.of(2022, 1, 14, 10, 11, 12);
		
		ListingViewedEvent 	event 		= ListingViewedEvent.builder().eventId(eventId).created(created).listingId(listingId).build();
		
		this.service.registerListingViewedEvent(event);
		
		verify(this.mockListingRepository, times(0)).save(any());
		
	}
	
	/**
	* Tests if a file is considered unsafe that an exception is thrown
	* @throws Exception
	*/
	@Test
	void testSendContactRequestToListingOwner_unsafeFile() {
		
		when(this.mockFileSecurityParser.isSafe(any())).thenReturn(false);
		MultipartFile mockMultipartFile = mock(MultipartFile.class);
		
		ListingContactRequest req = ListingContactRequest.builder().attachment(mockMultipartFile).build();
		
		assertThrows(RuntimeException.class, () -> {
			this.service.sendContactRequestToListingOwner(req);
		});
		
		verify(this.mockListingContactRequestEventDao, never()).persistEvent(any(ListingContactRequestEvent.class));
		
	}

	/**
	* Tests if Listing is not found exception is thrown
	* @throws Exception
	*/
	@Test
	void testSendContactRequestToListingOwner_unknownListing() {
		
		when(this.mockFileSecurityParser.isSafe(any())).thenReturn(true);
		when(this.mockListingRepository.findListingById(any(UUID.class))).thenReturn(Optional.empty());
		
		ListingContactRequest req = ListingContactRequest.builder().build();
		
		assertThrows(RuntimeException.class, () -> {
			this.service.sendContactRequestToListingOwner(req);
		});
		
		
		verify(this.mockListingContactRequestEventDao, never()).persistEvent(any(ListingContactRequestEvent.class));
		
	}
	
	/**
	* Tests generation and sending of contact event for the owner of the Listing
	* @throws Exception
	*/
	@Test
	void testSendContactRequestToListingOwner_success() throws Exception{
		
		final UUID				listingId		= UUID.randomUUID();
		final String 			title 			= "Java Developer";
		final String 			ownerId			= "kparkings001";
		final MultipartFile 	attachment 		= mock(MultipartFile.class);
		final String			message			= "dear recruiter blah blah";
		final String 			senderName		= "Kevin Parkings";
		final String			senderEmail		= "admin@arenella-ict.com";
		final byte[]			attachmentBytes	= new byte[] {1,22,3};
		final FileType			fileType		= FileType.doc;
		
		ArgumentCaptor<RequestListingContactEmailCommand> capt = ArgumentCaptor.forClass(RequestListingContactEmailCommand.class);
		
		final Listing listing = Listing.builder().title(title).ownerId(ownerId).build();
		
		when(this.mockFileSecurityParser.isSafe(any())).thenReturn(true);
		when(this.mockListingRepository.findListingById(any(UUID.class))).thenReturn(Optional.of(listing));
		when(this.mockFileSecurityParser.getFileType(any())).thenReturn(fileType);
		when(attachment.getBytes()).thenReturn(attachmentBytes);
		
		doNothing().when(this.mockExternalEventPublisher).publicRequestSendListingContactEmailCommand(capt.capture());
		
		ListingContactRequest contactRequest = 
				ListingContactRequest
					.builder()
						.attachment(attachment)
						.listingId(listingId)
						.message(message)
						.senderEmail(senderEmail)
						.senderName(senderName)						
					.build();
		
		this.service.sendContactRequestToListingOwner(contactRequest);
		
		assertEquals(attachmentBytes, 		capt.getValue().getFile());
		assertEquals(fileType.toString(), 	capt.getValue().getFileType());
		assertEquals(title, 				capt.getValue().getListingName());
		assertEquals(message, 				capt.getValue().getMessage());
		assertEquals(ownerId, 				capt.getValue().getRecruiterId());
		assertEquals(senderEmail, 			capt.getValue().getSenderEmail());
		assertEquals(senderName, 			capt.getValue().getSenderName());
		
		verify(this.mockListingContactRequestEventDao).persistEvent(any(ListingContactRequestEvent.class));
		
	}
	
	/**
	* Tests activating of Listings for a Recruiter
	* @throws Exception
	*/
	@Test
	void testEnableListingsForRecruiter() {
		
		final String recruiterId = "kparkings01";
		
		Set<Listing> listings = Set.of(Listing.builder().active(false).build(), Listing.builder().active(false).build());
		
		@SuppressWarnings("unchecked")
		ArgumentCaptor<Set<Listing>> listingArgCapt = ArgumentCaptor.forClass(Set.class);
		
		when(this.mockListingRepository.findAllListings(any(), any())).thenReturn(listings);
		doNothing().when(this.mockListingRepository).saveListings(listingArgCapt.capture());
		
		this.service.enableListingsForRecruiter(recruiterId);
		
		verify(this.mockListingRepository).saveListings(anySet());
		
		listingArgCapt.getValue().stream().forEach(l -> Assertions.assertTrue(l.isActive()));
		
	}
	
	/**
	* Tests activating of Listings for a Recruiter
	* @throws Exception
	*/
	@Test
	void testDisableListingsForRecruiter() {
		
		final String recruiterId = "kparkings01";
		
		Set<Listing> listings = Set.of(Listing.builder().active(true).build(), Listing.builder().active(true).build());
		
		@SuppressWarnings("unchecked")
		ArgumentCaptor<Set<Listing>> listingArgCapt = ArgumentCaptor.forClass(Set.class);
		
		when(this.mockListingRepository.findAllListings(any(), any())).thenReturn(listings);
		doNothing().when(this.mockListingRepository).saveListings(listingArgCapt.capture());
		
		this.service.disableListingsForRecruiter(recruiterId);
		
		verify(this.mockListingRepository).saveListings(anySet());
		
		listingArgCapt.getValue().stream().forEach(l -> Assertions.assertFalse(l.isActive()));
		
	}
	
	/**
	* Test credits assigned and event sent
	* @throws Exception
	*/
	@SuppressWarnings("unchecked")
	@Test
	void testUpdateCredits() {
		
		ArgumentCaptor<Set<RecruiterCredit>> argCapt = ArgumentCaptor.forClass(Set.class);
		
		RecruiterCredit rc1 = RecruiterCredit.builder().recruiterId("rec1").credits(1).build();
		RecruiterCredit rc2 = RecruiterCredit.builder().recruiterId("rec2").credits(2).build();
		RecruiterCredit rc3 = RecruiterCredit.builder().recruiterId("rec3").credits(RecruiterCredit.DISABLED_CREDITS).build();
		
		doNothing().when(this.mockCreditDao).saveAll(argCapt.capture());
		when(this.mockCreditDao.fetchRecruiterCredits()).thenReturn(Set.of(rc1,rc2,rc3));
		
		service.updateCredits(new GrantCreditCommand());
		
		assertEquals(2, argCapt.getValue().stream().filter(rc -> rc.getCredits()== RecruiterCredit.DEFAULT_CREDITS).count());
		assertEquals(1, argCapt.getValue().stream().filter(rc -> rc.getCredits()== RecruiterCredit.DISABLED_CREDITS).count());
		
	}
	
	/**
	* Test false returned if User not known
	* @throws Exception
	*/
	@Test
	void testDoCreditsCheck_unknownUser() {
		
		when(this.mockCreditDao.getByRecruiterId(anyString())).thenReturn(Optional.empty());
		
		assertFalse(service.doCreditsCheck("recruiter33"));
		
	}
	
	/**
	* Test false returned if User has no remaining credits
	* @throws Exception
	*/
	@Test
	void testDoCreditsCheck_knownUser_no_credits() {
		
		RecruiterCredit rc = RecruiterCredit.builder().credits(0).build();
		
		when(this.mockCreditDao.getByRecruiterId(anyString())).thenReturn(Optional.of(rc));
		
		assertFalse(service.doCreditsCheck("recruiter33"));
		
	} 
	
	/**
	* Test false returned if User has no remaining credits
	* @throws Exception
	*/
	@Test
	void testDoCreditsCheck_knownUser_has_credits() {
		
		RecruiterCredit rc = RecruiterCredit.builder().credits(1).build();
		
		when(this.mockCreditDao.getByRecruiterId(anyString())).thenReturn(Optional.of(rc));
		
		assertTrue(service.doCreditsCheck("recruiter33"));
		
	}
	
	/**
	* Happy path
	* @throws Exception
	*/
	@Test
	void testUpdateCreditsForUser() {
		
		final String 	userId 		= "kparkings";
		final int 		credits 	= 20;

		ArgumentCaptor<RecruiterCredit> argCapt = ArgumentCaptor.forClass(RecruiterCredit.class);
		
		RecruiterCredit recCredits = RecruiterCredit.builder().recruiterId(userId).credits(30).build();
		
		when(this.mockCreditDao.getByRecruiterId(userId)).thenReturn(Optional.of(recCredits));
		doNothing().when(this.mockCreditDao).persist(argCapt.capture());
		
		this.service.updateCreditsForUser(userId, credits);
	
		verify(this.mockCreditDao).persist(any());
		
		assertEquals(credits, argCapt.getValue().getCredits());
		
	}
	
	/**
	* If no credits for user does nothing
	* @throws Exception
	*/
	@Test
	void testUpdateCreditsForUser_unknownRecruiter() {
		
		final String 	userId 		= "kparkings";
		final int 		credits 	= 20;

		when(this.mockCreditDao.getByRecruiterId(userId)).thenReturn(Optional.empty());
		
		this.service.updateCreditsForUser(userId, credits);
	
		verify(this.mockCreditDao, never()).persist(any());
		
	}
	
	/**
	* Tests case no credits
	* @throws Exception
	*/
	@Test
	void testGetCreditCountForUser_unknownUser() {
		
		when(this.mockCreditDao.getByRecruiterId(anyString())).thenReturn(Optional.empty());
		
		assertThrows(IllegalArgumentException.class, () -> {
			this.service.getCreditCountForUser("rec22");
		});
		
	}
	
	/**
	* Tests case no credits
	* @throws Exception
	*/
	@Test
	void testGetCreditCountForUser() {
		
		final int credits = 5;
		
		RecruiterCredit rc = RecruiterCredit.builder().credits(credits).build();
		
		when(this.mockCreditDao.getByRecruiterId(anyString())).thenReturn(Optional.of(rc));
		
		assertEquals(credits, this.service.getCreditCountForUser("rec22"));
		
	}
	
	/**
	* Tests deletion of Listing of a specific recruiter
	* @throws Exception
	*/
	@Test
	void testDeleteRecruiterListings() {
		
		final String recruiterId = "anId";
		
		when(this.mockListingRepository.findAllListings(any(), any())).thenReturn(Set.of(Listing.builder().listingId(UUID.randomUUID()).build(), Listing.builder().listingId(UUID.randomUUID()).build()));
		
		this.service.deleteRecruiterListings(recruiterId);
		
		verify(this.mockListingRepository, times(2)).deleteById(any());
		
	}
	
	/**
	* Test grouping of individual events into stats
	*/
	@Test
	void testFetchListingContactRequestStats() {
		
		UUID listing1Id = UUID.randomUUID();
		UUID listing2Id = UUID.randomUUID();
		UUID listing3Id = UUID.randomUUID();
		
		ListingContactRequestEvent ev1 = ListingContactRequestEvent.builder().eventId(null).listingId(listing1Id).userType(CONTACT_USER_TYPE.REGISTERED).build();
		ListingContactRequestEvent ev2 = ListingContactRequestEvent.builder().eventId(null).listingId(listing2Id).userType(CONTACT_USER_TYPE.REGISTERED).build();
		ListingContactRequestEvent ev3 = ListingContactRequestEvent.builder().eventId(null).listingId(listing3Id).userType(CONTACT_USER_TYPE.UNREGISTERED).build();
		ListingContactRequestEvent ev4 = ListingContactRequestEvent.builder().eventId(null).listingId(listing1Id).userType(CONTACT_USER_TYPE.UNREGISTERED).build();
		ListingContactRequestEvent ev5 = ListingContactRequestEvent.builder().eventId(null).listingId(listing1Id).userType(CONTACT_USER_TYPE.REGISTERED).build();
		ListingContactRequestEvent ev6 = ListingContactRequestEvent.builder().eventId(null).listingId(listing1Id).userType(CONTACT_USER_TYPE.REGISTERED).build();
		ListingContactRequestEvent ev7 = ListingContactRequestEvent.builder().eventId(null).listingId(listing2Id).userType(CONTACT_USER_TYPE.UNREGISTERED).build();
		ListingContactRequestEvent ev8 = ListingContactRequestEvent.builder().eventId(null).listingId(listing3Id).userType(CONTACT_USER_TYPE.UNREGISTERED).build();
		ListingContactRequestEvent ev9 = ListingContactRequestEvent.builder().eventId(null).listingId(listing3Id).userType(CONTACT_USER_TYPE.REGISTERED).build();
		ListingContactRequestEvent ev0 = ListingContactRequestEvent.builder().eventId(null).listingId(listing3Id).userType(CONTACT_USER_TYPE.UNREGISTERED).build();
		
		Set<ListingContactRequestEvent> events = Set.of(ev1,ev2,ev3,ev4,ev5,ev6,ev7,ev8,ev9,ev0);
		
		when(this.mockListingContactRequestEventDao.fetchEventForListings(anySet())).thenReturn(events);
		
		Set<UUID> ids = new HashSet<>();
		
		ids.add(listing1Id);
		ids.add(listing2Id);
		ids.add(listing3Id);
		
		Set<ListingStatContactRequests> stats = this.service.fetchListingContactRequestStats(ids);
		
		ListingStatContactRequests stat1 = stats.stream().filter(s -> s.getListingId() == listing1Id).findAny().orElseThrow();
		ListingStatContactRequests stat2 = stats.stream().filter(s -> s.getListingId() == listing2Id).findAny().orElseThrow();
		ListingStatContactRequests stat3 = stats.stream().filter(s -> s.getListingId() == listing3Id).findAny().orElseThrow();
		
		assertEquals(3, stat1.getRegisteredUserRequests());
		assertEquals(1, stat1.getUnregisteredUserRequests());
		
		assertEquals(1, stat2.getRegisteredUserRequests());
		assertEquals(1, stat1.getUnregisteredUserRequests());
		
		assertEquals(1, stat3.getRegisteredUserRequests());
		assertEquals(3, stat3.getUnregisteredUserRequests());
		
	}
	
}