package com.arenella.recruit.listings.services;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.arenella.recruit.adapters.actions.GrantCreditCommand;
import com.arenella.recruit.listings.utils.ListingGeoZoneSearchUtil.GEO_ZONE;

import co.elastic.clients.elasticsearch.ElasticsearchClient;

import com.arenella.recruit.listings.adapters.CandidateRequestListingContactEmailCommand;
import com.arenella.recruit.listings.adapters.CandidateRequestListingContactEmailCommand.CandidateRequestListingContactEmailCommandBuilder;
import com.arenella.recruit.listings.adapters.ExternalEventPublisher;
import com.arenella.recruit.listings.adapters.RequestListingContactEmailCommand;
import com.arenella.recruit.listings.adapters.RequestListingContactEmailCommand.RequestListingContactEmailCommandBuilder;
import com.arenella.recruit.listings.beans.Listing;
import com.arenella.recruit.listings.beans.ListingFilter;
import com.arenella.recruit.listings.beans.ListingViewedEvent;
import com.arenella.recruit.listings.beans.RecruiterCredit;
import com.arenella.recruit.listings.controllers.CandidateListingContactRequest;
import com.arenella.recruit.listings.controllers.ListingContactRequest;

import com.arenella.recruit.listings.dao.ListingRecruiterCreditDao;
import com.arenella.recruit.listings.exceptions.ListingValidationException;
import com.arenella.recruit.listings.exceptions.ListingValidationException.ListingValidationExceptionBuilder;
import com.arenella.recruit.listings.repos.ListingRepository;
import com.arenella.recruit.listings.services.FileSecurityParser.FileType;
import com.arenella.recruit.listings.utils.ListingFunctionSynonymUtil;
import com.arenella.recruit.listings.utils.ListingGeoZoneSearchUtil;

/**
* Services for working with Listings
* @author K Parkings
*/
@Service
public class ListingServiceImpl implements ListingService{

	@Autowired
	//private ListingDao 						listingDao;
	private ListingRepository				listingRepository;
	
	@Autowired
	private ElasticsearchClient				esClient;
	
	@Autowired
	private FileSecurityParser 				fileSecurityParser;
	
	@Autowired
	private ExternalEventPublisher			externalEventPublisher;
	
	@Autowired
	private ListingRecruiterCreditDao 		creditDao;
	
	@Autowired
	private ListingGeoZoneSearchUtil		listingGeoZoneSearchUtil;
	
	@Autowired
	private ListingFunctionSynonymUtil		functionSynonymUil;
	
	/**
	* Refer to the Listing interface for details
	*/
	@Override
	public UUID addListing(Listing listing, boolean postToSocialMedia) {
		
		listing.initializeAsNewListing();
		listing.setOwnerId(SecurityContextHolder.getContext().getAuthentication().getName());
		
		this.performValidation(listing);
		
		//ListingEntity entity = ListingEntity.convertToEntity(listing, Optional.empty()); 
		this.listingRepository.saveListings(Set.of(listing));
		
		//this.listingDao.save(entity);
		
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
		
		//ListingEntity 	entity 			= this.listingDao.findById(listingId).orElseThrow(() -> new IllegalArgumentException("Cannot update unknown listing: " + listingId));
		Listing 		originalListing 	= this.listingRepository.findListingById(listingId).orElseThrow(() -> new IllegalArgumentException("Cannot update unknown listing: " + listingId));
		String 			currentUser			= SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		
		this.performValidation(listing);
		
		/**
		* 1. Only the owner can perform an update
		* 2. The owner cannot change the owner from themselves to another recruiter 
		*/
		if (!originalListing.getOwnerId().toString().equals(currentUser) || !listing.getOwnerId().toString().equals(currentUser)) {
			throw new AccessDeniedException("Not authroised to alter this Listing");
		}
		
		//ListingEntity entityUpdated = ListingEntity.convertToEntity(listing, Optional.of(entity)); 
		
		//this.listingDao.save(entityUpdated);
		
		this.listingRepository.saveListings(Set.of(listing));
		
	}

