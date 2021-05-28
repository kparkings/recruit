package com.arenella.recruit.curriculum.services;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arenella.recruit.curriculum.beans.CurriculumDownloadedEvent;
import com.arenella.recruit.curriculum.controllers.CurriculumDownloadsStatistics;
import com.arenella.recruit.curriculum.dao.CurriculumDownloadedEventDao;
import com.arenella.recruit.curriculum.entity.CurriculumDownloadedEventEntity;

/**
* Services relating to Curriculum related statistics
* @author K Parkings
*/
@Service
public class StatisticsServiceImpl implements StatisticsService{

	@Autowired
	private CurriculumDownloadedEventDao dao;
	
	/**
	* Refer to StatisticsService for detail 
	*/
	@Override
	public CurriculumDownloadsStatistics fetchCurriculumDownloadsStatistics() {
		
		List<CurriculumDownloadedEvent> events = StreamSupport.stream(dao.findAll().spliterator(), false).map(event -> CurriculumDownloadedEventEntity.fromEntity(event)).collect(Collectors.toList());
		
		return new CurriculumDownloadsStatistics(events);
		
		
	}

}
