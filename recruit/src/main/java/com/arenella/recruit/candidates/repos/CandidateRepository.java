package com.arenella.recruit.candidates.repos;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.annotations.CountQuery;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.query.Param;

import com.arenella.recruit.candidates.beans.Candidate;
import com.arenella.recruit.candidates.beans.CandidateFilterOptions;
import com.arenella.recruit.candidates.entities.CandidateDocument;
import com.arenella.recruit.candidates.entities.CandidateRoleStatsView;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.FieldSort;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.TermsAggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch.core.SearchResponse;


public interface CandidateRepository extends ElasticsearchRepository<CandidateDocument,Long>{

	//TODO: [KP] These will be case sensitive. Better to use fetchWithFilters
	//@Query("{\"bool\": {\"must\": [{\"match\": {\"email\": \"?0\"}}]}}")
	//public Set<CandidateDocument> fetchByEmail(String emailId);
	
	//@Query("{\"bool\": {\"must\": [{\"match\": {\"email\": \"?0\"}},{\\\"match\\\": {\\\"candidateId\\\": \\\"?1\\\"}}]}}")
	//public Set<CandidateDocument> fetchByEmail(String emailId, long candidateId);
	
	@CountQuery("{\"bool\": {\"must\": [{\"match\": {\"available\": \"?0\"}}]}}")
	public long getCountByAvailable(boolean available);
	
	/**
	* Returns whether or not Email is already used for the Candidate
	* @param email - email address to filter on
	* @return If Email already used for the Candidate
	*/
	default boolean emailInUse(String email, ElasticsearchClient esClient) {
		SearchResponse<CandidateDocument> response = this.fetchWithFilters(CandidateFilterOptions.builder().email(email).build(), esClient);
		return !response.hits().hits().isEmpty();
	}
	
