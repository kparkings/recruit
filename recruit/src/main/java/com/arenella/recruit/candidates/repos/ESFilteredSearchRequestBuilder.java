package com.arenella.recruit.candidates.repos;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.arenella.recruit.candidates.beans.CandidateFilterOptions;
import com.arenella.recruit.candidates.enums.FREELANCE;
import com.arenella.recruit.candidates.enums.PERM;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.GeoDistanceType;
import co.elastic.clients.elasticsearch._types.GeoLocation;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.ExistsQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.GeoDistanceQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.NestedQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermsQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermsQueryField;
import co.elastic.clients.json.JsonData;

/**
* Constructs a Query to send to Elasticsearch based upon the provided filters
* @author K Parkings
*/
public class ESFilteredSearchRequestBuilder {

	public static final DateTimeFormatter FMT_DATE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
	
	/**
	* Hide default constructor 
	*/
	private ESFilteredSearchRequestBuilder() {
		
	}
	
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
		
		if (!filterOptions.getSecurityLevels().isEmpty()) {
			
			List<co.elastic.clients.elasticsearch._types.query_dsl.Query> scLevelQueries = new ArrayList<>();
			
			filterOptions.getSecurityLevels().stream().forEach(l -> 
				scLevelQueries.add(BoolQuery.of(m -> m
						.must(List.of(
								MatchQuery.of(m1 -> m1.field("securityClearance").query(l.toString()))._toQuery()))
					)._toQuery())
			);
			
			mustQueries.add(BoolQuery.of(m -> m
					.queryName("scLevelQueries")
					.should(scLevelQueries).minimumShouldMatch("1"))._toQuery());
			
		}
		
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
		
		if (!filterOptions.getLastAccountRefreshLtEq().isEmpty()) {
			LocalDate cutOff = filterOptions.getLastAccountRefreshLtEq().get();
			//mustQueries.add(RangeQuery.of(m -> m
			//		.queryName("lastAccountRefresh")
			//		.field("lastAccountRefresh")
			//		.lte(JsonData.of(Date.from(cutOff.atStartOfDay(ZoneId.systemDefault()).toInstant())))
			//)._toQuery());
			
			mustQueries.add(RangeQuery.of(m -> m
					.date(n -> n
					.queryName("lastAccountRefresh")
					.field("lastAccountRefresh")
					.lte(cutOff.atStartOfDay(ZoneId.systemDefault()).format(FMT_DATE_TIME)))
			)._toQuery());
			
		}
		
		if (!filterOptions.getGeoPosFilter().isEmpty()) {
			
			GeoLocation loc = new GeoLocation.Builder()
            .latlon(l -> l
                    .lat(filterOptions.getGeoPosFilter().get().lat())
                    .lon(filterOptions.getGeoPosFilter().get().lon())
            ).build();
            
			GeoDistanceQuery gdq = QueryBuilders
			            .geoDistance()
			            .distanceType(GeoDistanceType.Plane)
			            .location(loc)
			            .field(filterOptions.getGeoPosFilter().get().field())
			            .distance(filterOptions.getGeoPosFilter().get().distance()+"km").build();
			
			mustQueries.add(gdq._toQuery());
		
		}
		
		if (!filterOptions.getLastAccountRefreshMissing().isEmpty()) {
			
			mustNotQueries.add(ExistsQuery.of(m -> m
					.queryName("lastAccountRefreshMissing")
					.field("lastAccountRefresh")
			)._toQuery());
		}
		
		//TODO:[KP] Originally accepts array of CanddiateIds. This impelmentation only 1 Cadidate
		if (!filterOptions.getCandidateIds().isEmpty()) {
			//String candidateId = (String) filterOptions.getCandidateIds().toArray()[0];
			//mustQueries.add(MatchQuery.of(m -> m
			//		.field("candidateId")
			//		.query(candidateId)
			//)._toQuery());
			
			List<FieldValue> fieldValueList = filterOptions.getCandidateIds().stream().map(c -> FieldValue.of(c)).toList();
			 
			 TermsQueryField termsQueryField = new TermsQueryField.Builder()
                    .value(fieldValueList)
                    .build();
			 
			mustQueries.add(TermsQuery.of(m -> m
					.queryName("candidateId")
					.field("candidateId")
					.terms(termsQueryField)
					
			)._toQuery());
			
		}
		
