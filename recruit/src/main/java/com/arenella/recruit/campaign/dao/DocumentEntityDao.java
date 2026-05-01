package com.arenella.recruit.campaign.dao;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import com.arenella.recruit.campaigns.beans.Document;
import com.arenella.recruit.campaigns.entities.DocumentEntity;

/**
* Repository for interacting with Document's 
*/
@Repository
public interface DocumentEntityDao extends ListCrudRepository<DocumentEntity, UUID> {

	/**
	* If present returns the Document identified by the Identifier
	* @param documentId - Unique Id of the Document
	* @return If present the Document
	*/
	default Optional<Document> fetchDocumentById(UUID documentId) {
		return this.findById(documentId).map(DocumentEntity::fromEntity);
	}
	
	/**
	* Perists a Document
	* @param document - Document to be persisted
	*/
	default void saveDocument(Document document) {
		this.save(DocumentEntity.toEntity(document));
	}
	
}
