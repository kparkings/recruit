package com.arenella.recruit.curriculum.services;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.security.Principal;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.arenella.recruit.adapters.events.CreditsAssignedEvent;
import com.arenella.recruit.curriculum.adapters.ExternalEventPublisher;
import com.arenella.recruit.curriculum.beans.Curriculum;
import com.arenella.recruit.curriculum.beans.PendingCurriculum;
import com.arenella.recruit.curriculum.beans.RecruiterCredit;
import com.arenella.recruit.curriculum.controllers.CurriculumUpdloadDetails;
import com.arenella.recruit.curriculum.dao.CurriculumDao;
import com.arenella.recruit.curriculum.dao.CurriculumDownloadedEventDao;
import com.arenella.recruit.curriculum.dao.CurriculumRecruiterCreditDao;
import com.arenella.recruit.curriculum.dao.PendingCurriculumDao;
import com.arenella.recruit.curriculum.entity.CurriculumDownloadedEventEntity;
import com.arenella.recruit.curriculum.entity.CurriculumEntity;
import com.arenella.recruit.curriculum.entity.PendingCurriculumEntity;
import com.arenella.recruit.curriculum.enums.FileType;

/**
* Unit tests for the CurriculumServiceImpl class
* @author K Parkings
*/
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
public class CurriculumServiceImplTest {

	@Mock
	private 				CurriculumDao			 		mockCurriculumDao;

	@Mock
	private 				PendingCurriculumDao			mockPendingCurriculumDao;
	
	@Mock
	private					CurriculumDownloadedEventDao	mockCurriculumDownloadedEventDao;
	
	@Mock
	private					Authentication					mockAuthentication;
	
	@Mock
	private 				ExternalEventPublisher			mockExternalEventPublisher;
	
	@Mock
	private 				SecurityContext					mockSecurityContext;
	
	@Mock
	private 				Principal						mockPrincipal;
	
	@Mock
	private					CurriculumRecruiterCreditDao	mockCreditDao;
	
	@InjectMocks
	private static final 	CurriculumServiceImpl 	service 			= new CurriculumServiceImpl();
	
	final 	String 		curriculumId 	= "501";
	final 	FileType 	fileType 		= FileType.doc;
	final 	byte[] 		file 			= new byte[] {};
	
	/**
	* Sets up test environment 
	*/
	@BeforeEach
	public void init() throws Exception{
		SecurityContextHolder.setContext(mockSecurityContext);
	}
	
