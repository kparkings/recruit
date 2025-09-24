package com.arenella.recruit.candidates.entities;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.arenella.recruit.candidates.beans.PendingCandidate;
import com.arenella.recruit.candidates.beans.Candidate.Photo;
import com.arenella.recruit.candidates.beans.Candidate.Rate;
import com.arenella.recruit.candidates.beans.Candidate.Photo.PHOTO_FORMAT;
import com.arenella.recruit.candidates.beans.Candidate.Rate.CURRENCY;
import com.arenella.recruit.candidates.beans.Candidate.Rate.PERIOD;

/**
* Represents a Candidate that has uploaded their details 
* but is not yet processed and added as a Candidate
* @author K Parkings
*/
@Entity
@Table(schema="candidate", name="pending_candidate")
public class PendingCandidateEntity {

	@Id
	@Column(name="pending_candidate_id")
	private UUID		pendingCandidateId;
	
	@Column(name="firstname")
	private String 		firstname;
	
	@Column(name="surname")
	private String 		surname;
	
	@Column(name="email")
	private String 		email;
	
	@Column(name="perm")
	private boolean 	perm;
	
	@Column(name="freelance")
	private boolean 	freelance;
	
	@Column(name="introduction")
	private String introduction;
	
	@Enumerated(EnumType.STRING)
	@Column(name="rate_contract_currency")
	private CURRENCY rateContractCurrency;
		
	@Enumerated(EnumType.STRING)
	@Column(name="rate_contract_period")
	private PERIOD rateContractPeriod;
	    
	@Column(name="rate_contract_value_min")
	private float rateContractValueMin;   
	
	@Column(name="rate_contract_value_max")
	private float rateContractValueMax;
	
	@Enumerated(EnumType.STRING)
	@Column(name="rate_perm_currency")
	private CURRENCY ratePermCurrency;
		
	@Enumerated(EnumType.STRING)
	@Column(name="rate_perm_period")
	private PERIOD ratePermPeriod;
	    
	@Column(name="rate_perm_value_min")
	private float ratePermValueMin;   
	
	@Column(name="rate_perm_value_max")
	private float ratePermValueMax;
	
    @Enumerated(EnumType.STRING)
    @Column(name="photo_format")
    private PHOTO_FORMAT photoFormat;      
    
    @Column(name="photo_bytes")
    private byte[] photoBytes;
	
	/**
	* Constructor based upon a builder
	* @param builder - Contains initialization parameters
	*/
	public PendingCandidateEntity(PendingCandidateEntityBuilder builder) {
		
		this.pendingCandidateId 		= builder.pendingCandidateId;
		this.firstname					= builder.firstname;
		this.surname					= builder.surname;
		this.email						= builder.email;
		this.perm 						= builder.perm;
		this.freelance 					= builder.freelance;
		this.introduction				= builder.introduction;
		this.rateContractCurrency 		= builder.rateContractCurrency;
		this.rateContractPeriod 		= builder.rateContractPeriod;
		this.rateContractValueMin 		= builder.rateContractValueMin;   
		this.rateContractValueMax 		= builder.rateContractValueMax;
		this.ratePermCurrency 			= builder.ratePermCurrency;
		this.ratePermPeriod 			= builder.ratePermPeriod;
		this.ratePermValueMin 			= builder.ratePermValueMin;   
		this.ratePermValueMax 			= builder.ratePermValueMax;
		
		this.photoFormat				= builder.photoFormat;      
		this.photoBytes					= builder.photoBytes;
		
	}
	
	/**
	* Default constructor 
	*/
	@SuppressWarnings("unused")
	private PendingCandidateEntity() {
		//Hibernate
	}
	
	/**
	* Returns the unique identifier of the Candidate
	* @return unique Id of the candidate
	*/
	public UUID getPendingCandidateId() {
		return this.pendingCandidateId;
	}
	
	/***
	* Returns the name of the Candidate
	* @return Candidates name
	*/
	public String getFirstname() {
		return this.firstname;
	}
	
	/***
	* Returns the surname of the Candidate
	* @return Candidates name
	*/
	public String getSurname() {
		return this.surname;
	}
	
	/**
	* Returns the Candidates Email address
	* @return Email address
	*/
	public String getEmail() {
		return this.email;
	}
	
