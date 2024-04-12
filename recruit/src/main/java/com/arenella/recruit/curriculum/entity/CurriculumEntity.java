package com.arenella.recruit.curriculum.entity;

import java.util.Optional;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.arenella.recruit.curriculum.beans.Curriculum;
import com.arenella.recruit.curriculum.enums.FileType;

/**
* Entity representation of a Curriculum Vitae
* @author K Parkings
*/
@Entity
@Table(schema="curriculum", name="curriculum")
public class CurriculumEntity {

	@Id
	@Column(name="id")
	private long 		curriculumId;
	
	@Column(name="file_type")
	@Enumerated(EnumType.STRING)
	private FileType 	fileType;
	
	@Column(name="file_bytes")
	private byte[] 		file;

	@Column(name="owner_id")
	private String 		ownerId;

	/**
	* Default constructor for Hibernate
	*/
	public CurriculumEntity() {}
	
	/**
	* Constructor based upon a builder
	* @param builder - contains initialization values
	*/
	public CurriculumEntity(CurriculumEntityBuilder builder){
		
		this.curriculumId 		= builder.curriculumId;
		this.fileType 			= builder.fileType;
		this.file 				= builder.file;
		this.ownerId			= builder.ownerId;
	}
	
	/**
	* Returns unique Id of the Curriculum
	* @return unique Id
	*/
	public long getCurriculumId() {
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
	* If Curriculum has an owner the id of the Owner
	* @return id of the Owner
	*/
	public Optional<String> getOwnerId() {
		return Optional.ofNullable(this.ownerId);
	}
	
	/**
	* Returns a Builder for the class
	* @return Builder
	*/
	public static CurriculumEntityBuilder builder() {
		return new CurriculumEntityBuilder();
	}
	
	/**
	* Builder class for CurriculumEntity
	* @author K Parkings
	*/
	public static class CurriculumEntityBuilder{
		
		private long 		curriculumId;
		private FileType 	fileType;
		private byte[] 		file;
		private String		ownerId;
		
		/**
		* Sets the Unique identifier of the Curriculum 
		* @param curriculumId - Unique Id of the Curriculum
		* @return Builder
		*/
		public CurriculumEntityBuilder curriculumId(long curriculumId) {
			this.curriculumId = curriculumId;
			return this;
		}
		
		/**
		* Sets the File Type of the Curriculum. That is word / pdf
		* @param fileType - Type of the physical file
		* @return Builder
		*/
		public CurriculumEntityBuilder fileType(FileType fileType) {
			this.fileType = fileType;
			return this;
		}
		
		/**
		* Sets the bits that represent the physical file
		* @param file - The Curriculum file
		* @return Builder
		*/
		public CurriculumEntityBuilder file(byte[] file) {
			this.file = file;
			return this;
		}
		
		/**
		* Sets the Id of the Owner of the Curriculum
		* @param ownerId - Id of the Owner
		* @return Builder
		*/
		public CurriculumEntityBuilder ownerId(String ownerId) {
			this.ownerId = ownerId;
			return this;
		}
		
		/**
		* Builds an instance of CurriculumEntity initialized with 
		* the values in the Builder
		* @return CurriculumEntity
		*/
		public CurriculumEntity build() {
			return new CurriculumEntity(this);
		}	
		
	}
	
	/**
	* Converts a Domain representation of a Curriculum to an Entity version
	* @param curriculum - Curriculum to convert
	* @return Entity representation of an Entity
	*/
	public static CurriculumEntity convertToEntity(Curriculum curriculum) {
		
		return CurriculumEntity
							.builder()
								.curriculumId(Long.valueOf(curriculum.getId().get()))	//TODO: [KP] Need to decide where how to set up the sequence
								.fileType(curriculum.getFileType())
								.file(curriculum.getFile())
								.ownerId(curriculum.getOwnerId().isPresent() ? curriculum.getOwnerId().get() : null)
							.build();
	}

	/**
	* Converts an Entity representation of a Curriculum to an Domain version
	* @param curriculumEntity - Curriculum to convert
	* @return Domain representation of an Entity
	*/
	public static Curriculum convertFromEntity(CurriculumEntity curriculumEntity) {
		
		return Curriculum
						.builder()
							.id(String.valueOf(curriculumEntity.getCurriculumId()))	//TODO: [KP] Need to decide where how to set up the sequence
							.fileType(curriculumEntity.getFileType())
							.file(curriculumEntity.getFile())
							.ownerId(curriculumEntity.getOwnerId().isPresent() ? curriculumEntity.getOwnerId().get() : null)
						.build();
	}
	
}