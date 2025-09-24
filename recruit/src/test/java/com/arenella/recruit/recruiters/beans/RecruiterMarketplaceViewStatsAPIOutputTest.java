package com.arenella.recruit.recruiters.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.recruiters.beans.SupplyAndDemandEvent.EventType;

/**
* Unit tests for the RecruiterOpenPositionStatsAPIOutput class
* @author K Parkings
*/
class RecruiterMarketplaceViewStatsAPIOutputTest {

	final String recruiterId1 = "rec1";
	final String recruiterId2 = "rec2";
	
	final Set<SupplyAndDemandEvent> events = Set.of(
			SupplyAndDemandEvent.builder().created(LocalDateTime.now().minusDays(1)).eventId(UUID.randomUUID()).recruiterId(recruiterId1).type(EventType.OFFERED_CANDIDATE).build(),
			SupplyAndDemandEvent.builder().created(LocalDateTime.now()).eventId(UUID.randomUUID()).recruiterId(recruiterId1).type(EventType.OFFERED_CANDIDATE).build(),
			SupplyAndDemandEvent.builder().created(LocalDateTime.now()).eventId(UUID.randomUUID()).recruiterId(recruiterId2).type(EventType.OFFERED_CANDIDATE).build(),
			SupplyAndDemandEvent.builder().created(LocalDateTime.now()).eventId(UUID.randomUUID()).recruiterId(recruiterId1).type(EventType.OFFERED_CANDIDATE).build(),
			SupplyAndDemandEvent.builder().created(LocalDateTime.now()).eventId(UUID.randomUUID()).recruiterId(recruiterId2).type(EventType.OFFERED_CANDIDATE).build(),
			SupplyAndDemandEvent.builder().created(LocalDateTime.now()).eventId(UUID.randomUUID()).recruiterId(recruiterId1).type(EventType.OFFERED_CANDIDATE).build(),
			SupplyAndDemandEvent.builder().created(LocalDateTime.now()).eventId(UUID.randomUUID()).recruiterId(recruiterId2).type(EventType.OFFERED_CANDIDATE).build(),
			SupplyAndDemandEvent.builder().created(LocalDateTime.now()).eventId(UUID.randomUUID()).recruiterId(recruiterId1).type(EventType.OFFERED_CANDIDATE).build(),
			SupplyAndDemandEvent.builder().created(LocalDateTime.now()).eventId(UUID.randomUUID()).recruiterId(recruiterId2).type(EventType.OFFERED_CANDIDATE).build(),
			SupplyAndDemandEvent.builder().created(LocalDateTime.now()).eventId(UUID.randomUUID()).recruiterId(recruiterId1).type(EventType.OFFERED_CANDIDATE).build()
			);
	
	/**
	* Tests constructor
	* @throws Exception
	*/
	@Test
	void testConstructor() {
		
		RecruiterMarketplaceViewStatsAPIOutput stats = new RecruiterMarketplaceViewStatsAPIOutput(events);
		
		assertEquals(6, stats.getStats().stream().filter(s -> s.getRecruiterId().equals(recruiterId1)).findAny().get().getViewsThisWeek());
		assertEquals(4, stats.getStats().stream().filter(s -> s.getRecruiterId().equals(recruiterId2)).findAny().get().getViewsThisWeek());
		assertEquals(5, stats.getStats().stream().filter(s -> s.getRecruiterId().equals(recruiterId1)).findAny().get().getViewsToday());
		assertEquals(4, stats.getStats().stream().filter(s -> s.getRecruiterId().equals(recruiterId2)).findAny().get().getViewsToday());
		
	}
	
}
