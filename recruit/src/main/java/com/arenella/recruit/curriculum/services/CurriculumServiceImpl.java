package com.arenella.recruit.curriculum.services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

//import org.apache.poi.xwpf.converter.pdf.PdfConverter;
//import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.arenella.recruit.candidates.adapters.ExternalEventPublisher;
import com.arenella.recruit.curriculum.beans.Curriculum;
import com.arenella.recruit.curriculum.beans.CurriculumDownloadedEvent;
import com.arenella.recruit.curriculum.beans.PendingCurriculum;
import com.arenella.recruit.curriculum.controllers.CurriculumUpdloadDetails;
import com.arenella.recruit.curriculum.dao.CurriculumDao;
import com.arenella.recruit.curriculum.dao.CurriculumDownloadedEventDao;
import com.arenella.recruit.curriculum.dao.PendingCurriculumDao;
import com.arenella.recruit.curriculum.dao.CurriculumSkillsDao;
import com.arenella.recruit.curriculum.utils.CurriculumDetailsExtractionFactory;

import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;

import com.arenella.recruit.curriculum.entity.CurriculumDownloadedEventEntity;
import com.arenella.recruit.curriculum.entity.CurriculumEntity;
import com.arenella.recruit.curriculum.entity.PendingCurriculumEntity;
import com.arenella.recruit.curriculum.entity.SkillEntity;
import com.arenella.recruit.curriculum.enums.FileType;

/**
* Services for interacting with Curriculums
* @author K Parkings
*/
@Service
public class CurriculumServiceImpl implements CurriculumService{

	@Autowired
	private CurriculumDao 					curriculumDao;
	
	@Autowired
	private PendingCurriculumDao 			pendingCurriculumDao;
	
	@Autowired
	private CurriculumDownloadedEventDao 	curriculumDownloadedEventDao;
	
	@Autowired
	private CurriculumSkillsDao				skillsDao;

	@Autowired
	private ExternalEventPublisher			externalEventPublisher;
	
	/**
	* Refer to the CurriculumService interface for details
	*/
	@Override
	public String persistCurriculum(Curriculum curriculum) {

		CurriculumEntity entity = CurriculumEntity.convertToEntity(curriculum);
		
		curriculumDao.save(entity);
		
		return curriculum.getId().get();
	}

	/**
	* Refer to the CurriculumService interface for details
	*/
	public void persistPendingCurriculum(PendingCurriculum pendingCurriculum) {
		
		PendingCurriculumEntity entity = PendingCurriculumEntity.convertToEntity(pendingCurriculum);
		
		pendingCurriculumDao.save(entity);
		
	}
	
	/**
	* Refer to the CurriculumService interface for details
	*/
	@Override
	public Curriculum fetchCurriculum(String curriculumId) {
		
		Optional<CurriculumEntity> entity = this.curriculumDao.findById(Long.valueOf(curriculumId));
		
		//TODO: [KP] Validation Curriculum does not exist and how to handle that in FE
		
		return CurriculumEntity.convertFromEntity(entity.get());
		
	}

	/**
	* Refer to the CurriculumService interface for details
	*/
	@Override
	public long getNextCurriculumId() {
		
		Optional<CurriculumEntity> entity = this.curriculumDao.findTopByOrderByCurriculumIdDesc();
		
		return entity.isEmpty() ? 1 : entity.get().getCurriculumId() + 1;
		
	}
	
	/**
	* Refer to the CurriculumService interface for details
	*/
	@Override
	public void logCurriculumDownloadedEvent(String curriculumId) {
		
		CurriculumDownloadedEvent event = CurriculumDownloadedEvent.builder().curriculumId(curriculumId).build();
		curriculumDownloadedEventDao.save(CurriculumDownloadedEventEntity.toEntity(event));
		
		
	}
	
