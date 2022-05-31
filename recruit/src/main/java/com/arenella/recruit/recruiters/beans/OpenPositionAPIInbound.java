package com.arenella.recruit.recruiters.beans;

import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
* Represents an incoming request from a Recruiter to 
* create a new OpenPosition that needs to be filled and 
* is being offered to other Recruiters
* @author K Parkings
*/
@JsonDeserialize(builder=OpenPositionAPIInbound.OpenPositionAPIInboundBuilder.class)
public class OpenPositionAPIInbound {
	
	private UUID 						id;
	private String 						recruiterId;
	private String 						positionTitle;
	private Country	 					country;
	private String						location;
	private ContractType 				contractType;
	private String	 					renumeration;
	private LocalDate 					startDate;
	private LocalDate 					positionClosingDate;
	private String 						description;
	private String 						comments;
	
	/**
	* Constructor based upon a Builder
	* @param builder - Contains initialization values
	*/
	private OpenPositionAPIInbound(OpenPositionAPIInboundBuilder builder) {
		
		this.id 							= builder.id;
		this.recruiterId					= builder.recruiterId;
		this.positionTitle 					= builder.positionTitle;
		this.country 						= builder.country;
		this.location 						= builder.location;
		this.contractType 					= builder.contractType;
		this.renumeration 					= builder.renumeration;
		this.startDate 						= builder.startDate;
		this.positionClosingDate 			= builder.positionClosingDate;
		this.description 					= builder.description;
		this.comments 						= builder.comments;
		
	}
	
	/**
	* Returns the Unique Identifier for the Open Position
	* @return Unique Id
	*/
	public UUID getId(){
		return this.id;
	}
	
	/**
	* Returns the unique Id of the Recruiter who owns the 
	* Open Position
	* @return Unique Id
	*/
	public String getRecruiterId(){
		return this.recruiterId;
	}
	
	/**
	* Returns the Title of the Open Position
	* @return Title of the Open Position
	*/
	public String getPositionTitle(){
		return this.positionTitle;
	}
	
	/**
	* Returns the Country the Open Position is based in
	* @return Country of the Open Position
	*/
	public Country getCountry(){
		return this.country;
	}
	
	/**
	* Returns the Location where the Open Position is based
	* @return Location of the Open position
	*/
	public String getLocation(){
		return this.location;
	}
	
	/**
	* Returns the Rate/Salary being offered
	* @return Renumeration being offered
	*/
	public String getRenumeration(){
		return this.renumeration;
	}
	
	/**
	* Returns the type of the Contract
	* @return Contract type
	*/
	public ContractType getContractType(){
		return this.contractType;
	}
	
	/**
	* Returns the start date of the Open Position
	* @return Start Date
	*/
	public LocalDate getStartDate(){
		return this.startDate;
	}
	
	/**
	* Returns the Date then the Position closes and no more candidates
	* can be offered
	* @return Position closing date
	*/
	public LocalDate getPositionClosingDate(){
		return this.positionClosingDate;
	}
	
	/**
	* Returns a description of the Open position 
	* @return Description of the Position
	*/
	public String getDescription(){
		return this.description;
	}
	
	/**
	* Returns any comments from the Recruiter relating to the 
	* Open Position
	* @return comments
	*/
	public String getComments(){
		return this.comments;
	}
	
	/**
	* Returns a Builder for the Class
	* @return Builder for the Class
	*/
	public static OpenPositionAPIInboundBuilder builder() {
		return new OpenPositionAPIInboundBuilder();
	} 
	
	/**
	* Builder for the OpenPositionAPIInbound class
	* @author K Parkings
	*/
	@JsonPOJOBuilder(buildMethodName="build", withPrefix="")
	public static class OpenPositionAPIInboundBuilder{
		
		private UUID 						id;
		private String 						recruiterId;
		private String 						positionTitle;
		private Country	 					country;
		private String						location;
		private ContractType 				contractType;
		private String 						renumeration;
		private LocalDate 					startDate;
		private LocalDate 					positionClosingDate;
		private String 						description;
		private String 						comments;
		
		/**
		* Sets the Unique Identifier for the Open Position
		* @param id - UniqueId of the Open Position
		* @return Builder
		*/
		public OpenPositionAPIInboundBuilder id(UUID id){
			this.id = id;
			return this;
		}
		
		/**
		* Sets the unique Id of the Recruiter who owns the 
		* Open Position
		* @param recruiterId - Unique Id of the owning Recruiter
		* @return Builder
		*/
		public OpenPositionAPIInboundBuilder recruiterId(String recruiterId){
			this.recruiterId = recruiterId;
			return this;
		}
		
		/**
		* Sets the Title of the Open Position
		* @param positionTitle - Title for the Open Position
		* @return Builder
		*/
		public OpenPositionAPIInboundBuilder positionTitle(String positionTitle){
			this.positionTitle = positionTitle;
			return this;
		}
		
		/**
		* Sets the Country the Open Position is based in
		* @param country - Country where the Position is located
		* @return Builder
		*/
		public OpenPositionAPIInboundBuilder country(Country country){
			this.country = country;
			return this;
		}
		
		/**
		* Sets the Location where the Open Position is based
		* @param location - Location of the Position
		* @return Builder
		*/
		public OpenPositionAPIInboundBuilder location(String location){
			this.location = location;
			return this;
		}
		
		/**
		* Sets the Rate/Salary being offered
		* @param renumeration - Renumeration offered for the Position
		* @return Builder
		*/
		public OpenPositionAPIInboundBuilder renumeration(String renumeration){
			this.renumeration = renumeration;
			return this;
		}
		
		/**
		* Sets the type of the Contract
		* @param contractType - Type of the Contract
		* @return Builder
		*/
		public OpenPositionAPIInboundBuilder contractType(ContractType contractType){
			this.contractType = contractType;
			return this;
		}
		
		/**
		* Sets the start date of the Open Position
		* @param startDate - When the Open Position starts
		* @return Builder
		*/
		public OpenPositionAPIInboundBuilder startDate(LocalDate startDate){
			this.startDate = startDate;
			return this;
		}
		
		/**
		* Sets the Date then the Position closes and no more candidates
		* can be offered
		* @param positionClosingDate - Position closing date
		* @return Builder
		*/
		public OpenPositionAPIInboundBuilder positionClosingDate(LocalDate positionClosingDate){
			this.positionClosingDate = positionClosingDate;
			return this;
		}
		
		/**
		* Sets a description of the Open position 
		* @param description - description of the Open Position
		* @return Builder
		*/
		public OpenPositionAPIInboundBuilder description(String description){
			this.description = description;
			return this;
		}
		
		/**
		* Sets any comments from the Recruiter relating to the 
		* Open Position
		* @param comments - comments relating to Position
		* @return Builder
		*/
		public OpenPositionAPIInboundBuilder comments(String comments){
			this.comments = comments;
			return this;
		}
		
		/**
		* Returns an instance of OpenPositionAPIInbound initalized with 
		* the values in the Builder
		* @return Builder
		*/
		public OpenPositionAPIInbound build() {
			return new OpenPositionAPIInbound(this);
		}
		
	}
	
	public enum Country {NETHERLANDS, BELGIUM, UK, IRL, EUROPE, WORLD}
	public enum ContractType {CONTRACT, PERM, BOTH}
	
}
