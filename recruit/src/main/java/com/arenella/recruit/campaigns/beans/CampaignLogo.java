package com.arenella.recruit.campaigns.beans;

/**
* Class represents a Photo
* @param imageBypes 	- Bytes of actual image
* @param format 		- image type
* @author K Parkings
*/
public record CampaignLogo(byte[] imageBytes, PHOTO_FORMAT format){
	
	public  enum PHOTO_FORMAT {jpeg, png}

}
