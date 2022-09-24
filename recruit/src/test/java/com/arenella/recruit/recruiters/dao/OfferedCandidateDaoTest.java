package com.arenella.recruit.recruiters.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.arenella.recruit.recruiters.beans.OfferedCandidate;
import com.arenella.recruit.recruiters.entities.OfferedCandidateEntity;

@ExtendWith(MockitoExtension.class)
public class OfferedCandidateDaoTest {
	
	private static final UUID id1 = UUID.randomUUID();
	private static final UUID id2 = UUID.randomUUID();
	private static final UUID id3 = UUID.randomUUID();
	
	private static final LocalDate created1 = LocalDate.of(2001, 1, 1);
	private static final LocalDate created2 = LocalDate.of(2003, 1, 1);
	private static final LocalDate created3 = LocalDate.of(2002, 1, 1);
	
	
	private OfferedCandidateDao	dao = new OfferedCandidateDao() {

		@Override
		public <S extends OfferedCandidateEntity> S save(S entity) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public <S extends OfferedCandidateEntity> Iterable<S> saveAll(Iterable<S> entities) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Optional<OfferedCandidateEntity> findById(UUID id) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean existsById(UUID id) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public Iterable<OfferedCandidateEntity> findAll() {
			
			OfferedCandidateEntity c1 = OfferedCandidateEntity.builder().id(id1).created(created1).build();
			OfferedCandidateEntity c2 = OfferedCandidateEntity.builder().id(id2).created(created2).build();
			OfferedCandidateEntity c3 = OfferedCandidateEntity.builder().id(id3).created(created3).build();
			
			return Set.of(c1,c2,c3);
			
		}

		@Override
		public Iterable<OfferedCandidateEntity> findAllById(Iterable<UUID> ids) {
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
		public void delete(OfferedCandidateEntity entity) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void deleteAll(Iterable<? extends OfferedCandidateEntity> entities) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void deleteAll() {
			// TODO Auto-generated method stub
			
		}
	};
	
	/**
	* Tests retrieval of OfferedCandidates
	* @throws Exception
	*/
	@Test
	public void testFetchOfferedCandidates() throws Exception{
		
		Set<OfferedCandidate> candidates = this.dao.findAllOfferedCandidates();
		
		assertEquals(id2, (((OfferedCandidate)candidates.toArray()[0]).getId()));
		assertEquals(id3, (((OfferedCandidate)candidates.toArray()[1]).getId()));
		assertEquals(id1, (((OfferedCandidate)candidates.toArray()[2]).getId()));
		
	}
	
}
