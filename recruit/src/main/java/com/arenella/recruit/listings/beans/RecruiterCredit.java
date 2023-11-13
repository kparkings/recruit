package com.arenella.recruit.listings.beans;

/**
* Stores information relating to the Credits available
* to the Recruiter
* @author K Parkings
*/
public class RecruiterCredit {

	public static final int DEFAULT_CREDITS 	= 2;
	public static final int DISABLED_CREDITS 	= -1;
	
	private String 	recruiterId;
	private int 	credits			= DEFAULT_CREDITS;
	
	
	/**
	* Constructor via Builder
	* @param builder - Contains initialization data
	*/
	public RecruiterCredit(RecruiterCreditBuilder builder) {
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
	* Sets the number of Credits the Recruiter has available
	* @param credits - number of available credits
	*/
	public void setCredits(int credits) {
		this.credits = credits;
	}
	
	/**
	* Decrements credits by 1 until 0 is reached 
	*/
	public void decrementCredits() {
		
		if (this.credits == 0) {
			return;
		}
		
		this.credits--;
		
	}
	
	/**
	* Returns a builder for the class
	* @return Builder
	*/
	public static RecruiterCreditBuilder builder() {
		return new RecruiterCreditBuilder();
	}
	
	/**
	* Builder for the RecruiterCreditEntity class
	*/
	public static class RecruiterCreditBuilder{
		
		private String 	recruiterId;
		private int 	credits			= DEFAULT_CREDITS;
		
		/**
		* Sets the id of the Recruiter
		* @param recruiterId - unique id of the Recruiter
		* @return Builder
		*/
		public RecruiterCreditBuilder recruiterId(String recruiterId) {
			this.recruiterId = recruiterId;
			return this;
		}
		
		/**
		* Sets the number of Credits the Recruiter has available
		* @param credits - number of available Credits
		* @return Builder
		*/
		public RecruiterCreditBuilder credits(int credits) {
			this.credits = credits;
			return this;
		}
		
		/**
		* Returns initialized instance of RecruiterCreditEntity
		* @return instance
		*/
		public RecruiterCredit build() {
			return new RecruiterCredit(this);
		}
		
	}
	
}
