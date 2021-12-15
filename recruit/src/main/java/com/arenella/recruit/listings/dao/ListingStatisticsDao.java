package com.arenella.recruit.listings.dao;

import org.springframework.data.repository.CrudRepository;

/**
* Defines repository for working with ListingViewedEventEntity
* @author K Parkings
*/
public interface ListingStatisticsDao extends CrudRepository<ListingViewedEventEntity,String>{

}
