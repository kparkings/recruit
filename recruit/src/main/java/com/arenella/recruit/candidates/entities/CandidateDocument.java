package com.arenella.recruit.candidates.entities;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.arenella.recruit.candidates.beans.Candidate.CANDIDATE_TYPE;
import com.arenella.recruit.candidates.beans.Candidate.DAYS_ON_SITE;
import com.arenella.recruit.candidates.beans.Candidate.SECURITY_CLEARANCE_TYPE;
import com.arenella.recruit.candidates.beans.Candidate.Photo.PHOTO_FORMAT;
import com.arenella.recruit.candidates.beans.Candidate.Rate.CURRENCY;
import com.arenella.recruit.candidates.beans.Candidate.Rate.PERIOD;
import com.arenella.recruit.candidates.enums.COUNTRY;
import com.arenella.recruit.candidates.enums.FREELANCE;
import com.arenella.recruit.candidates.enums.FUNCTION;
import com.arenella.recruit.candidates.enums.PERM;


@Document(indexName="candidates")
public class CandidateDocument {

	@Id
	@Field(type = FieldType.Long, name="candidate_id")
	private Long		candidateId;
	
	@Field(type = FieldType.Keyword, name="firstname")
	private String 		firstname;
	
	@Field(type = FieldType.Keyword, name="surname")
	private String 		surname;
	
	@Field(type = FieldType.Keyword, name="email")
	private String 		email;
	
	@Field(type = FieldType.Keyword, name="role_sought")
	private String roleSought;
	
	@Field(type = FieldType.Keyword, name="function")
	@Enumerated(EnumType.STRING)
	private FUNCTION function;
	
	@Field(type = FieldType.Keyword, name="country")
	@Enumerated(EnumType.STRING)
	private COUNTRY 	country;
	
	@Field(type = FieldType.Keyword, name="city")
	private String 		city;
	
	@Field(type = FieldType.Keyword, name="perm")
	@Enumerated(EnumType.STRING)
	private PERM 		perm;
	
	@Field(type = FieldType.Keyword, name="freelance")
	@Enumerated(EnumType.STRING)
	private FREELANCE 	freelance;
	
	@Field(type = FieldType.Integer, name="years_experience")
	private int			yearsExperience;
	
	@Field(type = FieldType.Boolean, name="available")
	private boolean 	available;
	
	@Field(type = FieldType.Date, name="registered")
	private LocalDate 	registerd;
	
	@Field(type = FieldType.Date, name="last_availability_check")
	private LocalDate 	lastAvailabilityCheck;
	
	@Field(type = FieldType.Keyword, name="introduction")
	private String introduction;
	
	@Enumerated(EnumType.STRING)
	@Field(type = FieldType.Keyword, name="rate_contract_currency")
	private CURRENCY rateContractCurrency;
		
	@Enumerated(EnumType.STRING)
	@Field(type = FieldType.Keyword, name="rate_contract_period")
	private PERIOD rateContractPeriod;
	    
	@Field(type = FieldType.Float, name="rate_contract_value_min")
	private float rateContractValueMin;   
	
	@Field(type = FieldType.Float, name="rate_contract_value_max")
	private float rateContractValueMax;
	
	@Enumerated(EnumType.STRING)
	@Field(type = FieldType.Keyword, name="rate_perm_currency")
	private CURRENCY ratePermCurrency;
		
	@Enumerated(EnumType.STRING)
	@Field(type = FieldType.Keyword, name="rate_perm_period")
	private PERIOD ratePermPeriod;
	    
	@Field(type = FieldType.Float, name="rate_perm_value_min")
	private float ratePermValueMin;   
	
	@Field(type = FieldType.Float, name="rate_perm_value_max")
	private float ratePermValueMax;
	
	@Field(type = FieldType.Date, name="available_from_date")
	private LocalDate 		availableFromDate;
	
	@Field(type = FieldType.Keyword, name="owner_id")
	private String 			ownerId;
	
	@Field(type = FieldType.Keyword, name="candidate_type")
	@Enumerated(EnumType.STRING)
	private CANDIDATE_TYPE	candidateType;
	
	@Field(type = FieldType.Keyword, name="comments")
	private String comments;
	
	@Field(type = FieldType.Keyword, name="days_on_site")
	@Enumerated(EnumType.STRING)
	private DAYS_ON_SITE daysOnSite;
    
	@Enumerated(EnumType.STRING)
	@Field(type = FieldType.Keyword, name="photo_format")
    private PHOTO_FORMAT photoFormat;      
    
	@Field(type = FieldType.Binary, name="photo_bytes")
    private byte[] photoBytes;
    
	@Field(type = FieldType.Boolean, name="requires_sponsorship")
    private boolean requiresSponsorship;
    
	@Field(type = FieldType.Keyword, name="security_clearance_type")
    @Enumerated(EnumType.STRING)
    private SECURITY_CLEARANCE_TYPE securityClearance;
       
	@Field(type = FieldType.Keyword, name="skills")
	private Set<String> 			skills						= new LinkedHashSet<>();
	
	@OneToMany(mappedBy = "id.candidateId", cascade = CascadeType.ALL, orphanRemoval=true, fetch=FetchType.EAGER)
	private Set<LanguageDocument> 	languages					= new LinkedHashSet<>();
	
}


