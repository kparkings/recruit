package com.arenella.recruit.listings.repos.entities;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.arenella.recruit.listings.beans.Listing;
import com.arenella.recruit.listings.beans.Listing.Country;
import com.arenella.recruit.listings.beans.Listing.currency;
import com.arenella.recruit.listings.beans.Listing.language;
import com.arenella.recruit.listings.beans.Listing.listing_type;
import com.arenella.recruit.listings.dao.ListingViewedEventEntity;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;

/**
* Elasticsearch document representing a Listing 
*/
@Document(indexName="listings", writeTypeHint = org.springframework.data.elasticsearch.annotations.WriteTypeHint.FALSE)
public class ListingDocument {

	@Id
	@Field(type = FieldType.Keyword)
	private UUID listingId;
	
	@Field(type = FieldType.Keyword)
	private String				ownerId;
	
	@Field(type = FieldType.Keyword)
	private String				ownerName;

	@Field(type = FieldType.Keyword)
	private String 				ownerCompany;
	
	@Field(type = FieldType.Keyword)
	private String				ownerEmail;
	
	@Field(type = FieldType.Date)
	private LocalDateTime		created;
	
	@Field(type = FieldType.Keyword)
	private String 				title;
	
	@Field(type = FieldType.Keyword)
	private String 				description;
	
	@Field(type = FieldType.Keyword)
	@Enumerated(EnumType.STRING)
	private listing_type 		type;
	
	@Field(type = FieldType.Keyword)
	@Enumerated(EnumType.STRING)
	private Country 			country;
	
	@Field(type = FieldType.Keyword)
	private String 				location;
	
	@Field(type = FieldType.Integer)
	private int 				yearsExperience;
	
	@Field(type = FieldType.Boolean)
	private boolean				active	= true; 
	
	@Field(type = FieldType.Keyword)
	private String 				rate;
	
	@Field(type = FieldType.Keyword)
	@Enumerated(EnumType.STRING)
	private currency			currency;
	
	@Field(type = FieldType.Keyword)
	private Set<String> 						skills			= new LinkedHashSet<>();
	
	@Field(type = FieldType.Keyword)
	@Enumerated(EnumType.STRING)
	private Set<language> 						languages		= new LinkedHashSet<>();
	
	@Field(type = FieldType.Keyword)
	private Set<ListingViewedEventEntity> 		views			= new LinkedHashSet<>();

	/**
	* Constructor based upon a Builder
	* @param builder - Contains initialization values
	*/
	public ListingDocument(ListingDocumentBuilder builder) {
		
		this.listingId 				= builder.listingId;
		this.ownerId 				= builder.ownerId;
		this.ownerName 				= builder.ownerName;
		this.ownerCompany 			= builder.ownerCompany;
		this.ownerEmail 			= builder.ownerEmail;
		this.created				= builder.created;
		this.title 					= builder.title;
		this.description 			= builder.description;
		this.type 					= builder.type;
		this.country 				= builder.country;
		this.location 				= builder.location;
		this.yearsExperience 		= builder.yearsExperience;
		this.active					= builder.active; 
		this.rate 					= builder.rate;
		this.currency 				= builder.currency;
		
		this.skills.clear();
		this.languages.clear();
		this.views.clear();
	
		this.skills.addAll(builder.skills);
		this.languages.addAll(builder.languages);
		this.views.addAll(builder.views);
		
	}
	
	/**
	* Returns the id of the Listing
	* @return unique Id of the Listing
	*/
	public UUID getListingId() {
		return this.listingId;
	}
	
	/**
	* Returns the id of the Recruiter who owns the listing
	* @return unique Id of the Recruiter
	*/
	public String getOwnerId() {
		return this.ownerId;
	}
	
	/**
	* Returns the name of the Recruiter who owns the Listing
	* @return name of the owning Recruiter
	*/
	public String getOwnerName() {
		return this.ownerName;
	}
	
	/**
	* Returns the name of the owning Recruiters company
	* @return name of the Company
	*/
	public String getOwnerCompany() {
		return this.ownerCompany;
	}
	
	/**
	* Returns the email to use to contact the Recruiter 
	* about the Listing
	* @return contact email
	*/
	public String getOwnerEmail() {
		return this.ownerEmail;
	}
	
	/**
	* Returns when the Listing was created
	* @return when the Listing was created
	*/
	public LocalDateTime getCreated() {
		return this.created;
	}
	
	/**
	* Returns the title of the Listing
	* @return title of the Role
	*/
	public String getTitle() {
		return this.title;
	}
	
