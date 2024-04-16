package com.arenella.recruit.candidates.repos;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.poi.ss.formula.functions.LinearRegressionFunction.FUNCTION;
import org.springframework.data.elasticsearch.annotations.CountQuery;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.query.Param;

import com.arenella.recruit.candidates.beans.Candidate;
import com.arenella.recruit.candidates.entities.CandidateDocument;
import com.arenella.recruit.candidates.entities.CandidateRoleStatsView;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.Buckets;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import co.elastic.clients.elasticsearch._types.aggregations.TermsAggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch.core.SearchResponse;

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
	
	public default List<CandidateRoleStatsView> getCandidateRoleStats(ElasticsearchClient esClient){

		List<CandidateRoleStatsView> stats = new java.util.ArrayList<>();
		
		Aggregation aggregation = new Aggregation.Builder()
				 .terms(new TermsAggregation.Builder().field("function").build())
				 .build();
		
		co.elastic.clients.elasticsearch._types.query_dsl.Query query = MatchQuery.of(m -> m
		    .field("available")
		    .query(true)
		)._toQuery();

		SearchResponse<CandidateDocument> response;
		
		try {
			response = esClient.search(b -> b
			    .index("candidates")
			    .size(0)
			    .query(query) 
			    .aggregations("functions", aggregation),
			    CandidateDocument.class 
			);
			
			response.aggregations().get("functions").sterms().buckets().array().forEach(bucket ->
				stats.add(new CandidateRoleStatsView(com.arenella.recruit.candidates.enums.FUNCTION.valueOf(bucket.key().stringValue()), bucket.docCount()))
			);
			
		} catch (ElasticsearchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return stats;
	}
	


	
}
