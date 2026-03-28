package com.arenella.recruit.campaigns.beans;

/**
* Class represents a Photo
* @author K Parkings
*/
public class CampaignLogo{
	
	public  enum PHOTO_FORMAT {jpeg, png}
	
	private final byte[] 		imageBytes;
	private final PHOTO_FORMAT 	format;

	/**
	* Class represents an uploaded Photo
	* @param imageBytes - bytes of actual file
	* @param format		- format of photo file
	*/
	public CampaignLogo(byte[] imageBytes, PHOTO_FORMAT format) {
		this.imageBytes 	= imageBytes;
		this.format 		= format;
	}
	
	/**
	* Returns the bytes of the file
	* @return file bytes
	*/
	public byte[] getImageBytes() {
		return this.imageBytes;
	}
	
	/**
	* Returns the file format
	* @return format of the file
	*/
	public PHOTO_FORMAT getFormat() {
		return this.format;
	}
	
}
