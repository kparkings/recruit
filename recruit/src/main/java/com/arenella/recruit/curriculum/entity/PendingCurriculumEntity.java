package com.arenella.recruit.curriculum.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.arenella.recruit.curriculum.beans.PendingCurriculum;
import com.arenella.recruit.curriculum.enums.FileType;

/**
* Entity representing a Curriculum that has been 
* uploaded by a Candidate but has not yet been 
* processed and made available to the Recruiters
* @author K Parkings
*/
@Entity
@Table(schema="curriculum", name="pending_curriculum")
public class PendingCurriculumEntity {

	@Id
	@Column(name="id")
	private UUID 		curriculumId;
	
	@Column(name="file_type")
	@Enumerated(EnumType.STRING)
	private FileType 	fileType;
	
	@Column(name="file_bytes")
	private byte[] 		file;
	
	/**
	* Default constructor for Hibernate
	*/
	public PendingCurriculumEntity() {}
	
	/**
	* Constructor based upon a builder
	* @param builder - contains initialization values
	*/
	public PendingCurriculumEntity(PendingCurriculumEntityBuilder builder){
		
		this.curriculumId 		= builder.curriculumId;
		this.fileType 			= builder.fileType;
		this.file 				= builder.file;
		 
	}
	
	/**
	* Returns unique Id of the Curriculum
	* @return unique Id
	*/
	public UUID getCurriculumId() {
		return curriculumId;
	}
	
	/**
	* Returns the format of the File
	* @return type of file
	*/
	public FileType getFileType() {
		return fileType;
	}
	
	/**
	* Returns the bytes that constitute the 
	* physical file
	* @return file bytes
	*/
	public byte[] getFile() {
		return file;
	}
	
	/**
	* Returns a Builder for the class
	* @return Builder
	*/
	public static PendingCurriculumEntityBuilder builder() {
		return new PendingCurriculumEntityBuilder();
	}
	
	/**
	* Builder class for CurriculumEntity
	* @author K Parkings
	*/
	public static class PendingCurriculumEntityBuilder{
		
		private UUID 		curriculumId;
		private FileType 	fileType;
		private byte[] 		file;
		
		/**
		* Sets the Unique identifier of the Curriculum 
		* @param curriculumId - Unique Id of the Curriculum
		* @return Builder
		*/
		public PendingCurriculumEntityBuilder curriculumId(UUID curriculumId) {
			this.curriculumId = curriculumId;
			return this;
		}
		
		/**
		* Sets the File Type of the Curriculum. That is word / pdf
		* @param fileType - Type of the physical file
		* @return Builder
		*/
		public PendingCurriculumEntityBuilder fileType(FileType fileType) {
			this.fileType = fileType;
			return this;
		}
		
		/**
		* Sets the bits that represent the physical file
		* @param file - The Curriculum file
		* @return Builder
		*/
		public PendingCurriculumEntityBuilder file(byte[] file) {
			this.file = file;
			return this;
		}
		
		/**
		* Builds an instance of CurriculumEntity initialized with 
		* the values in the Builder
		* @return CurriculumEntity
		*/
		public PendingCurriculumEntity build() {
			return new PendingCurriculumEntity(this);
		}	
		
	}
	
	/**
	* Converts a Domain representation of a Curriculum to an Entity version
	* @param curriculum - Curriculum to convert
	* @return Entity representation of an Entity
	*/
	public static PendingCurriculumEntity convertToEntity(PendingCurriculum curriculum) {
		
		return PendingCurriculumEntity
							.builder()
								.curriculumId(curriculum.getId().get())
								.fileType(curriculum.getFileType())
								.file(curriculum.getFile())
							.build();
	}

	/**
	* Converts an Entity representation of a Curriculum to an Domain version
	* @param curriculumEntity - Curriculum to convert
	* @return Domain representation of an Entity
	*/
	public static PendingCurriculum convertFromEntity(PendingCurriculumEntity curriculumEntity) {
		
		return PendingCurriculum
						.builder()
							.id(curriculumEntity.getCurriculumId())	
							.fileType(curriculumEntity.getFileType())
							.file(curriculumEntity.getFile())
						.build();
	}
	
}
