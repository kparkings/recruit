package com.arenella.recruit.listings.dao;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

/**
* Repository for ListingAlertEntities
* @author K Parkings
*/
public interface ListingAlertDao extends CrudRepository<ListingAlertEntity, UUID>{

}
