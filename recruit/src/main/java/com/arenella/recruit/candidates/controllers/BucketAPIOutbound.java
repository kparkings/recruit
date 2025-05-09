package com.arenella.recruit.candidates.controllers;

import com.arenella.recruit.candidates.controllers.CandidateProfileViewFormatterUtil.Bucket;

/**
* Outbound representation of a Bucket of events
* occurring within a certain timeframe 
*/
public record BucketAPIOutbound(String bucketId, long count) {

	/**
	* Converts from a domain representation of a Bucket to an
	* API Outbound specifc representation
	* @param bucket - To convert
	* @return converted
	*/
	public static BucketAPIOutbound fromDomain(Bucket bucket) {
		return new BucketAPIOutbound(bucket.getBucketId(), bucket.getCount());
	}
	
}
