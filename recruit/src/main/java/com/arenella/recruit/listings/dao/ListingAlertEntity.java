package com.arenella.recruit.listings.dao;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

import com.arenella.recruit.listings.beans.Listing;
import com.arenella.recruit.listings.beans.ListingAlert;

/**
* Represents an Alert configuration used to match 
* new listings against the interests of Candidates 
* so they can be alerted to new matching listings
* @author K Parkings
*/
@Entity
@Table(schema="listings", name="listing_alerts")
public class ListingAlertEntity {

	@Id
	@Column(name="id")
	private UUID id;
	
	@Column(name="user_id")
	private Long userId;
	
	@Column(name="email")
	private String email;
	
	@Column(name="created")
	private LocalDate created;
	
	@Enumerated(EnumType.STRING)
	@Column(name="contract_type")
	private Listing.listing_type contractType;
	
	@Column(name="country")
	@ElementCollection(targetClass=Listing.Country.class, fetch = FetchType.EAGER)
	@CollectionTable(schema="listings", name="listing_alerts_countries", joinColumns=@JoinColumn(name="listing_alert_id"))
	@Enumerated(EnumType.STRING) 
	//@OnDelete(action = OnDeleteAction.CASCADE)
	//@JoinColumn(name = "listing_alert_id")
	private Set<Listing.Country> countries = new LinkedHashSet<>();
	
	@Column(name="category")
	@ElementCollection(targetClass=Listing.TECH.class, fetch = FetchType.EAGER)
	@CollectionTable(schema="listings", name="listing_alerts_categories", joinColumns=@JoinColumn(name="listing_alert_id"))
	@Enumerated(EnumType.STRING) 
	//@OnDelete(action = OnDeleteAction.CASCADE)
	//@JoinColumn(name = "listing_alert_id")    
	private Set<Listing.TECH> categories = new LinkedHashSet<>();
	
	/**
	* Builder for the class
	* @param builder - Contains initialization information
	*/
	public ListingAlertEntity(ListingAlertEntityBuilder builder) {
		this.id 			= builder.id;
		this.userId 		= builder.userId;
		this.email 			= builder.email;
		this.created 		= builder.created;
		this.contractType 	= builder.contractType;
		this.countries 		= builder.countries;
		this.categories 	= builder.categories;
		
	}
	
	/**
	* Returns the unique id of the alert
	* @return id of the alert
	*/
	public UUID getId() {
		return this.id;
	}
	
	/**
	* Returns unique if of the user if known.
	* @return id of the user who owns the alert
	*/
	public Optional<Long> getUserId() {
		return Optional.ofNullable(this.userId);
	}
	
	/**
	* Returns the email of the owner of the Alert
	* @return owner of alerts email address
	*/
	public String getEmail() {
		return this.email;
	}
	
	/**
	* Returns the Date the Alert was created
	* @return when the Alter was created
	*/
	public LocalDate getCreated() {
		return this.created;
	}
	
	/**
	* Returns the types of contract the owner of the Alert is
	* interested in hearing about
	* @return types of interest
	*/
	public Listing.listing_type getContractType() {
		return this.contractType;
	}
	
	/**
	* Returns the countries the owner of the Alert is interested in 
	* hearing about
	* @return countries of interest
	*/
	public Set<Listing.Country> getCountries() {
		return this.countries;
	}
	
	/**
	* Returns the categories the owner of the Alert is interested in 
	* hearing about
	* @return categoties of interest
	*/
	public Set<Listing.TECH> getCategories() {
		return this.categories;
	}
	
	/**
	* Constructor
	*/
	public ListingAlertEntity() {
		//Hiibernate
	}
	
	/**
	* Returns a Builder for the class
	* @return
	*/
	public static ListingAlertEntityBuilder builder() {
		return new ListingAlertEntityBuilder();
	}

	/**
	* Builder for the class
	* @author K Parkings
	*/
	public static class ListingAlertEntityBuilder {
	
		private UUID 					id;
		private Long 					userId;
		private String 					email;
		private LocalDate 				created;
		private Listing.listing_type 	contractType;
		private Set<Listing.Country> 	countries 		= new LinkedHashSet<>();
		private Set<Listing.TECH> 		categories 		= new LinkedHashSet<>();
		
		/**
		* Sets the unique if of the Alert
		* @param id - unique id
		* @return Builder
		*/
		public ListingAlertEntityBuilder id(UUID id) {
			this.id = id;
			return this;
		}
		
		/**
		* Sets the unique if of the user who owns the Alter
		* @param userId - id of the user
		* @return Builder
		*/
		public ListingAlertEntityBuilder userId(Long userId) {
			this.userId = userId;
			return this;
		}
		
		/**
		* Sets the email of the USer
		* @param email - email of the owner of the Alert
		* @return Builder
		*/
		public ListingAlertEntityBuilder email(String email) {
			this.email = email;
			return this;
		}
		
		/**
		* Sets when the Alter was created
		* @param created - when Alert was created
		* @return Builder
		*/
		public ListingAlertEntityBuilder created(LocalDate created) {
			this.created = created;
			return this;
		}
		
		/**
		* Sets the contract types the User in interested in receiving Alerts for
		* @param contractType - contract types of interest
		* @return Builder
		*/
		public ListingAlertEntityBuilder contractType(Listing.listing_type contractType) {
			this.contractType = contractType;
			return this;
		}
		
		/**
		* Sets the countries of interest
		* @param countries - countries to receive Alerts for
		* @return Builder
		*/
		public ListingAlertEntityBuilder countries(Set<Listing.Country> countries) {
			this.countries.clear();
			this.countries.addAll(countries);
			return this;
		}
		
		/**
		* Sets the categories of interest
		* @param categories - Categories to receive alerts for
		* @return Builder
		*/
		public ListingAlertEntityBuilder categories(Set<Listing.TECH> categories) {
			this.categories.clear();
			this.categories.addAll(categories);
			return this;
		}
		
		/**
		* Returns initialized instance
		* @return Initialized instance
		*/
		public ListingAlertEntity build() {
			return new ListingAlertEntity(this);
		}
		
	}
	
	/**
	* Converts from the Entity to Domain representation of the Alert
	* @param entity - To convert
	* @return - Converted representation
	*/
	public static ListingAlert convertFromEntity(ListingAlertEntity entity) {
		return ListingAlert
				.builder()
					.categories(entity.getCategories())
					.contractType(entity.getContractType())
					.countries(entity.getCountries())
					.created(entity.getCreated())
					.email(entity.getEmail())
					.id(entity.getId())
					.userId(entity.getUserId().isPresent() ? entity.getUserId().get() : null)
				.build();
	}
	
	/**
	* Converts from the Domain to Entity representation of the Alert
	* @param alert - To convert
	* @return - Converted representation
	*/
	public static ListingAlertEntity convertToEntity(ListingAlert alert) {
		return ListingAlertEntity
				.builder()
					.categories(alert.getCategories())
					.contractType(alert.getContractType())
					.countries(alert.getCountries())
					.created(alert.getCreated())
					.email(alert.getEmail())
					.id(alert.getId())
					.userId(alert.getUserId().isPresent() ? alert.getUserId().get() : null)
				.build();
	}
	
}