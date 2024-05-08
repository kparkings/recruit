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
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.arenella.recruit.candidates.beans.Candidate;
import com.arenella.recruit.candidates.beans.CandidateFilterOptions;
import com.arenella.recruit.candidates.entities.CandidateDocument;
import com.arenella.recruit.candidates.entities.CandidateRoleStatsView;
import com.arenella.recruit.candidates.enums.RESULT_ORDER;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldSort;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.TermsAggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch.core.SearchResponse;

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
	
	//@Query("{\"bool\": {\"must\": [{\"term\": {\"available\": true}},{\"range\": {\"registered\": {\"gte\":\"?0\"}}}]}}")
	//public Set<CandidateDocument> findNewSinceLastDateRaw(@Param(value = "since") Date since);
	
	
	/**
	* Returns all the new candidates registered after a given date
	* @param since - data after which candidates must have been registered
	* @return New candidates
	*/
	public default Set<Candidate> findNewSinceLastDate(LocalDate since, ElasticsearchClient esClient){
		
		try {
			CandidateFilterOptions filterOptions = CandidateFilterOptions.builder().available(true).registeredAfter(since).build();
		
			return this.findCandidates(filterOptions, esClient);
		
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
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
		
		co.elastic.clients.elasticsearch._types.query_dsl.Query boolQuery = ESFilteredSearchRequestBuilder.createFilteredQuery(filterOptions);
		
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