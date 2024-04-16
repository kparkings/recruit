package com.arenella.recruit.candidates.repos;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.elasticsearch.annotations.CountQuery;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.query.Param;

import com.arenella.recruit.candidates.beans.Candidate;
import com.arenella.recruit.candidates.entities.CandidateDocument;

public interface CandidateRepository extends ElasticsearchRepository<CandidateDocument,Long>{

	@Query("{\"bool\": {\"must\": [{\"match\": {\"email\": \"?0\"}}]}}")
	public Set<CandidateDocument> fetchByEmail(String emailId);
	
	@Query("{\"bool\": {\"must\": [{\"match\": {\"email\": \"?0\"}},{\\\"match\\\": {\\\"candidateId\\\": \\\"?1\\\"}}]}}")
	public Set<CandidateDocument> fetchByEmail(String emailId, long candidateId);
	
	@CountQuery("{\"bool\": {\"must\": [{\"match\": {\"available\": \"?0\"}}]}}")
	public long getCountByAvailable(boolean available);
	
	/**
	* Returns whether or not Email is already used for the Candidate
	* @param email - email address to filter on
	* @return If Email already used for the Candidate
	*/
	default boolean emailInUse(String email) {
		return !this.fetchByEmail(email).isEmpty();
	}
	
	/**
	* Returns whether or not Email is already used by another Candidate
	* @param email - email address to filter on
	* @return If Email already used for another Candidate
	*/
	default boolean emailInUseByOtherUser(String email, long userId) {
		return !this.fetchByEmail(email, userId).isEmpty();
	}
	
	/**
	* If Candidate found return candidate. Otherwise empty
	* @param candidateId - Id to search for
	* @return Candidate matching id
	*/
	public default Optional<Candidate> findCandidateById(long candidateId){
		
		Optional<CandidateDocument> document = this.findById(candidateId);
		
		if (document.isEmpty()) {
			return Optional.empty();
		}
		
		return Optional.of(CandidateDocument.convertFromDocument(document.get()));
		
	}
	
	/**
	* Persists a Candidate
	* @param candidate - Candidate to persist
	*/
	default long saveCandidate(Candidate candidate) {
		return Long.valueOf(this.save(CandidateDocument.convertToDocument(candidate)).getCandidateId());
	}
	
	//@Query("from CandidateEntity c where c.available = true and c.registerd > :#{#since} order by c.registerd")
	@Query("{\"bool\": {\"must\": [{\"term\": {\"available\": true}},{\"range\": {\"registered\": {\"gte\":\"?0\"}}}]}}")
	public Set<CandidateDocument> findNewSinceLastDateRaw(@Param(value = "since") LocalDate since);
	
	
	/**
	* Returns all the new candidates registered after a given date
	* @param since - data after which candidates must have been registered
	* @return New candidates
	*/
	public default Set<Candidate> findNewSinceLastDate(LocalDate since){
		return this.findNewSinceLastDateRaw(since).stream().map(CandidateDocument::convertFromDocument).collect(Collectors.toSet());
	}
	
}
