package com.arenella.recruit.candidates.entities;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.arenella.recruit.candidates.beans.CandidateSearchEvent;
import com.arenella.recruit.candidates.beans.Language;
import com.arenella.recruit.candidates.enums.COUNTRY;
import com.arenella.recruit.candidates.enums.FUNCTION;

/**
* Entity representation of a CandidateSearchEvent
* @author K Parkings
*/
@Entity
@Table(schema="candidate", name="event_candidate_search")
public class CandidateSearchEventEntity {

	@Id
	@Column(name="event_id")
	private String			eventId;
	
	@Column(name="search_id")
	private String			searchId;

	@Column(name="user_id")
	private String			userId;
	
	@Column(name="years_experience_gt_eq")
	private Integer			yearsExperienceGtEq;
	
	@Column(name="years_experience_lt_eq")
	private Integer			yearsExperienceLtEq;
	
	private LocalDate 		timestamp				= LocalDate.now();
	
	@Enumerated(EnumType.STRING)
	private COUNTRY			country;
	
	@Enumerated(EnumType.STRING)
	private FUNCTION		function;
	
	private Boolean			freelance;
	private Boolean			perm;
	
	@Enumerated(EnumType.STRING)
	private Language.LEVEL 	dutch;
	
	@Enumerated(EnumType.STRING)
	private Language.LEVEL 	english;
	
	@Enumerated(EnumType.STRING)
	private Language.LEVEL 	french;
	
	private String 			skill;
	
	public CandidateSearchEventEntity() {}
	
	/**
	* Constructor based upon a builder
	* @param builder Contains initialization values
	*/
	public CandidateSearchEventEntity(CandidateSearchEventEntityBuilder builder) {

		this.eventId				= builder.eventId;
		this.searchId				= builder.searchId;
		this.timestamp				= builder.timestamp;
		this.userId					= builder.userId;
		this.country				= builder.country;
		this.function 				= builder.function;
		this.yearsExperienceGtEq 	= builder.yearsExperienceGtEq;
		this.yearsExperienceLtEq 	= builder.yearsExperienceLtEq;
		this.freelance 				= builder.freelance;
		this.perm 					= builder.perm;
		this.dutch 					= builder.dutch;
		this.english 				= builder.english;
		this.french 				= builder.french;
		this.skill 					= builder.skill;
		
	}

	/**
	* Returns Unique Id of the Event. A Search can have 
	* multiple Events. This is  the unique Id of the 
	* Event
	* @return Id of the Search
	*/
	public String getEventId() {
		return this.eventId;
	}
	
	/**
	* Returns Unique Id of the Search. A Search can have 
	* multiple Events. This is not the unique Id of the 
	* Event
	* @return Id of the Search
	*/
	public String getSearchId() {
		return this.searchId;
	}
	
	/**
	* Returns when the Search was made
	* @return Date of the Search
	*/
	public LocalDate getTimestamp() {
		return this.timestamp;
	}
	
	/**
	* Return the id of the User who performed the Saarch
	* @return Unique id of the User
	*/
	public String getUserId() {
		return this.userId;
	}
	
	/**
	* Returns the Country searched on
	* @return Country searched on
	*/
	public Optional<COUNTRY> getCountry() {
		return Optional.ofNullable(this.country);
	}
		
	/**
	* Returns the Function searched on
	* @return function searched on
	*/
	public Optional<FUNCTION> getFunction() {
		return Optional.ofNullable(this.function);
	}
	 
	/**
	* Returns the GtEq searched on 
	* @return GtEq searched on
	*/
	public Optional<Integer> getYearsExperienceGtEq() {
		return Optional.ofNullable(this.yearsExperienceGtEq);
	}
	
	/**
	* Returns the LtEq searched on 
	* @return LtEq searched on
	*/
	public Optional<Integer> getYearsExperienceLtEq() {
		return Optional.ofNullable(this.yearsExperienceLtEq);
	}
	
	/**
	* Returns whether freelance candidates were 
	* searched on
	* @return whether freelance candidates where searched on
	*/
	
