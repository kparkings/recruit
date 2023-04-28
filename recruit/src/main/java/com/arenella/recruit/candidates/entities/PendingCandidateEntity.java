package com.arenella.recruit.candidates.entities;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

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
	@Column(name="rate_currency")
	private CURRENCY rateCurrency;
	
	@Enumerated(EnumType.STRING)
    @Column(name="rate_period")
    private PERIOD ratePeriod;
    
    @Column(name="rate_value")
    private float rateValue;      
    
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
		this.rateCurrency				= builder.rateCurrency;
		this.ratePeriod					= builder.ratePeriod;
		this.rateValue					= builder.rateValue;      
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
	* Returns the Currency the Candidate 
	* charges in
	* @return currency
	*/
	public CURRENCY getRateCurrency() {
		return this.rateCurrency;
	}
	
	/**
	* Returns the period the Candidate 
	* charges in
	* @return Period
	*/
	public PERIOD getRatePeriod() {
		return this.ratePeriod;
	}
	
	/**
	* Returns the value per period
	* the Candidate charges
	* @return value
	*/
	public float getRateValue() {
		return this.rateValue;
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
		private CURRENCY 		rateCurrency;
		private PERIOD 			ratePeriod;
		private float 			rateValue;      
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
		* Sets the Currency the Candidate charges in
		* @param rateCurrency - Currency
		* @return Builder
		*/
		public PendingCandidateEntityBuilder rateCurrency(CURRENCY rateCurrency) {
			this.rateCurrency = rateCurrency;
			return this;
		}
		
		/**
		* Sets the unit the Rate is in
		* @param ratePeriod - Unit of the Rate
		* @return Builder
		*/
		public PendingCandidateEntityBuilder ratePeriod(PERIOD ratePeriod) {
			this.ratePeriod = ratePeriod;
			return this;
		}
		
		/**
		* Sets the amount the Candidate charges per period
		* @param rateValue - amount per period
		* @return Builder
		*/
		public PendingCandidateEntityBuilder rateValue(float rateValue) {
			this.rateValue = rateValue;
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
						.rateCurrency(pendingCandidate.getRate().isEmpty() 	? null 	: pendingCandidate.getRate().get().getCurrency())
						.ratePeriod(pendingCandidate.getRate().isEmpty() 	? null 	: pendingCandidate.getRate().get().getPeriod())
						.rateValue(pendingCandidate.getRate().isEmpty() 	? 0 	: pendingCandidate.getRate().get().getValue())
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
		
		Rate 	rate 	= null;
		Photo 	photo 	= null;
		
		if (pendingCandidateEntity.rateCurrency != null && pendingCandidateEntity.ratePeriod != null) {
			rate = new Rate(pendingCandidateEntity.getRateCurrency(), pendingCandidateEntity.getRatePeriod(), pendingCandidateEntity.getRateValue());
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
						.rate(rate)
						.photo(photo)
						.build();
		
	}
	
}