		if (!filterOptions.getSkills().isEmpty()) {
			List<FieldValue> fieldValueList = filterOptions.getSkills().stream().map(FieldValue::of).toList();
			 
			 TermsQueryField termsQueryField = new TermsQueryField.Builder()
				   .value(fieldValueList)
                   .build();
			 
			mustQueries.add(TermsQuery.of(m -> m
					.queryName("skills")
					.field("skills")
					.terms(termsQueryField)
					
			)._toQuery());
			
		}
		
		if (!filterOptions.getLanguages().isEmpty()) {
			
			
			List<String> 		languages 		= filterOptions.getLanguages().stream().map(Object::toString).map(String::toUpperCase).collect(Collectors.toList());
			
			/**
			* For speed. Almost all candidates speak English. Therefore we filter on the remaining languages which in most
			* cases will also return English. The accuracy check in the code above will still be able to take into account
			* the English language selection.
			* 
			* The only risk is if the filters have English and something else and the only language the candidate is English
			* they will not appear in the results but this should affect so few searches that the performance gain outweights 
			* the negatives. 
			*/
			if (filterOptions.getLanguages().size() >1 ) {
				languages = languages.stream().filter(l -> !l.equals("ENGLSH")).collect(Collectors.toList());
			}
			
			List<co.elastic.clients.elasticsearch._types.query_dsl.Query> langQueries 		= new ArrayList<>();
			
			languages.stream().forEach(l -> {
			NestedQuery h = NestedQuery.of(nq -> nq.path("languages").query(BoolQuery.of(m -> m
					.must(List.of(
							MatchQuery.of(m1 -> m1.field("languages.language").query(l))._toQuery(),
							MatchQuery.of(m2 -> m2.field("languages.level").query("PROFICIENT"))._toQuery()))
				)._toQuery()));
			
				langQueries.add(h._toQuery());
			});
			
			mustQueries.add(BoolQuery.of(m -> m
					.queryName("languages")
					.should(langQueries).minimumShouldMatch("1"))._toQuery());
			
		}
		
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
		
		if (!filterOptions.getFunctions().isEmpty()) {
			
			List<co.elastic.clients.elasticsearch._types.query_dsl.Query> langQueries 		= new ArrayList<>();
			
			filterOptions.getFunctions().stream().forEach(l -> 
				langQueries.add(BoolQuery.of(m -> m
						.must(List.of(
								MatchQuery.of(m1 -> m1.field("functions").query(l.toString()))._toQuery()))
					)._toQuery())
			);
			
			mustQueries.add(BoolQuery.of(m -> m
					.queryName("functions")
					.should(langQueries).minimumShouldMatch("1"))._toQuery());
			
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
			//mustQueries.add(RangeQuery.of(m -> m
			//		.queryName("yearsExperienceGte")
			//		.field("yearsExperience")
			//		.gte(JsonData.of(filterOptions.getYearsExperienceGtEq()))
			//)._toQuery());
			mustQueries.add(RangeQuery.of(m -> m
					.number(n -> n
					.queryName("yearsExperienceGte")
					.field("yearsExperience")
					.gte(Double.valueOf(filterOptions.getYearsExperienceGtEq())))
			)._toQuery());
			
		}
		
		if (filterOptions.getYearsExperienceLtEq() > 0 ) {
			//mustQueries.add(RangeQuery.of(m -> m
			//		.queryName("yearsExperienceLte")
			//		.field("yearsExperience")
			//		.lte(JsonData.of(filterOptions.getYearsExperienceLtEq()))
			//)._toQuery());
			
			mustQueries.add(RangeQuery.of(m -> m
					.number(n -> n
					.queryName("yearsExperienceLte")
					.field("yearsExperience")
					.lte(Double.valueOf(filterOptions.getYearsExperienceLtEq())))
			)._toQuery());
			
		}
		
