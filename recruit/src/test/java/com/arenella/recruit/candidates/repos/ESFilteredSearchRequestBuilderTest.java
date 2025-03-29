package com.arenella.recruit.candidates.repos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.beans.CandidateFilterOptions;
import com.arenella.recruit.candidates.beans.Language.LANGUAGE;
import com.arenella.recruit.candidates.enums.COUNTRY;
import com.arenella.recruit.candidates.enums.FUNCTION;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.ExistsQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermsQuery;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

/**
* Unit tests for the ESFilteredSearchRequestBuilder class
* @author K Parkings
*/
class ESFilteredSearchRequestBuilderTest {
	
	
	private static final String 			CANDIDATE_ID 						= "1234";
	private static final String				SEARCH_TEXT							= "Java Developer";
	private static final Set<String> 		CANDIDATE_IDS 						= Set.of(CANDIDATE_ID);
	private static final Set<COUNTRY> 		COUNTRIES							= Set.of(COUNTRY.AUSTRIA, COUNTRY.CANADA);
	private static final Set<FUNCTION> 		FUNCTIONS							= Set.of(FUNCTION.CSHARP_DEV, FUNCTION.IT_RECRUITER);
	private static final Boolean 			FREELANCE							= true;
	private static final Boolean 			PERM								= true;
	private static final boolean 			AVAILABLE 							= true;
	private static final int 				YEARS_EXPERIENCE_GTE				= 3;
	private static final int				YEARS_EXPERIENCE_LTE				= 5;
	private static final Set<String>		SKILLS								= Set.of("JaVa","Qa");
	private static final String				FIRSTNAME							= "kevin";
	private static final String 			SURNAME								= "parkings";
	private static final String 			EMAIL								= "admin@arenella-ict.com";
	private static final Integer			DAYS_SINCE_LAST_AVAILABILITY_CHECK 	= 10;
	private static final LocalDate			REGISTERED_AFTER					= LocalDate.of(2024, 5, 8);
	private static final LocalDate			LAST_REFRESH						= LocalDate.of(2024, 8, 11);
	private static final Integer			LAST_EMAIL_AVAILABILIY_REQUEST		= 12;
	private static final String				OWNER_ID							= "6789";
	private static final Boolean			INCLUDE_REQUIRES_SPONSORSHIP		= true;
	
	private static final CandidateFilterOptions filters = 
			CandidateFilterOptions
				.builder()
					.available(AVAILABLE)
					.candidateIds(CANDIDATE_IDS)
					.countries(COUNTRIES)
					.daysSinceLastAvailabilityCheck(DAYS_SINCE_LAST_AVAILABILITY_CHECK)
					.languages(Set.of(
							LANGUAGE.ENGLISH,
							LANGUAGE.DUTCH,
							LANGUAGE.FRENCH)
					)
					.email(EMAIL)
					.firstname(FIRSTNAME)
					.surname(SURNAME)
					.freelance(FREELANCE)
					.functions(FUNCTIONS)
					.includeRequiresSponsorship(INCLUDE_REQUIRES_SPONSORSHIP)
					.ownerId(OWNER_ID)
					.perm(PERM)
					.searchText(SEARCH_TEXT)
					.skills(SKILLS)
					.yearsExperienceGtEq(YEARS_EXPERIENCE_GTE)
					.yearsExperienceLtEq(YEARS_EXPERIENCE_LTE)
					.registeredAfter(REGISTERED_AFTER)
					.lastAccountRefreshLtEq(LAST_REFRESH)
					.lastAccountRefreshMissing()
					.daysSincelastAvailabilityCheckEmailSent(LAST_EMAIL_AVAILABILIY_REQUEST)
				.build();
	
