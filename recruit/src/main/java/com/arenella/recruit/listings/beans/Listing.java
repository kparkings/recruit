package com.arenella.recruit.listings.beans;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

/**
* Class represents a Listing made by a recruiter for some type of 
* advertised role
* @author K Parkings
*/
public class Listing {

	public static enum listing_type 	{CONTRACT_ROLE, PERM_ROLE, BOTH}
	public static enum LISTING_AGE		{TODAY, THIS_WEEK, THIS_MONTH, ALL}
	public static enum Country 			{
		NETHERLANDS, 
		BELGIUM, 
		LUXEMBOURG,
		UK,
		REPUBLIC_OF_IRELAND,
		NORTHERN_IRELAND,
		GERMANY,
		POLAND,
		FRANCE,
		SPAIN,
		ITALY,
		PORTUGAL,
		AUSTRIA,
		ROMANIA,
		GREECE,
		UKRAINE,
		SWITZERLAND,
		CZECH_REPUBLIC,
		SWEDEN,
		HUNGARY,
		BULGARIA,
		DENMARK,
		FINLAND,
		SLOVAKIA,
		CROATIA,
		LITHUANIA,
		SLOVENIA,
		LATVIA,
		ESTONIA,
		CYPRUS,
		MALTA,
		NORWAY,
		LIECHTENSTEIN,
		TURKEY,
		INDIA,
		PAKISTAN,
		US,
		CANADA, 
		EU_REMOTE, 
		WORLD_REMOTE}
	
	public enum language 		{
		BULGARIAN, 
		CROATIAN,
		CZECH, 
		DANISH, 
		DUTCH, 
		ENGLISH, 
		ESTONIAN,
		FRENCH, 
		FINNISH, 
		GERMAN, 
		GREEK, 
		HUNGARIAN,
		HINDI,
		ITALIAN, 
		LATVIAN,
		LITHUANIAN,
		MALTESE,
		NORWEGEN,
		POLISH, 
		PORTUGUESE, 
		ROMANIAN, 
		RUSSIAN,
		SWEDISH,
		SPANISH, 
		SLOVAKIAN,
		SLOVENIAN,
		TURKISH,
		UKRAINIAN,
		URDU}
	
	public enum currency		{EUR, GBP, BGN, CAD, CHF, CZK, DKK, HUF, INR, PLN, PKR, RON, SEK, USD}
	public enum TECH			{JAVA, DOT_NET, DEV_OPS, NETWORKS, CLOUD, WEB, UI_UX, PROJECT_NANAGMENT, TESTING, BUSINESS_ANALYSTS, SECURITY, IT_SUPPORT, ARCHITECT, BI, REC2REC }
	
	private UUID 						listingId;
	private String						ownerId;
	private String						ownerName;
	private String 						ownerCompany;
	private String						ownerEmail;
	private LocalDateTime				created;
	private String 						title;
	private String 						description;
	private listing_type 				type;
	private Country 					country;
	private String 						location;
	private int 						yearsExperience;
	private Set<language> 				languages			= new LinkedHashSet<>();
	private Set<String>					skills				= new LinkedHashSet<>();
	private String 						rate;
	private currency					currency;
	private Set<ListingViewedEvent> 	views				= new LinkedHashSet<>();
	private boolean						active				= true;
	
