package com.arenella.recruit.listings.controllers;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.arenella.recruit.listings.beans.Listing;
import com.arenella.recruit.listings.beans.Listing.Country;
import com.arenella.recruit.listings.beans.Listing.currency;
import com.arenella.recruit.listings.beans.Listing.language;
import com.arenella.recruit.listings.beans.Listing.listing_type;

/**
* Outbount API representation of the Listing for non authenticated users. This is the version that 
* is intended to be displayed to Candidates searching for a new role and has less info available than 
* the standard version for the Listing owners
* @author K Parkings
*/
public class ListingAPIOutboundPublic {

	private UUID 				listingId;
	private String				ownerId;
	private String				ownerName;
	private String 				ownerCompany;
	private String				ownerEmail;
	private LocalDateTime		created;
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
	private int 				views;
	
	/**
	* Constructor based upon a builder
	* @param builder - Contains details to populate instance of Listing
	*/
	public ListingAPIOutboundPublic(ListingAPIOutboundBuilder builder) {
		
		this.listingId 			= builder.listingId;
		this.ownerId			= builder.ownerId;
		this.ownerName			= builder.ownerName;
		this.ownerCompany		= builder.ownerCompany;
		this.ownerEmail			= builder.ownerEmail;
		this.created 			= builder.created;
		this.title 				= builder.title;
		this.description 		= builder.description;
		this.type 				= builder.type;
		this.country 			= builder.country;
		this.location 			= builder.location;
		this.yearsExperience 	= builder.yearsExperience;
		this.rate 				= builder.rate;
		this.currency 			= builder.currency;
		this.views				= builder.views;
		
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
	* Returns the Date the Listing was posted
	* @return Date Listing was posted
	*/
	public LocalDateTime getCreated() {
		return this.created;
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
	* Returns the Id of the Recruiter who owns
	* the Listing
	* @return id of Owner
	*/
	public String getOwnerId() {
		return this.ownerId;
	}
	
	/**
	* Returns the number of times the Listing has
	* been viewed
	* @return views by candidates 
	*/
	public int getViews() {
		return this.views;
	}
	
	/**
	* Returns an instance of a Builder for the ListingAPIOutbound class
	* @return Builder
	*/
	public static ListingAPIOutboundBuilder builder() {
		return new ListingAPIOutboundBuilder();
	}
	
	/**
	* Builder for the ListingAPIOutbound class
	* @author K Parkings
	*/
	public static class ListingAPIOutboundBuilder{

		private UUID 				listingId;
		private String				ownerId;
		private String				ownerName;
		private String 				ownerCompany;
		private String				ownerEmail;
		private LocalDateTime		created;
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
		private int					views;
		
		/**
		* Sets the Unique Identifier for the Listing
		* @param listingId - Unique Id of the Listing
		* @return Builder
		*/
		public ListingAPIOutboundBuilder listingId(UUID listingId) {
			this.listingId = listingId;
			return this;
		}
		
		/**
		* Sets the name of the Owner of the Listing
		* @param ownerName - Listing owners name
		* @return Builder
		*/
		public ListingAPIOutboundBuilder ownerName(String ownerName) {
			this.ownerName = ownerName;
			return this;
		}
		
		/**
		* Sets the name of the Company the Listing was created 
		* on behalf of 
		* @param ownerCompany - Company Owner of Listing works for
		* @return Builder
		*/
		public ListingAPIOutboundBuilder ownerCompany(String ownerCompany) {
			this.ownerCompany = ownerCompany;
			return this;
		}
		
		/**
		* Sets the email address to use to contact the Owner of the 
		* Listing
		* @param ownerEmail - Email address to contact the Owner
		* @return Builder
		*/
		public ListingAPIOutboundBuilder ownerEmail(String ownerEmail) {
			this.ownerEmail = ownerEmail;
			return this;
		}
		
		
		/**
		* Sets the Date the Listing was created
		* @param created - Date of creation 
		* @return Builder
		*/
		public ListingAPIOutboundBuilder created(LocalDateTime created) {
			this.created = created;
			return this;
		}
		
		/**
		* Sets a title for the Listing
		* @param title - Title for the Listing
		* @return Builder
		*/
		public ListingAPIOutboundBuilder title(String title) {
			this.title = title;
			return this;
		}
		
		/**
		* Sets the full description / body for the Listing
		* @param description - Information about the listing
		* @return Builder
		*/
		public ListingAPIOutboundBuilder description(String description) {
			this.description = description;
			return this;
		}
		
		/**
		* Sets the type of the Listing
		* @param type - contract/perm etc
		* @return Builder
		*/
		public ListingAPIOutboundBuilder type(listing_type type) {
			this.type = type;
			return this;
		}
		
		/**
		* Sets the Country the Listing is associated with
		* @param country - Where the Listing is for
		* @return Builder
		*/
		public ListingAPIOutboundBuilder country(Country country) {
			this.country = country;
			return this;
		}
		
		/**
		* Sets location the listing is for (i.e City / Remote etc)
		* @param location - Location where the Listing is based
		* @return Builder
		*/
		public ListingAPIOutboundBuilder location(String location) {
			this.location = location;
			return this;
		}
		
		/**
		* Sets the years experiences required for the Listing
		* @param yearsExperience - Years experience expected from the candidate
		* @return Builder
		*/
		public ListingAPIOutboundBuilder yearsExperience(int yearsExperience) {
			this.yearsExperience = yearsExperience;
			return this;
		}
		
		/**
		* Sets the language required for the Listing
		* @param languages - Desired languages for the Listing
		* @return Builder
		*/
		public ListingAPIOutboundBuilder languages(Set<language> languages) {
			this.languages.clear();
			this.languages.addAll(languages);
			return this;
		}
		
		/**
		* Sets the Skills requested for the Listing
		* @param skills - Skills requested
		* @return Builder
		*/
		public ListingAPIOutboundBuilder skills(Set<String> skills) {
			this.skills.clear();
			this.skills.addAll(skills);
			return this;
		}
		
		/**
		* Sets the Rate being offered for the Listing
		* @param rate - Rate offered for the Listing
		* @return Builder
		*/
		public ListingAPIOutboundBuilder rate(String rate) {
			this.rate = rate;
			return this;
		}
		
		/**
		* Sets the Currency associated with the Rate
		* @param currency - Currency associated with the Rate
		* @return Builder
		*/
		public ListingAPIOutboundBuilder currency(currency currency) {
			this.currency = currency;
			return this;
		}
		
		/**
		* Sets the Id of the recruiter who owns the Listing
		* @param ownerId - Id of recruiter
		* @return Builder
		*/
		public ListingAPIOutboundBuilder ownerId(String ownerId) {
			this.ownerId = ownerId;
			return this;
		}
		
		/**
		* Sets the number of times the Listing has been viewed
		* @param views - number of views
		* @return Builder
		*/
		public ListingAPIOutboundBuilder views(int views) {
			this.views = views;
			return this;
		}
		
		/**
		* Returns an instantiated instance of the ListingAPIOutbound class
		* @return instance of Listing
		*/
		public ListingAPIOutboundPublic build() {
			return new ListingAPIOutboundPublic(this);
		}
		
	}
	
	/**
	* Converts the Domain representation of a Listing to the API 
	* outbound version
	* @param listing - Listing to convert
	* @return converted Listing
	*/
	public static ListingAPIOutboundPublic convertFromListing(Listing listing) {
		
		return ListingAPIOutboundPublic
								.builder()
									.country(listing.getCountry())
									.yearsExperience(listing.getYearsExperience())
									.type(listing.getType())
									.title(listing.getTitle())
									.rate(listing.getRate())
									.location(listing.getLocation())
									.listingId(listing.getListingId())
									.languages(listing.getLanguages())
									.skills(listing.getSkills().stream().sorted().collect(Collectors.toCollection(LinkedHashSet::new)))
									.description(listing.getDescription())
									.currency(listing.getCurrency())
									.created(listing.getCreated())
									.country(listing.getCountry())
									.ownerName(listing.getOwnerName())
									.ownerCompany(listing.getOwnerCompany())
									.ownerEmail(listing.getOwnerEmail())
									.ownerId(listing.getOwnerId())
									.views(listing.getViews().size())
								.build();
		
	}
	
}