	/**
	* Returns whether or not Email is already used by another Candidate
	* @param email - email address to filter on
	* @return If Email already used for another Candidate
	*/
	default boolean emailInUseByOtherUser(String email, long userId, ElasticsearchClient esClient) {
		//SearchResponse<CandidateDocument> response = this.fetchWithFilters(CandidateFilterOptions.builder().email(email).candidateIds(Set.of(String.valueOf(userId))).build(), esClient);
		//return !response.hits().hits().isEmpty();
		
		boolean matches = false;
		
		co.elastic.clients.elasticsearch._types.query_dsl.Query mustQry = MatchQuery.of(m -> m
			    .field("email")
			    .query(email)
			)._toQuery();
		
		co.elastic.clients.elasticsearch._types.query_dsl.Query mustNotQry = MatchQuery.of(m -> m
			    .field("candidateId")
			    .query(String.valueOf(userId))
			)._toQuery();
		
		co.elastic.clients.elasticsearch._types.query_dsl.Query boolQuery = BoolQuery.of(b -> b
				.must(List.of(mustQry))
				.mustNot(mustNotQry)
		)._toQuery();
		
		SearchResponse<CandidateDocument> response;
		
		try {
			response = esClient.search(b -> b
			    .index("candidates")
			    .size(1)
			    .query(boolQuery),
			    CandidateDocument.class 
			);
			
			matches = response.hits().hits().isEmpty();
			
		} catch (ElasticsearchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return !matches;
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
	
	/**
	* Returns all Candidates matching the filter options
	* @param filterOptions - options to filter Candidates on
	* @return Candidates matching the filter options
	*/
	//public default Set<Candidate> findAll(CandidateFilterOptions filterOptions, ElasticsearchClient esClient) {
		
	//	SearchResponse<CandidateDocument> response =  this.fetchWithFilters(filterOptions, esClient);
		
	//	return response
	//			.hits()
	//			.hits().
	//			stream().
	//			map(h -> CandidateDocument.convertFromDocument(h.source()))
	//			.collect(Collectors.toCollection(LinkedHashSet::new));
	//	
	//}
	
	public default Page<Candidate> findAll(CandidateFilterOptions filterOptions, ElasticsearchClient esClient, Pageable pageable) {
		
		SearchResponse<CandidateDocument> response =  this.fetchWithFilters(filterOptions, esClient);
		
		List<Candidate> hits = response
				.hits()
				.hits()
				.stream()
				.map(h -> CandidateDocument.convertFromDocument(h.source()))
				.collect(Collectors.toCollection(ArrayList::new));
		
		long totalHits = response.hits().total().value();
		int pageSize = pageable.getPageSize();
		long  pages = (long) Math.ceil(response.hits().total().value() / pageable.getPageSize());
		
		int totalPages = (int) (response.hits().total().value() == 0 ? 0 : Math.ceil(response.hits().total().value() / pageable.getPageSize()));
		
		return  new FUPage<Candidate>(hits, totalPages+1);
		
	}
	
	class FUPage<T> implements Page<T>{

		List<T> hits = new ArrayList<>();
		int totalPages = 0;
		
		public FUPage(List<T> hits, int totalPages) {
			this.hits = hits;
			this.totalPages = totalPages;
		}
		
		@Override
		public int getNumber() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int getSize() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int getNumberOfElements() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public List<T> getContent() {
			// TODO Auto-generated method stub
			return hits;
		}

		@Override
		public boolean hasContent() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public Sort getSort() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean isFirst() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isLast() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean hasNext() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean hasPrevious() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public Pageable nextPageable() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Pageable previousPageable() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Iterator<T> iterator() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getTotalPages() {
			// TODO Auto-generated method stub
			return this.totalPages;
		}

		@Override
		public long getTotalElements() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public <U> Page<U> map(Function<? super T, ? extends U> converter) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	
	public default Set<Candidate> findCandidates(CandidateFilterOptions filterOptions, ElasticsearchClient esClient){
		
		SearchResponse<CandidateDocument> response =  this.fetchWithFilters(filterOptions, esClient);
		
		return response
				.hits()
				.hits()
				.stream()
				.map(h -> CandidateDocument.convertFromDocument(h.source()))
				.collect(Collectors.toCollection(LinkedHashSet::new));
	}

	/**
	* Refer to the JpaSpecificationExecutor interface for details 
	*/
	
	default SearchResponse<CandidateDocument> fetchWithFilters(CandidateFilterOptions filterOptions, ElasticsearchClient esClient) {
		
		List<co.elastic.clients.elasticsearch._types.query_dsl.Query> mustQueries = new ArrayList<>();
		
		co.elastic.clients.elasticsearch._types.query_dsl.Query boolQuery = BoolQuery.of(m -> m
				.must(mustQueries)	
			)._toQuery();
		
		if (!filterOptions.getFirstname().isEmpty()) {
			mustQueries.add(MatchQuery.of(m -> m
					.field("firstname")
					.query(filterOptions.getFirstname().get())
			)._toQuery());
		}
		
		if (!filterOptions.getSurname().isEmpty()) {
			mustQueries.add(MatchQuery.of(m -> m
					.field("surname")
					.query(filterOptions.getSurname().get())
			)._toQuery());
		}
		
		
		if (!filterOptions.getEmail().isEmpty()) {
			mustQueries.add(MatchQuery.of(m -> m
					.field("email")
					.query(filterOptions.getEmail().get())
			)._toQuery());
		}
		
		//TODO:[KP] Originally accepts array of CanddiateIds. This impelmentation only 1 Cadidate
		if (!filterOptions.getCandidateIds().isEmpty()) {
			String candidateId = (String) filterOptions.getCandidateIds().toArray()[0];
			mustQueries.add(MatchQuery.of(m -> m
					.field("candidateId")
					.query(candidateId)
			)._toQuery());
		}
		
		//if (!this.filterOptions.getSkills().isEmpty()) {
			
		//	Expression<Collection<String>> skillValues = root.get("skills");
			
		//	this.filterOptions.getSkills().forEach(skill -> 
		//		predicates.add(criteriaBuilder.isMember(skill.toLowerCase().trim(), skillValues))
		//	);
			
		//}
		
		//if (!this.filterOptions.getDutch().isEmpty()) {
		//	Join<CandidateEntity,LanguageEntity> dutchJoin = root.join("languages", JoinType.INNER);
			
		//	dutchJoin.on(criteriaBuilder.and(
		//				criteriaBuilder.equal(dutchJoin.get("id").get("language"), LANGUAGE.DUTCH),
		//				criteriaBuilder.equal(dutchJoin.get("level"), this.filterOptions.getDutch().get())
		//			));
		//}
		
		//if (!this.filterOptions.getFrench().isEmpty()) {
		//	Join<CandidateEntity,LanguageEntity> frenchJoin = root.join("languages", JoinType.INNER);
			
		//	frenchJoin.on(criteriaBuilder.and(
		//				criteriaBuilder.equal(frenchJoin.get("id").get("language"), LANGUAGE.FRENCH),
		//				criteriaBuilder.equal(frenchJoin.get("level"), this.filterOptions.getFrench().get())
		//			));
		//}
		
		//if (!this.filterOptions.getEnglish().isEmpty()) {
		//	Join<CandidateEntity,LanguageEntity> frenchJoin = root.join("languages", JoinType.INNER);
			
		//	frenchJoin.on(criteriaBuilder.and(
		//				criteriaBuilder.equal(frenchJoin.get("id").get("language"), LANGUAGE.ENGLISH),
		//				criteriaBuilder.equal(frenchJoin.get("level"), this.filterOptions.getEnglish().get())
		//			));
		//}
		
		
		
		//if (!this.filterOptions.getCountries().isEmpty()) {
		//	Predicate countriesFltr 						= root.get("country").in(filterOptions.getCountries());
		//	predicates.add(countriesFltr);
		//}
		
		//if (!this.filterOptions.getFunctions().isEmpty()) {
		//	Predicate functionsFltr 						= root.get("function").in(filterOptions.getFunctions());
		//	predicates.add(functionsFltr);
		//}
		
		//if (!this.filterOptions.isFreelance().isEmpty() && this.filterOptions.isFreelance().get()) {
		//	Expression<String> freelanceExpression 			= root.get("freelance");
		//	predicates.add(criteriaBuilder.equal(freelanceExpression, 	FREELANCE.TRUE));
		//}
		
		//if (!this.filterOptions.isPerm().isEmpty() && this.filterOptions.isPerm().get()) {
		//	Expression<String> permExpression 				= root.get("perm");
		//	predicates.add(criteriaBuilder.equal(permExpression, 		PERM.TRUE));
		//}
		
		//if (this.filterOptions.getYearsExperienceGtEq() > 0 ) {
		//	Expression<Integer> yearsExperienceExpression 	= root.get("yearsExperience");
		//	predicates.add(criteriaBuilder.greaterThanOrEqualTo(yearsExperienceExpression, this.filterOptions.getYearsExperienceGtEq()));
		//}
		
		//if (this.filterOptions.getYearsExperienceLtEq() > 0 ) {
		//	Expression<Integer> yearsExperienceExpression 	= root.get("yearsExperience");
		//	predicates.add(criteriaBuilder.lessThanOrEqualTo(yearsExperienceExpression, this.filterOptions.getYearsExperienceLtEq()));
		//}
		
		//if (this.filterOptions.getDaysSinceLastAvailabilityCheck().isPresent()) {
		//	Expression<LocalDate>  daysSinceLastAvailabilityCheck	= root.get("lastAvailabilityCheck");
		//	LocalDate cutOff = LocalDate.now().minusDays(this.filterOptions.getDaysSinceLastAvailabilityCheck().get());
		//	predicates.add(criteriaBuilder.lessThanOrEqualTo(daysSinceLastAvailabilityCheck, cutOff));
		//}
		
		//if (this.filterOptions.getOwnerId().isPresent()) {
		//	Expression<String> ownerIdExpression 				= root.get("ownerId");
		//	predicates.add(criteriaBuilder.equal(ownerIdExpression, 		this.filterOptions.getOwnerId().get()));
		//}
		
		//if (this.filterOptions.getIncludeRequiresSponsorship().isEmpty() || this.filterOptions.getIncludeRequiresSponsorship().get() == false) {
		//	Expression<Boolean> includeRequiredSponsorship = root.get("requiresSponsorship");
		//	predicates.add(criteriaBuilder.notEqual(includeRequiredSponsorship, true));
		//}
		
		//Expression<String> sortExpression 				= root.get("candidateId");
		
		//if (this.filterOptions.getOrderAttribute().isPresent()) {
		//	sortExpression 				= root.get(filterOptions.getOrderAttribute().get());
		//	if (this.filterOptions.getOrder().get() == RESULT_ORDER.asc) {
		//		query.orderBy(criteriaBuilder.asc(sortExpression));
		//	} else {
		//		query.orderBy(criteriaBuilder.desc(sortExpression));
		//	}
		//	
		//} else {
		//	query.orderBy(criteriaBuilder.desc(sortExpression));
		//}
		
		
		
		////After CandidateId search so only Admin and paid subscription users can search on unavailable candidates
		//if (this.filterOptions.isAvailable().isEmpty()) {
		//	//Predicate isActiveFltr 						= root.get("available").in(true);
		//	//predicates.add(isActiveFltr);
		//} else {
		//	Predicate isActiveFltr 						= root.get("available").in(filterOptions.isAvailable().get().booleanValue());
		//	predicates.add(isActiveFltr);
		//}
		
		//return criteriaBuilder.and(predicates.stream().toArray(n -> new Predicate[n]));
		
		try {
			
			Aggregation aggregation = new Aggregation.Builder()
					 .terms(new TermsAggregation.Builder().field("function").build())
					 .build();
			
			return esClient.search(b -> b
				    .index("candidates")
				    .size(10000)
				    .sort(f -> f.field(FieldSort.of(a -> a.field("candidateId").order(SortOrder.Desc))))
				    // .searchAfter(null)
				    .query(boolQuery) ,
				    
				    
				    //.aggregations("functions", aggregation),
				    CandidateDocument.class 
				);
		} catch (ElasticsearchException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	


	
}