	public Optional<Boolean> getFreelance() {
		return Optional.ofNullable(this.freelance);
	}
	
	/**
	* Returns whether perm candidates were 
	* searched on
	* @return whether perm candidates where searched on
	*/
	public Optional<Boolean> getPerm() {
		return Optional.ofNullable(this.perm);
	}
	
	/**
	* Returns what Dutch language level was searched on
	* @return Dutch language level;
	*/
	public Optional<Language.LEVEL> getDutch() {
		return Optional.ofNullable(this.dutch);
	}
	
	/**
	* Returns what English language level was searched on
	* @return English language level;
	*/
	public Optional<Language.LEVEL> getEnglish() {
		return Optional.ofNullable(this.english);
	}
	 
	/**
	* Returns what French language level was searched on
	* @return French language level;
	*/
	public Optional<Language.LEVEL> getFrench() {
		return Optional.ofNullable(this.french);
	}
	 
	/**
	* Returns the skill searched on
	* @return Skill searched on
	*/
	public Optional<String> getSkill() {
		return Optional.ofNullable(this.skill);
	}
	 	
	/**
	* Returns an instance of the Builder for the class
	* @return
	*/
	public static CandidateSearchEventEntityBuilder builder() {
		return new CandidateSearchEventEntityBuilder();
	}
	
	/**
	* Builder for the class 
	* @author K Parkings
	*/
	public static class CandidateSearchEventEntityBuilder {
		
		private String			eventId;
		private String			searchId;
		private LocalDate 		timestamp;
		private String			userId;
		private COUNTRY			country;
		private FUNCTION		function;
		private Integer			yearsExperienceGtEq;
		private Integer			yearsExperienceLtEq;
		private Boolean			freelance;
		private Boolean			perm;
		private Language.LEVEL 	dutch;
		private Language.LEVEL 	english;
		private Language.LEVEL 	french;
		private String 			skill;
		
		/**
		* Sets the Unique id of the Event. This is the id of 
		* Event. A single search can produce multiple events
		* @param eventId - Unique Id of the Event
		* @return Builder
		*/
		public CandidateSearchEventEntityBuilder eventId(String eventId) {
			this.eventId = eventId;
			return this;
		}
		
		/**
		* Sets the Unique id of the Search. This is not an id of the 
		* Event. A single search can produce multiple events
		* @param searchId - Unique Id of the Search
		* @return Builder
		*/
		public CandidateSearchEventEntityBuilder searchId(String searchId) {
			this.searchId = searchId;
			return this;
		}

		/**
		* Sets the timestamp of the Search. T
		* @param timestamp - when the event occurred
		* @return Builder
		*/
		public CandidateSearchEventEntityBuilder timestamp(LocalDate timestamp) {
			this.timestamp = timestamp;
			return this;
		}
		
		/**
		* Sets the Unique id of the User that performed the Search
		* @param userId - Unique Id of the user
		* @return Builder
		*/
		public CandidateSearchEventEntityBuilder userId(String userId) {
			this.userId = userId;
			return this;
		}
		
		/**
		* Sets the country that was searched on
		* @param country country searched on
		* @return Builder
		*/
		public CandidateSearchEventEntityBuilder country(COUNTRY country) {
			this.country = country;
			return this;
		}
		
		/**
		* Sets the Function Searched on
		* @param function Function searched on
		* @return Builder
		*/
		public CandidateSearchEventEntityBuilder function(FUNCTION function) {
			this.function = function;
			return this;
		}
		
		/**
		* Sets the GtEq number of years searched on
		* @param yearsExperienceGtEq GtEq number of years searched on
		* @return Builder
		*/
		public CandidateSearchEventEntityBuilder yearsExperienceGtEq(int yearsExperienceGtEq) {
			this.yearsExperienceGtEq = yearsExperienceGtEq;
			return this;
		}
		