	/**
	* Returns a description of the Listing
	* @return description of role
	*/
	public String getDescription() {
		return this.description;
	}
	
	/**
	* Returns the contract type 
	* @return type of contract
	*/
	public listing_type getType() {
		return this.type;
	}
	
	/**
	* Returns the county where the role is based
	* @return country
	*/
	public Country getCountry() {
		return this.country;
	}
	
	/**
	* Returns the location where the role is based
	* @return City/Town 
	*/
	public String getLocation() {
		return this.location;
	}
	
	/**
	* Returns the number of years experience required 
	* of the Candidate for the role
	* @return years experience being asked
	*/
	public int getYearsExperience() {
		return this.yearsExperience;
	}
	
	/**
	* Returns whether the listing is active
	* @return whether this is an active Listing
	*/
	public boolean isActive() {
		return this.active;
	} 
	
	/**
	* Returns the Rate being offered for the Role
	* @return Rate being offered
	*/
	public String getRate() {
		return this.rate;
	}
	
	/**
	* Returns the Currency the Candidate will be 
	* Paid in
	* @return Payment Currency
	*/
	public currency	getCurrency() {
		return this.currency;
	}
	
	/**
	* Returns the Skills required from the Candidate
	* @return Required skills for the Role
	*/
	public Set<String> getSkills() {
		return this.skills;
	}
	
	/**
	* Returns the Languages the Candidate needs tot speak
	* @return Required Languages
	*/
	public Set<language> getLanguages() {
		return this.languages;
	}
	
	/**
	* Returns the number of times the Listing has been viewed
	* @return number of views
	*/
	public Set<ListingViewedEventEntity> getViews() {
		return this.views;
	}
	
	/**
	* Returns a builder for the class
	* @return Builder for the class
	*/
	public static ListingDocumentBuilder builder() {
		return new ListingDocumentBuilder();
	}
	
	/**
	* Builder for the class 
	*/
	public static class ListingDocumentBuilder {
	
		private UUID 							listingId;
		private String							ownerId;
		private String							ownerName;
		private String 							ownerCompany;
		private String							ownerEmail;
		private LocalDateTime					created;
		private String 							title;
		private String 							description;
		private listing_type 					type;
		private Country 						country;
		private String 							location;
		private int 							yearsExperience;
		private boolean							active			= true; 
		private String 							rate;
		private currency						currency;
		private Set<String> 					skills			= new LinkedHashSet<>();
		private Set<language> 					languages		= new LinkedHashSet<>();
		private Set<ListingViewedEventEntity> 	views			= new LinkedHashSet<>();

		/**
		* Sets the unique id of te Listing
		* @param listingId Unique Identifier
		* @return Builder
		*/
		public ListingDocumentBuilder listingId(UUID listingId) {
			this.listingId = listingId;
			return this;
		}
		
		/**
		* Sets the unique Id of the Recruiter who owns the 
		* Listing
		* @param ownerId - Unique id of the owning recruiter
		* @return Builder
		*/
		public ListingDocumentBuilder ownerId(String ownerId) {
			this.ownerId = ownerId;
			return this;
		}
		
		/**
		* Sets the name of the Recruiter who owns the Post
		* @param ownerName - name of owning recruiter
		* @return Builder
		*/
		public ListingDocumentBuilder ownerName(String ownerName) {
			this.ownerName = ownerName;
			return this;
		}
		
		/**
		* Sets the name of the company of the recruiter who 
		* owns the Listing
		* @param ownerCompany - company name
		* @return Builder
		*/
		public ListingDocumentBuilder ownerCompany(String ownerCompany) {
			this.ownerCompany = ownerCompany;
			return this;
		}
		
		/**
		* Sets the Email to use to contact the Recruiter who 
		* owns the Listing
		* @param ownerEmail - Recruiters email address
		* @return Builder
		*/
		public ListingDocumentBuilder ownerEmail(String	ownerEmail) {
			this.ownerEmail = ownerEmail;
			return this;
		}
		
		/**
		* Sets when the Listing was created
		* @param created - When the listing was created
		* @return Builder
		*/
		public ListingDocumentBuilder created(LocalDateTime created) {
			this.created = created;
			return this;
		}
		
		/**
		* Sets the title of the Listing
		* @param title - Title of the Listing
		* @return Builder
		*/
		public ListingDocumentBuilder title(String title) {
			this.title = title;
			return this;
		}
		
		/**
		* Sets the description of the Listing
		* @param description - Information about the role
		* @return Builder
		*/
		public ListingDocumentBuilder description(String description) {
			this.description = description;
			return this;
		}
		
