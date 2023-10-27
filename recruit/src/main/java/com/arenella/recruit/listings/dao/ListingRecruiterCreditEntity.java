package com.arenella.recruit.listings.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.arenella.recruit.listings.beans.RecruiterCredit;

/**
* Stores information relating to the Credits available
* to the Recruiter
* @author K Parkings
*/
@Entity
@Table(schema="listings", name="credits")
public class ListingRecruiterCreditEntity {

	@Id
	@Column(name="recruiter_id")
	private String 	recruiterId;
	
	@Column(name="credits")
	private int 	credits;
	
	/**
	* Default Constructor 
	*/
	@SuppressWarnings("unused")
	private ListingRecruiterCreditEntity() {
		//Hibernate
	}
	
	/**
	* Constructor via Builder
	* @param builder - Contains initialization data
	*/
	public ListingRecruiterCreditEntity(ListingRecruiterCreditEntityBuilder builder) {
		this.recruiterId 	= builder.recruiterId;
		this.credits 		= builder.credits;
	}
	
	/**
	* Returns the id of the Recruiter with the credits
	* @return unique id of the Recruiter
	*/
	public String getRecruiterId() {
		return this.recruiterId;
	}
	
	/**
	* Returns the number of remaining credits the Recruiter
	* has to use
	* @return remaining credits
	*/
	public int getCredits() {
		return this.credits;
	}
	
	/**
	* Returns a builder for the class
	* @return Builder
	*/
	public static ListingRecruiterCreditEntityBuilder builder() {
		return new ListingRecruiterCreditEntityBuilder();
	}
	
	/**
	* Builder for the RecruiterCreditEntity class
	*/
	public static class ListingRecruiterCreditEntityBuilder{
		
		private String 	recruiterId;
		private int 	credits;
		
		/**
		* Sets the id of the Recruiter
		* @param recruiterId - unique id of the Recruiter
		* @return Builder
		*/
		public ListingRecruiterCreditEntityBuilder recruiterId(String recruiterId) {
			this.recruiterId = recruiterId;
			return this;
		}
		
		/**
		* Sets the number of Credits the Recruiter has available
		* @param credits - number of available Credits
		* @return Builder
		*/
		public ListingRecruiterCreditEntityBuilder credits(int credits) {
			this.credits = credits;
			return this;
		}
		
		/**
		* Returns initialized instance of RecruiterCreditEntity
		* @return instance
		*/
		public ListingRecruiterCreditEntity build() {
			return new ListingRecruiterCreditEntity(this);
		}
		
	}
	
	/**
	* Converts from Entity to the Domain representation
	* @param entity - Entity representation
	* @return Domain representation
	*/
	public static RecruiterCredit convertFromEntity(ListingRecruiterCreditEntity entity) {
		return RecruiterCredit
				.builder()
					.recruiterId(entity.getRecruiterId())
					.credits(entity.getCredits())
				.build();
	}
	
	/**
	* Converts from Domain to the Entity representation
	* @param domain - Domain representation
	* @return Entity representation
	*/
	public static ListingRecruiterCreditEntity convertToEntity(RecruiterCredit domain) {
		return ListingRecruiterCreditEntity
				.builder()
					.recruiterId(domain.getRecruiterId())
					.credits(domain.getCredits())
				.build();
	}
	
}