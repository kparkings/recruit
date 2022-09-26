package com.arenella.recruit.recruiters.beans;

import java.time.LocalDate;
import java.util.UUID;

import com.arenella.recruit.recruiters.beans.OfferedCandidateAPIOutbound.RecruiterDetails;
import com.arenella.recruit.recruiters.beans.OpenPosition.ContractType;
import com.arenella.recruit.recruiters.beans.OpenPosition.Country;

/**
* Represents an Open Position that needs to be filled and 
* is being offered to other Recruiters
* @author K Parkings
*/
public class OpenPositionAPIOutbound {

	private UUID 						id;
	private RecruiterDetails 			recruiter;
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
	private OpenPositionAPIOutbound(OpenPositionAPIOutboundBuilder builder) {
		
		this.id 							= builder.id;
		this.recruiter						= builder.recruiter;
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
	* Returns details of the Recruiter who owns the 
	* Open Position
	* @return Unique recruiterName
	*/
	public RecruiterDetails getRecruiter(){
		return this.recruiter;
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
	public static OpenPositionAPIOutboundBuilder builder() {
		return new OpenPositionAPIOutboundBuilder();
	} 
	
	/**
	* Builder for the OpenPositionAPIOutbound class
	* @author K Parkings
	*/
	public static class OpenPositionAPIOutboundBuilder{
		
		private UUID 						id;
		private RecruiterDetails			recruiter;
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
		* Sets the Unique Identifier for the Open Position
		* @param id - UniqueId of the Open Position
		* @return Builder
		*/
		public OpenPositionAPIOutboundBuilder id(UUID id){
			this.id = id;
			return this;
		}
		
		/**
		* Sets details of the Recruiter who owns the 
		* Open Position
		* @param recruiter - Recruiter's details
		* @return Builder
		*/
		public OpenPositionAPIOutboundBuilder recruiter(RecruiterDetails recruiter){
			this.recruiter = recruiter;
			return this;
		}
		
		/**
		* Sets the Title of the Open Position
		* @param positionTitle - Title for the Open Position
		* @return Builder
		*/
		public OpenPositionAPIOutboundBuilder positionTitle(String positionTitle){
			this.positionTitle = positionTitle;
			return this;
		}
		
		/**
		* Sets the Country the Open Position is based in
		* @param country - Country where the Position is located
		* @return Builder
		*/
		public OpenPositionAPIOutboundBuilder country(Country country){
			this.country = country;
			return this;
		}
		
		/**
		* Sets the Location where the Open Position is based
		* @param location - Location of the Position
		* @return Builder
		*/
		public OpenPositionAPIOutboundBuilder location(String location){
			this.location = location;
			return this;
		}
		
		/**
		* Sets the Rate/Salary being offered
		* @param renumeration - Renumeration offered for the Position
		* @return Builder
		*/
		public OpenPositionAPIOutboundBuilder renumeration(String renumeration){
			this.renumeration = renumeration;
			return this;
		}
		
		/**
		* Sets the type of the Contract
		* @param contractType - Type of the Contract
		* @return Builder
		*/
		public OpenPositionAPIOutboundBuilder contractType(ContractType contractType){
			this.contractType = contractType;
			return this;
		}
		
		/**
		* Sets the start date of the Open Position
		* @param startDate - When the Open Position starts
		* @return Builder
		*/
		public OpenPositionAPIOutboundBuilder startDate(LocalDate startDate){
			this.startDate = startDate;
			return this;
		}
		
		/**
		* Sets the Date then the Position closes and no more candidates
		* can be offered
		* @param positionClosingDate - Position closing date
		* @return Builder
		*/
		public OpenPositionAPIOutboundBuilder positionClosingDate(LocalDate positionClosingDate){
			this.positionClosingDate = positionClosingDate;
			return this;
		}
		
		/**
		* Sets a description of the Open position 
		* @param description - description of the Open Position
		* @return Builder
		*/
		public OpenPositionAPIOutboundBuilder description(String description){
			this.description = description;
			return this;
		}
		
		/**
		* Sets any comments from the Recruiter relating to the 
		* Open Position
		* @param comments - comments relating to Position
		* @return Builder
		*/
		public OpenPositionAPIOutboundBuilder comments(String comments){
			this.comments = comments;
			return this;
		}
		
		/**
		* Returns an instance of OpenPositionAPIOutbound initalized with 
		* the values in the Builder
		* @return Initialzied instance of OpenPositionAPIOutbound
		*/
		public OpenPositionAPIOutbound build() {
			return new OpenPositionAPIOutbound(this);
		}
		
	}

	/**
	* Converts from Domain to API Outbound representation of an OpenPosition
	* @param openPosition - Domain representation
	* @param recruiter - Recruiter who is owner of the Open Position
	* @return API Outbound representation
	*/
	public static OpenPositionAPIOutbound convertFromDomain(OpenPosition openPosition, RecruiterDetails recruiter) {
		return OpenPositionAPIOutbound
				.builder()
					.comments(openPosition.getComments())
					.contractType(openPosition.getContractType())
					.country(openPosition.getCountry())
					.description(openPosition.getDescription())
					.id(openPosition.getId())
					.location(openPosition.getLocation())
					.positionClosingDate(openPosition.getPositionClosingDate())
					.positionTitle(openPosition.getPositionTitle())
					.recruiter(recruiter)
					.renumeration(openPosition.getRenumeration())
					.startDate(openPosition.getStartDate())
				.build();
	}
	
}