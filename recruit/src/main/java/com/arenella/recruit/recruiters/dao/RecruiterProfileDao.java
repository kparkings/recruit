package com.arenella.recruit.recruiters.dao;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.arenella.recruit.recruiters.beans.Recruiter;
import com.arenella.recruit.recruiters.beans.RecruiterProfile;
import com.arenella.recruit.recruiters.beans.RecruiterProfile.CONTRACT_TYPE;
import com.arenella.recruit.recruiters.beans.RecruiterProfile.COUNTRY;
import com.arenella.recruit.recruiters.beans.RecruiterProfile.TECH;
import com.arenella.recruit.recruiters.beans.RecruiterProfileFilter;
import com.arenella.recruit.recruiters.entities.RecruiterProfileEntity;

/**
* Repository for RecruiterProfile's
* @author K Parkings
*/
@Repository
public interface RecruiterProfileDao extends PagingAndSortingRepository<RecruiterProfileEntity,String>, JpaSpecificationExecutor<RecruiterProfileEntity>{

	/**
	* Saves the Recruiter Profile
	* @param recruiterProfile
	*/
	default void saveRecruiterProfile(RecruiterProfile recruiterProfile) {
		this.save(RecruiterProfileEntity.convertToEntity(recruiterProfile));
	}
	
	/**
	* Returns Domain RecruiterProfile matching recruiter id where exists
	* @param recruiter - contains recruiter info
	* @return RecruiterProfile
	*/
	default Optional<RecruiterProfile> fetchRecruiterProfileById(Recruiter recruiter){
		
		if (!this.existsById(recruiter.getUserId())){
			return Optional.empty();
		}
		
		return this.findById(recruiter.getUserId()).map(r -> RecruiterProfileEntity.convertFromEntity(r, recruiter));
		
	}
	
	/**
	* Returns RecruiterProfile's matching the filters
	* @return matching Recruiter Profiles
	*/
	default Set<RecruiterProfile> fetchByFilters(RecruiterProfileFilter filters){
		
		Set<RecruiterProfileEntity> matches = this.findAll(new RecruiterEntitySpecification(filters)).stream().collect(Collectors.toSet());
		
		return matches.stream().map(RecruiterProfileEntity::convertFromEntity).collect(Collectors.toCollection(LinkedHashSet::new));
		
	}
	
	/**
	* Filter specification for filtering on ListingEntity objects
	* @author K Parkings
	*/
	public static class RecruiterEntitySpecification implements Specification<RecruiterProfileEntity>{

		
		private static final long serialVersionUID = 8489215858312284800L;
		
		private RecruiterProfileFilter filters;
		
		/**
		* Constructor
		* @param filterOptions - Information on how to set up the FilterSpecification
		*/
		public RecruiterEntitySpecification(RecruiterProfileFilter filters){
			this.filters = filters;
		}
		
		/**
		* Refer to JpaSpecificationExecutor for details
		*/
		@Override
		public Predicate toPredicate(Root<RecruiterProfileEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
			
			List<Predicate> predicates = new ArrayList<>();
			
			if (!filters.getRecruitsContractTypes().isEmpty()) {
				
				In<CONTRACT_TYPE> inClause = criteriaBuilder.in(root.get("recruitsContractTypes"));
			
				for (CONTRACT_TYPE type : filters.getRecruitsContractTypes()) {
				    inClause.value(type);
				}
				criteriaBuilder.in(inClause);
				
				predicates.add(criteriaBuilder.in(inClause));
			}
			
			if (!filters.getCoreTech().isEmpty()) {
				
				In<TECH> inClause = criteriaBuilder.in(root.get("coreTech"));
				
				for (TECH tech : filters.getCoreTech()) {
				    inClause.value(tech);
				}
				criteriaBuilder.in(inClause);
				
				predicates.add(criteriaBuilder.in(inClause));
				
			}
			
			if (!filters.getRecruitsIn().isEmpty()) {
				
				In<COUNTRY> inClause = criteriaBuilder.in(root.get("recruitsIn"));
				
				for (COUNTRY country : filters.getRecruitsIn()) {
				    inClause.value(country);
				}
				criteriaBuilder.in(inClause);
				
				predicates.add(criteriaBuilder.in(inClause));
				
			}
			
			if (filters.isVisibleToCandidates().isPresent() || filters.isVisibleToPublic().isPresent() || filters.isVisibleToRecruiters().isPresent()) {
				
				if (filters.isVisibleToCandidates().isPresent()) {
					Expression<Boolean> expression = root.get("visibleToCandidates");
					predicates.add(criteriaBuilder.equal(expression, filters.isVisibleToCandidates().get()));
				}
				
				if (filters.isVisibleToPublic().isPresent()) {
					Expression<Boolean> expression = root.get("visibleToPublic");
					predicates.add(criteriaBuilder.equal(expression, filters.isVisibleToPublic().get()));
				}

				if (filters.isVisibleToRecruiters().isPresent()) {
					Expression<Boolean> expression = root.get("visibleToRecruiters");
					predicates.add(criteriaBuilder.equal(expression, filters.isVisibleToRecruiters().get()));
				}
				
			}
			
			return criteriaBuilder.and(predicates.stream().toArray(n -> new Predicate[n]));
		}
		
	}
}