	/**
	* Constructor based upon a builder
	* @param builder - Contains details to populate instance of Listing
	*/
	public Listing(ListingBuilder builder) {
		
		this.listingId 			= builder.listingId;
		this.ownerId 			= builder.ownerId;
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
		this.active				= builder.active;
		
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
	* Returns the Unique Id of the Owner
	* @return Unique Id of the owner
	*/
	public String getOwnerId() {
		return this.ownerId;
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
	* Returns the views of the Listing
	* @return Views of the Listing
	*/
	public Set<ListingViewedEvent> getViews() {
		return this.views;
	}
	
	/**
	* Returns whether the listing is active. A listing will be active
	* if the owning recruiter has an active account and disabled if the 
	* listing is not active
	* @return
	*/
	public boolean isActive() {
		return this.active;
	}
	
	/**
	* Generates a unique Id for the Listing and sets
	* the created data
	*/
	public void initializeAsNewListing() {
		this.listingId 	= UUID.randomUUID();
		this.created 	= LocalDateTime.now();
	}
	
	/**
	* Sets the unique id of the owner of the Listing
	* @param ownerId - Unique Id of the Owner
	*/
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	
	/**
	* Sets whether the Listing is active or not
	* @param ownerId - Whether the Listing is active or not
	*/
	public void setActive(boolean active) {
		this.active = active;
	}
	
	/**
	* Sets the unique Id of the Listing
	* @param id - unique Id
	*/
	public void setListingId(UUID listingId) {
		this.listingId = listingId;
	}
	
	/**
	* Returns an instance of a Builder for the Listing class
	* @return Builder
	*/
	public static ListingBuilder builder() {
		return new ListingBuilder();
	}
	
	/**
	* Builder for the Listing class
	* @author K Parkings
	*/
	public static class ListingBuilder{

		private UUID 						listingId;
		private String						ownerId;
		private String						ownerName;
		private String 						ownerCompany;
		private String						ownerEmail;
		private LocalDateTime				created;
		private String 						title;
		private String 						description;
		private listing_type 				type;
		private Country 					country;
		private String 						location;
		private int 						yearsExperience;
		private Set<language> 				languages			= new LinkedHashSet<>();
		private Set<String>					skills				= new LinkedHashSet<>();
		private String 						rate;
		private currency					currency;
		private Set<ListingViewedEvent>		views				= new LinkedHashSet<>();
		private boolean						active				= true;
		
		/**
		* Sets the Unique Identifier for the Listing
		* @param listingId - Unique Id of the Listing
		* @return Builder
		*/
		public ListingBuilder listingId(UUID listingId) {
			this.listingId = listingId;
			return this;
		}
		
		/**
		* Sets the Unique Id of the Owner of the Listing
		* @param ownerId - Unique Id of the Owner
		* @return Builder
		*/
		public ListingBuilder ownerId(String ownerId) {
			this.ownerId = ownerId;
			return this;
		}
		
		/**
		* Sets the name of the Owner of the Listing
		* @param ownerName - Listing owners name
		* @return Builder
		*/
		public ListingBuilder ownerName(String ownerName) {
			this.ownerName = ownerName;
			return this;
		}
		
		/**
		* Sets the name of the Company the Listing was created 
		* on behalf of 
		* @param ownerCompany - Company Owner of Listing works for
		* @return Builder
		*/
		public ListingBuilder ownerCompany(String ownerCompany) {
			this.ownerCompany = ownerCompany;
			return this;
		}
		
		/**
		* Sets the email address to use to contact the Owner of the 
		* Listing
		* @param ownerEmail - Email address to contact the Owner
		* @return Builder
		*/
		public ListingBuilder ownerEmail(String ownerEmail) {
			this.ownerEmail = ownerEmail;
			return this;
		}
		
		/**
		* Sets the Date the Listing was created
		* @param created - Date of creation 
		* @return Builder
		*/
		public ListingBuilder created(LocalDateTime created) {
			this.created = created;
			return this;
		}
		
		/**
		* Sets a title for the Listing
		* @param title - Title for the Listing
		* @return Builder
		*/
		public ListingBuilder title(String title) {
			this.title = title;
			return this;
		}
		
		/**
		* Sets the full description / body for the Listing
		* @param description - Information about the listing
		* @return Builder
		*/
		public ListingBuilder description(String description) {
			this.description = description;
			return this;
		}
		
		/**
		* Sets the type of the Listing
		* @param type - contract/perm etc
		* @return Builder
		*/
		public ListingBuilder type(listing_type type) {
			this.type = type;
			return this;
		}
		
		/**
		* Sets the Country the Listing is associated with
		* @param country - Where the Listing is for
		* @return Builder
		*/
		public ListingBuilder country(Country country) {
			this.country = country;
			return this;
		}
		
		/**
		* Sets location the listing is for (i.e City / Remote etc)
		* @param location - Location where the Listing is based
		* @return Builder
		*/
		public ListingBuilder location(String location) {
			this.location = location;
			return this;
		}
		
		/**
		* Sets the years experiences required for the Listing
		* @param yearsExperience - Years experience expected from the candidate
		* @return Builder
		*/
		public ListingBuilder yearsExperience(int yearsExperience) {
			this.yearsExperience = yearsExperience;
			return this;
		}
		
		/**
		* Sets the language required for the Listing
		* @param languages - Desired languages for the Listing
		* @return Builder
		*/
		public ListingBuilder languages(Set<language> languages) {
			this.languages.clear();
			this.languages.addAll(languages);
			return this;
			
		}
		
		/**
		* Sets the Skills requested for the Listing
		* @param skills - Skills requested
		* @return Builder
		*/
		public ListingBuilder skills(Set<String> skills) {
			this.skills.clear();
			this.skills.addAll(skills);
			return this;
		}
		
		/**
		* Sets the Rate being offered for the Listing
		* @param rate - Rate offered for the Listing
		* @return Builder
		*/
		public ListingBuilder rate(String rate) {
			this.rate = rate;
			return this;
		}
		
		/**
		* Sets the Currency associated with the Rate
		* @param currency - Currency associated with the Rate
		* @return Builder
		*/
		public ListingBuilder currency(currency currency) {
			this.currency = currency;
			return this;
		}
		
		/**
		* Sets the views for the Listing
		* @param views - Views of the listing by users
		* @return Builder
		*/
		public ListingBuilder views(Set<ListingViewedEvent> views) {
			this.views = views;
			return this;
		}
		
		/**
		* Sets whether or not the Listing is active
		* @param active - whether or not the Listing is active
		* @return Builder
		*/
		public ListingBuilder active(boolean active){
			this.active = active;
			return this;
		}
		
		/**
		* Returns an instantiated instance of the Listing class
		* @return instance of Listing
		*/
		public Listing build() {
			return new Listing(this);
		}
		
	}

}