	/**
	* Happy Path test for persisting a new Curriculum
	* @throws Exception
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testPersistCurriculum_admin() throws Exception{
		
		ArgumentCaptor<CurriculumEntity> captorEntity = ArgumentCaptor.forClass(CurriculumEntity.class);
		
		final Collection authorities = new HashSet<>();
		
		authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		
		Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		Mockito.when(mockAuthentication.getAuthorities()).thenReturn(authorities);
		
		Curriculum curriculum = Curriculum
										.builder()
											.id(curriculumId)
											.fileType(fileType)
											.file(file)
										.build();
		
		String returnedId = service.persistCurriculum(curriculum);
		
		Mockito.verify(mockCurriculumDao).save(captorEntity.capture());
		
		assertEquals(curriculumId, 					returnedId);
		assertEquals(Long.valueOf(curriculumId), 	captorEntity.getValue().getCurriculumId());
		assertEquals(fileType, 						captorEntity.getValue().getFileType());
		
		assertNotNull(captorEntity.getValue().getFile());
		assertTrue(captorEntity.getValue().getOwnerId().isPresent());
	}
	
	/**
	* Happy Path test for persisting a new Curriculum
	* @throws Exception
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testPersistCurriculum_candidate() throws Exception{
		
		ArgumentCaptor<CurriculumEntity> captorEntity = ArgumentCaptor.forClass(CurriculumEntity.class);
		
		final Collection 	authorities = new HashSet<>();
	
		authorities.add(new SimpleGrantedAuthority("ROLE_CANDIDATE"));
		
		Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		Mockito.when(mockAuthentication.getAuthorities()).thenReturn(authorities);
		
		Curriculum curriculum = Curriculum
										.builder()
											.id(curriculumId)
											.fileType(fileType)
											.file(file)
										.build();
		
		String returnedId = service.persistCurriculum(curriculum);
		
		Mockito.verify(mockCurriculumDao).save(captorEntity.capture());
		
		assertEquals(curriculumId, 					returnedId);
		assertEquals(Long.valueOf(curriculumId), 	captorEntity.getValue().getCurriculumId());
		assertEquals(fileType, 						captorEntity.getValue().getFileType());
		
		assertNotNull(captorEntity.getValue().getFile());
		assertTrue(captorEntity.getValue().getOwnerId().isEmpty());
	}
	
	/**
	* Happy Path test for persisting a new Curriculum
	* @throws Exception
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testPersistCurriculum_recruiter() throws Exception{
		
		ArgumentCaptor<CurriculumEntity> captorEntity = ArgumentCaptor.forClass(CurriculumEntity.class);
		
		final Collection 	authorities = new HashSet<>();
		final String 		ownerId 	= "a1";
		
		authorities.add(new SimpleGrantedAuthority("ROLE_RECRUITER"));
		
		Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		Mockito.when(mockAuthentication.getAuthorities()).thenReturn(authorities);
		Mockito.when(this.mockAuthentication.getPrincipal()).thenReturn(ownerId);
		
		Curriculum curriculum = Curriculum
										.builder()
											.id(curriculumId)
											.fileType(fileType)
											.file(file)
											.ownerId(ownerId)
										.build();
		
		String returnedId = service.persistCurriculum(curriculum);
		
		Mockito.verify(mockCurriculumDao).save(captorEntity.capture());
		
		assertEquals(curriculumId, 					returnedId);
		assertEquals(Long.valueOf(curriculumId), 	captorEntity.getValue().getCurriculumId());
		assertEquals(fileType, 						captorEntity.getValue().getFileType());
		
		assertNotNull(captorEntity.getValue().getFile());
		assertEquals(ownerId, captorEntity.getValue().getOwnerId().get());
	}
	
	/**
	* Happy Path test for persisting a new Curriculum
	* @throws Exception
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testPersistCurriculum_candidate_own_curriculum() throws Exception{
		
		ArgumentCaptor<CurriculumEntity> captorEntity = ArgumentCaptor.forClass(CurriculumEntity.class);
		
		final Collection 	authorities 	= new HashSet<>();
		
		authorities.add(new SimpleGrantedAuthority("ROLE_CANDIDATE"));
		
		Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		Mockito.when(mockAuthentication.getAuthorities()).thenReturn(authorities);
		
		Curriculum curriculum = Curriculum
										.builder()
											.id(curriculumId)
											.fileType(fileType)
											.file(file)
										.build();
		
		String returnedId = service.persistCurriculum(curriculum);
		
		Mockito.verify(mockCurriculumDao).save(captorEntity.capture());
		
		assertEquals(curriculumId, 					returnedId);
		assertEquals(Long.valueOf(curriculumId), 	captorEntity.getValue().getCurriculumId());
		assertEquals(fileType, 						captorEntity.getValue().getFileType());
		
		assertNotNull(captorEntity.getValue().getFile());
		
	}
	
	/**
	* Tests happy path for fetching an existing Curriculum
	* @throws Exception
	*/
	@Test
	public void testFetchCurriculum() throws Exception {
		
		CurriculumEntity curriculumEntity = CurriculumEntity
				.builder()
					.curriculumId(Long.valueOf(curriculumId))
					.fileType(fileType)
					.file(file)
				.build();
		
		Mockito.when(this.mockCurriculumDao.findById(Long.valueOf(curriculumId))).thenReturn(Optional.of(curriculumEntity));
		
		Curriculum curriculum = service.fetchCurriculum(curriculumId);
		
		assertEquals(curriculumId, 					curriculum.getId().get());
		assertEquals(fileType, 						curriculum.getFileType());
		
		assertNotNull(curriculum.getFile());
		
	}
	
	/**
	* Tests case where no Curriculum entities are in the DB. Should return
	* 1
	* @throws Exception
	*/
	@Test
	public void testGetNextCurriculumId_noCurriculums() throws Exception{
		
		Mockito.when(mockCurriculumDao.findTopByOrderByCurriculumIdDesc()).thenReturn(Optional.empty());
		
		assertEquals(1, service.getNextCurriculumId());
		
	}
	
