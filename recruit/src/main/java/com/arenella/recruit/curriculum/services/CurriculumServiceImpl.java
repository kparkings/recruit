package com.arenella.recruit.curriculum.services;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arenella.recruit.curriculum.beans.Curriculum;
import com.arenella.recruit.curriculum.beans.CurriculumDownloadedEvent;
import com.arenella.recruit.curriculum.beans.PendingCurriculum;
import com.arenella.recruit.curriculum.controllers.CurriculumUpdloadDetails;
import com.arenella.recruit.curriculum.dao.CurriculumDao;
import com.arenella.recruit.curriculum.dao.CurriculumDownloadedEventDao;
import com.arenella.recruit.curriculum.dao.PendingCurriculumDao;
import com.arenella.recruit.curriculum.dao.SkillsDao;
import com.arenella.recruit.curriculum.utils.CurriculumDetailsExtractionFactory;
import com.arenella.recruit.curriculum.entity.CurriculumDownloadedEventEntity;
import com.arenella.recruit.curriculum.entity.CurriculumEntity;
import com.arenella.recruit.curriculum.entity.PendingCurriculumEntity;
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
	private SkillsDao						skillsDao;
	
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
				
			Set<String> skills = StreamSupport.stream(skillsDao.findAll().spliterator(), false).map(skill -> skill.getSkill()).collect(Collectors.toSet());
			
			return CurriculumDetailsExtractionFactory.getInstance(fileType).extract(skills, curriculumId, curriculumFileBytes);
				
		}catch(Exception e) {
			return CurriculumUpdloadDetails.builder().id(curriculumId).build();
		}
				
	} 
	

}
