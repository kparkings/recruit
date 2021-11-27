package com.arenella.recruit.listings.dao;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import com.arenella.recruit.listings.beans.Listing;
import com.arenella.recruit.listings.beans.Listing.country;
import com.arenella.recruit.listings.beans.Listing.currency;
import com.arenella.recruit.listings.beans.Listing.language;
import com.arenella.recruit.listings.beans.Listing.listing_type;

/**
* Entity representation of a Listing
* @author K Parkings
*/
@Entity
@Table(schema="listings", name="listing")
public class ListingEntity {

	@Id
	private UUID listingId;
	
	@Column(name="owner_id")
	private String				ownerId;
	
	@Column(name="owner_name")
	private String				ownerName;
	
	@Column(name="owner_company")
	private String 				ownerCompany;
	
	@Column(name="owner_email")
	private String				ownerEmail;
	
	@Column(name="created")
	private LocalDate 			created;
	
	@Column(name="title")
	private String 				title;
	
	@Column(name="description")
	private String 				description;
	
	@Column(name="type")
	@Enumerated(EnumType.STRING)
	private listing_type 		type;
	
	@Column(name="country")
	@Enumerated(EnumType.STRING)
	private country 			country;
	
	@Column(name="location")
	private String 				location;
	
	@Column(name="years_experience")
	private int 				yearsExperience;
	
	@Column(name="languages")
	@ElementCollection(targetClass=language.class)
	@CollectionTable(schema="listings", name="listing_language", joinColumns=@JoinColumn(name="listing_id"))
	private Set<language> 		languages			= new LinkedHashSet<>();

	@Column(name="skills")
	@ElementCollection(targetClass=String.class)
	@CollectionTable(schema="listings", name="listing_skill", joinColumns=@JoinColumn(name="listing_id"))
	private Set<String> 		skills			= new LinkedHashSet<>();
	
	@Column(name="rate")
	private float 				rate;
	
	@Column(name="currency")
	@Enumerated(EnumType.STRING)
	private currency			currency;
	
	@Column(name="views")
	private int 				views;
	
	/**
	* Constructor based upon a Builder
	* @param builder - Contains initialization values
	*/
	public ListingEntity(ListingEntityBuilder builder) {
		
		this.listingId 			= builder.listingId;
		this.ownerId 			= builder.ownerId;
		this.ownerName 			= builder.ownerName;
		this.ownerCompany 		= builder.ownerCompany;
		this.ownerEmail 		= builder.ownerEmail;
		this.created 			= builder.created;
		this.title 				= builder.title;
		this.description 		= builder.description;
		this.type 				= builder.type;
		this.country 			= builder.country;
		this.location 			= builder.location;
		this.yearsExperience 	= builder.yearsExperience;
		this.rate 				= builder.rate;
		this.currency 			= builder.currency;
		this.views 				= builder.views;
		
		this.languages.clear();
		this.skills.clear();
		
		this.languages.addAll(builder.languages);
		this.skills.addAll(builder.skills);
		
	}
	
	/**
	* Returns the unique identifier for the Listing
	* @return Unique identifier for the Listing
	*/
	public UUID getListingId() {
		return this.listingId;
	}
	
	/**
	* Returns the Unique id of the owner of the Listing
	* @return owner Id
	*/
	public String getOwnerId() {
		return this.ownerId;
	}
	
	/**
	* Returns the name of the Owner of the Listing
	* @return Owners name
	*/
	public String getOwnerName() {
		return this.ownerName;
	}
	
	/**
	* Returns the Owners company's name
	* @return Name of the Company
	*/
	public String getOwnerCompany() {
		return this.ownerCompany;
	}
	
	/**
	* Returns the Email address to contract the Owner 
	* of the Listing on
	* @return owners email address
	*/
	public String getOwnerEmail() {
		return this.ownerEmail;
	}
	
	/**
	* Returns the creation date of the Listing
	* @return Date the Listing was created
	*/
	public LocalDate getCreated() {
		return this.created;
	}
	
