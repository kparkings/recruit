package com.arenella.recruit.newsfeed.controllers;

import java.security.Principal;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.arenella.recruit.newsfeed.beans.NewsFeedItem;
import com.arenella.recruit.newsfeed.beans.NewsFeedItemFilters;
import com.arenella.recruit.newsfeed.services.NewsFeedItemService;

/**
* Rest API for NewsFeedItems
* @author K Parkings
*/
@RestController
public class NewsFeedController {

	@Autowired
	private NewsFeedItemService newsFeedItemService;
	
	/**
	* Returns the NewsFeedItems
	* @param types - Types to filter on if any
	* @return NewsFeedItems
	*/
	@PreAuthorize("hasRole('ROLE_RECRUITER') OR hasRole('ROLE_ADMIN')")
	@GetMapping(value="/newsfeeditem")
	public ResponseEntity<Set<NewsFeedItemAPIOutbound>> fetchNewsItems(@RequestParam(required = false) Set<NewsFeedItem.NEWSFEED_ITEM_TYPE> types){
		
		NewsFeedItemFilters filters = 
				NewsFeedItemFilters
				.builder()
					.maxResults(200)
					.types(types)
				.build();
		
		
		return ResponseEntity.status(HttpStatus.OK).body(newsFeedItemService.fetchNewsFeedItems(filters).stream().map(NewsFeedItemAPIOutbound::convertFromDomain).collect(Collectors.toCollection(LinkedHashSet::new)));
	
	}
	
	/**
	* Fetches the NewsFeedUserView view information for a specific User
	* @param principal - Currently logged in user
	* @return view information
	*/
	@GetMapping(value="/newsfeeduserview")
	@PreAuthorize("hasRole('ROLE_RECRUITER') OR hasRole('ROLE_ADMIN') OR hasRole('ROLE_CANDIDATE')")
	public ResponseEntity<NewsFeedUserViewAPIOutbound> fetchNewsFeedUserView(Principal principal) {
		return ResponseEntity.ok().body(NewsFeedUserViewAPIOutbound.convertFromDomain(this.newsFeedItemService.getNewsFeedUserView(principal.getName())));
	}
	
	/**
	* Updates the last time the User viewed the Newsfeed
	* @return ResponseEntity
	*/
	@PreAuthorize("hasRole('ROLE_RECRUITER') OR hasRole('ROLE_ADMIN') OR hasRole('ROLE_CANDIDATE')")
	@PutMapping("/newsfeeduserview")
	public ResponseEntity<Void> updateNewsFeedUserViewLastViewed(Principal principal){
		this.newsFeedItemService.saveNewsFeedUserView(principal);
		return ResponseEntity.ok().build();
	}
	
}