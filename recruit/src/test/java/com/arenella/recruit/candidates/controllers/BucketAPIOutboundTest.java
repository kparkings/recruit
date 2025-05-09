package com.arenella.recruit.candidates.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.controllers.CandidateProfileViewFormatterUtil.Bucket;

/**
* Unit tests for the BucketAPIOutbound class
*/
class BucketAPIOutboundTest {

	/**
	* Tests conversion from Domain to APIOutbound representation
	*/
	@Test
	void testFromDomain() {
		
		final String bucketName = "2025-95";
		
		Bucket bucket = new Bucket(bucketName);
		
		bucket.increment();
		bucket.increment();
		bucket.increment();
		
		BucketAPIOutbound outbound = BucketAPIOutbound.fromDomain(bucket);
		
		assertEquals(bucketName, outbound.bucketId());
		assertEquals(3, outbound.count());
		
	}
	
}
