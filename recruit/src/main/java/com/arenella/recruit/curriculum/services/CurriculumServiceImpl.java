package com.arenella.recruit.curriculum.services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.arenella.recruit.adapters.events.CreditsAssignedEvent;
import com.arenella.recruit.adapters.events.CreditsUsedEvent;
import com.arenella.recruit.authentication.spring.filters.ArenellaRoleManager;
import com.arenella.recruit.curriculum.adapters.ExternalEventPublisher;
import com.arenella.recruit.curriculum.beans.Curriculum;
import com.arenella.recruit.curriculum.beans.CurriculumDownloadedEvent;
import com.arenella.recruit.curriculum.beans.PendingCurriculum;
import com.arenella.recruit.curriculum.beans.RecruiterCredit;
import com.arenella.recruit.curriculum.controllers.CurriculumUpdloadDetails;
import com.arenella.recruit.curriculum.dao.CurriculumDao;
import com.arenella.recruit.curriculum.dao.CurriculumDownloadedEventDao;
import com.arenella.recruit.curriculum.dao.CurriculumRecruiterCreditDao;
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
	
	@Autowired
	private CurriculumRecruiterCreditDao	creditDao;
	
	@Autowired
	private ArenellaRoleManager				roleManager;
	
	/**
	* Refer to the CurriculumService interface for details
	*/
	@Override
	public String persistCurriculum(Curriculum curriculum) {

		if (roleManager.isRecruiter()) {
			curriculum.setOwnerId(this.getAuthenticatedUserId());
		}
		
		if (roleManager.isAdmin()) {
			curriculum.setOwnerId(curriculum.getId().get());
		}
		
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
		
		//TODO: [KP] If Candidate can only update CV for self. Must be just one curriculum with owner_id of the candidate
		//TODO: [KP] If Recruiter can only update CV where owner_id is userId
		
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
		
		Optional<Curriculum> curriculumOpt = this.curriculumDao.findCurriculumById(curriculumId);
		
		final String 	userId 			= this.getAuthenticatedUserId();
		final boolean 	isAdmin			= this.roleManager.isAdmin();
		final boolean 	isRecruiter		= this.roleManager.isRecruiter();
		final boolean 	isCandidate		= this.roleManager.isCandidate();
		
		if (isCandidate || isRecruiter) {
			String ownerId = curriculumOpt.get().getOwnerId().get();
			
			if (!ownerId.equals(userId)) {
				throw new IllegalArgumentException("Cannot update another Candidates Curriculum");
			}
		}
		
		if (!isAdmin && !isRecruiter && !isCandidate) {
			throw new IllegalArgumentException("You are not authorized to update Curriculums");
		}
		
		curriculum.setOwnerId(curriculumOpt.isPresent() ? curriculumOpt.get().getOwnerId().get() : null);
		
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
	//private boolean checkHasRole(String roleToCheck) {
	//	return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().filter(role -> role.getAuthority().equals(roleToCheck)).findAny().isPresent();
	//}

	/**
	* Refer to the CurriculumService interface for details
	*/
	@Override
	public void updateCredits(int credits) {
		
		creditDao.fetchRecruiterCredits().stream().filter(rc -> rc.getCredits() != RecruiterCredit.DISABLED_CREDITS).forEach(rc -> {
			rc.setCredits(credits);
			this.creditDao.persist(rc);
			this.externalEventPublisher.publishCreditsAssignedEvent(new CreditsAssignedEvent(rc.getRecruiterId(), credits));
		});
		
	}
	
	/**
	* Refer to the CurriclumService interface for details
	* @param userId
	*/
	@Override
	public void useCredit(String userId) {
		
		RecruiterCredit credit = this.creditDao.getByRecruiterId(userId).orElseThrow(() -> new IllegalArgumentException("Unknown User - Cant use Credit"));
		
		if (credit.getCredits() == 0) {
			throw new IllegalStateException("No credits available for User");
		}
		
		credit.decrementCredits();
		
		this.creditDao.persist(credit);
		
		this.externalEventPublisher.publishCreditsUsedEvent(new CreditsUsedEvent(credit.getRecruiterId(), credit.getCredits()));
		
	}

	/**
	* Refer to the CurriculumService interface for details
	*/
	@Override
	public boolean doCreditsCheck(String recruiterId) {
		
		Optional<RecruiterCredit> recruiterCreditOpt = this.creditDao.getByRecruiterId(recruiterId);
		
		if (recruiterCreditOpt.isEmpty()) {
			return false;
		}
		
		return recruiterCreditOpt.get().getCredits() > 0;
	}

	/**
	* Refer to the CurriculumService interface for details
	*/
	@Override
	public int getCreditCountForUser(String name) {
		
		RecruiterCredit credits = this.creditDao.getByRecruiterId(name).orElseThrow(() -> new IllegalArgumentException("Unknwn User. No Credit count information"));
		
		return credits.getCredits();
	}
	
	/**
	* Refer to the CurriclumService for details 
	*/
	@Override
	public void updateCreditsForUser(String userId, int availableCredits) {
		
		Optional<RecruiterCredit> creditOpt = this.creditDao.getByRecruiterId(userId);
		
		if (!creditOpt.isPresent()) {
			return;
		}
		
		RecruiterCredit credits = creditOpt.get();
		
		credits.setCredits(availableCredits);
		
		creditDao.persist(credits);
		
	}
	
	/**
	* Refer to the CandidateService for details 
	*/
	@Override
	public void addCreditsRecordForUser(String userId) {
		
		Optional<RecruiterCredit> creditOpt = this.creditDao.getByRecruiterId(userId);
		
		if (creditOpt.isPresent()) {
			throw new IllegalStateException("Candidate Credits already exist for user " + userId);
		}
		
		creditDao.persist(RecruiterCredit.builder().recruiterId(userId).credits(RecruiterCredit.DISABLED_CREDITS).build());
		
	}

	/**
	* Refer to the CandidateService for details 
	*/
	@Override
	public Set<String> extractSkillsFromCurriculum(long curriculumId) {
		
		Curriculum curriculum = this.curriculumDao.findCurriculumById(curriculumId).orElseThrow();
		
		try {
			
			CurriculumUpdloadDetails results = extractDetails(curriculumId+"", curriculum.getFileType(), curriculum.getFile());
			
			return results.getSkills();
			
		} catch (IOException e) {
			throw new RuntimeException("Unable to read curriculum file " + curriculumId);
		}
		
	}
	
}