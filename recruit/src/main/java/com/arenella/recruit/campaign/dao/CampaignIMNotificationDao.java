package com.arenella.recruit.campaign.dao;

import java.util.UUID;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

/**
* Repository for IMNotifications 
*/
@Repository
public interface CampaignIMNotificationDao extends ListCrudRepository<IMNotificationEntity, UUID>{

}