	/**
	* Returns the title of the Listing 
	* @return title
	*/
	public String getTitle() {
		return this.title;
	}
	
	/**
	* Returns the listing Description
	* @return Body of the Listing
	*/
	public String getDescription() {
		return this.description;
	}
	
	/**
	* Returns the type of the Listing
	* @return Listing type
	*/
	public listing_type getType() {
		return this.type;
	}
	
	/**
	* Returns the country the Listing is for
	* @return Country listing is for
	*/
	public country getCountry() {
		return this.country;
	}
	
	/**
	* Returns the location the Listing is for
	* @return I.e Town where the work will be carried out
	*/
	public String getLocation() {
		return this.location;
	}
	
	/**
	* Returns the number of years expereince required
	* @return experience required
	*/
	public int getYearsExperience() {
		return this.yearsExperience;
	}
	
	/**
	* Returns the langauges requested for the Listing
	* @return requested Langauges
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
	* Returns the rate offered
	* @return offered Rate
	*/
	public float getRate() {
		return this.rate;
	}
	
	/**
	* Returns the Currency the Rate is in
	* @return Currency for the Rate
	*/
	public currency	getCurrency() {
		return this.currency;
	}
	
	/**
	* Returns number of times the Listing has been viewed
	* @return number of views
	*/
	public int getViews() {
		return this.views;
	}
	
	/**
	* Sets the unique identifier for the Listing
	* @return Unique identifier for the Listing
	*/
	public void setListingId(UUID listingId) {
		this.listingId = listingId;
	}
	
	/**
	* Sets the Unique id of the owner of the Listing
	* @return owner Id
	*/
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	
	/**
	* Sets the name of the Owner of the Listing
	* @return Owners name
	*/
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	
	/**
	* Sets the Owners company's name
	* @return Name of the Company
	*/
	public void setOwnerCompany(String ownerCompany) {
		this.ownerCompany = ownerCompany;
	}
	
	/**
	* Sets the Email address to contract the Owner 
	* of the Listing on
	* @return owners email address
	*/
	public void setOwnerEmail(String ownerEmail) {
		this.ownerEmail = ownerEmail;
	}
	
	/**
	* Sets the creation date of the Listing
	* @return Date the Listing was created
	*/
	public void setCreated(LocalDate created) {
		this.created = created;
	}
	
	/**
	* Sets the title of the Listing 
	* @return title
	*/
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	* Sets the listing Description
	* @return Body of the Listing
	*/
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	* Sets the type of the Listing
	* @return Listing type
	*/
	public void setType(listing_type type) {
		this.type = type;
	}
	
	/**
	* Sets the country the Listing is for
	* @return Country listing is for
	*/
	public void setCountry(country country) {
		this.country = country;
	}
	
	/**
	* Sets the location the Listing is for
	* @return I.e Town where the work will be carried out
	*/
	public void setLocation(String location) {
		this.location = location;
	}
	
	/**
	* Sets the number of years experience required
	* @return experience required
	*/
	public void setYearsExperience(int yearsExperience) {
		this.yearsExperience = yearsExperience;
	}
	
	/**
	* Sets the languages requested for the Listing
	* @return requested Languages
	*/
	public void setLanguages(Set<language> languages) {
		this.languages = languages;
	}
	
	/**
	* Sets the Skills requested for the Listing
	* @param skills - Requested Skills
	*/
	public void setSkills(Set<String> skills) {
		this.skills = skills;
	}
	
	/**
	* Sets the rate offered
	* @return offered Rate
	*/
	public void setRate(float rate) {
		this.rate = rate;
	}
	
	/**
	* Sets the Currency the Rate is in
	* @return Currency for the Rate
	*/
	public void	setCurrency(currency currency) {
		this.currency = currency;
	}
	
	/**
	* Sets number of times the Listing has been viewed
	* @return number of views
	*/
	public void setViews(int views) {
		this.views = views;
	}
	