	/**
	* Refer to the Listing interface for details
	*/
	@Override
	public void deleteListing(UUID listingId) {
		
		//ListingEntity 	entity 			= this.listingDao.findById(listingId).orElseThrow(() -> new IllegalArgumentException("Cannot delete unknown listing: " + listingId));
		Listing 	listing 			= this.listingRepository.findListingById(listingId).orElseThrow(() -> new IllegalArgumentException("Cannot delete unknown listing: " + listingId));
		String 			currentUser		= SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		
		if (!listing.getOwnerId().toString().equals(currentUser)) {
			throw new AccessDeniedException("Not authroised to alter this Listing");
		}
		
		this.listingRepository.deleteById(listingId);
		//this.listingDao.delete(entity);
	}

	/**
	* Refer to the Listing interface for details
	*/
	@SuppressWarnings("static-access")
	@Override
	public Page<Listing> fetchListings(ListingFilter filters, Pageable pageable) {
		
		GEO_ZONE[] geoZones = filters.getGeoZones().toArray(new GEO_ZONE[] {});
		
		listingGeoZoneSearchUtil.fetchCountriesFor(geoZones).stream().forEach(country -> filters.addCountry(country));
		
		if (!filters.getSearchTerms().isEmpty()) {
			String originalTerm = (String)filters.getSearchTerms().toArray()[0];
			
		//	Set<String> allTerms = new HashSet<>();
			
			this.functionSynonymUil.extractAllFunctionAndSynonyms(originalTerm).stream().forEach(synonym -> {
				filters.addSearchTerm(synonym);
				//allTerms.add(synonym);
			});
			
			//allTerms.forEach(t -> filters.addSearchTerm(t));
			
			//System.out.println("boop");
		}
		
		//return listingDao.findAll(filters, pageable).map(ListingEntity::convertFromEntity);
		return listingRepository.findAll(filters, this.esClient, pageable);
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
		
		//ListingEntity listingEntity = this.listingDao.findById(event.getListingId()).orElseThrow(() -> new IllegalArgumentException("Cannot update unknown listing: " + event.getListingId()));
		Listing listing  = this.listingRepository.findListingById(event.getListingId()).orElseThrow(() -> new IllegalArgumentException("Cannot update unknown listing: " + event.getListingId()));
		
		//ListingViewedEventEntity viewEntity = ListingViewedEventEntity.convertToEntity(event);
		
		//listingEntity.addView(viewEntity);
		listing.addView(event);
		
		
		//this.listingDao.save(listingEntity);
		this.listingRepository.saveListings(Set.of(listing));
	}

