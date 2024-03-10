package com.arenella.recruit.recruiters.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.arenella.recruit.recruiters.beans.RecruiterCredit;

/**
* Stores information relating to the Credits available
* to the Recruiter
* @author K Parkings
*/
@Entity
@Table(schema="recruiter", name="credits")
public class CreditEntity {

	@Id
	@Column(name="recruiter_id")
	private String 	recruiterId;
	
	@Column(name="credits")
	private int 	credits;
	
	@Column(name="paid_subscription")
	private boolean paidSubscription;
	
	/**
	* Default Constructor 
	*/
	@SuppressWarnings("unused")
	private CreditEntity() {
		//Hibernate
	}
	
	/**
	* Constructor via Builder
	* @param builder - Contains initialization data
	*/
	public CreditEntity(CreditEntityBuilder builder) {
		this.recruiterId 		= builder.recruiterId;
		this.credits 			= builder.credits;
		this.paidSubscription 	= builder.paidSubscription;
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
	* Returns whether the Recruiter has a paid subscription active
	* @return whether the Recruiter has a paid subscription active
	*/
	public boolean hasPaidSubscription() {
		return this.paidSubscription;
	}
	
	/**
	* Returns a builder for the class
	* @return Builder
	*/
	public static CreditEntityBuilder builder() {
		return new CreditEntityBuilder();
	}
	
	/**
	* Builder for the RecruiterCreditEntity class
	*/
	public static class CreditEntityBuilder{
		
		private String 	recruiterId;
		private int 	credits;
		private boolean paidSubscription;
		
		/**
		* Sets the id of the Recruiter
		* @param recruiterId - unique id of the Recruiter
		* @return Builder
		*/
		public CreditEntityBuilder recruiterId(String recruiterId) {
			this.recruiterId = recruiterId;
			return this;
		}
		
		/**
		* Sets the number of Credits the Recruiter has available
		* @param credits - number of available Credits
		* @return Builder
		*/
		public CreditEntityBuilder credits(int credits) {
			this.credits = credits;
			return this;
		}
		
		/**
		* Sets whether the Recruiter has a currently active paid subscription
		* @param paidSubscription - Active paid subscription
		* @return Builder
		*/
		public CreditEntityBuilder paidSubscription(boolean paidSubscription) {
			this.paidSubscription = paidSubscription;
			return this;
		}
		
		/**
		* Returns initialized instance of RecruiterCreditEntity
		* @return instance
		*/
		public CreditEntity build() {
			return new CreditEntity(this);
		}
		
	}
	
	/**
	* Converts from Entity to the Domain representation
	* @param entity - Entity representation
	* @return Domain representation
	*/
	public static RecruiterCredit convertFromEntity(CreditEntity entity) {
		return RecruiterCredit
				.builder()
					.recruiterId(entity.getRecruiterId())
					.credits(entity.getCredits())
					.paidSubscription(entity.hasPaidSubscription())
				.build();
	}
	
	/**
	* Converts from Domain to the Entity representation
	* @param domain - Domain representation
	* @return Entity representation
	*/
	public static CreditEntity convertToEntity(RecruiterCredit domain) {
		return CreditEntity
				.builder()
					.recruiterId(domain.getRecruiterId())
					.credits(domain.getCredits())
					.paidSubscription(domain.hasPaidSubscription())
				.build();
	}
	
}