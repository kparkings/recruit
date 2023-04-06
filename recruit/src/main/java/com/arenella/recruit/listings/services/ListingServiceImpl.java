package com.arenella.recruit.listings.services;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.arenella.recruit.listings.adapters.CandidateRequestListingContactEmailCommand;
import com.arenella.recruit.listings.adapters.CandidateRequestListingContactEmailCommand.CandidateRequestListingContactEmailCommandBuilder;
import com.arenella.recruit.listings.adapters.ExternalEventPublisher;
import com.arenella.recruit.listings.adapters.RequestListingContactEmailCommand;
import com.arenella.recruit.listings.adapters.RequestListingContactEmailCommand.RequestListingContactEmailCommandBuilder;
import com.arenella.recruit.listings.beans.Listing;
import com.arenella.recruit.listings.beans.ListingFilter;
import com.arenella.recruit.listings.beans.ListingViewedEvent;
import com.arenella.recruit.listings.controllers.CandidateListingContactRequest;
import com.arenella.recruit.listings.controllers.ListingContactRequest;
import com.arenella.recruit.listings.dao.ListingDao;
import com.arenella.recruit.listings.dao.ListingEntity;
import com.arenella.recruit.listings.dao.ListingViewedEventEntity;
import com.arenella.recruit.listings.exceptions.ListingValidationException;
import com.arenella.recruit.listings.exceptions.ListingValidationException.ListingValidationExceptionBuilder;
import com.arenella.recruit.listings.services.FileSecurityParser.FileType;

/**
* Services for working with Listings
* @author K Parkings
*/
@Service
public class ListingServiceImpl implements ListingService{

	@Autowired
	private ListingDao 				listingDao;
	
	@Autowired
	private FileSecurityParser 		fileSecurityParser;
	
	@Autowired
	private ExternalEventPublisher	externalEventPublisher;
	
	/**
	* Refer to the Listing interface for details
	*/
	@Override
	public UUID addListing(Listing listing, boolean postToSocialMedia) {
		
		listing.initializeAsNewListing();
		listing.setOwnerId(SecurityContextHolder.getContext().getAuthentication().getName());
		
		this.performValidation(listing);
		
		ListingEntity entity = ListingEntity.convertToEntity(listing, Optional.empty()); 
		
		this.listingDao.save(entity);
		
		if (postToSocialMedia) {
			//TODO: Post on LinkedIN 
		}
		
		return listing.getListingId(); 
	}

	/**
	* Refer to the Listing interface for details
	*/
	@Override
	public void updateListing(UUID listingId, Listing listing) {
		
		ListingEntity 	entity 			= this.listingDao.findById(listingId).orElseThrow(() -> new IllegalArgumentException("Cannot update unknown listing: " + listingId));
		String 			currentUser		= SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		
		this.performValidation(listing);
		
		if (!entity.getOwnerId().toString().equals(currentUser)) {
			throw new AccessDeniedException("Not authroised to alter this Listing");
		}
		
		ListingEntity entityUpdated = ListingEntity.convertToEntity(listing, Optional.of(entity)); 
		
		this.listingDao.save(entityUpdated);
		
	}

	/**
	* Refer to the Listing interface for details
	*/
	@Override
	public void deleteListing(UUID listingId) {
		
		ListingEntity 	entity 			= this.listingDao.findById(listingId).orElseThrow(() -> new IllegalArgumentException("Cannot delete unknown listing: " + listingId));
		String 			currentUser		= SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		
		if (!entity.getOwnerId().toString().equals(currentUser)) {
			throw new AccessDeniedException("Not authroised to alter this Listing");
		}
		
		this.listingDao.delete(entity);
	}

	/**
	* Refer to the Listing interface for details
	*/
	@Override
	public Page<Listing> fetchListings(ListingFilter filters, Pageable pageable) {
		return listingDao.findAll(filters, pageable).map(listing -> ListingEntity.convertFromEntity(listing));
	}
	
