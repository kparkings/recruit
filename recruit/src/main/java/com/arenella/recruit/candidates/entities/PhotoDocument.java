package com.arenella.recruit.candidates.entities;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.arenella.recruit.candidates.beans.Candidate.Photo;
import com.arenella.recruit.candidates.beans.Candidate.Photo.PHOTO_FORMAT;

/**
* Elasticsearch Document representation of a Photo
* @author K Parkings
*/
public class PhotoDocument {

	@Field(type = FieldType.Binary, name="image_bytes")
	private final byte[] 		imageBytes;
	
	@Field(type = FieldType.Keyword, name="format")
	private final PHOTO_FORMAT 	format;
	
	/**
	* Constructor
	* @param imageBytes - Bytes making up the photo
	* @param format		- Format of file
	*/
	public PhotoDocument(byte[] imageBytes, PHOTO_FORMAT format) {
		this.imageBytes = imageBytes;
		this.format 	= format;
	}
	
	/**
	* Return the bytes making up the Phot
	* @return bytes
	*/
	public byte[] getImageBytes() {
		return this.imageBytes;
	}
	
	/**
	* Return the format of the Photo's file
	* @return format
	*/
	public PHOTO_FORMAT getFormat() {
		return this.format;
	}
	
	/**
	* Converts from Domain to Persistence layer representation
	* @param photo - Photo to convert
	* @return converted Photo
	*/
	public static PhotoDocument convertFromDomain(Photo photo) {
		return new PhotoDocument(photo.getImageBytes(), photo.getFormat());
	}
	
	/**
	* Converts from Persistence layer to Domain representation
	* @param photo - Photo to convert
	* @return converted Photo
	*/
	public static Photo convertToDomain(PhotoDocument photo) {
		return new Photo(photo.getImageBytes(), photo.getFormat());
	}
	
}