package com.arenella.recruit.messaging.beans;

/**
* Class represents a Photo
* @author K Parkings
*/
public record Photo(byte[] imageBytes, PHOTO_FORMAT format){
	
	public  enum PHOTO_FORMAT {JPEG, PNG}
	
}
