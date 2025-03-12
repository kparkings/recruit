package com.arenella.recruit.listings.repos;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.arenella.recruit.listings.beans.Listing;
import com.arenella.recruit.listings.beans.ListingFilter;
import com.arenella.recruit.listings.repos.entities.ListingDocument;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldSort;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.SearchResponse;


public interface ListingRepository extends ElasticsearchRepository<ListingDocument,UUID>{

	/**
	* Returns page of results for ListingEntity objects matching the filter values 
	* @param filterOptions - Filter values to apply to the search results
	* @param pageable - Pagination information
	* @return results
	*/
	public default Page<Listing> findAll(ListingFilter filterOptions, ElasticsearchClient esClient, Pageable pageable) {
	
		try {
		
			SearchResponse<ListingDocument> response =  this.fetchWithFilters(filterOptions, esClient, pageable.getPageNumber(), pageable.getPageSize());
		
			LinkedList<Listing> listings = response
				.hits()
				.hits()
				.stream()
				.map(h -> ListingDocument.fromEntity(h.source()))
				.collect(Collectors.toCollection(LinkedList::new));
		
			return new PageImpl<Listing>(listings);
			
		}catch(Exception e) {
			throw new RuntimeException();
		}
		
	}
	
	/**
	* Returns all listing matching the filters 
	* @param filterOptions - Filter values to apply to the search results
	* @param pageable - Pagination information
	* @return results
	*/
	public default Set<Listing> findAllListings(ListingFilter filterOptions, ElasticsearchClient esClient) {
		
		try {
	
			SearchResponse<ListingDocument> response =  this.fetchWithFilters(filterOptions, esClient, 0, 10000);
		
			return response
				.hits()
				.hits()
				.stream()
				.map(h -> ListingDocument.fromEntity(h.source()))
				.collect(Collectors.toCollection(LinkedHashSet::new));
		}catch(Exception e) {
			throw new RuntimeException();
		}
	}
	
	/**
	* Converts domain to entity and persists
	* @param listings
	*/
	public default void saveListings(Set<Listing> listings) {
		this.saveAll(listings.stream().map(l -> ListingDocument.toEntity(l)).collect(Collectors.toSet()));
	}
	
	/**
	* Returns listing matching listingId if present
	* @param listingId - Unique id of the Listing
	*/
	public default Optional<Listing> findListingById(UUID listingId) {
	
		Optional<ListingDocument> entity = this.findById(listingId);
		
		if (entity.isEmpty()) {
			return Optional.empty();
		}
	
		return entity.stream().map(ListingDocument::fromEntity).findAny();
		
	}
	
	private int computeFromRecord(int pageNumber, int maxRecordsInPage) {
		
		if (pageNumber == 0) {
			return 0;
		}
		
		return ((pageNumber * maxRecordsInPage) + maxRecordsInPage ) - (maxRecordsInPage + 1)+1;
	}
	
	default SearchResponse<ListingDocument> fetchWithFilters(ListingFilter filterOptions, ElasticsearchClient esClient, int pageNumber, int maxRecordsInPage) throws Exception{
		
		co.elastic.clients.elasticsearch._types.query_dsl.Query boolQuery = ESFilteredListingSearchRequestBuilder.createFilteredQuery(filterOptions);
		
		SortOrder sortOrder = SortOrder.Desc;
		String sortField = "created";
		
		return esClient.search(b -> b
			    .index("listings")
			    .sort(f -> f.field(FieldSort.of(a -> a.field(sortField).order(sortOrder))))
			    .from(computeFromRecord(pageNumber, (maxRecordsInPage-1)))
			    .size(maxRecordsInPage)
			    .query(boolQuery) ,
			    ListingDocument.class);
	}
	
}