	/**
	* Listing to Validate
	* @param listing
	*/
	private void performValidation(Listing listing) {
		
		ListingValidationExceptionBuilder exception = ListingValidationException.builder();
		
		if (!StringUtils.hasText(listing.getOwnerName())) {
			exception.addFailedValidationField("ownerName", "An Company name must be provided");
		}
		
		if (!StringUtils.hasText(listing.getOwnerEmail())) {
			exception.addFailedValidationField("ownerEmail", "An Company Email Address must be provided");
		}
		
		if (!StringUtils.hasText(listing.getOwnerCompany())) {
			exception.addFailedValidationField("ownerCompanyName", "An Company name must be provided");
		}
		
		if (!StringUtils.hasText(listing.getTitle())) {
			exception.addFailedValidationField("title", "A Listing Title must be provided");
		}
		
		if (!StringUtils.hasText(listing.getDescription())) {
			exception.addFailedValidationField("description", "A Listing Description must be provided");
		}	
		
		ListingValidationException builtException = exception.build();
		
		if (builtException.hasFailedFields()) {
			throw builtException;
		}
		
	}

	/**
	* Refer to the Listing interface for details
	*/
	@Override
	public void registerListingViewedEvent(ListingViewedEvent event) {
		
		/**
		* We don't want to know if admin has been viewing the listings 
		*/
		//TODO: BUG: This doesnt work because in WebSecurityConfig we return before the ROLE is assigend and the role for the user including Admin is ANONYMOUS_USER
		if (isAdmin()) {
			return;
		}
		
		ListingEntity listingEntity = this.listingDao.findById(event.getListingId()).orElseThrow(() -> new IllegalArgumentException("Cannot update unknown listing: " + event.getListingId()));
		
		ListingViewedEventEntity viewEntity = ListingViewedEventEntity.convertToEntity(event);
		
		listingEntity.addView(viewEntity);
		
		this.listingDao.save(listingEntity);
		
	}

	/**
	* Refer to the Listing interface for details
	*/
	@Override
	public void sendContactRequestToListingOwner(ListingContactRequest contactRequest) {
		
		performFileSafetyCheck(contactRequest.getAttachment());
		
		Listing listing = this.listingDao.findListingById(contactRequest.getListingId()).orElseThrow(() -> new RuntimeException("Unknown Listing"));
		
		try {
			
			RequestListingContactEmailCommandBuilder builder = RequestListingContactEmailCommand.builder();
			
			builder
				.listingName(listing.getTitle())
				.message(contactRequest.getMessage())
				.recruiterId(listing.getOwnerId())
				.senderEmail(contactRequest.getSenderEmail())
				.senderName(contactRequest.getSenderName());
			
			if (contactRequest.getAttachment() != null) {
				
				FileType fileType = fileSecurityParser.getFileType(contactRequest.getAttachment());
				
				builder
					.file(contactRequest.getAttachment().getBytes())
					.fileType(fileType.toString());
			}
			
			externalEventPublisher
				.publicRequestSendListingContactEmailCommand(builder.build());
		
		}catch(IOException e) {
			throw new RuntimeException("Unable to send request"); 
		}
	}
	
	/**
	* Refer to the Listing interface for details
	*/
	@Override
	public void sendContactRequestFomCandidateToListingOwner(CandidateListingContactRequest contactRequest) {
		
		if (contactRequest.getAttachment().isPresent()) {
			performFileSafetyCheck(contactRequest.getAttachment().get());
		}
		
		Listing listing = this.listingDao.findListingById(contactRequest.getListingId()).orElseThrow(() -> new RuntimeException("Unknown Listing"));
		
		try {
			
			CandidateRequestListingContactEmailCommandBuilder builder = CandidateRequestListingContactEmailCommand.builder();
			
			builder
				.listingName(listing.getTitle())
				.message(contactRequest.getMessage())
				.recruiterId(listing.getOwnerId())
				.candidateId(contactRequest.getCandidateId());
			
			if (contactRequest.getAttachment().isPresent()) {
				
				FileType fileType = fileSecurityParser.getFileType(contactRequest.getAttachment().get());
				
				builder
					.file(contactRequest.getAttachment().get().getBytes())
					.fileType(fileType.toString());
			}
			
			externalEventPublisher
				.publicRequestSendListingContactEmailCommand(builder.build());
		
		} catch(IOException e) {
			throw new RuntimeException("Unable to send request"); 
		}
		
	}
	
	/**
	* Checks safety of File
	* @param attachment
	*/
	private void performFileSafetyCheck(MultipartFile attachment) {
		
		if (attachment != null && !fileSecurityParser.isSafe(attachment)) {
			throw new RuntimeException("Invalid file type detected"); 
		}
		
	}
	
	/**
	* Whether or not the User is an Admin User
	* @return Whether or not the current User is an Admin
	*/
	private boolean isAdmin() {
		
		return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().filter(role -> role.getAuthority().equals("ROLE_ADMIN")).findAny().isPresent();
		
		
	}
	
}