		/**
		* Sets the LtEq number of years searched on
		* @param yearsExperienceLtEq LtEq number of years searched on
		* @return Builder
		*/
		public CandidateSearchEventEntityBuilder yearsExperienceLtEq(int yearsExperienceLtEq) {
			this.yearsExperienceLtEq = yearsExperienceLtEq;
			return this;
		}
		
		/**
		* Sets whether the search was for freelance candidates
		* @param freelance
		* @return Builder
		*/
		public CandidateSearchEventEntityBuilder freelance(boolean freelance) {
			this.freelance = freelance;
			return this;
		}
		
		/**
		* Sets whether the search was for perm candidates
		* @param perm whether the Search was for perm candidates
		* @return Builder
		*/
		public CandidateSearchEventEntityBuilder perm(boolean perm) {
			this.perm = perm;
			return this;
		}
		
		/**
		* Sets the Dutch language level searched on
		* @param dutch - Dutch language level searched on
		* @return Builder
		*/
		public CandidateSearchEventEntityBuilder dutch(Language.LEVEL dutch) {
			this.dutch = dutch;
			return this;
		}
		
		/**
		* Sets the English language level searched on
		* @param english - English language level searched on
		* @return Builder
		*/
		public CandidateSearchEventEntityBuilder english(Language.LEVEL english) {
			this.english = english;
			return this;
		}
		
		/**
		* Sets the French language level searched on
		* @param french - French language level searched on
		* @return Builder
		*/
		public CandidateSearchEventEntityBuilder french(Language.LEVEL french) {
			this.french = french;
			return this;
		}
		
		/**
		* Sets the skill that was searched on
		* @param skill Skill that was searched on
		* @return Builder
		*/
		public CandidateSearchEventEntityBuilder skill(String skill) {
			this.skill = skill;
			return this;
		}
		
		/**
		* Returns an instance of the CandidateSearchEvent class 
		* initialized with the values in the builder
		* @return instance of CandidateSearchEvent
		*/
		public CandidateSearchEventEntity build() {
			return new CandidateSearchEventEntity(this);
		} 
	}
	
	/**
	* 
	* @param entity
	* @return
	*/
	public static CandidateSearchEvent fromEntity(CandidateSearchEventEntity entity) {

		return CandidateSearchEvent
				.builder()
					.userId(entity.getUserId())
					.timestamp(entity.getTimestamp())
					.country(entity.getCountry().orElse(null))
					.dutch(entity.getDutch().orElse(null))
					.english(entity.getEnglish().orElse(null))
					.french(entity.getFrench().orElse(null))
					.perm(entity.getPerm().orElse(null))
					.searchId(UUID.fromString(entity.getSearchId()))
					.eventId(UUID.fromString(entity.getEventId()))
					.skill(entity.getSkill().orElse(null))
					.freelance(entity.getFreelance().orElse(null))
					.function(entity.getFunction().orElse(null))
					.yearsExperienceGtEq(entity.getYearsExperienceGtEq().orElse(null))
					.yearsExperienceLtEq(entity.getYearsExperienceLtEq().orElse(null))
				.build();
		
	}
	
	/**
	* 
	* @param event
	* @return
	*/
	public static CandidateSearchEventEntity toEntity(CandidateSearchEvent event) {
		
		return CandidateSearchEventEntity
									.builder()
										.userId(event.getUserId())
										.timestamp(event.getTimestamp())
										.country(event.getCountry().orElse(null))
										.dutch(event.getDutch().orElse(null))
										.english(event.getEnglish().orElse(null))
										.french(event.getFrench().orElse(null))
										.perm(event.getPerm().orElse(null))
										.searchId(event.getSearchId().toString())
										.eventId(event.getEventId().toString())
										.skill(event.getSkill().orElse(null))
										.freelance(event.getFreelance().orElse(null))
										.function(event.getFunction().orElse(null))
										.yearsExperienceGtEq(event.getYearsExperienceGtEq().orElse(null))
										.yearsExperienceLtEq(event.getYearsExperienceLtEq().orElse(null))
									.build();
		
	}

}