	/**
	* Refer to the Listing interface for details
	*/
	@Override
	public void sendContactRequestToListingOwner(ListingContactRequest contactRequest) {
		
		performFileSafetyCheck(contactRequest.getAttachment());
		
		//Listing listing = this.listingDao.findListingById(contactRequest.getListingId()).orElseThrow(() -> new RuntimeException("Unknown Listing"));
		Listing listing = this.listingRepository.findListingById(contactRequest.getListingId()).orElseThrow(() -> new RuntimeException("Unknown Listing"));
		
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
	public void disableListingsForRecruiter(String recruiterId) {
		
		ListingFilter filters = ListingFilter.builder().ownerId(recruiterId).build();
		
		//Set<Listing> listings = this.listingDao.findAllListings(filters);
		Set<Listing> listings = this.listingRepository.findAllListings(filters, this.esClient);
		
		listings.stream().forEach(l -> l.setActive(false));
		
		//this.listingDao.saveListings(listings);
		this.listingRepository.saveListings(listings);
	}

	/**
	* Refer to the Listing interface for details
	*/
	@Override
	public void enableListingsForRecruiter(String recruiterId) {
		
		ListingFilter filters = ListingFilter.builder().ownerId(recruiterId).build();
		
		//Set<Listing> listings = this.listingDao.findAllListings(filters);
		Set<Listing> listings = this.listingRepository.findAllListings(filters, this.esClient);
		
		listings.stream().forEach(l -> l.setActive(true));
		
		//this.listingDao.saveListings(listings);
		this.listingRepository.saveListings(listings);
		
	}
	
	/**
	* Refer to the Listing interface for details
	*/
	@Override
	public void sendContactRequestFomCandidateToListingOwner(CandidateListingContactRequest contactRequest) {
		
		if (contactRequest.getAttachment().isPresent()) {
			performFileSafetyCheck(contactRequest.getAttachment().get());
		}
		
		//Listing listing = this.listingDao.findListingById(contactRequest.getListingId()).orElseThrow(() -> new RuntimeException("Unknown Listing"));
		Listing listing = this.listingRepository.findListingById(contactRequest.getListingId()).orElseThrow(() -> new RuntimeException("Unknown Listing"));
		
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
	* Refer to the ListingsService for details 
	*/
	@Override
	public void updateCredits(GrantCreditCommand command) {
		
		Set<RecruiterCredit> credits = this.creditDao.fetchRecruiterCredits();
		
		credits.stream().filter(rc -> rc.getCredits() != RecruiterCredit.DISABLED_CREDITS).forEach(credit -> credit.setCredits(RecruiterCredit.DEFAULT_CREDITS));
		
		creditDao.saveAll(credits);
		
	}

	/**
	* Refer to the ListingsService for details 
	*/
	@Override
	public Boolean doCreditsCheck(String recruiterId) {
		
		Optional<RecruiterCredit> recruiterCreditOpt = this.creditDao.getByRecruiterId(recruiterId);
		
		if (recruiterCreditOpt.isEmpty()) {
			return false;
		}
		
		return recruiterCreditOpt.get().getCredits() > 0;
		
	}
	
	/**
	* Refer to the CurriclumService interface for details
	* @param userId
	*/
	@Override
	public void useCredit(String userId) {
		
		RecruiterCredit credit = this.creditDao.getByRecruiterId(userId).orElseThrow(() -> new IllegalArgumentException("Unknown User - Cant use Credit"));
		
		if (credit.getCredits() == 0) {
			throw new IllegalStateException("No credits available for User");
		}
		
		credit.decrementCredits();
		
		this.creditDao.persist(credit);
		
	}
	
	/**
	* Refer to the CurriclumService for details 
	*/
	@Override
	public void updateCreditsForUser(String userId, int availableCredits) {
		
		Optional<RecruiterCredit> creditOpt = this.creditDao.getByRecruiterId(userId);
		
		if (!creditOpt.isPresent()) {
			return;
		}
		
		RecruiterCredit credits = creditOpt.get();
		
		credits.setCredits(availableCredits);
		
		creditDao.persist(credits);
		
	}
	
	/**
	* Refer to the Listings service for details 
	*/
	@Override
	public int getCreditCountForUser(String userId) {
		
		Optional<RecruiterCredit> credits =  this.creditDao.getByRecruiterId(userId);
			
		if(credits.isEmpty()) {
			throw new IllegalArgumentException("Unknown User: " + userId);	
		}
		
		return credits.get().getCredits();
		
	}
	
	/**
	* Refer to the ListingsService for details 
	*/
	@Override
	public void addCreditsRecordForUser(String userId) {
		
		Optional<RecruiterCredit> creditOpt = this.creditDao.getByRecruiterId(userId);
		
		if (creditOpt.isPresent()) {
			throw new IllegalStateException("Candidate Credits already exist for user " + userId);
		}
		
		creditDao.persist(RecruiterCredit.builder().recruiterId(userId).credits(RecruiterCredit.DISABLED_CREDITS).build());
		
	}
	
	/**
	* Refer to the ListingsService for details 
	*/
	@Override
	public void deleteRecruiterListings(String recruiterId) {
		
		ListingFilter filters = ListingFilter.builder().ownerId(recruiterId).build();
		
		//this.listingDao.findAllListings(filters).forEach(listing -> this.listingDao.deleteById(listing.getListingId()));
		this.listingRepository.findAllListings(filters, this.esClient).forEach(listing -> this.listingRepository.deleteById(listing.getListingId()));
		
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