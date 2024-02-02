package com.arenella.recruit.newsfeed.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
import com.arenella.recruit.newsfeed.dao.NewsFeedItemDao;

/**
* Unit tests for the NewsFeedItemServiceImpl class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
public class NewsFeedItemServiceImplTest {

	@Mock
	private NewsFeedItemDao mockDao;
	
	@InjectMocks
	private NewsFeedItemServiceImpl service = new NewsFeedItemServiceImpl();
	
	/**
	* Tests adding a new Item
	* @throws Exception
	*/
	@Test
	public void testAddNewsFeedItem() throws Exception{
		
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
	public void testAddNewsFeedItem_already_exists() throws Exception{
		
		final UUID id = UUID.randomUUID();
		
		Mockito.when(this.mockDao.existsById(id)).thenReturn(true);
		
		assertThrows(IllegalArgumentException.class, () -> {
			this.service.addNewsFeedItem(NewsFeedItem.builder().id(id).build());
		});
				
	}
	
	/**
	* Tests retrieval of NewsFeedItems
	* @throws Exception
	*/
	@Test
	public void testFetchNewsFeedItems() throws Exception{
		
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
	public void testDeleteNewsFeedItem() throws Exception {
		
		final UUID id = UUID.randomUUID();
		
		this.service.deleteNewsFeedItem(id);
		
		Mockito.verify(this.mockDao).deleteById(id);
		
	}
	
	/**
	* Tests deletion of NewsFeedItem referencing a specific User
	* @throws Exception
	*/
	@Test
	public void testDeleteAllNewsFeedItemsForReferencedUserId() throws Exception{
		
		final String userId = "123";
		
		this.service.deleteAllNewsFeedItemsForReferencedUserId(userId);
		
		Mockito.verify(this.mockDao).deleteAllNewsFeedItemsForReferencedUserId(userId);
		
	}
	
}