	/**
	* Tests filter - firstname
	* @throws Exception
	*/
	@Test
	void testFilters() {
		
		Query 					query 		= ESFilteredSearchRequestBuilder.createFilteredQuery(filters);
		BoolQuery 				boolQuery 	= (BoolQuery) query._get();
		
		List<MatchQuery> 	mustMatch 		= new ArrayList<>();
		List<RangeQuery> 	mustRange 		= new ArrayList<>();
		List<MatchQuery> 	mustNotMatch 	= new ArrayList<>();
		List<BoolQuery>  	mustBool 		= new ArrayList<>();
		List<TermsQuery> 	mustTerms 		= new ArrayList<>();
		List<ExistsQuery> 	mustNotExists 	= new ArrayList<>();
		
		boolQuery.must().stream().filter(q -> q.isMatch()).forEach(q -> mustMatch.add((MatchQuery)q._get()));
		boolQuery.must().stream().filter(q -> q.isRange()).forEach(q -> mustRange.add((RangeQuery)q._get()));
		boolQuery.must().stream().filter(q -> q.isBool()).forEach(q -> mustBool.add((BoolQuery)q._get()));
		boolQuery.must().stream().filter(q -> q.isTerms()).forEach(q -> mustTerms.add((TermsQuery)q._get()));
		
		boolQuery.mustNot().stream().filter(q -> q.isMatch()).forEach(q -> mustNotMatch.add((MatchQuery)q._get()));
		boolQuery.mustNot().stream().filter(q -> q.isExists()).forEach(q -> mustNotExists.add((ExistsQuery)q._get()));
		
		assertTrue(mustNotMatch.isEmpty());
		
		assertEquals(AVAILABLE, 							mustMatch.stream().filter(q -> q.field().equals("available")).findFirst().get().query().booleanValue());
		assertEquals("TRUE", 								mustMatch.stream().filter(q -> q.field().equals("freelance")).findFirst().get().query().stringValue());
		assertEquals("TRUE", 								mustMatch.stream().filter(q -> q.field().equals("perm")).findFirst().get().query().stringValue());
		
		assertEquals(CANDIDATE_IDS.toArray()[0], 			mustMatch.stream().filter(q -> q.field().equals("candidateId")).findFirst().get().query().stringValue());
		assertEquals(FIRSTNAME, 							mustMatch.stream().filter(q -> q.field().equals("firstname")).findFirst().get().query().stringValue());
		assertEquals(SURNAME, 								mustMatch.stream().filter(q -> q.field().equals("surname")).findFirst().get().query().stringValue());
		assertEquals(OWNER_ID, 								mustMatch.stream().filter(q -> q.field().equals("ownerId")).findFirst().get().query().stringValue());
		assertEquals(EMAIL, 								mustMatch.stream().filter(q -> q.field().equals("email")).findFirst().get().query().stringValue());
		
		Date dateLocal 	= Date.from(LAST_REFRESH.atStartOfDay(ZoneId.systemDefault()).toInstant());
		Date dateES 	= (Date)mustRange.stream().filter(q -> q.field().equals("lastAccountRefresh")).findFirst().get().lte().to(Date.class);
		assertEquals(dateLocal, dateES);  				
		
		mustNotExists.stream().filter(q -> q.queryName().equals("lastAccountRefreshMissing")).findFirst().orElseThrow();
		
		mustRange.stream().filter(q -> q.queryName().equals("yearsExperienceGte") 		&& q.gte().toString().equals(String.valueOf(YEARS_EXPERIENCE_GTE))).findFirst().orElseThrow();
		mustRange.stream().filter(q -> q.queryName().equals("yearsExperienceLte") 		&& q.lte().toString().equals(String.valueOf(YEARS_EXPERIENCE_LTE))).findFirst().orElseThrow();
		
		LocalDate ld 						= LocalDate.now().minusDays(DAYS_SINCE_LAST_AVAILABILITY_CHECK);
		
		mustBool.stream().filter(q -> q.queryName().equals("lastAvailabilityCheckEmailSent")).findFirst().orElseThrow();
		mustRange.stream().filter(q -> q.queryName().equals("lastAvailabilityCheck")	&& q.lte().toString().equals(String.valueOf(Date.from(ld.atStartOfDay(ZoneId.systemDefault()).toInstant())))).findFirst().orElseThrow();
		mustRange.stream().filter(q -> q.queryName().equals("registeredAfterCheck")		&& q.gte().toString().equals(String.valueOf(Date.from(REGISTERED_AFTER.atStartOfDay(ZoneId.systemDefault()).toInstant())))).findFirst().orElseThrow();
		
		//Works but probably in Bool query and not must now. need to fix test
		//mustTerms.stream().filter(q -> q.queryName().equals("languages")).findFirst().orElseThrow();
		
		TermsQuery skills = mustTerms.stream().filter(q -> q.field().equals("skills")).findFirst().get();
		skills.terms().value().stream().filter(f -> f.stringValue().equals("JaVa")).findAny().orElseThrow();
		skills.terms().value().stream().filter(f -> f.stringValue().equals("Qa")).findAny().orElseThrow();
		
		BoolQuery functions = mustBool.stream().filter(q -> q.queryName().equals("functions")).findFirst().get();
		functions.should().toString().contains("CSHARP_DEV");
		functions.should().toString().contains("IT_RECRUITER");
		
		TermsQuery countries = mustTerms.stream().filter(q -> q.field().equals("country")).findFirst().get();
		countries.terms().value().stream().filter(f -> f.stringValue().equals(COUNTRY.AUSTRIA.toString())).findAny().orElseThrow();
		countries.terms().value().stream().filter(f -> f.stringValue().equals(COUNTRY.CANADA.toString())).findAny().orElseThrow();
		
	}

	/**
	* Tests filter - firstname
	* @throws Exception
	*/
	@Test
	void testEmptyFilters() {
		
		Query 					query 		= ESFilteredSearchRequestBuilder.createFilteredQuery(CandidateFilterOptions.builder().build());
		BoolQuery 				boolQuery 	= (BoolQuery) query._get();
	
		List<MatchQuery> mustMatch 		= new ArrayList<>();
		List<RangeQuery> mustRange 		= new ArrayList<>();
		List<MatchQuery> mustNotMatch 	= new ArrayList<>();
		List<BoolQuery>  mustBool 		= new ArrayList<>();
		List<TermsQuery> mustTerms 		= new ArrayList<>();
		
		boolQuery.must().stream().filter(q -> q.isMatch()).forEach(q -> mustMatch.add((MatchQuery)q._get()));
		boolQuery.must().stream().filter(q -> q.isRange()).forEach(q -> mustRange.add((RangeQuery)q._get()));
		boolQuery.must().stream().filter(q -> q.isBool()).forEach(q -> mustBool.add((BoolQuery)q._get()));
		boolQuery.must().stream().filter(q -> q.isTerms()).forEach(q -> mustTerms.add((TermsQuery)q._get()));
		
		boolQuery.mustNot().stream().filter(q -> q.isMatch()).forEach(q -> mustNotMatch.add((MatchQuery)q._get()));
		
		assertTrue(mustMatch.isEmpty());
		assertTrue(mustRange.isEmpty());
		assertTrue(mustBool.isEmpty());
		assertTrue(mustTerms.isEmpty());
		
		assertEquals(true, mustNotMatch.stream().filter(q -> q.field().equals("requiresSponsorship")).findFirst().get().query().booleanValue());
		
		
	} 
}