	/**
	* Tests case that Curriculums are in the DB. Should return the id of the 
	* latest Curriculum + 1
	* @throws Exception
	*/
	@Test
	public void testGetNextCurriculumId_existingCurriculums() throws Exception{
		
		final long lastCurriculumId = 500;
		
		Mockito.when(mockCurriculumDao.findTopByOrderByCurriculumIdDesc()).thenReturn(Optional.of(CurriculumEntity.builder().curriculumId(lastCurriculumId).build()));
		
		assertEquals(lastCurriculumId + 1, service.getNextCurriculumId());
		
	}
	
	/**
	* Tests that method leads to an event being persisted for 
	* the curriculum 
	* @throws Exception
	*/
	@Test
	public void testLogCurriculumDownloadedEvent() throws Exception{
		
		final String curriculumId = "100";
	
		Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		
		ArgumentCaptor<CurriculumDownloadedEventEntity> captor = ArgumentCaptor.forClass(CurriculumDownloadedEventEntity.class);
		
		Mockito.when(mockAuthentication.getPrincipal()).thenReturn("kparkings");
		Mockito.when(mockAuthentication.getAuthorities()).thenReturn(List.of());
		Mockito.when(mockCurriculumDownloadedEventDao.save(captor.capture())).thenReturn(null);
		
		service.logCurriculumDownloadedEvent(curriculumId);
		
		Mockito.verify(mockCurriculumDownloadedEventDao).save(Mockito.any(CurriculumDownloadedEventEntity.class));
		
		assertEquals(curriculumId, captor.getValue().getCurriculumId());
		
	}
	
	/**
	* Tests if there is a Exception the response is still returned with the id
	* for the Curriculum
	* @throws Exception
	*/
	@Test
	public void testExtractDetails_failure_returnsMinumum() throws Exception {
		
		final String 		curriculumId 	= "897";
		final FileType 		fileType 		= FileType.pdf;
		final byte[] 		file 			= new byte[] {};
		
		CurriculumUpdloadDetails extractedDetails = service.extractDetails(curriculumId, fileType, file);
		
		assertEquals(curriculumId, extractedDetails.getId());
		
	}
	
	/**
	* Test persistence of PendingCurriclum 
	* @throws Exception
	*/
	@Test
	public void testPersistPendingCurriculum() throws Exception {
		
		ArgumentCaptor<PendingCurriculumEntity> pendingCurriculumCaptor = ArgumentCaptor.forClass(PendingCurriculumEntity.class);
		
		final UUID 		id 			= UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
		final FileType 	fileType 	= FileType.docx;
		final byte[] 	file		= new byte[] {};
		
		PendingCurriculum curriculum = PendingCurriculum
													.builder()
														.id(id)
														.fileType(fileType)
														.file(file)
													.build();
		
		Mockito.when(mockPendingCurriculumDao.save(pendingCurriculumCaptor.capture())).thenReturn(null);
		
		service.persistPendingCurriculum(curriculum);
		
		Mockito.verify(mockPendingCurriculumDao).save(Mockito.any(PendingCurriculumEntity.class));
		
		assertEquals(id, 		pendingCurriculumCaptor.getValue().getCurriculumId());
		assertEquals(fileType, 	pendingCurriculumCaptor.getValue().getFileType());
		assertEquals(file, 		pendingCurriculumCaptor.getValue().getFile());
		
	}
	
	/**
	* Tests exception is thrown if an attempt is made to fetch a non existing
	* PendingCurriculum
	* @throws Exception
	*/
	@Test
	public void testFetchPendingCurriculum_unknownCurriculumId() throws Exception {
		
		assertThrows(IllegalArgumentException.class, () -> {
	        service.fetchPendingCurriculum(UUID.randomUUID());
	    });
		
	}
	
	/**
	* Tests successful retrieval of PendingCurriculum
	* @throws Exception
	*/
	@Test
	public void testFetchPendingCurriculum_knownCurriculumId() throws Exception {
		
		UUID id = UUID.randomUUID();
		
		PendingCurriculumEntity entity = PendingCurriculumEntity
														.builder()
															.curriculumId(id)
															.file(new byte[] {})
															.fileType(FileType.doc)
														.build();
		
		Mockito.when(mockPendingCurriculumDao.existsById(id)).thenReturn(true);
		Mockito.when(mockPendingCurriculumDao.findById(id)).thenReturn(Optional.of(entity));
		
		PendingCurriculum pendingCurriculum = service.fetchPendingCurriculum(id);
		
		assertEquals(pendingCurriculum.getId().get(), entity.getCurriculumId());
		assertEquals(pendingCurriculum.getFileType(), entity.getFileType());
		
	}
	
