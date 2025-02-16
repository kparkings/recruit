package com.arenella.recruit.listings.controllers;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import com.arenella.recruit.listings.beans.Listing;
import com.arenella.recruit.listings.beans.Listing.Country;
import com.arenella.recruit.listings.beans.Listing.currency;
import com.arenella.recruit.listings.beans.Listing.language;
import com.arenella.recruit.listings.beans.Listing.listing_type;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
* API representation of the Listing. This is used by the consumer of 
* the REST API to create and amend a Listing
* @author K Parkings
*/
@JsonDeserialize(builder=ListingAPIInbound.ListingAPIInboundBuilder.class)
public class ListingAPIInbound {

	private UUID 				listingId;
	private String				ownerName;
	private String 				ownerCompany;
	private String				ownerEmail;
	private String 				title;
	private String 				description;
	private listing_type 		type;
	private Country 			country;
	private String 				location;
	private int 				yearsExperience;
	private Set<language> 		languages			= new LinkedHashSet<>();
	private Set<String>			skills				= new LinkedHashSet<>();
	private String 				rate;
	private currency			currency;
	private boolean 			postToSocialMedia	= false;
	
	/**
	* Constructor based upon a builder
	* @param builder - Contains details to populate instance of Listing
	*/
	public ListingAPIInbound(ListingAPIInboundBuilder builder) {
		
		this.listingId 			= builder.listingId;
		this.ownerName			= builder.ownerName;
		this.ownerCompany		= builder.ownerCompany;
		this.ownerEmail			= builder.ownerEmail;
		this.title 				= builder.title;
		this.description 		= builder.description;
		this.type 				= builder.type;
		this.country 			= builder.country;
		this.location 			= builder.location;
		this.yearsExperience 	= builder.yearsExperience;
		this.rate 				= builder.rate;
		this.currency 			= builder.currency;
		this.postToSocialMedia	= builder.postToSocialMedia;
		
		this.languages.addAll(builder.languages);
		this.skills.addAll(builder.skills);
		
	}
	
	/**
	* Returns the unique Identifier of the Listing
	* @return Unique Id of the Listing
	*/
	public UUID getListingId() {
		return this.listingId;
	}
	
	/**
	* Returns the name of the owner of the Listing 
	* @return Name of Owner of the Listing
	*/
	public String getOwnerName() {
		return this.ownerName;
	}
	
	/**
	* Returns the Name of the company the 
	* Owner of the Listing is working for
	* @return Owners company
	*/
	public String getOwnerCompany() {
		return this.ownerCompany;
	}
	
	/**
	* Returns the Email address to contact he Owner 
	* about the Listing on
	* @return Email address to contact the Owner of the Listing
	*/
	public String getOwnerEmail() {
		return this.ownerEmail;
	}
	
	/**
	* Returns the Title for the Listing
	* @return Descriptive Title for the Listing
	*/
	public String getTitle() {
		return this.title;
	}
	
	/**
	* Returns the full description of the Listing
	* @return Body of Listing
	*/
	public String getDescription() {
		return this.description;
	}
	
	/**
	* Returns the Type of Listing. For example if it is a listing
	* for a perm or contract role
	* @return Type of Listing
	*/
	public listing_type getType() {
		return this.type;
	}
	
	/**
	* Returns the Country the Listing is for
	* @return Country location of the Listing
	*/
	public Country getCountry() {
		return this.country;
	}
	
	/**
	* Returns the Location within the country for the Listing
	* @return Probably the City name 
	*/
	public String getLocation() {
		return this.location;
	}
	
	/**
	* Returns the number of years experience the Candidate must have
	* @return Years of experience
	*/
	public int getYearsExperience() {
		return this.yearsExperience;
	}
	
	/**
	* Returns the Languages requested
	* @return Requested Languages for the Listing
	*/
	public Set<language> getLanguages() {
		return this.languages;
	}
	
	/**
	* Returns the requested skills for the 
	* Listing
	* @return requested skills
	*/
	public Set<String> getSkills() {
		return this.skills;
	}
	
	/**
	* Returns the rate being offered
	* @return rate offered for the Listing
	*/
	public String getRate() {
		return this.rate;
	}
	
	/**
	* Currency being used for the rate in the Listing
	* @return Currency for the Rate
	*/
	public currency	getCurrency() {
		return this.currency;
	}
	
	/**
	* Whether or not the Owner of the Listing has requested that the 
	* Listing be shared on Social media
	* @return whether to share the Listing on social media
	*/
	public boolean isPostToSocialMedia() {
		return this.postToSocialMedia;
	}
	
	/**
	* Returns an instance of a Builder for the Listing class
	* @return Builder
	*/
	public static ListingAPIInboundBuilder builder() {
		return new ListingAPIInboundBuilder();
	}
	
	/**
	* Builder for the Listing class
	* @author K Parkings
	*/
	@JsonPOJOBuilder(buildMethodName="build", withPrefix="")
	public static class ListingAPIInboundBuilder{

		private UUID 				listingId;
		private String				ownerName;
		private String 				ownerCompany;
		private String				ownerEmail;
		private String 				title;
		private String 				description;
		private listing_type 		type;
		private Country 			country;
		private String 				location;
		private int 				yearsExperience;
		private Set<language> 		languages			= new LinkedHashSet<>();
		private Set<String>			skills				= new LinkedHashSet<>();
		private String 				rate;
		private currency			currency;
		private boolean 			postToSocialMedia	= false;
		