	/**
	* Returns a Builder for the ListingEntity class
	* @return Builder for ListingEntity
	*/
	public static ListingEntityBuilder builder() {
		return new ListingEntityBuilder();
	}
	
	/**
	* Builder for the ListingEntity class
	* @author K Parkings
	*/
	public static class ListingEntityBuilder{
	
		private UUID 				listingId;
		private String				ownerId;
		private String				ownerName;
		private String 				ownerCompany;
		private String				ownerEmail;
		private LocalDate 			created;
		private String 				title;
		private String 				description;
		private listing_type 		type;
		private country 			country;
		private String 				location;
		private int 				yearsExperience;
		private Set<language> 		languages			= new LinkedHashSet<>();
		private Set<String>			skills				= new LinkedHashSet<>();
		private float 				rate;
		private currency			currency;
		private int 				views;
		
		/**
		* Sets the Unique identifier of the ListingEntity
		* @param listingId - Unique Identifier
		* @return Builder
		*/
		public ListingEntityBuilder listingId(UUID listingId) {
			this.listingId = listingId;
			return this;
		}
		
		/**
		* Sets the unique id of the owner of the Listing
		* @param ownerId - Id of the owner
		* @return Builder
		*/
		public ListingEntityBuilder ownerId(String ownerId) {
			this.ownerId = ownerId;
			return this;
		}
		
		/**
		* Sets the name of the owner of the Listing
		* @param ownerName - owners name
		* @return Builder
		*/
		public ListingEntityBuilder ownerName(String ownerName){
			this.ownerName = ownerName;
			return this;
		}
		
		/**
		* Sets the name of the company the owner works for
		* @param ownerCompany - Name of the Company
		* @return Builder
		*/
		public ListingEntityBuilder ownerCompany(String ownerCompany){
			this.ownerCompany = ownerCompany;
			return this;
		}
		
		/**
		* Sets the email of the address with which the candidates can 
		* respond to the Listing
		* @param email - email address of the Listing owner
		* @return Builder
		*/
		public ListingEntityBuilder ownerEmail(String ownerEmail){
			this.ownerEmail = ownerEmail;
			return this;
		}
		
		/**
		* Sets the date the Listing was created
		* @param created - Creation Date
		* @return Builder
		*/
		public ListingEntityBuilder created(LocalDate created){
			this.created = created;
			return this;
		}
		
		/**
		* Sets the title of the Listing
		* @param title - Title for the Listing
		* @return Builder
		*/
		public ListingEntityBuilder title(String title){
			this.title = title;
			return this;
		}
		
		/**
		* Sets the description/body text of the Listing
		* @param description - body of the Listing
		* @return Builder
		*/
		public ListingEntityBuilder description(String description){
			this.description = description;
			return this;
		}
		
		/**
		* Sets the type of Listing
		* @param type - listing type
		* @return Builder
		*/
		public ListingEntityBuilder type(listing_type type){
			this.type = type;
			return this;
		}
		
		/**
		* Sets the Country the Listing is for
		* @param country - Country the Listing relates to
		* @return Builder
		*/
		public ListingEntityBuilder country(country country){
			this.country = country;
			return this;
		}
		
		/**
		* Sets the location the Listing related to 
		* @param location - Location the listing relates to
		* @return Builder
		*/
		public ListingEntityBuilder location(String location){
			this.location = location;
			return this;
		}
		
		/**
		* Sets the Years experience required for the Listing
		* @param yearsExpereince - Years required
		* @return Builder
		*/
		public ListingEntityBuilder yearsExperience(int yearsExperience){
			this.yearsExperience = yearsExperience;
			return this;
		}
		
		/**
		* Sets the Languages requested for the Listing
		* @param languages - Languages requested
		* @return Builder
		*/
		public ListingEntityBuilder languages(Set<language> languages){
			this.languages.clear();
			this.languages.addAll(languages);
			return this;
		}
		
