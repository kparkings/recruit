package com.arenella.recruit.campaigns.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.campaigns.beans.CandidateGroup.Group;

/**
* Unit tests for the UpdateGroupAPIInbound class 
*/
class UpdateGroupAPIInboundTest {

	/**
	* Tests construction via the Builder 
	*/
	@Test
	void testConstructor() {
		
		final String candiadteId = "123";
		final Group group1 = Group.GROUP1;
		
		UpdateGroupAPIInbound inbound = UpdateGroupAPIInbound
				.builder()
					.candidateIds(Set.of(candiadteId))
					.group(group1)
				.build();
		
		assertEquals(1, inbound.getCandidateIds().size());
		assertEquals(group1, inbound.getGroup());
		
	}
	
}