		/**
		* Sets the Unique Identifier for the Listing
		* @param listingId - Unique Id of the Listing
		* @return Builder
		*/
		public ListingAPIInboundBuilder listingId(UUID listingId) {
			this.listingId = listingId;
			return this;
		}
		
		/**
		* Sets the name of the Owner of the Listing
		* @param ownerName - Listing owners name
		* @return Builder
		*/
		public ListingAPIInboundBuilder ownerName(String ownerName) {
			this.ownerName = ownerName;
			return this;
		}
		
		/**
		* Sets the name of the Company the Listing was created 
		* on behalf of 
		* @param ownerCompany - Company Owner of Listing works for
		* @return Builder
		*/
		public ListingAPIInboundBuilder ownerCompany(String ownerCompany) {
			this.ownerCompany = ownerCompany;
			return this;
		}
		
		/**
		* Sets the email address to use to contact the Owner of the 
		* Listing
		* @param ownerEmail - Email address to contact the Owner
		* @return Builder
		*/
		public ListingAPIInboundBuilder ownerEmail(String ownerEmail) {
			this.ownerEmail = ownerEmail;
			return this;
		}
		
		/**
		* Sets a title for the Listing
		* @param title - Title for the Listing
		* @return Builder
		*/
		public ListingAPIInboundBuilder title(String title) {
			this.title = title;
			return this;
		}
		
		/**
		* Sets the full description / body for the Listing
		* @param description - Information about the listing
		* @return Builder
		*/
		public ListingAPIInboundBuilder description(String description) {
			this.description = description;
			return this;
		}
		
		/**
		* Sets the type of the Listing
		* @param type - contract/perm etc
		* @return Builder
		*/
		public ListingAPIInboundBuilder type(listing_type type) {
			this.type = type;
			return this;
		}
		
		/**
		* Sets the Country the Listing is associated with
		* @param country - Where the Listing is for
		* @return Builder
		*/
		public ListingAPIInboundBuilder country(Country country) {
			this.country = country;
			return this;
		}
		
		/**
		* Sets location the listing is for (i.e City / Remote etc)
		* @param location - Location where the Listing is based
		* @return Builder
		*/
		public ListingAPIInboundBuilder location(String location) {
			this.location = location;
			return this;
		}
		
		/**
		* Sets the years experiences required for the Listing
		* @param yearsExperience - Years experience expected from the candidate
		* @return Builder
		*/
		public ListingAPIInboundBuilder yearsExperience(int yearsExperience) {
			this.yearsExperience = yearsExperience;
			return this;
		}
		
		/**
		* Sets the language required for the Listing
		* @param languages - Desired languages for the Listing
		* @return Builder
		*/
		public ListingAPIInboundBuilder languages(Set<language> languages) {
			this.languages.clear();
			this.languages.addAll(languages);
			return this;
		}
		
		/**
		* Sets the Skills requested for the Listing
		* @param skills - Skills requested
		* @return Builder
		*/
		public ListingAPIInboundBuilder skills(Set<String> skills) {
			this.skills.clear();
			this.skills.addAll(skills);
			return this;
		}
		
		/**
		* Sets the Rate being offered for the Listing
		* @param rate - Rate offered for the Listing
		* @return Builder
		*/
		public ListingAPIInboundBuilder rate(String rate) {
			this.rate = rate;
			return this;
		}
		
		/**
		* Sets the Currency associated with the Rate
		* @param currency - Currency associated with the Rate
		* @return Builder
		*/
		public ListingAPIInboundBuilder currency(currency currency) {
			this.currency = currency;
			return this;
		}
		
		/**
		* Sets whether or not to Post the Listing on Social Media
		* @param postToSocialMedia - Whether or not to post Listing on Social Media
		* @return Builder
		*/
		public ListingAPIInboundBuilder postToSocialMedia(boolean postToSocialMedia) {
			this.postToSocialMedia = postToSocialMedia;
			return this;
		}
		
		/**
		* Returns an instantiated instance of the Listing class
		* @return instance of Listing
		*/
		public ListingAPIInbound build() {
			return new ListingAPIInbound(this);
		}
		
	}
	
	/**
	* Converts API representation of a Listing to Domain version
	* @param inbound API version of Listing
	* @return domain version of the Listing
	*/
	public static Listing convertToListing(ListingAPIInbound inbound) {
		
		return Listing
				.builder()
					.country(inbound.getCountry())
					.currency(inbound.getCurrency())
					.description(inbound.getDescription())
					.languages(inbound.getLanguages())
					.listingId(inbound.getListingId())
					.skills(inbound.getSkills())
					.location(inbound.getLocation())
					.ownerId(null)
					.rate(inbound.getRate())
					.title(inbound.getTitle())
					.type(inbound.getType())
					.yearsExperience(inbound.getYearsExperience())
					.ownerName(inbound.getOwnerName())
					.ownerCompany(inbound.getOwnerCompany())
					.ownerEmail(inbound.getOwnerEmail())
				.build();
		
	}
	
}