	/**
	* Tests Curriculum is deleted and Event is published
	* @throws Exception
	*/
	@Test
	public void testDeletePendingCurriculum() throws Exception {
		
		UUID id = UUID.randomUUID();
		
		Mockito.doNothing().when(mockPendingCurriculumDao).deleteById(id);
		Mockito.doNothing().when(mockExternalEventPublisher).publishPendingCurriculumDeletedEvent(id);
		
		service.deletePendingCurriculum(id);


		Mockito.verify(mockExternalEventPublisher).publishPendingCurriculumDeletedEvent(id);
		
	}
	
	/**
	* Tests deletion of a Curriculum
	* @throws Exception
	*/
	@Test
	public void testDeleteCurriculum() throws Exception {
		
		final long curriculumId = 123L;
		
		Mockito.when(this.mockCurriculumDao.existsById(curriculumId)).thenReturn(true);
		
		service.deleteCurriculum(curriculumId);
		
		Mockito.verify(this.mockCurriculumDao).deleteById(curriculumId);
		
	}
	
	/**
	* Tests deletion of a non existent Curriculum
	* @throws Exception
	*/
	@Test
	public void testDeleteCurriculum_non_existen_curriculum() throws Exception {
		
		final long curriculumId = 123L;
		
		Mockito.when(this.mockCurriculumDao.existsById(curriculumId)).thenReturn(false);
		
		service.deleteCurriculum(curriculumId);
		
		Mockito.verify(this.mockCurriculumDao, Mockito.never()).deleteById(curriculumId);
		
	}
	
