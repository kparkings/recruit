package com.arenella.recruit.candidates.repos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.arenella.recruit.candidates.beans.CandidateFilterOptions;
import com.arenella.recruit.candidates.beans.Language.LANGUAGE;
import com.arenella.recruit.candidates.enums.FREELANCE;
import com.arenella.recruit.candidates.enums.PERM;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermsQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermsQueryField;
import co.elastic.clients.json.JsonData;

/**
* Constructs a Query to send to Elasticsearch based upon the provided filters
* @author K Parkings
*/
public class ESFilteredSearchRequestBuilder {

	/**
	* Creates Query
	* @param filterOptions - Filters to based query on
	* @return Query based upon the filters
	*/
	public static co.elastic.clients.elasticsearch._types.query_dsl.Query createFilteredQuery(CandidateFilterOptions filterOptions) {
		
		List<co.elastic.clients.elasticsearch._types.query_dsl.Query> mustQueries 		= new ArrayList<>();
		List<co.elastic.clients.elasticsearch._types.query_dsl.Query> mustNotQueries 	= new ArrayList<>();
		
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
				.queryName("dutch")
				.must(List.of(
						MatchQuery.of(m1 -> m1.field("language").query(LANGUAGE.DUTCH.toString()))._toQuery(),
						MatchQuery.of(m2 -> m2.field("language").query(filterOptions.getDutch().get().toString()))._toQuery())
			))._toQuery());
		}
		
		if (!filterOptions.getFrench().isEmpty()) {
			mustQueries.add(BoolQuery.of(m -> m
					.queryName("french")
					.must(List.of(
							MatchQuery.of(m1 -> m1.field("language").query(LANGUAGE.FRENCH.toString()))._toQuery(),
							MatchQuery.of(m2 -> m2.field("language").query(filterOptions.getFrench().get().toString()))._toQuery())
				))._toQuery());
		}
		
		if (!filterOptions.getEnglish().isEmpty()) {
			mustQueries.add(BoolQuery.of(m -> m
					.queryName("english")
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
					.queryName("yearsExperienceGte")
					.field("yearsExperience")
					.gte(JsonData.of(filterOptions.getYearsExperienceGtEq()))
			)._toQuery());
		}
		
		if (filterOptions.getYearsExperienceLtEq() > 0 ) {
			mustQueries.add(RangeQuery.of(m -> m
					.queryName("yearsExperienceLte")
					.field("yearsExperience")
					.lte(JsonData.of(filterOptions.getYearsExperienceLtEq()))
			)._toQuery());
		}
		
		if (filterOptions.getDaysSinceLastAvailabilityCheck().isPresent()) {
			LocalDate cutOff = LocalDate.now().minusDays(filterOptions.getDaysSinceLastAvailabilityCheck().get());
			mustQueries.add(RangeQuery.of(m -> m
					.queryName("lastAvailabilityCheck")
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
		
		if (filterOptions.isAvailable().isEmpty()) {
		} else {
			mustQueries.add(MatchQuery.of(m -> m
					.field("available")
					.query(filterOptions.isAvailable().get().booleanValue())
			)._toQuery());
		}
		
		return boolQuery;
		
	}
	
}
