package com.arenella.recruit.curriculum.beans;

import java.util.Optional;

import com.arenella.recruit.curriculum.enums.FileType;

/**
* Curriculum of a Candidate
* @author K Parkings
*/
public class Curriculum {

	private String 		id;
	private FileType 	fileType;
	private byte[] 		file;
	
	/**
	* Constructor based upon a Builder
	* @param builder - Contains initialization attributes
	*/
	public Curriculum(CurriculumAPIInboundBuilder builder) {
		this.id 		= builder.id;
		this.fileType 	= builder.fileType;
		this.file 		= builder.file;
	}
	
	/**
	* If present returns the Unique Id of the Curriculum. New Curriculum's 
	* not yet persisted will not have an Id
	* @return Unique identifier of the Curriculum
	*/
	public Optional<String> getId(){
		return Optional.ofNullable(this.id);
	}
	
	/**
	* Returns the type of the physical file of the Curriculum
	* @return File type of the Curriculum
	*/
	public FileType getFileType() {
		return this.fileType;
	}
	
	/**
	* Returns the bytes that make of the Physical file of the 
	* Curriculum
	* @return Curriculum file
	*/
	public byte[] getFile() {
		return this.file;
	}
	
	/**
	* Returns a Builder for the class
	* @return
	*/
	public static CurriculumAPIInboundBuilder builder() {
		return new CurriculumAPIInboundBuilder();
	}
	
	/**
	* Builder for the Curriculum class
	* @author K Parkings
	*/
	public static class CurriculumAPIInboundBuilder {
		
		private String 		id;
		private FileType 	fileType;
		private byte[] 		file;
		
		/**
		* Unique id of the Curriculum if it has already been set
		* @param id - Unique id of the curriculum
		* @return Builder
		*/
		public CurriculumAPIInboundBuilder id(String id) {
			this.id = id;
			return this;
		}
		
		/**
		* Sets the type of file. This is the postfix. For example
		* pdf, doc, docx
		* @param fileType - Type of the Curriculum file
		* @return Builder
		*/
		public CurriculumAPIInboundBuilder fileType(FileType fileType) {
			this.fileType = fileType;
			return this;
		}
		
		/**
		* Sets the bytes that are physical Curriculum file
		* @param file - Curriculum file
		* @return Builder
		*/
		public CurriculumAPIInboundBuilder file(byte[] file) {
			this.file = file;
			return this;
		}
		
		/**
		* Returns an initialized Curriculum based upon the 
		* values set in the Builder
		* @return
		*/
		public Curriculum build() {
			return new Curriculum(this);
		}
	}
	
}