	/**
	* Returns whether or not the Candidate is looking for
	* freelance projects
	* @return Whether or not the Candidate is interested in freelance roles
	*/
	public boolean isFreelance() {
		return this.freelance;
	}
	
	/**
	* Returns whether or not the Candidate is interested in permanent roles
	* @return Whether or not the Candidate is interested in permanent roles
	*/
	public boolean isPerm() {
		return this.perm;
	}
	
	/**
	* Returns the Candidates introduction about themselves
	* @return intro
	*/
	public String getIntroduction() {
		return this.introduction;
	}
	
	/**
	* Returns the Currency for the contract rate
	* @return Currency for the contract rate
	*/
	public CURRENCY getRateContractCurrency() {
		return this.rateContractCurrency;
	}
	
	/**
	* Returns the Period for the Contract Rate
	* @return Period for the Contract Rate
	*/
	public PERIOD getRateContractPeriod() {
		return this.rateContractPeriod;
	}
	
	/**
	* Returns the min contract rate
	* @return min contract rate
	*/
	public float getRateContractValueMin() {
		return this.rateContractValueMin;
	}   
	
	/**
	* Returns the max contract rate
	* @return max contract rate
	*/
	public float getRateContractValueMax() {
		return this.rateContractValueMax;
	}
	
	/**
	* Returns the Currency for the perm salary
	* @return Currency for the perm salary
	*/
	public CURRENCY getRatePermCurrency() {
		return this.ratePermCurrency;
	}
	
	/**
	* Returns the Period for the Perm salary
	* @return Period for the Perm salary
	*/
	public PERIOD getRatePermPeriod() {
		return this.ratePermPeriod;
	}
	
	/**
	* Returns the min perm salart
	* @return min perm salary
	*/
	public float getRatePermValueMin() {
		return this.ratePermValueMin;
	}   
	
	/**
	* Returns the max perm salary
	* @return max perm salary
	*/
	public float getRatePermValueMax() {
		return this.ratePermValueMax;
	}
	
	/**
	* Returns the format of the photo
	* @return file format
	*/
	public PHOTO_FORMAT getPhotoFormat() {
		return this.photoFormat;
	}
	
	/**
	* Returns the bytes for the image file
	* @return bytes
	*/
	public byte[] getPhotoBytes() {
		return this.photoBytes;
	}
	
	/**
	* Returns a Builder for the PendingCandidateEntity class
	* @return Builder for the PendingCandidateEntity class
	*/
	public static PendingCandidateEntityBuilder builder() {
		return new PendingCandidateEntityBuilder();
	}
	
	/**
	* Builder for the PendingCandidateEntity class
	* @author K Parkings
	*/
	public static class PendingCandidateEntityBuilder {
	
		private UUID 			pendingCandidateId;
		private String 			firstname;
		private String			surname;
		private String 			email;
		private boolean	 		perm;
		private boolean 		freelance;
		private String 			introduction;
		private CURRENCY 		rateContractCurrency;
		private PERIOD 			rateContractPeriod;
		private float 			rateContractValueMin;   
		private float 			rateContractValueMax;
		private CURRENCY 		ratePermCurrency;
		private PERIOD 			ratePermPeriod;
		private float 			ratePermValueMin;   
		private float 			ratePermValueMax;
		private PHOTO_FORMAT 	photoFormat;      
		private byte[] 			photoBytes;
		
		/**
		* Sets the Unique Identifier of the Candidate
		* @param candidateId - UniqueId of the Candidate
		* @return Builder
		*/
		public PendingCandidateEntityBuilder pendingCandidateId(UUID pendingCandidateId) {
			this.pendingCandidateId = pendingCandidateId;
			return this;
		}
		
		/**
		* Sets the First name of the Candidate
		* @param firstname - Candidates first name
		* @return Builder
		*/
		public PendingCandidateEntityBuilder firstname(String firstname) {
			this.firstname = firstname;
			return this;
		}
		
		/**
		* Sets the Surname of the Candidate
		* @param surname - Candidates Surname
		* @return Builder
		*/
		public PendingCandidateEntityBuilder surname(String surname) {
			this.surname = surname;
			return this;
		}
		
		/**
		* Sets the Email address of the Candidate
		* @param email - Email address
		* @return Builder
		*/
		public PendingCandidateEntityBuilder email(String email) {
			this.email = email;
			return this;
		}
		
