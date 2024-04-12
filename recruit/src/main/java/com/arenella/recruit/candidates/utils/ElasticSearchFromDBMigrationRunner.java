package com.arenella.recruit.candidates.utils;

import java.util.stream.StreamSupport;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arenella.recruit.candidates.beans.Candidate;
import com.arenella.recruit.candidates.dao.CandidateDao;
import com.arenella.recruit.candidates.entities.CandidateDocument;
import com.arenella.recruit.candidates.entities.CandidateEntity;
import com.arenella.recruit.candidates.repos.CandidateRepository;

@Component
public class ElasticSearchFromDBMigrationRunner {

	
	@Autowired
	private CandidateDao 		candidateDao;
	
	@Autowired
	private CandidateRepository candidateRepo;
	
	
	@PostConstruct
	public void migrateDBtoElasticsearch() {
		
	//	StreamSupport.stream(this.candidateDao.findAll().spliterator(),false).forEach(candidateEntity -> {
			
	//		Candidate candidate = CandidateEntity.convertFromEntity(candidateEntity);
			
	//		candidateRepo.save(CandidateDocument.convertToDocument(candidate));
			
	//	});		
		
		
	}
	
}
