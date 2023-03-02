package com.arenella.recruit.emailservice.dao;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Repository;

import com.arenella.recruit.emailservice.beans.Email;
import com.arenella.recruit.emailservice.beans.Email.EmailRecipient.ContactType;
import com.arenella.recruit.emailservice.beans.Email.Status;
import com.arenella.recruit.emailservice.beans.EmailAttachment;
import com.arenella.recruit.emailservice.entity.EmailAttachmentEntity;
import com.arenella.recruit.emailservice.entity.EmailEntity;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
* Repository for EmailEntities
* @author K Parkings
*/
@Repository
public interface EmailServiceDao extends CrudRepository<EmailEntity, UUID> {

	/**
	* Persists an email 
	* @param email - Email to persist
	*/
	default void saveEmail(Email email) {
		if (email.isPersistable()) {
			this.save(EmailEntity.convertToEntity(email));
		}
	}
	
	/**
	* Fetches email's that have not yet been sent
	* @return
	*/
	default Set<Email> fetchEmailsByStatus(Status status){
		return StreamSupport
			.stream(this.findEmailEntitiessByStatus(status).spliterator(),false)
			.map(e -> EmailEntity.convertFromEntity(e)).collect(Collectors.toSet());
		
	}
	
	@Query("FROM EmailEntity where status = :status ")
	Set<EmailEntity> findEmailEntitiessByStatus(@Param("status") Status status);
	   
	@Query("FROM EmailEntity e left join e.recipients r where r.contactId = :recipientId and r.contactType = :contactType")
	Set<EmailEntity> fetchEmailEntitiesByRecipientId(@Param("recipientId") String recipientId, @Param("contactType") ContactType contactType);
	
	@Query("FROM EmailEntity e left join e.recipients r left join e.attachments a where r.contactId = :recipientId and e.id = :emailId and a.id = :attachmentId " )
	Set<EmailEntity> fetchAttachment(@Param("recipientId") String recipientId, @Param("emailId") UUID emailId, @Param("attachmentId") UUID attachmentId);
	
	
	/**
	* Returns email's sent to a specific user
	* @param recipientId - Id of the recipient
	* @param contactType - type of recipient
	* @return recipients email's
	*/
	default Set<Email> fetchEmailsByRecipientId(@Param("recipientId") String recipientId, @Param("contactType") ContactType contactType){
		return this.fetchEmailEntitiesByRecipientId(recipientId, contactType)
				.stream()
				.map(e -> EmailEntity.convertFromEntity(e))
				.sorted(Comparator.comparing(Email::getCreated).reversed())
				.collect(Collectors.toCollection(LinkedHashSet::new));
	}

	/**
	* If present returns the Attachment 
	* @param attachmentId - Id of the attachment
	* @param name - Id of the owner of the attachment
	* @return If User has attachment the Attachment
	*/
	default Optional<EmailAttachment> fetchAttachment(UUID emailId, UUID attachmentId, String name) {
		
		Set<EmailEntity> results = this.fetchAttachment(name, emailId,attachmentId);
	
		if (results.isEmpty()) {
			return Optional.empty();
		}
		
		return results.stream().findFirst().get().getAttachments().stream().filter(a -> a.getAttachmentId().toString().equals(attachmentId.toString())).findAny().map(a -> EmailAttachmentEntity.convertToDomain(a));
	
	};
}
