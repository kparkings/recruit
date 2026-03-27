package com.arenella.recruit.campaigns.beans;

import java.util.UUID;

/**
* Class represents a recruitment Campaign. It is a high level container
* that could, for example represent a recruiters clients and contain 
* amonst other things each of the Roles for that candidate 
*/
public class Campaign {

	private UUID id;
	private String name;
	private CampaignLogo logo;
	public static class CampaignLogo{}
	
}
