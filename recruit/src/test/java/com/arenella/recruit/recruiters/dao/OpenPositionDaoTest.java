package com.arenella.recruit.recruiters.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.arenella.recruit.recruiters.beans.OpenPosition;
import com.arenella.recruit.recruiters.entities.OpenPositionEntity;

@ExtendWith(MockitoExtension.class)
public class OpenPositionDaoTest {
	
	private static final UUID id1 = UUID.randomUUID();
	private static final UUID id2 = UUID.randomUUID();
	private static final UUID id3 = UUID.randomUUID();
	
	private static final LocalDate created1 = LocalDate.of(2001, 1, 1);
	private static final LocalDate created2 = LocalDate.of(2003, 1, 1);
	private static final LocalDate created3 = LocalDate.of(2002, 1, 1);
	
	/**
	* Sets up test version of Interface
	*/
	private OpenPositionDao	dao = new OpenPositionDao() {

		@Override
		public <S extends OpenPositionEntity> S save(S entity) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public <S extends OpenPositionEntity> Iterable<S> saveAll(Iterable<S> entities) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Optional<OpenPositionEntity> findById(UUID id) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean existsById(UUID id) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public Iterable<OpenPositionEntity> findAllById(Iterable<UUID> ids) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long count() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void deleteById(UUID id) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void delete(OpenPositionEntity entity) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void deleteAll(Iterable<? extends OpenPositionEntity> entities) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void deleteAll() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Iterable<OpenPositionEntity> findAll() {

			OpenPositionEntity c1 = OpenPositionEntity.builder().id(id1).created(created1).build();
			OpenPositionEntity c2 = OpenPositionEntity.builder().id(id2).created(created2).build();
			OpenPositionEntity c3 = OpenPositionEntity.builder().id(id3).created(created3).build();
			
			return Set.of(c1,c2,c3);
		}
	};
	
	/**
	* Tests retrieval of OpenPositions
	* @throws Exception
	*/
	@Test
	public void testFetchOOpenPositions() throws Exception{
		
		Set<OpenPosition> openPositions = this.dao.findAllOpenPositions();
		
		assertEquals(id2, (((OpenPosition)openPositions.toArray()[0]).getId()));
		assertEquals(id3, (((OpenPosition)openPositions.toArray()[1]).getId()));
		assertEquals(id1, (((OpenPosition)openPositions.toArray()[2]).getId()));
		
	}
	
}
