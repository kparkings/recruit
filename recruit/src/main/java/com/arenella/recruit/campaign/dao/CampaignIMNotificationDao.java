package com.arenella.recruit.campaign.dao;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import com.arenella.recruit.campaigns.beans.IMNotification;

/**
* Repository for IMNotifications 
*/
@Repository
public interface CampaignIMNotificationDao extends ListCrudRepository<IMNotificationEntity, UUID>{

	@Query("from IMNotificationEntity where recruiterId = :recruiterId")
	public Set<IMNotificationEntity> fetchIMNotificationEntitiesForRecruiter(String recruiterId);
	
	@Query("from IMNotificationEntity where recruiterId = :recruiterId and campaignId = :campaignId")
	public Set<IMNotificationEntity> fetchIMNotificationEntitiesForCampaign(String recruiterId, UUID campaignId);
	
	@Query("from IMNotificationEntity where recruiterId = :recruiterId and candidateId = :candidateId")
	public Set<IMNotificationEntity> fetchIMNotificationBetweenRecruiterAndCandidate(String recruiterId, String candidateId);
	
	/**
	* Fetches all Notification for the Recruiter
	* @param recruiterId - Id of the Recruiter
	* @return IM Notification for the Recruiter
	*/
	default Set<IMNotification> fetchIMNotificationForRecruiter(String recruiterId){
		return this.fetchIMNotificationEntitiesForRecruiter(recruiterId).stream().map(IMNotificationEntity::fromEntity).collect(Collectors.toSet());
	}
	
	/**
	* Fetches all Notification for the Recruiter for a specific Campaign
	* @param recruiterId - Id of the Recruiter
	* @param campaignId  - Id of the Campaign
	* @return IM Notification for the Recruiter for a given campaign
	*/
	default Set<IMNotification> fetchIMNotificationForCampaign(String recruiterId, UUID campaignId){
		return this.fetchIMNotificationEntitiesForCampaign(recruiterId, campaignId).stream().map(IMNotificationEntity::fromEntity).collect(Collectors.toSet());
	}
	
	/**
	* Saves a new IM Notification
	* @param notification - Notification to be persisted
	*/
	default void addNotification(IMNotification notification) {
		this.save(IMNotificationEntity.toEntity(notification));
	}

	/**
	* Retrieves all the notifications from the Candidate to the Recruiter
	* @param recruiterId - 
	* @param candidateId
	* @return Notifications between Candidate and Recruiter
	*/
	default Set<IMNotification> fetchNotificationsBetweenRecruiterAndCandodate(String recruiterId, String candidateId){
		return this.fetchIMNotificationBetweenRecruiterAndCandidate(recruiterId, candidateId).stream().map(IMNotificationEntity::fromEntity).collect(Collectors.toSet());
	}
	
}
