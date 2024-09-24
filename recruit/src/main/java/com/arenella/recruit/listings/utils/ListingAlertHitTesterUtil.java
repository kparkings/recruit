package com.arenella.recruit.listings.utils;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.arenella.recruit.emailservice.adapters.RequestSendEmailCommand;
import com.arenella.recruit.emailservice.beans.Email.EmailRecipient;
import com.arenella.recruit.emailservice.beans.Email.EmailTopic;
import com.arenella.recruit.emailservice.beans.Email.EmailType;
import com.arenella.recruit.emailservice.beans.Email.Sender;
import com.arenella.recruit.emailservice.beans.Email.EmailRecipient.ContactType;
import com.arenella.recruit.emailservice.beans.Email.Sender.SenderType;
import com.arenella.recruit.listings.adapters.ExternalEventPublisher;
import com.arenella.recruit.listings.beans.Listing;
import com.arenella.recruit.listings.beans.ListingAlertFilterOptions;
import com.arenella.recruit.listings.services.ListingAlertService;

/**
* Utility for testing an new listing against the Listing alerts already setup in the
* system.
* @author K Parkings
*/
@Service
public class ListingAlertHitTesterUtil {

	@Autowired
	private ListingAlertService 	listingAlertService;
	
	@Autowired
	private CategoryExtractorUtil 	categoryExtractor;
	
	@Autowired
	private ExternalEventPublisher 	eventPublisher;
	
	private final HashSet<Listing> 	pendingListings = new HashSet<>();
	
	/**
	* Registers a new Listing to be tested against existing Alerts to 
	* see if the Listing is a match for one or more Alerts
	* @param listing - Listing to be tested against the Alerts
	*/
	public void registerListing(Listing listing) {
		this.pendingListings.add(listing);
	}
	
	/**
	* Task to periodically test pending Listings against the existing
	* Alerts to ascertain if there is a Hit
	*/
	@Scheduled(fixedRate=10000)
	private void pollForHits() {
		
		Optional<Listing> listing = this.pendingListings.stream().findFirst();
		
		if (listing.isEmpty()) {
			return;
		}
		
		this.pendingListings.remove(listing.get());
		
		ListingAlertFilterOptions filters = extractListingAlertFilters(listing.get());
		
		this.listingAlertService.fetchListingAlerts(filters).stream().forEach(hit -> {
			
			try {
				RequestSendEmailCommand command = RequestSendEmailCommand
						.builder()
							.emailType(EmailType.EXTERN)
							.recipients(Set.of(new EmailRecipient<UUID>(UUID.randomUUID(),hit.getEmail(), ContactType.UNREGISTERED_USER)))
							.sender(new Sender<>(UUID.randomUUID(), "", SenderType.SYSTEM, "no-reply@arenella-ict.com"))
							.title("Arenella-ICT - Matching Role")
							.topic(EmailTopic.LISTING_MATCHING_ROLE)
							.model(Map.of("listingId",listing.get().getListingId(),"listingTitle",listing.get().getTitle(),"alertId",hit.getId()))
							.persistable(false)
						.build();
				
				this.eventPublisher.RequestSendListingAlertHitEmailCommand(command);
			}catch(Exception e) {
				e.printStackTrace();
			}
		});
		
	}
	
	/**
	* Constructs the filters based upon the contents of the Listing
	* @param listing - Listing to extract filters from
	* @return Filters
	*/
	private ListingAlertFilterOptions extractListingAlertFilters(Listing listing) {
		
		ListingAlertFilterOptions.ListingAlertFilterOptionsBuilder filters = ListingAlertFilterOptions.builder();
		
		if (Optional.ofNullable(listing.getCountry()).isPresent()) {
			filters.countries(Set.of(listing.getCountry()));
		}
		
		if (Optional.ofNullable(listing.getType()).isPresent()) {
			filters.contractType(listing.getType());
		}
		
		this.categoryExtractor.extractFilters(listing.getTitle(), filters);
		
		return filters.build();
	
	}
	
}