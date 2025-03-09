package com.arenella.recruit.listings.repos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.arenella.recruit.listings.beans.ListingFilter;
import com.arenella.recruit.listings.beans.Listing.LISTING_AGE;
import com.arenella.recruit.listings.beans.Listing.listing_type;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermsQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermsQueryField;
import co.elastic.clients.elasticsearch._types.query_dsl.WildcardQuery;
import co.elastic.clients.json.JsonData;

/**
* Constructs a Query to send to Elasticsearch based upon the provided filters
* @author K Parkings
*/
public class ESFilteredListingSearchRequestBuilder {

	/**
	* Creates Query
	* @param filterOptions - Filters to based query on
	* @return Query based upon the filters
	*/
	public static co.elastic.clients.elasticsearch._types.query_dsl.Query createFilteredQuery(ListingFilter filterOptions) {
		
		List<co.elastic.clients.elasticsearch._types.query_dsl.Query> mustQueries 		= new ArrayList<>();
		List<co.elastic.clients.elasticsearch._types.query_dsl.Query> mustNotQueries 	= new ArrayList<>();
		
		co.elastic.clients.elasticsearch._types.query_dsl.Query boolQuery = BoolQuery.of(m -> m
				.must(mustQueries)	
				.mustNot(mustNotQueries)
			)._toQuery();
		
		filterOptions.getActive().ifPresent(value -> {
			mustQueries.add(MatchQuery.of(m -> m
					.field("active")
					.query(value)
			)._toQuery());
		});
		
		if (!filterOptions.getCountries().isEmpty()) {
			List<FieldValue> fieldValueList = filterOptions.getCountries().stream().map(c -> FieldValue.of(c.name())).toList();
			
			TermsQueryField termsQueryField = new TermsQueryField.Builder()
					.value(fieldValueList)
	                .build();
				 
			mustQueries.add(TermsQuery.of(m -> m
					.queryName("country")
					.field("country")
					.terms(termsQueryField)
					
			)._toQuery());
		}
		
		//if (!filterOptions.getGeoZones().isEmpty()) {
		//	
		//}
		
		filterOptions.getListingAge().ifPresent(value -> {
			
			LocalDateTime todayStart 		= LocalDateTime.now().minusHours(24);
			LocalDateTime thisWeekStart 	= todayStart.minusDays(7);
			LocalDateTime thisMonthStart 	= todayStart.minusDays(31);
			
			final Optional<LocalDate> cutOff; 
			
			if (value == LISTING_AGE.TODAY){
				cutOff =  Optional.of(todayStart.toLocalDate());
			} else if (value == LISTING_AGE.THIS_WEEK){
				cutOff = Optional.of(thisWeekStart.toLocalDate());
			} else if (value == LISTING_AGE.THIS_MONTH){
				cutOff = Optional.of(thisMonthStart.toLocalDate());
			} else {
				cutOff = Optional.empty();
			}
			
			cutOff.ifPresent(cutOffDate -> {
				mustQueries.add(RangeQuery.of(m -> m
						.queryName("created")
						.field("created")
						.gte(JsonData.of(Date.from(cutOffDate.atStartOfDay(ZoneId.systemDefault()).toInstant())))
						)._toQuery());
			});
			
			
		});
		
		filterOptions.getListingId().ifPresent(value -> {
			mustQueries.add(MatchQuery.of(m -> m
					.field("listingId")
					.query(value.toString())
					)._toQuery());
		});
		
		filterOptions.getOwnerId().ifPresent(value -> {
			mustQueries.add(MatchQuery.of(m -> m
					.field("ownerId")
					.query(value.toString())
					)._toQuery());
		});
		
		if (!filterOptions.getSearchTerms().isEmpty()) {
			 
			List<co.elastic.clients.elasticsearch._types.query_dsl.Query> shouldQueries 		= new ArrayList<>();
			
			filterOptions.getSearchTerms().stream().forEach(term -> {
				shouldQueries.add(new WildcardQuery.Builder().field("title").value("*"+term+"*").caseInsensitive(true).build()._toQuery());
			});
			
			mustQueries.add(BoolQuery.of(m -> m
			.queryName("atLeastOneTerm")
			.should(shouldQueries).minimumShouldMatch("1"))._toQuery());
			
		}
		
		filterOptions.getType().ifPresent(value -> {
		
			
			List<FieldValue> fieldValueList = new ArrayList<>();
			
			if (value == listing_type.BOTH) {
				fieldValueList.add(FieldValue.of((listing_type.PERM_ROLE.toString())));
				fieldValueList.add(FieldValue.of((listing_type.CONTRACT_ROLE.toString())));
			} else {
				fieldValueList.add(FieldValue.of((value.toString())));
			}
			
			TermsQueryField termsQueryField = new TermsQueryField.Builder()
					.value(fieldValueList)
					.build();
		 
			mustQueries.add(TermsQuery.of(m -> m
					.queryName("type")
					.field("type")
					.terms(termsQueryField)
					
			)._toQuery());
		
		});
		
		return boolQuery;
		
	}
	
}
