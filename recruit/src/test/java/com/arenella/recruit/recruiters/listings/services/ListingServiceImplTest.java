package com.arenella.recruit.recruiters.listings.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.arenella.recruit.adapters.actions.GrantCreditCommand;
import com.arenella.recruit.listings.adapters.ExternalEventPublisher;
import com.arenella.recruit.listings.adapters.RequestListingContactEmailCommand;
import com.arenella.recruit.listings.beans.Listing;
import com.arenella.recruit.listings.beans.ListingViewedEvent;
import com.arenella.recruit.listings.controllers.ListingContactRequest;
import com.arenella.recruit.listings.dao.ListingDao;
import com.arenella.recruit.listings.dao.ListingEntity;
import com.arenella.recruit.listings.exceptions.ListingValidationException;
import com.arenella.recruit.listings.services.FileSecurityParser;
import com.arenella.recruit.listings.services.FileSecurityParser.FileType;
import com.arenella.recruit.listings.beans.RecruiterCredit;
import com.arenella.recruit.listings.dao.ListingRecruiterCreditDao;
import com.arenella.recruit.listings.services.ListingServiceImpl;
import com.arenella.recruit.listings.utils.ListingFunctionSynonymUtil;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

/**
* Unit tests for the ListingService class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
public class ListingServiceImplTest {

	@InjectMocks
	private ListingServiceImpl 	service = new ListingServiceImpl();
	
	@Mock
	private ListingDao 						mockListingDao;
	
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
	
	/**
	* Sets up test environment
	* @throws Exception
	*/
	@BeforeEach
	public void init() throws Exception {
		SecurityContextHolder.getContext().setAuthentication(mockAuthentication);
	}
	
	/**
	* Tests happy path for Listing creation
	* @throws Exception
	*/
	@Test
	public void testAddListing() throws Exception {
		
		final Listing 			listing 		= Listing
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
		
		Mockito.verify(mockListingDao).save(Mockito.any(ListingEntity.class));
		
	}
	
	/**
	* Asserts Exception is thrown if the Listing being updated does not exist
	* @throws Exception
	*/
	@Test
	public void testUpdateListing_unknownListing() throws Exception{
		
		final UUID listingId = UUID.randomUUID();
		
		Mockito.when(this.mockListingDao.findById(listingId)).thenReturn(Optional.empty());
		
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
	public void testUpdateListing_notOwnerOfListing() throws Exception{
		
		final UUID 				listingId 		= UUID.randomUUID();
		final Listing			listing			= Listing.builder().ownerId("kevin").title("a").description("b").ownerCompany("c").ownerName("d").ownerEmail("e").build();
		final ListingEntity 	existingEntity 	= ListingEntity.builder().ownerId("kevin").title("a").description("b").ownerCompany("c").ownerName("d").ownerEmail("e").build();
		
		Mockito.when(this.mockListingDao.findById(listingId)).thenReturn(Optional.of(existingEntity));
		Mockito.when(mockAuthentication.getPrincipal()).thenReturn("notKevin");
		
		Assertions.assertThrows(AccessDeniedException.class, () -> {
			this.service.updateListing(listingId, listing);
		});
		
	}

	/**
	* Asserts Exception is thrown if the Listing being Updates does not exist
	* @throws Exception
	*/
	@Test
	public void testDeleteListing_unknownListing() throws Exception{
		
		final UUID listingId = UUID.randomUUID();
		
		Mockito.when(this.mockListingDao.findById(listingId)).thenReturn(Optional.empty());
		
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
	public void testDeleteListing_notOwnerOfListing() throws Exception{
		
		final UUID 				listingId 		= UUID.randomUUID();
		final ListingEntity 	existingEntity 	= ListingEntity.builder().ownerId("kevin").title("a").description("b").ownerCompany("c").ownerName("d").ownerEmail("e").build();
		
		Mockito.when(this.mockListingDao.findById(listingId)).thenReturn(Optional.of(existingEntity));
		Mockito.when(mockAuthentication.getPrincipal()).thenReturn("notKevin");
		
		Assertions.assertThrows(AccessDeniedException.class, () -> {
			this.service.deleteListing(listingId);
		});
		 
	}
	
	/**
	* Tests happy path
	* @throws Exception
	*/
	@Test
	public void testUpdateListing() throws Exception {
		
		final UUID 				listingId 		= UUID.randomUUID();
		final String			ownerId			= "kevin";
		final Listing			listing			= Listing.builder().ownerId("kevin").title("a").description("b").ownerCompany("c").ownerName("d").ownerEmail("e").build();
		final ListingEntity 	existingEntity 	= ListingEntity.builder().ownerId(ownerId).title("a").description("b").ownerCompany("c").ownerName("d").ownerEmail("e").build();
		
		Mockito.when(this.mockListingDao.findById(listingId)).thenReturn(Optional.of(existingEntity));
		Mockito.when(mockAuthentication.getPrincipal()).thenReturn(ownerId);
		
		this.service.updateListing(listingId, listing);
		
		Mockito.verify(this.mockListingDao).save(Mockito.any(ListingEntity.class));
	}
	
	/**
	* Tests happy path
	* @throws Exception
	*/
	@Test
	public void testDeleteListing() throws Exception {
		
		final UUID 				listingId 		= UUID.randomUUID();
		final String			ownerId			= "kevin";
		final ListingEntity 	existingEntity 	= ListingEntity.builder().ownerId(ownerId).title("a").description("b").ownerCompany("c").ownerName("d").ownerEmail("e").build();
		
		Mockito.when(this.mockListingDao.findById(listingId)).thenReturn(Optional.of(existingEntity));
		Mockito.when(mockAuthentication.getPrincipal()).thenReturn(ownerId);
		
		this.service.deleteListing(listingId);
		
		Mockito.verify(this.mockListingDao).delete(existingEntity);
		
	}
	
	/**
	* Tests Exception thrown if one of the mandatory fields 
	* has no value provided
	* @throws Exception
	*/
	@Test
	public void testAddListingValidation_missingValue() throws Exception {
		
		Assertions.assertThrows(ListingValidationException.class, () -> {
			
			Listing 			listing 		= Listing
					.builder()
						.title(null)
						.description("aDesc")
						.ownerName("anOwnerName")
						.ownerEmail("anEmail")
						.ownerCompany("aCompany")
					.build();

			service.addListing(listing, true);

			
		});
		

		Assertions.assertThrows(ListingValidationException.class, () -> {
			Listing 			listing 		= Listing
					.builder()
						.title("aTitle")
						.description(null)
						.ownerName("anOwnerName")
						.ownerEmail("anEmail")
						.ownerCompany("aCompany")
					.build();

			service.addListing(listing, true);

		});

		Assertions.assertThrows(ListingValidationException.class, () -> {
			Listing 			listing 		= Listing
					.builder()
						.title("aTitle")
						.description("aDesc")
						.ownerName(null)
						.ownerEmail("anEmail")
						.ownerCompany("aCompany")
					.build();

			service.addListing(listing, true);

		});
		
		Assertions.assertThrows(ListingValidationException.class, () -> {
			
			Listing 			listing 		= Listing
					.builder()
						.title("aTitle")
						.description("aDesc")
						.ownerName("anOwnerName")
						.ownerEmail(null)
						.ownerCompany("aCompany")
					.build();

			service.addListing(listing, true);

		});

		Assertions.assertThrows(ListingValidationException.class, () -> {
			
			Listing 			listing 		= Listing
					.builder()
						.title(null)
						.description("aDesc")
						.ownerName("anOwnerName")
						.ownerEmail("anEmail")
						.ownerCompany(null)
					.build();

			service.addListing(listing, true);
			
		});

	}
	
	/**
	* Tests Exception thrown if one of the mandatory fields 
	* have empty values provided
	* @throws Exception
	*/
	@Test
	public void testAddListingValidation_emptyStringValue() throws Exception {

		Assertions.assertThrows(ListingValidationException.class, () -> {
			
			Listing 			listing 		= Listing
					.builder()
						.title("  ")
						.description("aDesc")
						.ownerName("anOwnerName")
						.ownerEmail("anEmail")
						.ownerCompany("aCompany")
					.build();

			service.addListing(listing, true);

		});

		Assertions.assertThrows(ListingValidationException.class, () -> {
			Listing 			listing 		= Listing
					.builder()
						.title("aTitle")
						.description("  ")
						.ownerName("anOwnerName")
						.ownerEmail("anEmail")
						.ownerCompany("aCompany")
					.build();

			service.addListing(listing, true);

		});

		Assertions.assertThrows(ListingValidationException.class, () -> {
			Listing 			listing 		= Listing
					.builder()
						.title("aTitle")
						.description("aDesc")
						.ownerName("  ")
						.ownerEmail("anEmail")
						.ownerCompany("aCompany")
					.build();

			service.addListing(listing, true);

		});
		
		Assertions.assertThrows(ListingValidationException.class, () -> {
			
			Listing 			listing 		= Listing
					.builder()
						.title("aTitle")
						.description("aDesc")
						.ownerName("anOwnerName")
						.ownerEmail("  ")
						.ownerCompany("aCompany")
					.build();

			service.addListing(listing, true);

		});

		Assertions.assertThrows(ListingValidationException.class, () -> {
			
			Listing 			listing 		= Listing
					.builder()
						.title(null)
						.description("aDesc")
						.ownerName("anOwnerName")
						.ownerEmail("anEmail")
						.ownerCompany("  ")
					.build();

			service.addListing(listing, true);
			
		});

	}
	
	/**
	* Tests exception is thrown if attempt is made to log an event
	* for viewing a Listing that does not exist
	* @throws Exception
	*/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testRegisterListingViewedEvent_unknownListing() throws Exception {
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_RECRUITER"));

		Mockito.when(mockAuthentication.getAuthorities()).thenReturn(authorities);
		
		final UUID eventId 		= UUID.randomUUID();
		final UUID listingId 	= UUID.randomUUID();
		final LocalDateTime created = LocalDateTime.of(2022, 1, 14, 10, 11, 12);
		ListingViewedEvent event = ListingViewedEvent.builder().eventId(eventId).created(created).listingId(listingId).build();
				
		Mockito.when(this.mockListingDao.findById(event.getListingId())).thenReturn(Optional.empty());
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.service.registerListingViewedEvent(event);
		});
		
	}
	
	/**
	* Tests logging of event to show Listing was viewed
	* @throws Exception
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testRegisterListingViewedEvent() throws Exception {
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_RECRUITER"));

		Mockito.when(mockAuthentication.getAuthorities()).thenReturn(authorities);
		
		final UUID eventId 		= UUID.randomUUID();
		final UUID listingId 	= UUID.randomUUID();
		final LocalDateTime created = LocalDateTime.of(2022, 1, 14, 10, 11, 12);
		
		ListingViewedEvent 	event 		= ListingViewedEvent.builder().eventId(eventId).created(created).listingId(listingId).build();
		ListingEntity 		eventEntity = ListingEntity.builder().created(created).listingId(listingId).build();
				
		Mockito.when(this.mockListingDao.findById(event.getListingId())).thenReturn(Optional.of(eventEntity));
		
		this.service.registerListingViewedEvent(event);
		
		Mockito.verify(this.mockListingDao, Mockito.times(1)).save(Mockito.any());
		
	}
	
	/**
	* Tests if Admin user views event it is not logged
	* @throws Exception
	*/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testRegisterListingViewedEvent_admin() throws Exception {
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

		Mockito.when(mockAuthentication.getAuthorities()).thenReturn(authorities);
		
		final UUID eventId 		= UUID.randomUUID();
		final UUID listingId 	= UUID.randomUUID();
		final LocalDateTime created = LocalDateTime.of(2022, 1, 14, 10, 11, 12);
		
		ListingViewedEvent 	event 		= ListingViewedEvent.builder().eventId(eventId).created(created).listingId(listingId).build();
		
		this.service.registerListingViewedEvent(event);
		
		Mockito.verify(this.mockListingDao, Mockito.times(0)).save(Mockito.any());
		
	}
	
	/**
	* Tests if a file is considered unsafe that an exception is thrown
	* @throws Exception
	*/
	@Test
	public void testSendContactRequestToListingOwner_unsafeFile() throws Exception{
		
		Mockito.when(this.mockFileSecurityParser.isSafe(Mockito.any())).thenReturn(false);
		MultipartFile mockMultipartFile = Mockito.mock(MultipartFile.class);
		
		Assertions.assertThrows(RuntimeException.class, () -> {
			this.service.sendContactRequestToListingOwner(ListingContactRequest.builder().attachment(mockMultipartFile).build());
		});
		
	}

	/**
	* Tests if Listing is not found exception is thrown
	* @throws Exception
	*/
	@Test
	public void testSendContactRequestToListingOwner_unknownListing() throws Exception{
		
		Mockito.when(this.mockFileSecurityParser.isSafe(Mockito.any())).thenReturn(true);
		Mockito.when(this.mockListingDao.findListingById(Mockito.any(UUID.class))).thenReturn(Optional.empty());
		
		Assertions.assertThrows(RuntimeException.class, () -> {
			this.service.sendContactRequestToListingOwner(ListingContactRequest.builder().build());
		});
		
	}
	
	/**
	* Tests generation and sending of contact event for the owner of the Listing
	* @throws Exception
	*/
	@Test
	public void testSendContactRequestToListingOwner_success() throws Exception{
		
		final UUID				listingId		= UUID.randomUUID();
		final String 			title 			= "Java Developer";
		final String 			ownerId			= "kparkings001";
		final MultipartFile 	attachment 		= Mockito.mock(MultipartFile.class);
		final String			message			= "dear recruiter blah blah";
		final String 			senderName		= "Kevin Parkings";
		final String			senderEmail		= "admin@arenella-ict.com";
		final byte[]			attachmentBytes	= new byte[] {1,22,3};
		final FileType			fileType		= FileType.doc;
		
		ArgumentCaptor<RequestListingContactEmailCommand> capt = ArgumentCaptor.forClass(RequestListingContactEmailCommand.class);
		
		final Listing listing = Listing.builder().title(title).ownerId(ownerId).build();
		
		Mockito.when(this.mockFileSecurityParser.isSafe(Mockito.any())).thenReturn(true);
		Mockito.when(this.mockListingDao.findListingById(Mockito.any(UUID.class))).thenReturn(Optional.of(listing));
		Mockito.when(this.mockFileSecurityParser.getFileType(Mockito.any())).thenReturn(fileType);
		Mockito.when(attachment.getBytes()).thenReturn(attachmentBytes);
		
		Mockito.doNothing().when(this.mockExternalEventPublisher).publicRequestSendListingContactEmailCommand(capt.capture());
		
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
		
	}
	
	/**
	* Tests activating of Listings for a Recruiter
	* @throws Exception
	*/
	@Test
	public void testEnableListingsForRecruiter() throws Exception{
		
		final String recruiterId = "kparkings01";
		
		Set<Listing> listings = Set.of(Listing.builder().active(false).build(), Listing.builder().active(false).build());
		
		@SuppressWarnings("unchecked")
		ArgumentCaptor<Set<Listing>> listingArgCapt = ArgumentCaptor.forClass(Set.class);
		
		Mockito.when(this.mockListingDao.findAllListings(Mockito.any())).thenReturn(listings);
		Mockito.doNothing().when(this.mockListingDao).saveListings(listingArgCapt.capture());
		
		this.service.enableListingsForRecruiter(recruiterId);
		
		Mockito.verify(this.mockListingDao).saveListings(Mockito.anySet());
		
		listingArgCapt.getValue().stream().forEach(l -> Assertions.assertTrue(l.isActive()));
		
	}
	
	/**
	* Tests activating of Listings for a Recruiter
	* @throws Exception
	*/
	@Test
	public void testDisableListingsForRecruiter() throws Exception{
		
		final String recruiterId = "kparkings01";
		
		Set<Listing> listings = Set.of(Listing.builder().active(true).build(), Listing.builder().active(true).build());
		
		@SuppressWarnings("unchecked")
		ArgumentCaptor<Set<Listing>> listingArgCapt = ArgumentCaptor.forClass(Set.class);
		
		Mockito.when(this.mockListingDao.findAllListings(Mockito.any())).thenReturn(listings);
		Mockito.doNothing().when(this.mockListingDao).saveListings(listingArgCapt.capture());
		
		this.service.disableListingsForRecruiter(recruiterId);
		
		Mockito.verify(this.mockListingDao).saveListings(Mockito.anySet());
		
		listingArgCapt.getValue().stream().forEach(l -> Assertions.assertFalse(l.isActive()));
		
	}
	
	/**
	* Test credits assigned and event sent
	* @throws Exception
	*/
	@SuppressWarnings("unchecked")
	@Test
	public void testUpdateCredits() throws Exception{
		
		ArgumentCaptor<Set<RecruiterCredit>> argCapt = ArgumentCaptor.forClass(Set.class);
		
		RecruiterCredit rc1 = RecruiterCredit.builder().recruiterId("rec1").credits(1).build();
		RecruiterCredit rc2 = RecruiterCredit.builder().recruiterId("rec2").credits(2).build();
		RecruiterCredit rc3 = RecruiterCredit.builder().recruiterId("rec3").credits(RecruiterCredit.DISABLED_CREDITS).build();
		
		Mockito.doNothing().when(this.mockCreditDao).saveAll(argCapt.capture());
		Mockito.when(this.mockCreditDao.fetchRecruiterCredits()).thenReturn(Set.of(rc1,rc2,rc3));
		
		service.updateCredits(new GrantCreditCommand());
		
		assertEquals(2, argCapt.getValue().stream().filter(rc -> rc.getCredits()== RecruiterCredit.DEFAULT_CREDITS).count());
		assertEquals(1, argCapt.getValue().stream().filter(rc -> rc.getCredits()== RecruiterCredit.DISABLED_CREDITS).count());
		
	}
	
	/**
	* Test false returned if User not known
	* @throws Exception
	*/
	@Test
	public void testDoCreditsCheck_unknownUser() throws Exception{
		
		Mockito.when(this.mockCreditDao.getByRecruiterId(Mockito.anyString())).thenReturn(Optional.empty());
		
		assertFalse(service.doCreditsCheck("recruiter33"));
		
	}
	
	/**
	* Test false returned if User has no remaining credits
	* @throws Exception
	*/
	@Test
	public void testDoCreditsCheck_knownUser_no_credits() throws Exception{
		
		RecruiterCredit rc = RecruiterCredit.builder().credits(0).build();
		
		Mockito.when(this.mockCreditDao.getByRecruiterId(Mockito.anyString())).thenReturn(Optional.of(rc));
		
		assertFalse(service.doCreditsCheck("recruiter33"));
		
	} 
	
	/**
	* Test false returned if User has no remaining credits
	* @throws Exception
	*/
	@Test
	public void testDoCreditsCheck_knownUser_has_credits() throws Exception{
		
		RecruiterCredit rc = RecruiterCredit.builder().credits(1).build();
		
		Mockito.when(this.mockCreditDao.getByRecruiterId(Mockito.anyString())).thenReturn(Optional.of(rc));
		
		assertTrue(service.doCreditsCheck("recruiter33"));
		
	}
	
	/**
	* Happy path
	* @throws Exception
	*/
	@Test
	public void testUpdateCreditsForUser() throws Exception{
		
		final String 	userId 		= "kparkings";
		final int 		credits 	= 20;

		ArgumentCaptor<RecruiterCredit> argCapt = ArgumentCaptor.forClass(RecruiterCredit.class);
		
		RecruiterCredit recCredits = RecruiterCredit.builder().recruiterId(userId).credits(30).build();
		
		Mockito.when(this.mockCreditDao.getByRecruiterId(userId)).thenReturn(Optional.of(recCredits));
		Mockito.doNothing().when(this.mockCreditDao).persist(argCapt.capture());
		
		this.service.updateCreditsForUser(userId, credits);
	
		Mockito.verify(this.mockCreditDao).persist(Mockito.any());
		
		assertEquals(credits, argCapt.getValue().getCredits());
		
	}
	
	/**
	* If no credits for user does nothing
	* @throws Exception
	*/
	@Test
	public void testUpdateCreditsForUser_unknownRecruiter() throws Exception{
		
		final String 	userId 		= "kparkings";
		final int 		credits 	= 20;

		Mockito.when(this.mockCreditDao.getByRecruiterId(userId)).thenReturn(Optional.empty());
		
		this.service.updateCreditsForUser(userId, credits);
	
		Mockito.verify(this.mockCreditDao, Mockito.never()).persist(Mockito.any());
		
	}
	
	/**
	* Tests case no credits
	* @throws Exception
	*/
	@Test
	public void testGetCreditCountForUser_unknownUser() throws Exception{
		
		Mockito.when(this.mockCreditDao.getByRecruiterId(Mockito.anyString())).thenReturn(Optional.empty());
		
		assertThrows(IllegalArgumentException.class, () -> {
			this.service.getCreditCountForUser("rec22");
		});
		
	}
	
	/**
	* Tests case no credits
	* @throws Exception
	*/
	@Test
	public void testGetCreditCountForUser() throws Exception{
		
		final int credits = 5;
		
		RecruiterCredit rc = RecruiterCredit.builder().credits(credits).build();
		
		Mockito.when(this.mockCreditDao.getByRecruiterId(Mockito.anyString())).thenReturn(Optional.of(rc));
		
		assertEquals(credits, this.service.getCreditCountForUser("rec22"));
		
	}
	
	/**
	* Tests deletion of Listing of a specific recruiter
	* @throws Exception
	*/
	@Test
	public void testDeleteRecruiterListings() throws Exception {
		
		final String recruiterId = "anId";
		
		Mockito.when(this.mockListingDao.findAllListings(Mockito.any())).thenReturn(Set.of(Listing.builder().listingId(UUID.randomUUID()).build(), Listing.builder().listingId(UUID.randomUUID()).build()));
		
		this.service.deleteRecruiterListings(recruiterId);
		
		Mockito.verify(this.mockListingDao, Mockito.times(2)).deleteById(Mockito.any());
		
	}
	
}