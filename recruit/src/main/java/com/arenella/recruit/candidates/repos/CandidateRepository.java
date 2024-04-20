package com.arenella.recruit.candidates.repos;
	
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.poi.openxml4j.exceptions.InvalidOperationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.annotations.CountQuery;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.query.Param;

import com.arenella.recruit.candidates.beans.Candidate;
import com.arenella.recruit.candidates.beans.CandidateFilterOptions;
import com.arenella.recruit.candidates.beans.Language.LANGUAGE;
import com.arenella.recruit.candidates.entities.CandidateDocument;
import com.arenella.recruit.candidates.entities.CandidateRoleStatsView;
import com.arenella.recruit.candidates.enums.FREELANCE;
import com.arenella.recruit.candidates.enums.PERM;
import com.arenella.recruit.candidates.enums.RESULT_ORDER;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldSort;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.TermsAggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermsQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermsQueryField;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.json.JsonData;

/**
* Repository for Cadidates Data. Elasticsearch implementation
* @author K Parkings
*/
public interface CandidateRepository extends ElasticsearchRepository<CandidateDocument,Long>{

	@CountQuery("{\"bool\": {\"must\": [{\"match\": {\"available\": \"?0\"}}]}}")
	public long getCountByAvailable(boolean available);
	
	/**
	* Returns whether or not Email is already used for the Candidate
	* @param email - email address to filter on
	* @return If Email already used for the Candidate
	*/
	default boolean emailInUse(String email, ElasticsearchClient esClient) throws Exception{
		SearchResponse<CandidateDocument> response = this.fetchWithFilters(CandidateFilterOptions.builder().email(email).build(), esClient, 1);
		return !response.hits().hits().isEmpty();
	}
	
