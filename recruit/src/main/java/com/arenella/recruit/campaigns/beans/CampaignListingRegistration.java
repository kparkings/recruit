package com.arenella.recruit.campaigns.beans;

import java.util.UUID;

/**
* Represents a Listing that exists in the System and defines its owner 
*/
public record CampaignListingRegistration(UUID listingId, String listingOwnerId) {

}
