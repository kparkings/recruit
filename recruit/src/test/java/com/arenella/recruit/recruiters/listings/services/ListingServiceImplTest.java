package com.arenella.recruit.recruiters.listings.services;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.arenella.recruit.listings.beans.Listing;
import com.arenella.recruit.listings.beans.ListingViewedEvent;
import com.arenella.recruit.listings.dao.ListingDao;
import com.arenella.recruit.listings.dao.ListingEntity;
import com.arenella.recruit.listings.exceptions.ListingValidationException;
import com.arenella.recruit.listings.services.ListingServiceImpl;

import org.springframework.security.core.context.SecurityContextHolder;

/**
* Unit tests for the ListingService class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
public class ListingServiceImplTest {

	@InjectMocks
	private ListingServiceImpl 	service = new ListingServiceImpl();
	
	@Mock
	private ListingDao 			mockListingDao;
	
	@Mock
	private	Authentication		mockAuthentication;
	
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

}