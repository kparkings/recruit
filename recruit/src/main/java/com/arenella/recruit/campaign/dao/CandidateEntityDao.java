package com.arenella.recruit.campaign.dao;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import com.arenella.recruit.campaigns.beans.Candidate;
import com.arenella.recruit.campaigns.entities.CandidateEntity;

@Repository
public interface CandidateEntityDao extends ListCrudRepository<CandidateEntity, String>{

	@Query("from CandidateEntity where type = 'EXTERNAL' and deletedFromSystem = false and lastDataRetentionConfirmation is null and created < :cuttoffDate")
	public Set<CandidateEntity> fetchExternalCandidateEntitiesThatMissedAuthorisationCuttoff(LocalDateTime cuttoffDate);
	
	@Query("from CandidateEntity where type = 'EXTERNAL' and deletedFromSystem = false and dataRetentionRenewalEmailSent < :cuttoffRenewalEmail and lastDataRetentionConfirmation < :cuttoffDate")
	public Set<CandidateEntity> fetchExternalCandidateEntitiesThatRequireAnnualRenewalAuthorizationEmail(LocalDateTime cuttoffRenewalEmail, LocalDateTime cuttoffDate);
	
	@Query("from CandidateEntity where type = 'EXTERNAL' and deletedFromSystem = false and lastDataRetentionConfirmation < :cuttoffDate and dataRetentionRenewalEmailSent < :cuttoffRenewalEmail")
	public Set<CandidateEntity> fetchExternalCandidateEntitiesThatMissedAuthorisationAnnualRenewalCuttoff(LocalDateTime cuttoffRenewalEmail, LocalDateTime cuttoffDate);
	
	/**
	* Saves a Candidate
	* @param candidate - Candidate to persist
	*/
	default void saveCandidate(Candidate candidate) {
		this.save(CandidateEntity.toEntity(candidate));
	}

	/**
	* If present returns the Candidate references by the ID
	* @param candidateId - Unique id of the Candidate
	* @return
	*/
	default Optional<Candidate> findCandidateById(String candidateId){
		return this.findById(candidateId).map(CandidateEntity::fromEntity);
	}

	/**
	* Returns External candidates that have not replied to the request to confirm their details can be stored in the System
	* @param cuttoffDate - Date that the request had to be sent before to be considered a non reply
	* @return Candidates that didn't reply to authorization request
	*/
	default Set<Candidate> fetchExternalCandidatesThatMissedAuthorisationCuttoff(LocalDateTime cuttoffDate){
		return this.fetchExternalCandidateEntitiesThatMissedAuthorisationCuttoff(cuttoffDate).stream().map(CandidateEntity::fromEntity).collect(Collectors.toSet());
	}
	
	/**
	* Returns External candidates requiring an email asking to external the authorization to keep them in 
	* the system as an External Candidate for a specific Campaign/Role
	* @param cuttoffRenewalEmail - We only want one email to be sent. If email sent after this cuttoff Candidate wont be included
	* @param cuttoff - If the candidates has provided permission after this cuttoff then they wont be included
	* @return
	*/
	default Set<Candidate> fetchExternalCandidatesThatRequireAnnualRenewalAuthorizationEmail(LocalDateTime cuttoffRenewalEmail, LocalDateTime cuttoff){
		return this.fetchExternalCandidateEntitiesThatRequireAnnualRenewalAuthorizationEmail(cuttoffRenewalEmail, cuttoff).stream().map(CandidateEntity::fromEntity).collect(Collectors.toSet());
	}
	
	
	/**
	* Returns External candidates not having replied to the  email asking to external the authorization to keep them in 
	* the system as an External Candidate for a specific Campaign/Role
	* @param cuttoff - If the Candidate hasn't provided authorization after this date then they are included in the results 
	* @param cuttoffRenewalEmail - If the candidate received an email asking for authorization before this date they are included 
	* @return
	*/
	default Set<Candidate> fetchExternalCandidatesThatMissedAuthorisationAnnualRenewalCuttoff(LocalDateTime cuttoffRenewalEmail, LocalDateTime cuttoff){
		return this.fetchExternalCandidateEntitiesThatMissedAuthorisationAnnualRenewalCuttoff(cuttoffRenewalEmail, cuttoff).stream().map(CandidateEntity::fromEntity).collect(Collectors.toSet());
	}
	
}