	/**
	* Refer to the CurriculumService interface for details
	*/
	@Override
	public CurriculumUpdloadDetails extractDetails(String curriculumId, FileType fileType, byte[] curriculumFileBytes) throws IOException {
		
		try {
				
			Set<String> skills = StreamSupport.stream(skillsDao.findAll().spliterator(), false).map(SkillEntity::getSkill).collect(Collectors.toSet());
			
			return CurriculumDetailsExtractionFactory.getInstance(fileType).extract(skills, curriculumId, curriculumFileBytes);
				
		}catch(Exception e) {
			return CurriculumUpdloadDetails.builder().id(curriculumId).build();
		}
				
	}

	/**
	* Refer to the CurriculumService interface for details
	*/
	@Override
	public PendingCurriculum fetchPendingCurriculum(UUID pendingCurriculumId) {
		
		if (!this.pendingCurriculumDao.existsById(pendingCurriculumId)) {
			throw  new IllegalArgumentException("No matching pending curriculum for id: " + pendingCurriculumId.toString());
		}
		
		return PendingCurriculumEntity.convertFromEntity(pendingCurriculumDao.findById(pendingCurriculumId).get());
	
	}

	/**
	* Refer to the CurriculumService interface for details
	*/
	@Override
	public void deletePendingCurriculum(UUID pendingCurriculumId) {
		this.pendingCurriculumDao.deleteById(pendingCurriculumId);
		this.externalEventPublisher.publishPendingCurriculumDeletedEvent(pendingCurriculumId);
	}

	/**
	* Refer to the CurriculumService interface for details
	*/
	@Override
	public void deleteCurriculum(long curriculumId) {
		if (this.curriculumDao.existsById(curriculumId)) {
			this.curriculumDao.deleteById(curriculumId);
		}
	}

	/**
	* Refer to the CurriculumService interface for details
	*/
	@Override
	public byte[] getCurriculamAsPdfBytes(String curriculumId) throws IOException {
		
		Curriculum 				curriculum 		= this.fetchCurriculum(curriculumId);
		byte[] 					fileBytes 		= null;
		ByteArrayOutputStream 	stream;
		
		if (curriculum.getFileType() != FileType.pdf) {
		
			stream = new ByteArrayOutputStream();
			
			XWPFDocument document = new XWPFDocument(new ByteArrayInputStream(curriculum.getFile()));
				
			PdfOptions options = PdfOptions.create();
		
			PdfConverter.getInstance().convert(document, stream, options);
			
		} else {
			fileBytes 	= curriculum.getFile();
			stream 		= new ByteArrayOutputStream(fileBytes.length);
			stream.write(fileBytes);
		}
		
		this.logCurriculumDownloadedEvent(curriculumId);
		
		return stream.toByteArray();
	}

	/**
	* Refer to the CurriculumService interface for details
	*/
	@Override
	public String updateCurriculum(long curriculumId, Curriculum curriculum) {
		
		if (!this.curriculumDao.existsById(curriculumId)) {
			throw new IllegalArgumentException("Cannot update a non existand Curriculum");
		}
		
		final String 	userId 			= this.getAuthenticatedUserId();
		final boolean 	isAdmin			= checkHasRole("ROLE_ADMIN");
		final boolean 	isCandidate		= checkHasRole("ROLE_CANDIDATE");
			
		if (isCandidate && !String.valueOf(curriculumId).equals(userId)) {
			throw new IllegalArgumentException("Cannot update another Candidates Curriculum");
		}
		
		if (!isAdmin && !isCandidate) {
			throw new IllegalArgumentException("You are not authorized to update Curriculums");
		}
		
		curriculumDao.updateCurriculum(curriculum);
		
		return curriculum.getId().get();
		
		
	} 
	
	/**
	* Retrieves the Id of the current Recruiter
	* @return id from security context
	*/
	private String getAuthenticatedUserId() {
		return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
	}
	
	/**
	* Checks if the currently authenticated user has 
	* a specific role
	* @param roleToCheck - Role to check
	* @return whether or not the user has the role
	*/
	private boolean checkHasRole(String roleToCheck) {
		return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().filter(role -> role.getAuthority().equals(roleToCheck)).findAny().isPresent();
	}
	
}