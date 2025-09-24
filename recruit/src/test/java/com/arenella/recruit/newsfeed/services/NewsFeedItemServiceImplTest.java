package com.arenella.recruit.newsfeed.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.arenella.recruit.newsfeed.beans.NewsFeedItem;
import com.arenella.recruit.newsfeed.beans.NewsFeedItemFilters;
import com.arenella.recruit.newsfeed.beans.NewsFeedUserView;
import com.arenella.recruit.newsfeed.dao.NewsFeedItemDao;
import com.arenella.recruit.newsfeed.dao.NewsFeedUserViewsDao;

/**
* Unit tests for the NewsFeedItemServiceImpl class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
class NewsFeedItemServiceImplTest {

	@Mock
	private NewsFeedItemDao 		mockDao;
	
	@Mock
	private Principal				mockPrincipal;
	
	@Mock
	private NewsFeedUserViewsDao 	mockNewsFeedItemViewDao;
	
	@InjectMocks
	private NewsFeedItemServiceImpl service = new NewsFeedItemServiceImpl();
	
	/**
	* Tests adding a new Item
	* @throws Exception
	*/
	@Test
	void testAddNewsFeedItem() {
		
		final UUID 				id 		= UUID.randomUUID();
		final NewsFeedItem 		item 	= NewsFeedItem.builder().id(id).build();
		
		Mockito.when(this.mockDao.existsById(id)).thenReturn(false);
		
		service.addNewsFeedItem(item);
		
		Mockito.verify(this.mockDao).saveNewsFeedItem(item);
		
	}
	
	/**
	* Tests exception is thrown if an existing Item is added a second time
	* @throws Exception
	*/
	@Test
	void testAddNewsFeedItem_already_exists() {
		
		final UUID id = UUID.randomUUID();
		
		Mockito.when(this.mockDao.existsById(id)).thenReturn(true);
		
		NewsFeedItem newsFeedItem = NewsFeedItem.builder().id(id).build();
		
		assertThrows(IllegalArgumentException.class, () -> {
			this.service.addNewsFeedItem(newsFeedItem);
		});
				
	}
	
	/**
	* Tests retrieval of NewsFeedItems
	* @throws Exception
	*/
	@Test
	void testFetchNewsFeedItems() {
		
		final Set<NewsFeedItem> items = Set.of(NewsFeedItem.builder().build(), NewsFeedItem.builder().build());
		
		Mockito.when(this.mockDao.fetchNewsFeedItem(Mockito.any())).thenReturn(items);
		
		Set<NewsFeedItem> results = this.service.fetchNewsFeedItems(NewsFeedItemFilters.builder().build());
		
		assertEquals(items.size(), results.size());
		
	}
	
	/**
	* Tests deletion of NewsFeedItem
	* @throws Exception
	*/
	@Test
	void testDeleteNewsFeedItem() {
		
		final UUID id = UUID.randomUUID();
		
		this.service.deleteNewsFeedItem(id);
		
		Mockito.verify(this.mockDao).deleteById(id);
		
	}
	
	/**
	* Tests deletion of NewsFeedItem referencing a specific User
	* @throws Exception
	*/
	@Test
	void testDeleteAllNewsFeedItemsForReferencedUserId() {
		
		final String userId = "123";
		
		this.service.deleteAllNewsFeedItemsForReferencedUserId(userId);
		
		Mockito.verify(this.mockDao).deleteAllNewsFeedItemsForReferencedUserId(userId);
		
	}
	
	/**
	* Tests persisting NewsFeedUserView
	* @throws Exception
	*/
	@Test
	void testSaveNewsFeedUserView() {
		
		Mockito.when(this.mockPrincipal.getName()).thenReturn("1234");
		
		this.service.saveNewsFeedUserView(this.mockPrincipal);
	
		Mockito.verify(this.mockNewsFeedItemViewDao).saveNewsFeedUserView(Mockito.any(NewsFeedUserView.class));
		
	}
	
	/**
	* Tests retrieval of View information for User
	* @throws Exception
	*/
	@Test
	void testGetNewsFeedUserView() {
		
		final LocalDateTime 	lastView 	= LocalDateTime.of(2024, 2, 8, 10,11);
		final String 			userId 		= "1234";
		
		Mockito.when(this.mockNewsFeedItemViewDao.existsById(userId)).thenReturn(true);
		
		NewsFeedUserView newsFeedUserView = new NewsFeedUserView(userId, lastView);
		
		Mockito.when(this.mockNewsFeedItemViewDao.getNewsFeedUserView(userId)).thenReturn(Optional.of(newsFeedUserView));
		
		NewsFeedUserView result = this.service.getNewsFeedUserView(userId);
	
		assertEquals(userId, 	result.getUserId());
		assertEquals(lastView, 	result.getLastViewed());
		
		Mockito.verify(this.mockNewsFeedItemViewDao, Mockito.never()).saveNewsFeedUserView(Mockito.any());
		
	}
	
	/**
	* Tests If record for user does not already exist that one is created and returned
	* @throws Exception
	*/
	@Test
	void testGetNewsFeedUserView_unknownUser() {
		
		final String 			userId 		= "1234";
		
		Mockito.when(this.mockNewsFeedItemViewDao.existsById(userId)).thenReturn(false);
		
		Mockito.when(this.mockNewsFeedItemViewDao.getNewsFeedUserView(userId)).thenReturn(Optional.of(new NewsFeedUserView(userId, LocalDateTime.of(2024, 2, 11, 10, 8))));
		
		this.service.getNewsFeedUserView(userId);

		Mockito.verify(this.mockNewsFeedItemViewDao).saveNewsFeedUserView(Mockito.any());
		
	}
	
	/**
	* Tests deletion of NewsFeed view record of the User
	* @throws Exception
	*/
	@Test
	void testDeleteNewsFeedUserView() {
		
		final String 			userId 		= "1234";
		
		Mockito.when(this.mockNewsFeedItemViewDao.existsById(userId)).thenReturn(true);
		
		this.service.deleteNewsFeedUserView(userId);
		
		Mockito.verify(this.mockNewsFeedItemViewDao).deleteNewsFeedUserView(userId);
		
	}
	
	/**
	* Tests that deletion is only carried out if record for User exists
	* @throws Exception
	*/
	@Test
	void testDeleteNewsFeedUserView_no_recrd() {
		
		final String 			userId 		= "1234";
		
		Mockito.when(this.mockNewsFeedItemViewDao.existsById(userId)).thenReturn(false);
		
		this.service.deleteNewsFeedUserView(userId);
		
		Mockito.verify(this.mockNewsFeedItemViewDao, Mockito.never()).deleteNewsFeedUserView(userId);
		
	} 
	
}