package com.arenella.recruit.listings.utils;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arenella.recruit.listings.beans.ListingFilter;
import com.arenella.recruit.listings.dao.ListingDao;
import com.arenella.recruit.listings.repos.ListingRepository;
import com.arenella.recruit.listings.repos.entities.ListingDocument;

@Component
public class ListingReindexer {

	@Autowired
	private ListingDao listingDao;
	
	@Autowired
	private ListingRepository listingRepo;
	
	@PostConstruct
	public void doReindex() {
		
		this.listingDao
				.findAllListings(ListingFilter.builder().build()).stream().forEach(l -> {
					
					ListingDocument doc = ListingDocument.toEntity(l);
					
					try {
						listingRepo.save(doc);
						System.out.println("SUCCESS" + doc.getListingId() + " Views= " + l.getViews().size());
					}catch(Exception e) {
						System.out.println("FAILURE" + doc.getListingId() + " Views= " + l.getViews().size());
						
					}
					});
		
	}
	
}