		/**
		* Sets the Skills requested for the Listing
		* @param skills - Skills requested
		* @return Builder
		*/
		public ListingEntityBuilder skills(Set<String> skills) {
			this.skills.clear();
			this.skills.addAll(skills);
			return this;
		}
		
		/**
		* Sets the Rate offered for the Listing
		* @param rate - Rate offered for the Listing
		* @return Builder
		*/
		public ListingEntityBuilder rate(float rate){
			this.rate = rate;
			return this;
		}
		
		/**
		* Sets the Currency for the Rate
		* @param currency - Currency the Rate is in
		* @return Builder
		*/
		public ListingEntityBuilder currency(currency currency){
			this.currency = currency;
			return this;
		}
		
		/**
		* Sets the number of times the Listing has been viewed
		* @param views - Listings of the View
		* @return Builder
		*/
		public ListingEntityBuilder views(int views){
			this.views = views;
			return this;
		}
		
		/**
		* Returns a new Instance of ListingEntity initialized with the 
		* values in the Builder
		* @return new Instance of ListingEntity
		*/
		public ListingEntity build() {
			return new ListingEntity(this);
		}
		
	}
	
	/**
	* Converts a Domain representation of a Listing to an 
	* Entity representation
	* @param listing - Domain representation
	* @return Entity representation
	*/
	public static ListingEntity convertToEntity(Listing listing, Optional<ListingEntity> entityListing) {
		
		if (entityListing.isPresent()) {
			
			ListingEntity entity = entityListing.get();
			
			entity.setYearsExperience(listing.getYearsExperience());
			entity.setViews(listing.getViews());
			entity.setType(listing.getType());
			entity.setTitle(listing.getTitle());
			entity.setRate(listing.getRate());
			entity.setOwnerName(listing.getOwnerName());
			entity.setOwnerId(listing.getOwnerId());
			entity.setOwnerEmail(listing.getOwnerEmail());
			entity.setOwnerCompany(listing.getOwnerCompany());
			entity.setLocation(listing.getLocation());
			entity.setLanguages(listing.getLanguages());
			entity.setSkills(listing.getSkills());
			entity.setDescription(listing.getDescription());
			entity.setCurrency(listing.getCurrency());
			entity.setCountry(listing.getCountry());
			entity.setCreated(listing.getCreated());
			
			return entity;
		}
		
		return ListingEntity
					.builder()
						.yearsExperience(listing.getYearsExperience())
						.views(listing.getViews())
						.type(listing.getType())
						.title(listing.getTitle())
						.rate(listing.getRate())
						.ownerName(listing.getOwnerName())
						.ownerId(listing.getOwnerId())
						.ownerEmail(listing.getOwnerEmail())
						.ownerCompany(listing.getOwnerCompany())
						.location(listing.getLocation())
						.listingId(listing.getListingId())
						.languages(listing.getLanguages())
						.skills(listing.getSkills())
						.description(listing.getDescription())
						.currency(listing.getCurrency())
						.created(listing.getCreated())
						.country(listing.getCountry())
					.build();
	}
	
	/**
	* Converts an entity representation of a Listing to a 
	* Domain representation
	* @param listing - Domain representation
	* @return Entity representation
	*/
	public static Listing convertFromEntity(ListingEntity entity) {
		return Listing
				.builder()
					.yearsExperience(entity.getYearsExperience())
					.views(entity.getViews())
					.type(entity.getType())
					.title(entity.getTitle())
					.rate(entity.getRate())
					.ownerName(entity.getOwnerName())
					.ownerId(entity.getOwnerId())
					.ownerEmail(entity.getOwnerEmail())
					.ownerCompany(entity.getOwnerCompany())
					.location(entity.getLocation())
					.listingId(entity.getListingId())
					.languages(entity.getLanguages())
					.skills(entity.getSkills())
					.description(entity.getDescription())
					.currency(entity.getCurrency())
					.created(entity.getCreated())
					.country(entity.getCountry())
				.build();
	}
	
}