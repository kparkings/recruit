package com.arenella.recruit.recruiters.services;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.arenella.recruit.recruiters.beans.Recruiter;
import com.arenella.recruit.recruiters.dao.RecruiterDao;
import com.arenella.recruit.recruiters.entities.RecruiterEntity;

/**
* Servcies relating to Recruiters
* @author K Parkings
*/
@Service
public class RecruiterServiceImpl implements RecruiterService{

	@Autowired
	private RecruiterDao recruiterDao;
	
	/**
	* Refer to the RecruiterService for details
	*/
	@Override
	public void addRecruiter(Recruiter recruiter) {
		
		if (this.recruiterDao.existsById(recruiter.getUserId())) {
			throw new IllegalArgumentException("Recruiter already exists");
		}
		
		recruiter.activateAccount();
		
		this.recruiterDao.save(RecruiterEntity.convertToEntity(recruiter, Optional.empty()));
	}

	/**
	* Refer to the RecruiterService for details
	*/
	@Override
	public void updateRecruiter(Recruiter recruiter) {
		
		RecruiterEntity entity = this.recruiterDao.findById(recruiter.getUserId()).orElseThrow(() -> new IllegalArgumentException("Recruiter doesnt exists"));
		
		entity.setCompanyName(recruiter.getCompanyName());
		entity.setEmail(recruiter.getEmail());
		entity.setFirstName(recruiter.getFirstName());
		entity.setLanguage(recruiter.getLanguage());
		entity.setSurname(recruiter.getSurname());
		
		this.recruiterDao.save(entity);
		
	}

	/**
	* Refer to the RecruiterService for details
	*/
	@Override
	public Set<Recruiter> fetchRecruiters() {
		return StreamSupport.stream(recruiterDao.findAll().spliterator(), false).map(re -> RecruiterEntity.convertFromEntity(re)).collect(Collectors.toCollection(LinkedHashSet::new));
	}

	/**
	* Refer to the RecruiterService for details
	 * @throws IllegalAccessException 
	*/
	@Override
	public Recruiter fetchRecruiter(String recruiterId) throws IllegalAccessException {
		
		boolean isAdminUser	= SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().filter(role -> role.getAuthority().equals("ROLE_ADMIN")).findAny().isPresent();
		
		if (!isAdminUser && !recruiterId.equalsIgnoreCase(SecurityContextHolder.getContext().getAuthentication().getName())) {
			throw new IllegalAccessException("A Recruiter can only view their own accout");
		}
		
		RecruiterEntity entity =  this.recruiterDao.findById(recruiterId).orElseThrow(()-> new IllegalArgumentException("Unknown Recruiter"));
		return RecruiterEntity.convertFromEntity(entity);
	}

	/**
	* Refer to the RecruiterService for details
	 * @throws IllegalAccessException 
	*/
	@Override
	public Recruiter fetchRecruiterOwnAccount()  throws IllegalAccessException {
		
		String recruiterId = SecurityContextHolder.getContext().getAuthentication().getName();
		
		return fetchRecruiter(recruiterId);
		
	}

	/**
	* Refer to the RecruiterService for details
	* @throws IllegalAccessException 
	*/
	@Override
	public void addRecruiterAccountRequest(Recruiter recruiter) {
		
		UUID temporaryRecruiterId = UUID.randomUUID();
		
		recruiter.setUserId(temporaryRecruiterId.toString());
		
		this.recruiterDao.save(RecruiterEntity.convertToEntity(recruiter, Optional.empty()));
		
		
	}

}