		if (filterOptions.getDaysSinceLastAvailabilityCheck().isPresent()) {
			LocalDate cutOff = LocalDate.now().minusDays(filterOptions.getDaysSinceLastAvailabilityCheck().get());
			//mustQueries.add(RangeQuery.of(m -> m
			//		.queryName("lastAvailabilityCheck")
			//		.field("lastAvailabilityCheck")
			//		.lte(JsonData.of(Date.from(cutOff.atStartOfDay(ZoneId.systemDefault()).toInstant())))
			//)._toQuery());
			mustQueries.add(RangeQuery.of(m -> m
					.date(n -> n
					.queryName("lastAvailabilityCheck")
					.field("lastAvailabilityCheck")
					.lte(cutOff.atStartOfDay(ZoneId.systemDefault()).format(FMT_DATE_TIME)))
			)._toQuery());
			
		}
		
		if (filterOptions.getDaysSincelastAvailabilityCheckEmailSent().isPresent()) {
			LocalDate cutOff = LocalDate.now().minusDays(filterOptions.getDaysSincelastAvailabilityCheckEmailSent().get());
			
			//Query daysSinceLastEmail = RangeQuery.of(m -> m
			//		.queryName("lastAvailabilityCheckEmailSent")
			//		.field("lastAvailabilityCheckEmailSent")
			//		.lte(JsonData.of(Date.from(cutOff.atStartOfDay(ZoneId.systemDefault()).toInstant())))
			//)._toQuery();
			Query daysSinceLastEmail = RangeQuery.of(m -> m
					.date(n -> n
					.queryName("lastAvailabilityCheckEmailSent")
					.field("lastAvailabilityCheckEmailSent")
					.lte(cutOff.atStartOfDay(ZoneId.systemDefault()).format(FMT_DATE_TIME)))
			)._toQuery();
			
			Query lastEmailSentDateExists = ExistsQuery.of(m -> m
					.queryName("lastAvailabilityCheckEmailSentExists")
					.field("lastAvailabilityCheckEmailSent")
			)._toQuery();
			
			//
			List<co.elastic.clients.elasticsearch._types.query_dsl.Query> shouldQueries 		= new ArrayList<>();
			
			Query queryCuttoffLastEmailSent = BoolQuery.of(m -> m
					.must(List.of(
							daysSinceLastEmail))
					
				)._toQuery();
			
			Query queryEmailNeverSent = BoolQuery.of(m -> m
					.mustNot(List.of(
							lastEmailSentDateExists))
				)._toQuery();
			
			
			Query queryAdminUpdatedAlready = RangeQuery.of(m -> m
					.date(n -> n
				    .queryName("lastAvailabilityCheck")
				    .field("lastAvailabilityCheck")
				    .gt(cutOff.atStartOfDay(ZoneId.systemDefault()).format(FMT_DATE_TIME)))
				)._toQuery();
			//Query queryAdminUpdatedAlready = RangeQuery.of(m -> m
			//		.queryName("lastAvailabilityCheck")
			//		.field("lastAvailabilityCheck")
			//		.gt(JsonData.of(Date.from(cutOff.atStartOfDay(ZoneId.systemDefault()).toInstant())))
			//)._toQuery();
			
			shouldQueries.add(queryCuttoffLastEmailSent);
			shouldQueries.add(queryEmailNeverSent);
			
			mustQueries.add(BoolQuery.of(m -> m
					.queryName("lastAvailabilityCheckEmailSent")
					.mustNot(queryAdminUpdatedAlready)
					.should(shouldQueries).minimumShouldMatch("1"))._toQuery());
		}
		
		if (filterOptions.getRegisteredAfter().isPresent()) {
			LocalDate cutOff = filterOptions.getRegisteredAfter().get();
			//mustQueries.add(RangeQuery.of(m -> m
			//		.queryName("registeredAfterCheck")
			//		.field("registerd")
			//		.gte(JsonData.of(Date.from(cutOff.atStartOfDay(ZoneId.systemDefault()).toInstant())))
			//)._toQuery());
			mustQueries.add(RangeQuery.of(m -> m
					.date(n -> n
					.queryName("registeredAfterCheck")
					.field("registerd")
					.gte(cutOff.atStartOfDay(ZoneId.systemDefault()).format(FMT_DATE_TIME)))
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
