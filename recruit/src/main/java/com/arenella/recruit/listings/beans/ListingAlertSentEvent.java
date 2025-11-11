package com.arenella.recruit.listings.beans;

import java.time.LocalDate;
import java.util.UUID;

/**
* Represents and event of an alert being sent to a candidate, either registered or 
* unregistered to inform them of a new listing mathching their requirements 
*/
public record ListingAlertSentEvent (UUID id, UUID listingId, LocalDate alertSent){

}