	/**
	* Returns whether or not Email is already used by another Candidate
	* @param email - email address to filter on
	* @return If Email already used for another Candidate
	*/
	default boolean emailInUseByOtherUser(String email, long userId, ElasticsearchClient esClient) throws Exception{
		
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
		
		return !esClient.search(b -> b
			    .index("candidates")
			    .size(1)
			    .query(boolQuery),
			    CandidateDocument.class 
			).hits().hits().isEmpty();
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
	
	public default List<CandidateRoleStatsView> getCandidateRoleStats(ElasticsearchClient esClient) throws Exception{

		List<CandidateRoleStatsView> stats = new java.util.ArrayList<>();
		
		Aggregation aggregation = new Aggregation.Builder()
				 .terms(new TermsAggregation.Builder().field("function").build())
				 .build();
		
		co.elastic.clients.elasticsearch._types.query_dsl.Query query = MatchQuery.of(m -> m
		    .field("available")
		    .query(true)
		)._toQuery();

		esClient.search(b -> b
			    .index("candidates")
			    .size(0)
			    .query(query) 
			    .aggregations("functions", aggregation),
			    CandidateDocument.class 
			).aggregations().get("functions").sterms().buckets().array().forEach(bucket ->
			stats.add(new CandidateRoleStatsView(com.arenella.recruit.candidates.enums.FUNCTION.valueOf(bucket.key().stringValue()), bucket.docCount()))
		);
			
		return stats;
	}
	
	/**
	* Returns a page of filtered Candidate results
	* @param filterOptions 	- Filters to apply
	* @param esClient		- Elasticsearch client 
	* @param pageable		- Pagination information
	* @return Page of matching results
	*/
	public default Page<Candidate> findAll(CandidateFilterOptions filterOptions, ElasticsearchClient esClient, Pageable pageable) throws Exception{
		
		SearchResponse<CandidateDocument> response =  this.fetchWithFilters(filterOptions, esClient, pageable.getPageSize());
		
		List<Candidate> hits = response
				.hits()
				.hits()
				.stream()
				.map(h -> CandidateDocument.convertFromDocument(h.source()))
				.collect(Collectors.toCollection(ArrayList::new));
		
		int totalPages = (int) (response.hits().total().value() == 0 ? 0 : response.hits().total().value() / pageable.getPageSize());
		
		return  new FUPage<>(hits, totalPages+1);
		
	}
	
	/**
	* 
	* @param filterOptions
	* @param esClient
	* @return
	*/
	public default Set<Candidate> findCandidates(CandidateFilterOptions filterOptions, ElasticsearchClient esClient) throws Exception{
		
		SearchResponse<CandidateDocument> response =  this.fetchWithFilters(filterOptions, esClient, 10000);
		
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
	
	default SearchResponse<CandidateDocument> fetchWithFilters(CandidateFilterOptions filterOptions, ElasticsearchClient esClient, int pageSize) throws Exception{
		
		List<co.elastic.clients.elasticsearch._types.query_dsl.Query> mustQueries = new ArrayList<>();
		List<co.elastic.clients.elasticsearch._types.query_dsl.Query> mustNotQueries = new ArrayList<>();
		
		co.elastic.clients.elasticsearch._types.query_dsl.Query boolQuery = BoolQuery.of(m -> m
				.must(mustQueries)	
				.mustNot(mustNotQueries)
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
		
		if (!filterOptions.getSkills().isEmpty()) {
			List<FieldValue> fieldValueList = filterOptions.getSkills().stream().map(FieldValue::of).toList();
			 
			 TermsQueryField termsQueryField = new TermsQueryField.Builder()
                   .value(fieldValueList)
                   .build();
			 
			mustQueries.add(TermsQuery.of(m -> m
					.field("skills")
					.terms(termsQueryField)
					
			)._toQuery());
			
		}
		
		if (!filterOptions.getDutch().isEmpty()) {
			mustQueries.add(BoolQuery.of(m -> m
				.must(List.of(
						MatchQuery.of(m1 -> m1.field("language").query(LANGUAGE.DUTCH.toString()))._toQuery(),
						MatchQuery.of(m2 -> m2.field("language").query(filterOptions.getDutch().get().toString()))._toQuery())
			))._toQuery());
		}
		
		if (!filterOptions.getFrench().isEmpty()) {
			mustQueries.add(BoolQuery.of(m -> m
					.must(List.of(
							MatchQuery.of(m1 -> m1.field("language").query(LANGUAGE.FRENCH.toString()))._toQuery(),
							MatchQuery.of(m2 -> m2.field("language").query(filterOptions.getFrench().get().toString()))._toQuery())
				))._toQuery());
		}
		
		if (!filterOptions.getEnglish().isEmpty()) {
			mustQueries.add(BoolQuery.of(m -> m
					.must(List.of(
							MatchQuery.of(m1 -> m1.field("language").query(LANGUAGE.ENGLISH.toString()))._toQuery(),
							MatchQuery.of(m2 -> m2.field("language").query(filterOptions.getEnglish().get().toString()))._toQuery())
				))._toQuery());
		}
		
		if (!filterOptions.getCountries().isEmpty()) {
			
			 List<FieldValue> fieldValueList = filterOptions.getCountries().stream().map(c -> FieldValue.of(c.name())).toList();
			 
			 TermsQueryField termsQueryField = new TermsQueryField.Builder()
                     .value(fieldValueList)
                     .build();
			 
			mustQueries.add(TermsQuery.of(m -> m
					.field("country")
					.terms(termsQueryField)
					
			)._toQuery());
		}
		
		if (!filterOptions.getFunctions().isEmpty()) {
			List<FieldValue> fieldValueList = filterOptions.getFunctions().stream().map(c -> FieldValue.of(c.name())).toList();
			 
			 TermsQueryField termsQueryField = new TermsQueryField.Builder()
                    .value(fieldValueList)
                    .build();
			 
			mustQueries.add(TermsQuery.of(m -> m
					.field("function")
					.terms(termsQueryField)
					
			)._toQuery());
		}
		
		if (!filterOptions.isFreelance().isEmpty() && filterOptions.isFreelance().get()) {
			mustQueries.add(MatchQuery.of(m -> m
					.field("freelance")
					.query(FREELANCE.TRUE.toString())
			)._toQuery());
		}
		
		if (!filterOptions.isPerm().isEmpty() && filterOptions.isPerm().get()) {
			mustQueries.add(MatchQuery.of(m -> m
					.field("perm")
					.query(PERM.TRUE.toString())
			)._toQuery());
		}
		
		if (filterOptions.getYearsExperienceGtEq() > 0 ) {
			mustQueries.add(RangeQuery.of(m -> m
					.field("yearsExperience")
					.gte(JsonData.of(filterOptions.getYearsExperienceGtEq()))
			)._toQuery());
		}
		
		if (filterOptions.getYearsExperienceLtEq() > 0 ) {
			mustQueries.add(RangeQuery.of(m -> m
					.field("yearsExperience")
					.lte(JsonData.of(filterOptions.getYearsExperienceLtEq()))
			)._toQuery());
		}
		
		if (filterOptions.getDaysSinceLastAvailabilityCheck().isPresent()) {
			LocalDate cutOff = LocalDate.now().minusDays(filterOptions.getDaysSinceLastAvailabilityCheck().get());
			mustQueries.add(RangeQuery.of(m -> m
					.field("lastAvailabilityCheck")
					.lte(JsonData.of(cutOff))
			)._toQuery());
		}
		
		if (filterOptions.getOwnerId().isPresent()) {
			mustQueries.add(MatchQuery.of(m -> m
					.field("ownerId")
					.query(filterOptions.getOwnerId().get())
			)._toQuery());
		}
		
		if (filterOptions.getIncludeRequiresSponsorship().isEmpty() || filterOptions.getIncludeRequiresSponsorship().get() == false) {
			mustNotQueries.add(MatchQuery.of(m -> m
					.field("requiresSponsorship")
					.query(true)
			)._toQuery());
		}
		
		SortOrder sortOrder;
		String sortField;
		if (filterOptions.getOrderAttribute().isPresent()) {
			sortField = filterOptions.getOrderAttribute().get();
		} else {
			sortField = "candidateId";
		}
		
		if (filterOptions.getOrder().isPresent() && filterOptions.getOrder().get() == RESULT_ORDER.asc) {
			sortOrder = SortOrder.Asc;
		} else {
			sortOrder = SortOrder.Desc;
		}
		
		if (filterOptions.isAvailable().isEmpty()) {
		} else {
			mustQueries.add(MatchQuery.of(m -> m
					.field("available")
					.query(filterOptions.isAvailable().get().booleanValue())
			)._toQuery());
		}
			
		return esClient.search(b -> b
			    .index("candidates")
			    .size(pageSize)
			    .sort(f -> f.field(FieldSort.of(a -> a.field(sortField).order(sortOrder))))
			    .query(boolQuery) ,
			    CandidateDocument.class);
	
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
			throw new InvalidOperationException("");
		}

		@Override
		public int getSize() {
			throw new InvalidOperationException("");
		}

		@Override
		public int getNumberOfElements() {
			return 0;
		}

		@Override
		public List<T> getContent() {
			return hits;
		}

		@Override
		public boolean hasContent() {
			throw new InvalidOperationException("");
		}

		@Override
		public Sort getSort() {
			throw new InvalidOperationException("");
		}

		@Override
		public boolean isFirst() {
			throw new InvalidOperationException("");
		}

		@Override
		public boolean isLast() {
			throw new InvalidOperationException("");
		}

		@Override
		public boolean hasNext() {
			throw new InvalidOperationException("");
		}

		@Override
		public boolean hasPrevious() {
			throw new InvalidOperationException("");
		}

		@Override
		public Pageable nextPageable() {
			throw new InvalidOperationException("");
		}

		@Override
		public Pageable previousPageable() {
			throw new InvalidOperationException("");
		}

		@Override
		public Iterator<T> iterator() {
			throw new InvalidOperationException("");
		}

		@Override
		public int getTotalPages() {
			return this.totalPages;
		}

		@Override
		public long getTotalElements() {
			throw new InvalidOperationException("");
		}

		@Override
		public <U> Page<U> map(Function<? super T, ? extends U> converter) {
			throw new InvalidOperationException("");
		}
		
	}
	
}