		/**
		* Sets the type of contract of the listing
		* @param type - type of listing
		* @return Builder
		*/
		public ListingDocumentBuilder type(listing_type type) {
			this.type = type;
			return this;
		}
		
		/**
		* Sets the country the role is located in
		* @param country - Country listing is recruiting for
		* @return Builder
		*/
		public ListingDocumentBuilder country(Country country) {
			this.country = country;
			return this;
		}
		
		/**
		* Sets the location of where the work is to take place
		* @param location - Location of the listing
		* @return Builder
		*/
		public ListingDocumentBuilder location(String location) {
			this.location = location;
			return this;
		}

		/**
		* Sets how many years experience the Candidate needs to have
		* @param yearsExperience - Required years experience
		* @return Builder
		*/
		public ListingDocumentBuilder yearsExperience(int yearsExperience) {
			this.yearsExperience = yearsExperience;
			return this;
		}
		
		/**
		* Sets whether the Listing is active
		* @param active - Whether it is an active Listing
		* @return Builder
		*/
		public ListingDocumentBuilder active(boolean active) {
			this.active = active;
			return this;
		}
		
		/**
		* Sets the rate offered for the Listing
		* @param rate - Offered Rate/Salary
		* @return Builder
		*/
		public ListingDocumentBuilder rate(String rate) {
			this.rate = rate;
			return this;
		}
		
		/**
		* Sets the Currency the Candidate will be paid in
		* @param currency Currency used for payment
		* @return Builder
		*/
		public ListingDocumentBuilder currency(currency currency) {
			this.currency = currency;
			return this;
		}
		
		/**
		* Sets the skills required from the Candidate
		* @param skills - Required skills
		* @return Builder
		*/
		public ListingDocumentBuilder skills(Set<String> skills) {
			this.skills = skills;
			return this;
		}
		
		/**
		* Sets the languages the Candidate needs to speak
		* @param languages - Required Languages
		* @return Builder
		*/
		public ListingDocumentBuilder languages(Set<language> languages) {
			this.languages.clear();
			this.languages.addAll(languages);
			return this;
		}
		
		/**
		* Sets the number of times the Listing has been viewed
		* @param views - Number of times the Listing has been viewed
		* @return Builder
		*/
		public ListingDocumentBuilder views(Set<ListingViewedEventEntity> views) {
			this.views.clear();
			this.views.addAll(views);
			return this;
		}

		/**
		* Returns an initialized instance of the class
		* @return initialized instance
		*/
		public ListingDocument build() {
			return new ListingDocument(this);
		}
		
	}
	
	/**
	* 
	* @param listing
	* @return
	*/
	public static ListingDocument toEntity(Listing listing) {
		return ListingDocument
				.builder()
					.active(listing.isActive())
					.country(listing.getCountry())
					.created(listing.getCreated())
					.currency(listing.getCurrency())
					.description(listing.getDescription())
					.languages(listing.getLanguages())
					.listingId(listing.getListingId())
					.location(listing.getLocation())
					.ownerCompany(listing.getOwnerCompany())
					.ownerEmail(listing.getOwnerEmail())
					.ownerId(listing.getOwnerId())
					.ownerName(listing.getOwnerName())
					.rate(listing.getRate())
					.skills(listing.getSkills())
					.title(listing.getTitle())
					.type(listing.getType())
					.views(listing.getViews().stream().map(ListingViewedEventEntity::convertToEntity).collect(Collectors.toSet()))
					.yearsExperience(listing.getYearsExperience())
				.build();
	}
	
	/**
	* 
	* @param document
	* @return
	*/
	public static Listing fromEntity(ListingDocument listing) {
		return Listing
				.builder()
					.active(listing.isActive())
					.country(listing.getCountry())
					.created(listing.getCreated())
					.currency(listing.getCurrency())
					.description(listing.getDescription())
					.languages(listing.getLanguages())
					.listingId(listing.getListingId())
					.location(listing.getLocation())
					.ownerCompany(listing.getOwnerCompany())
					.ownerEmail(listing.getOwnerEmail())
					.ownerId(listing.getOwnerId())
					.ownerName(listing.getOwnerName())
					.rate(listing.getRate())
					.skills(listing.getSkills())
					.title(listing.getTitle())
					.type(listing.getType())
					.views(listing.getViews().stream().map(ListingViewedEventEntity::convertFromEntity).collect(Collectors.toSet()))
					.yearsExperience(listing.getYearsExperience())
				.build();
	}
	
}