		/**
		* Sets whether the Candidate is looking for Perm roles
		* @param perm - Whether candidate is looking for Perm roles
		* @return Builder
		*/
		public PendingCandidateEntityBuilder perm(boolean perm) {
			this.perm = perm;
			return this;
		}
		
		/**
		* Sets whether the Candidate is looking for freelance roles
		* @param perm - Whether candidate is looking for freelance roles
		* @return Builder
		*/
		public PendingCandidateEntityBuilder freelance(boolean freelance) {
			this.freelance = freelance;
			return this;
		}
		
		/**
		* Sets the Candidates introduction about themselves
		* @param introduction - Candiadate's introduction
		* @return Builder
		*/
		public PendingCandidateEntityBuilder introduction(String introduction) {
			this.introduction = introduction;
			return this;
		}
		
		/**
		* Sets the currency to use 
		* @param rateContractCurrency - contract currency
		* @return Builder
		*/
		public PendingCandidateEntityBuilder rateContractCurrency(CURRENCY rateContractCurrency) {
			this.rateContractCurrency = rateContractCurrency;
			return this;
		}
		
		/**
		* Sets the Rate period
		* @param rateContractPeriod - Rate period for contract
		* @return Builder
		*/
		public PendingCandidateEntityBuilder rateContractPeriod(PERIOD rateContractPeriod) {
			this.rateContractPeriod = rateContractPeriod;
			return this;
		}
		
		/**
		* Sets the min contract rate
		* @param rateContractValueFrom - Min contract rate
		* @return Builder
		*/
		public PendingCandidateEntityBuilder rateContractValueMin(float rateContractValueMin) {
			this.rateContractValueMin = rateContractValueMin;
			return this;
		}   
		
		/**
		* Sets the max contract rate
		* @param rateContractValueTo - Max contract rate
		* @return Builder
		*/
		public PendingCandidateEntityBuilder rateContractValueMax(float rateContractValueMax) {
			this.rateContractValueMax = rateContractValueMax;
			return this;
		}
		
		/**
		* Sets the currency to use 
		* @param ratePermCurrency - perm currency
		* @return Builder
		*/
		public PendingCandidateEntityBuilder ratePermCurrency(CURRENCY ratePermCurrency) {
			this.ratePermCurrency = ratePermCurrency;
			return this;
		}
		
		/**
		* Sets the Rate period
		* @param rateContractPeriod - Contract rate period
		* @return Builder
		*/
		public PendingCandidateEntityBuilder ratePermPeriod(PERIOD ratePermPeriod) {
			this.ratePermPeriod = ratePermPeriod;
			return this;
		}
		
		/**
		* Sets the min perm salary
		* @param ratePermValueFrom - min perm salary
		* @return Builder
		*/
		public PendingCandidateEntityBuilder ratePermValueMin(float ratePermValueMin) {
			this.ratePermValueMin = ratePermValueMin;
			return this;
		}   
		
		/**
		* Sets the max perm salary
		* @param ratePermValueTo - max perm salary
		* @return Builder
		*/
		public PendingCandidateEntityBuilder ratePermValueMax(float ratePermValueMax) {
			this.ratePermValueMax = ratePermValueMax;
			return this;
		}
		
		/**
		* Sets the format of the Candidates Profile Photo
		* @param photoFormat - file format
		* @return Builder
		*/
		public PendingCandidateEntityBuilder photoFormat(PHOTO_FORMAT photoFormat) {
			this.photoFormat = photoFormat;
			return this;
		}
		
		/**
		* Sets the bytes of the Candidates profile photo
		* @param photoBytes - bytes of image
		* @return Builder
		*/
		public PendingCandidateEntityBuilder photoBytes(byte[] photoBytes) {
			this.photoBytes = photoBytes;
			return this;
		}
		
		/**
		* Returns a CandidateEntity instance initialized with 
		* the values in the Builder
		* @return Instance of CandidateEntity
		*/
		public PendingCandidateEntity build() {
			return new PendingCandidateEntity(this);
		}
		
	}
	
