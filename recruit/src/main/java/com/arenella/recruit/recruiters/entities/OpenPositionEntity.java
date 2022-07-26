package com.arenella.recruit.recruiters.entities;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.Entity;

import com.arenella.recruit.recruiters.beans.OpenPosition;
import com.arenella.recruit.recruiters.beans.OpenPosition.ContractType;
import com.arenella.recruit.recruiters.beans.OpenPosition.Country;

/**
* Entity representation of an Open Position
* @author K Parkings
*/
@Entity
public class OpenPositionEntity {

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
	private LocalDate					created;
	
	/**
	* Constructor based upon a Builder
	* @param builder - Contains initialization values
	*/
	private OpenPositionEntity(OpenPositionEntityBuilder builder) {
		
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
		this.created						= builder.created;
		
		if (Optional.ofNullable(created).isEmpty()) {
			this.created = LocalDate.now();
		}
		
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
	* Returns the date the OpenPosition was created
	* @return When OpenPosition was created
	*/
	public LocalDate getCreated() {
		return this.created;
	}
	
	/**
	* Sets the unique Id of the Object to a 
	* random UUID to make it a new OpenPosition and 
	* sets the id of the recruiter that owns the OpenPosition
	* @param recruiterId - id of the recruiter that owns the OpenPosition
	*/
	public void initializeAsNewObject(String recruiterId) {
		this.id 			= UUID.randomUUID();
		this.recruiterId 	= recruiterId;
	}
	
	/**
	* Returns a Builder for the Class
	* @return Builder for the Class
	*/
	public static OpenPositionEntityBuilder builder() {
		return new OpenPositionEntityBuilder();
	} 
	
	/**
	* Builder for theOpenPositionEntity class
	* @author K Parkings
	*/
	public static class OpenPositionEntityBuilder{
		
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
		private LocalDate					created;
		
		
		/**
		* Sets the Unique Identifier for the Open Position
		* @param id - UniqueId of the Open Position
		* @return Builder
		*/
		public OpenPositionEntityBuilder id(UUID id){
			this.id = id;
			return this;
		}
		
		/**
		* Sets the unique Id of the Recruiter who owns the 
		* Open Position
		* @param recruiterId - Unique Id of the owning Recruiter
		* @return Builder
		*/
		public OpenPositionEntityBuilder recruiterId(String recruiterId){
			this.recruiterId = recruiterId;
			return this;
		}
		
		/**
		* Sets the Title of the Open Position
		* @param positionTitle - Title for the Open Position
		* @return Builder
		*/
		public OpenPositionEntityBuilder positionTitle(String positionTitle){
			this.positionTitle = positionTitle;
			return this;
		}
		
		/**
		* Sets the Country the Open Position is based in
		* @param country - Country where the Position is located
		* @return Builder
		*/
		public OpenPositionEntityBuilder country(Country country){
			this.country = country;
			return this;
		}
		
		/**
		* Sets the Location where the Open Position is based
		* @param location - Location of the Position
		* @return Builder
		*/
		public OpenPositionEntityBuilder location(String location){
			this.location = location;
			return this;
		}
		
		/**
		* Sets the Rate/Salary being offered
		* @param renumeration - Renumeration offered for the Position
		* @return Builder
		*/
		public OpenPositionEntityBuilder renumeration(String renumeration){
			this.renumeration = renumeration;
			return this;
		}
		
		/**
		* Sets the type of the Contract
		* @param contractType - Type of the Contract
		* @return Builder
		*/
		public OpenPositionEntityBuilder contractType(ContractType contractType){
			this.contractType = contractType;
			return this;
		}
		
		/**
		* Sets the start date of the Open Position
		* @param startDate - When the Open Position starts
		* @return Builder
		*/
		public OpenPositionEntityBuilder startDate(LocalDate startDate){
			this.startDate = startDate;
			return this;
		}
		
		/**
		* Sets the Date then the Position closes and no more candidates
		* can be offered
		* @param positionClosingDate - Position closing date
		* @return Builder
		*/
		public OpenPositionEntityBuilder positionClosingDate(LocalDate positionClosingDate){
			this.positionClosingDate = positionClosingDate;
			return this;
		}
		
		/**
		* Sets a description of the Open position 
		* @param description - description of the Open Position
		* @return Builder
		*/
		public OpenPositionEntityBuilder description(String description){
			this.description = description;
			return this;
		}
		
		/**
		* Sets any comments from the Recruiter relating to the 
		* Open Position
		* @param comments - comments relating to Position
		* @return Builder
		*/
		public OpenPositionEntityBuilder comments(String comments){
			this.comments = comments;
			return this;
		}

		/**
		* Sets when the OpenPosition was created
		* @param created - Data the OpenPosition was created
		* @return Builder
		*/
		public OpenPositionEntityBuilder created(LocalDate created){
			this.created = created;
			return this;
		}
		
		/**
		* Returns an instance of OpenPositionEntityBuilder initalized with 
		* the values in the Builder
		* @return Builder
		*/
		public OpenPositionEntity build() {
			return new OpenPositionEntity(this);
		}
		
	}
	
	/**
	* Converts from Entity representation to Domain representation
	* @param entity - Entity representation
	* @return Domain representation
	*/
	public static OpenPosition convertFromEntity(OpenPositionEntity entity) {
		return OpenPosition
					.builder()
						.comments(entity.getComments())
						.contractType(entity.getContractType())
						.country(entity.getCountry())
						.description(entity.getDescription())
						.id(entity.getId())
						.location(entity.getLocation())
						.positionClosingDate(entity.getPositionClosingDate())
						.positionTitle(entity.getPositionTitle())
						.recruiterId(entity.getRecruiterId())
						.renumeration(entity.getRenumeration())
						.startDate(entity.getStartDate())
					.build();
	}
	
	/**
	* Converts from Domain representation to Entity representation
	* @param openPosition - Domain representation
	* @return Entity representation
	*/
	public static OpenPositionEntity convertToEntity(OpenPosition openPosition) {
		return OpenPositionEntity
							.builder()
								.comments(openPosition.getComments())
								.contractType(openPosition.getContractType())
								.country(openPosition.getCountry())
								.description(openPosition.getDescription())
								.id(openPosition.getId())
								.location(openPosition.getLocation())
								.positionClosingDate(openPosition.getPositionClosingDate())
								.positionTitle(openPosition.getPositionTitle())
								.recruiterId(openPosition.getRecruiterId())
								.renumeration(openPosition.getRenumeration())
								.startDate(openPosition.getStartDate())
							.build();
	}
	
}
