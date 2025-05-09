package com.arenella.recruit.candidates.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.beans.CandidateProfileViewedEvent;
import com.arenella.recruit.candidates.controllers.CandidateProfileViewFormatterUtil.Bucket;

/**
* Unit tests for the CandidateProfileViewFormatterUtil class 
*/
class CandidateProfileViewFormatterUtilTest {

	/**
	* Tests events are split into year-week buckets and returned in ascending order. 
	*/
	@Test
	void testByWeeks() {
		
		CandidateProfileViewedEvent event01 = CandidateProfileViewedEvent.builder().viewed(LocalDateTime.of(2025, 5, 2, 9, 12, 15)).build();
		CandidateProfileViewedEvent event02 = CandidateProfileViewedEvent.builder().viewed(LocalDateTime.of(2025, 5, 3, 9, 12, 15)).build();
		CandidateProfileViewedEvent event03 = CandidateProfileViewedEvent.builder().viewed(LocalDateTime.of(2025, 5, 3, 9, 12, 15)).build();
		CandidateProfileViewedEvent event04 = CandidateProfileViewedEvent.builder().viewed(LocalDateTime.of(2025, 5, 3, 9, 12, 15)).build();
		CandidateProfileViewedEvent event05 = CandidateProfileViewedEvent.builder().viewed(LocalDateTime.of(2025, 5, 4, 9, 12, 15)).build();
		
		CandidateProfileViewedEvent event06 = CandidateProfileViewedEvent.builder().viewed(LocalDateTime.of(2025, 5, 5, 9, 12, 15)).build();
		CandidateProfileViewedEvent event07 = CandidateProfileViewedEvent.builder().viewed(LocalDateTime.of(2025, 5, 6, 9, 12, 15)).build();
		CandidateProfileViewedEvent event08 = CandidateProfileViewedEvent.builder().viewed(LocalDateTime.of(2025, 5, 7, 9, 12, 15)).build();
		CandidateProfileViewedEvent event09 = CandidateProfileViewedEvent.builder().viewed(LocalDateTime.of(2025, 5, 7, 9, 12, 15)).build();
		CandidateProfileViewedEvent event10 = CandidateProfileViewedEvent.builder().viewed(LocalDateTime.of(2025, 5, 8, 9, 12, 15)).build();
		CandidateProfileViewedEvent event11 = CandidateProfileViewedEvent.builder().viewed(LocalDateTime.of(2025, 5, 8, 9, 12, 15)).build();
		CandidateProfileViewedEvent event12 = CandidateProfileViewedEvent.builder().viewed(LocalDateTime.of(2025, 5, 8, 9, 12, 15)).build();
		CandidateProfileViewedEvent event13 = CandidateProfileViewedEvent.builder().viewed(LocalDateTime.of(2025, 5, 9, 9, 12, 15)).build();
		CandidateProfileViewedEvent event14 = CandidateProfileViewedEvent.builder().viewed(LocalDateTime.of(2025, 5, 10, 9, 12, 15)).build();
		CandidateProfileViewedEvent event15 = CandidateProfileViewedEvent.builder().viewed(LocalDateTime.of(2025, 5, 11, 9, 12, 15)).build();
		
		CandidateProfileViewedEvent event16 = CandidateProfileViewedEvent.builder().viewed(LocalDateTime.of(2025, 5, 12, 9, 12, 15)).build();
		CandidateProfileViewedEvent event17 = CandidateProfileViewedEvent.builder().viewed(LocalDateTime.of(2025, 5, 13, 9, 12, 15)).build();
		CandidateProfileViewedEvent event18 = CandidateProfileViewedEvent.builder().viewed(LocalDateTime.of(2025, 5, 13, 9, 12, 15)).build();
		CandidateProfileViewedEvent event19 = CandidateProfileViewedEvent.builder().viewed(LocalDateTime.of(2025, 5, 13, 9, 12, 15)).build();
		CandidateProfileViewedEvent event20 = CandidateProfileViewedEvent.builder().viewed(LocalDateTime.of(2025, 5, 13, 9, 12, 15)).build();
		CandidateProfileViewedEvent event21 = CandidateProfileViewedEvent.builder().viewed(LocalDateTime.of(2025, 5, 14, 9, 12, 15)).build();
		CandidateProfileViewedEvent event22 = CandidateProfileViewedEvent.builder().viewed(LocalDateTime.of(2025, 5, 15, 9, 12, 15)).build();
		CandidateProfileViewedEvent event23 = CandidateProfileViewedEvent.builder().viewed(LocalDateTime.of(2025, 5, 16, 9, 12, 15)).build();
		CandidateProfileViewedEvent event24 = CandidateProfileViewedEvent.builder().viewed(LocalDateTime.of(2025, 5, 17, 9, 12, 15)).build();
		CandidateProfileViewedEvent event25 = CandidateProfileViewedEvent.builder().viewed(LocalDateTime.of(2025, 5, 18, 9, 12, 15)).build();
		
		Set<Bucket> buckets = CandidateProfileViewFormatterUtil
				.byWeek(Set.of(
						event01,
						event02,
						event03,
						event04,
						event05,
						event06,
						event07,
						event08,
						event09,
						event10,
						event11,
						event12,
						event13,
						event14,
						event15,
						event16,
						event17,
						event18,
						event19,
						event20,
						event21,
						event22,
						event23,
						event24,
						event25
						));
		
		assertEquals(5,   buckets.stream().filter(b -> b.getBucketId().equals("2025 - 18")).findAny().orElseThrow().getCount());
		assertEquals(10	, buckets.stream().filter(b -> b.getBucketId().equals("2025 - 19")).findAny().orElseThrow().getCount());
		assertEquals(10,  buckets.stream().filter(b -> b.getBucketId().equals("2025 - 20")).findAny().orElseThrow().getCount());
		
		
	}
	
}