	/**
	* Converts a Domain representation of a Candidate Object to an Entity 
	* representation
	* @param candidate - Instance of Candidate to convert to an Entity
	* @return Entity representation of a Candidate object
	*/
	public static PendingCandidateEntity convertToEntity(PendingCandidate pendingCandidate) {
		
		
		
		return PendingCandidateEntity
					.builder()
						.pendingCandidateId(pendingCandidate.getPendingCandidateId())
						.firstname(pendingCandidate.getFirstname())
						.surname(pendingCandidate.getSurname())
						.email(pendingCandidate.getEmail())
						.freelance(pendingCandidate.isFreelance())
						.perm(pendingCandidate.isPerm())
						.introduction(pendingCandidate.getIntroduction())
						//.rateCurrency(pendingCandidate.getRate().isEmpty() 	? null 	: pendingCandidate.getRate().get().getCurrency())
						//.ratePeriod(pendingCandidate.getRate().isEmpty() 	? null 	: pendingCandidate.getRate().get().getPeriod())
						//.rateValue(pendingCandidate.getRate().isEmpty() 	? 0 	: pendingCandidate.getRate().get().getValue())
						.rateContractCurrency(pendingCandidate.getRateContract().isEmpty() ? null 	: pendingCandidate.getRateContract().get().getCurrency())
						.rateContractPeriod(pendingCandidate.getRateContract().isEmpty() ? null 	: pendingCandidate.getRateContract().get().getPeriod())
						.rateContractValueMin(pendingCandidate.getRateContract().isEmpty() ? 0f 	: pendingCandidate.getRateContract().get().getValueMin())
						.rateContractValueMax(pendingCandidate.getRateContract().isEmpty() ? 0f 	: pendingCandidate.getRateContract().get().getValueMax())
						.ratePermCurrency(pendingCandidate.getRatePerm().isEmpty() ? null 	: pendingCandidate.getRatePerm().get().getCurrency())
						.ratePermPeriod(pendingCandidate.getRatePerm().isEmpty() ? null 	: pendingCandidate.getRatePerm().get().getPeriod())
						.ratePermValueMin(pendingCandidate.getRatePerm().isEmpty() ? 0f 	: pendingCandidate.getRatePerm().get().getValueMin())
						.ratePermValueMax(pendingCandidate.getRatePerm().isEmpty() ? 0f 	: pendingCandidate.getRatePerm().get().getValueMax())
						.photoFormat(pendingCandidate.getPhoto().isEmpty() 	? null 	: pendingCandidate.getPhoto().get().getFormat())
						.photoBytes(pendingCandidate.getPhoto().isEmpty() 	? null 	: pendingCandidate.getPhoto().get().getImageBytes())
					.build();
		
	}

	/**
	* Converts a Entity representation of a Candidate Object to an Domain 
	* representation
	* @param candidate - Instance of Candidate to convert
	* @return Domain representation of a Candidate object
	*/
	public static PendingCandidate convertFromEntity(PendingCandidateEntity pendingCandidateEntity) {
		
		Rate 	rateContract 	= null;
		Rate 	ratePerm 		= null;
		Photo 	photo 			= null;
		
		if (pendingCandidateEntity.getRateContractCurrency() != null && pendingCandidateEntity.getRateContractPeriod() != null) {
			rateContract = new Rate(pendingCandidateEntity.getRateContractCurrency(), pendingCandidateEntity.getRateContractPeriod(), pendingCandidateEntity.getRateContractValueMin(), pendingCandidateEntity.getRateContractValueMax());
		}
		
		if (pendingCandidateEntity.getRatePermCurrency() != null && pendingCandidateEntity.getRatePermPeriod() != null) {
			ratePerm = new Rate(pendingCandidateEntity.getRatePermCurrency(), pendingCandidateEntity.getRatePermPeriod(), pendingCandidateEntity.getRatePermValueMin(), pendingCandidateEntity.getRatePermValueMax());
		}
		
		if (pendingCandidateEntity.getPhotoBytes() != null && pendingCandidateEntity.getPhotoFormat() != null) {
			photo = new Photo(pendingCandidateEntity.getPhotoBytes(), pendingCandidateEntity.getPhotoFormat());
		}
		
		return PendingCandidate
					.builder()
						.pendingCandidateId(pendingCandidateEntity.getPendingCandidateId())
						.firstname(pendingCandidateEntity.getFirstname())
						.surname(pendingCandidateEntity.getSurname())
						.email(pendingCandidateEntity.getEmail())
						.freelance(pendingCandidateEntity.isFreelance())
						.perm(pendingCandidateEntity.isPerm())
						.introduction(pendingCandidateEntity.getIntroduction())
						.rateContract(rateContract)
						.ratePerm(ratePerm)
						.photo(photo)
						.build();
		
	}
	
}