package com.arenella.recruit.newsfeed.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.arenella.recruit.newsfeed.beans.NewsFeedItem;
import com.arenella.recruit.newsfeed.beans.NewsFeedItemFilters;
import com.arenella.recruit.newsfeed.beans.NewsFeedUserView;
import com.arenella.recruit.newsfeed.services.NewsFeedItemService;

/**
* Unit tests for the NewsFeedController class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
class NewsFeedControllerTest {

	@Mock
	private NewsFeedItemService 	mockService;
	
	@Mock
	private Principal				mockPrincipal;
	
	@InjectMocks
	private NewsFeedController 		controller 		= new NewsFeedController();
	
	/**
	* Tests fetching NewsItems
	* @throws Exception
	*/
	//@Test
	//void testFetchNewsItems() {
	
	//	final Set<NewsFeedItem.NEWSFEED_ITEM_TYPE> types = Set.of(NewsFeedItem.NEWSFEED_ITEM_TYPE.CANDIDATE_ADDED, NewsFeedItem.NEWSFEED_ITEM_TYPE.CANDIDATE_DELETED);
	//	final NewsFeedItem item1 = NewsFeedItem.builder().build();
	//	final NewsFeedItem item2 = NewsFeedItem.builder().build();
		
	//	ArgumentCaptor<NewsFeedItemFilters> filterArgCapt = ArgumentCaptor.forClass(NewsFeedItemFilters.class);
		
	//	Mockito.when(this.mockService.fetchNewsFeedItems(filterArgCapt.capture())).thenReturn(Set.of(item1, item2));
		
	//	ResponseEntity<Set<NewsFeedItemAPIOutbound>> response = controller.fetchNewsItems(types);
		
	//	assertTrue(filterArgCapt.getValue().getTypes().contains(NewsFeedItem.NEWSFEED_ITEM_TYPE.CANDIDATE_ADDED));
	//	assertTrue(filterArgCapt.getValue().getTypes().contains(NewsFeedItem.NEWSFEED_ITEM_TYPE.CANDIDATE_DELETED));
		
	//	Mockito.verify(this.mockService).fetchNewsFeedItems(Mockito.any(NewsFeedItemFilters.class));
		
	//	assertEquals(HttpStatus.OK, response.getStatusCode());
	//	assertEquals(2, response.getBody().size());
		
	//}
	
	/**
	* Tests adding updating of Users last view of the NewsFeed
	* @throws Exception
	*/
	//@Test
	//void testUpdateNewsFeedUserViewLastViewed() {
		
	//	ResponseEntity<Void> response = this.controller.updateNewsFeedUserViewLastViewed(this.mockPrincipal);
		
	//	assertEquals(HttpStatus.OK, response.getStatusCode());
		
	//	Mockito.verify(this.mockService).saveNewsFeedUserView(mockPrincipal);
		
	//}
	
	/**
	* Tests retrieval of NewsFeedView information for user
	* @throws Exception
	*/
	//@Test
	//void fetchNewsFeedUserView() {
		
	//	final String userId = "4455";
		
	//	Mockito.when(this.mockPrincipal.getName()).thenReturn(userId);
	//	Mockito.when(this.mockService.getNewsFeedUserView(userId)).thenReturn(new NewsFeedUserView(userId, LocalDateTime.of(2024, 2, 10, 11,11,11)));
		
	//	ResponseEntity<NewsFeedUserViewAPIOutbound> result = this.controller.fetchNewsFeedUserView(this.mockPrincipal);
		
	//	assertEquals(userId, result.getBody().getUserId());
	//	assertEquals(HttpStatus.OK, result.getStatusCode());
		
	//}
	
}