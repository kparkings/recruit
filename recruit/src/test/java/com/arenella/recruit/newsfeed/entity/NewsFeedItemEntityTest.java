package com.arenella.recruit.newsfeed.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.newsfeed.beans.NewsFeedItem;
import com.arenella.recruit.newsfeed.beans.NewsFeedItem.NEWSFEED_ITEM_TYPE;
import com.arenella.recruit.newsfeed.beans.NewsFeedItemLine;

/**
* Unit tests for the NewsFeedItem class
* @author K Parkings
*/
public class NewsFeedItemEntityTest {

	private static final UUID 							ID 				= UUID.randomUUID();
	private static final LocalDateTime 					CREATED 		= LocalDateTime.of(2024, 1, 31, 23, 06);
	private static final NEWSFEED_ITEM_TYPE 			ITEM_TYPE 		= NEWSFEED_ITEM_TYPE.CANDIDATE_BECAME_AVAILABLE;
	private static final Set<NewsFeedItemLineEntity> 	LINES 			= new LinkedHashSet<>();
	
	/**
	* Tests construction via Builder 
	*/
	@Test
	public void testBuilder() {
		
		NewsFeedItemEntity item = NewsFeedItemEntity.builder().id(ID).created(CREATED).itemType(ITEM_TYPE).lines(LINES).build();
		
		assertEquals(ID, 		item.getId());
		assertEquals(CREATED, 	item.getCreated());
		assertEquals(ITEM_TYPE, item.getItemType());
		assertEquals(LINES, 	item.getLines());
	}
	
	/**
	* Tests conversion from Entity to Domain representation
	* @throws Exception
	*/
	@Test
	public void testConvertFromEntity() throws Exception{
		
		final Set<NewsFeedItemLineEntity> lines = Set.of(NewsFeedItemLineEntity.builder().build(), NewsFeedItemLineEntity.builder().build());
		
		NewsFeedItemEntity entity = NewsFeedItemEntity.builder().id(ID).created(CREATED).itemType(ITEM_TYPE).lines(lines).build();
		
		NewsFeedItem item = NewsFeedItemEntity.convertFromEntity(entity); 
		
		assertEquals(ID, 			item.getId());
		assertEquals(CREATED, 		item.getCreated());
		assertEquals(ITEM_TYPE, 	item.getItemType());
		assertEquals(lines.size(), 	item.getLines().size());
		
		NewsFeedItemLine line = item.getLines().stream().findFirst().get();
		
		assertNotNull(line);
		
	}
	
	/**
	* Tests conversion from Domain to Entity representation
	* @throws Exception
	*/
	@Test
	public void testConvertToEntity() throws Exception{
		
		final Set<NewsFeedItemLine> lines = Set.of(NewsFeedItemLine.builder().build(), NewsFeedItemLine.builder().build());
		
		NewsFeedItem item = NewsFeedItem.builder().id(ID).created(CREATED).itemType(ITEM_TYPE).lines(lines).build();
		
		NewsFeedItemEntity entity = NewsFeedItemEntity.convertToEntity(item); 
		
		assertEquals(ID, 			entity.getId());
		assertEquals(CREATED, 		entity.getCreated());
		assertEquals(ITEM_TYPE, 	entity.getItemType());
		assertEquals(lines.size(), 	entity.getLines().size());
		
		NewsFeedItemLineEntity line = entity.getLines().stream().findFirst().get();
		
		assertNotNull(line);
		
	}
	
}
