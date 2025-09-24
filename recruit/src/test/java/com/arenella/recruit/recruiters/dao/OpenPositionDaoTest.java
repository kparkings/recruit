package com.arenella.recruit.recruiters.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.arenella.recruit.recruiters.beans.OpenPosition;
import com.arenella.recruit.recruiters.entities.OpenPositionEntity;

@ExtendWith(MockitoExtension.class)
class OpenPositionDaoTest {
	
	private static final UUID id1 = UUID.randomUUID();
	private static final UUID id2 = UUID.randomUUID();
	private static final UUID id3 = UUID.randomUUID();
	
	private static final LocalDateTime created1 = LocalDateTime.of(2001, 1, 1, 0, 0, 0);
	private static final LocalDateTime created2 = LocalDateTime.of(2003, 1, 1, 0, 0, 0);
	private static final LocalDateTime created3 = LocalDateTime.of(2002, 1, 1, 0, 0, 0);
	
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

		@Override
		public Set<OpenPositionEntity> findAllByRecruiterId(String recruiterId) {
			
			OpenPositionEntity c1 = OpenPositionEntity.builder().id(id1).created(created1).build();
			OpenPositionEntity c2 = OpenPositionEntity.builder().id(id2).created(created2).build();
			OpenPositionEntity c3 = OpenPositionEntity.builder().id(id3).created(created3).build();
			
			return Set.of(c1,c2,c3);
		}

		@Override
		public void deleteAllById(Iterable<? extends UUID> ids) {
			// TODO Auto-generated method stub
			
		}
	};
	
	/**
	* Tests retrieval of OpenPositions
	* @throws Exception
	*/
	@Test
	void testFetchOOpenPositions() {
		
		Set<OpenPosition> openPositions = this.dao.findAllOpenPositions();
		
		assertEquals(id2, (((OpenPosition)openPositions.toArray()[0]).getId()));
		assertEquals(id3, (((OpenPosition)openPositions.toArray()[1]).getId()));
		assertEquals(id1, (((OpenPosition)openPositions.toArray()[2]).getId()));
		
	}

	/**
	* Tests retrieval of OpenPositions by Recruiter Id
	* @throws Exception
	*/
	@Test
	void testFetchOOpenPositionsByRecruiterId() {
		
		Set<OpenPosition> openPositions = this.dao.findAllOpenPositionsByRecruiterId("aRecruiterId");
		
		assertEquals(id2, (((OpenPosition)openPositions.toArray()[0]).getId()));
		assertEquals(id3, (((OpenPosition)openPositions.toArray()[1]).getId()));
		assertEquals(id1, (((OpenPosition)openPositions.toArray()[2]).getId()));
		
	}
	
}