	/**
	* Tests Exception thrown if Curriculum not found
	* @throws Exception
	*/
	@Test
	public void testUpdateCurriculum_does_not_exist() throws Exception {
		
		Curriculum curriculum = Curriculum
				.builder()
					.id(curriculumId)
					.fileType(fileType)
					.file(file)
				.build();
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			service.updateCurriculum(Long.valueOf(curriculumId), curriculum);
		});
		
	}
	
	/**
	* Tests Exception thrown if Curriculum being updates by Candidate
	* belongs to another Candidate
	* @throws Exception
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testUpdateCurriculum_candidate_user_other_candidates_curriculum() throws Exception {
		
		Curriculum curriculum = Curriculum
				.builder()
					.id(curriculumId)
					.fileType(fileType)
					.file(file)
					.ownerId("2L")
				.build();
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_CANDIDATE"));
		
		Mockito.when(this.mockCurriculumDao.findCurriculumById(Long.valueOf(curriculumId))).thenReturn(Optional.of(curriculum));

		Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		Mockito.when(mockAuthentication.getAuthorities()).thenReturn(authorities);
		Mockito.when(mockAuthentication.getPrincipal()).thenReturn("5L");
		Mockito.when(this.mockCurriculumDao.existsById(Long.valueOf(curriculumId))).thenReturn(true);
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			service.updateCurriculum(Long.valueOf(curriculumId), curriculum);
		});
		
	}
	
	/**
	* Tests Exception thrown if Curriculum being updates by Candidate
	* belongs to another Candidate
	* @throws Exception
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testUpdateCurriculum_recruiter_user_other_candidates_curriculum() throws Exception {
		
		Curriculum curriculum = Curriculum
				.builder()
					.id(curriculumId)
					.fileType(fileType)
					.file(file)
					.ownerId("2L")
				.build();
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_RECRUITER"));

		Mockito.when(this.mockCurriculumDao.findCurriculumById(Long.valueOf(curriculumId))).thenReturn(Optional.of(curriculum));

		Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		Mockito.when(mockAuthentication.getAuthorities()).thenReturn(authorities);
		Mockito.when(mockAuthentication.getPrincipal()).thenReturn("5L");
		Mockito.when(this.mockCurriculumDao.existsById(Long.valueOf(curriculumId))).thenReturn(true);
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			service.updateCurriculum(Long.valueOf(curriculumId), curriculum);
		});
		
	}
	
	/**
	* Tests Exception thrown if Curriculum being updates by Candidate
	* belongs to another Candidate
	* @throws Exception
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testUpdateCurriculum_not_candidate_not_admin() throws Exception {
		
		Curriculum curriculum = Curriculum
				.builder()
					.id(curriculumId)
					.fileType(fileType)
					.file(file)
				.build();
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_RECRUITER"));

		Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		
		Mockito.when(mockAuthentication.getPrincipal()).thenReturn(curriculumId);
		Mockito.when(this.mockCurriculumDao.existsById(Long.valueOf(curriculumId))).thenReturn(true);
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			service.updateCurriculum(Long.valueOf(curriculumId), curriculum);
		});
		
	}
	
	/**
	* Tests Exception thrown if Curriculum being updates by Candidate
	* belongs to another Candidate
	* @throws Exception
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testUpdateCurriculum_owning_canidate() throws Exception {
		
		Curriculum curriculum = Curriculum
				.builder()
					.id(curriculumId)
					.fileType(fileType)
					.file(file)
					.ownerId(curriculumId)
				.build();
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_CANDIDATE"));

		Mockito.when(this.mockCurriculumDao.findCurriculumById(Long.valueOf(curriculumId))).thenReturn(Optional.of(curriculum));

		Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		Mockito.when(mockAuthentication.getAuthorities()).thenReturn(authorities);
		Mockito.when(mockAuthentication.getPrincipal()).thenReturn(curriculumId);
		Mockito.when(this.mockCurriculumDao.existsById(Long.valueOf(curriculumId))).thenReturn(true);
		
		service.updateCurriculum(Long.valueOf(curriculumId), curriculum);
		
		Mockito.verify(this.mockCurriculumDao).updateCurriculum(curriculum);
		
	}
	
	/**
	* Tests Exception thrown if Curriculum being updates by Candidate
	* belongs to another Candidate
	* @throws Exception
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testUpdateCurriculum_admin() throws Exception {
		
		Curriculum curriculum = Curriculum
				.builder()
					.id(curriculumId)
					.fileType(fileType)
					.file(file)
				.build();
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

		Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		Mockito.when(mockAuthentication.getAuthorities()).thenReturn(authorities);
		Mockito.when(mockAuthentication.getPrincipal()).thenReturn(2L);
		Mockito.when(this.mockCurriculumDao.existsById(Long.valueOf(curriculumId))).thenReturn(true);
		
		service.updateCurriculum(Long.valueOf(curriculumId), curriculum);
		
		Mockito.verify(this.mockCurriculumDao).updateCurriculum(curriculum);
		
	}
	
	/**
	* Test credits assigned and event sent
	* @throws Exception
	*/
	@Test
	public void testUpdateCredits() throws Exception{
		
		RecruiterCredit rc1 = RecruiterCredit.builder().recruiterId("rec1").credits(1).build();
		RecruiterCredit rc2 = RecruiterCredit.builder().recruiterId("rec2").credits(2).build();
		RecruiterCredit rc3 = RecruiterCredit.builder().recruiterId("rec3").credits(RecruiterCredit.DISABLED_CREDITS).build();
		
		Mockito.when(this.mockCreditDao.fetchRecruiterCredits()).thenReturn(Set.of(rc1,rc2,rc3));
		
		service.updateCredits(0);
		
		Mockito.verify(this.mockCreditDao, 				Mockito.times(2)).persist(Mockito.any(RecruiterCredit.class));
		Mockito.verify(this.mockExternalEventPublisher, Mockito.times(2)).publishCreditsAssignedEvent(Mockito.any(CreditsAssignedEvent.class));;
		
		
	}
	
	/**
	* Checks Exception thrown if credits used for unknown user
	* @throws Exception
	*/
	@Test
	public void testUseCredit_unknownUser() throws Exception{
		
		Mockito.when(this.mockCreditDao.getByRecruiterId(Mockito.anyString())).thenReturn(Optional.empty());
		
		assertThrows(IllegalArgumentException.class, () -> {
			service.useCredit("userId");
		});
		
	}
	
	/**
	* Tests exception thrown if User has no credits left
	* @throws Exception
	*/
	@Test
	public void testUseCredit_noCredits() throws Exception{
		
		RecruiterCredit rc = RecruiterCredit.builder().recruiterId("r1").credits(0).build();
		
		Mockito.when(this.mockCreditDao.getByRecruiterId(Mockito.anyString())).thenReturn(Optional.of(rc));
		
		assertThrows(IllegalStateException.class, () -> {
			service.useCredit("userId");
		});
		
	}
	
	/**
	* Tests successful decrement
	* @throws Exception
	*/
	@Test
	public void testUseCredit() throws Exception{
		
		ArgumentCaptor<RecruiterCredit> rcCapt = ArgumentCaptor.forClass(RecruiterCredit.class);
		
		RecruiterCredit rc = RecruiterCredit.builder().recruiterId("r1").credits(2).build();
		
		Mockito.when(this.mockCreditDao.getByRecruiterId(Mockito.anyString())).thenReturn(Optional.of(rc));
		Mockito.doNothing().when(this.mockCreditDao).persist(rcCapt.capture());
		
		service.useCredit("userId");
		
		assertEquals(1, rcCapt.getValue().getCredits());
		
	}
	
	/**
	* Test false returned if User not known
	* @throws Exception
	*/
	@Test
	public void testDoCreditsCheck_unknownUser() throws Exception{
		
		Mockito.when(this.mockCreditDao.getByRecruiterId(Mockito.anyString())).thenReturn(Optional.empty());
		
		assertFalse(service.doCreditsCheck("recruiter33"));
		
	}
	
	/**
	* Test false returned if User has no remaining credits
	* @throws Exception
	*/
	@Test
	public void testDoCreditsCheck_knownUser_no_credits() throws Exception{
		
		RecruiterCredit rc = RecruiterCredit.builder().credits(0).build();
		
		Mockito.when(this.mockCreditDao.getByRecruiterId(Mockito.anyString())).thenReturn(Optional.of(rc));
		
		assertFalse(service.doCreditsCheck("recruiter33"));
		
	} 
	
	/**
	* Test false returned if User has no remaining credits
	* @throws Exception
	*/
	@Test
	public void testDoCreditsCheck_knownUser_has_credits() throws Exception{
		
		RecruiterCredit rc = RecruiterCredit.builder().credits(1).build();
		
		Mockito.when(this.mockCreditDao.getByRecruiterId(Mockito.anyString())).thenReturn(Optional.of(rc));
		
		assertTrue(service.doCreditsCheck("recruiter33"));
		
	} 
	
	/**
	* Tests case User is not known
	* @throws Exception
	*/
	@Test
	public void testGetCreditCountForUser_unknownUser() throws Exception{
	
		Mockito.when(this.mockCreditDao.getByRecruiterId(Mockito.anyString())).thenReturn(Optional.empty());
		
		assertThrows(IllegalArgumentException.class, () -> {
			service.getCreditCountForUser("aUser");
		});
		
	}
	
	/**
	* Tests case User is known
	* @throws Exception
	*/
	@Test
	public void testGetCreditCountForUser_knownUser() throws Exception{
	
		Mockito.when(this.mockCreditDao.getByRecruiterId(Mockito.anyString())).thenReturn(Optional.of(RecruiterCredit.builder().credits(1).build()));
		
		assertEquals(1, service.getCreditCountForUser("aUser"));
		
	}
	
	/**
	* Happy path
	* @throws Exception
	*/
	@Test
	public void testUpdateCreditsForUser() throws Exception{
		
		final String 	userId 		= "kparkings";
		final int 		credits 	= 20;

		ArgumentCaptor<RecruiterCredit> argCapt = ArgumentCaptor.forClass(RecruiterCredit.class);
		
		RecruiterCredit recCredits = RecruiterCredit.builder().recruiterId(userId).credits(30).build();
		
		Mockito.when(this.mockCreditDao.getByRecruiterId(userId)).thenReturn(Optional.of(recCredits));
		Mockito.doNothing().when(this.mockCreditDao).persist(argCapt.capture());
		
		service.updateCreditsForUser(userId, credits);
	
		Mockito.verify(this.mockCreditDao).persist(Mockito.any());
		
		assertEquals(credits, argCapt.getValue().getCredits());
		
	}
	
	/**
	* If no credits for user does nothing
	* @throws Exception
	*/
	@Test
	public void testUpdateCreditsForUser_unknownRecruiter() throws Exception{
		
		final String 	userId 		= "kparkings";
		final int 		credits 	= 20;

		Mockito.when(this.mockCreditDao.getByRecruiterId(userId)).thenReturn(Optional.empty());
		
		service.updateCreditsForUser(userId, credits);
	
		Mockito.verify(this.mockCreditDao, Mockito.never()).persist(Mockito.any());
		
	}
	
}