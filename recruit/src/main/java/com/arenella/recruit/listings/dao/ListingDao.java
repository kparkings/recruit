package com.arenella.recruit.listings.dao;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

/**
* Repository for Listings
* @author K Parkings
*/
public interface ListingDao extends CrudRepository<ListingEntity, UUID>{

}
