package com.arenella.recruit.candudates.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.arenella.recruit.candidates.entities.CandidateEntity;
import com.arenella.recruit.candidates.enums.FREELANCE;
import com.arenella.recruit.candidates.enums.PERM;
import com.arenella.recruit.candidates.enums.RESULT_ORDER;
import com.arenella.recruit.candudates.beans.CandidateFilterOptions;

/**
* Defines DAO functions for interacting with CandidateEntity objects
* @author K Parkings
*/
public interface CandidateDao extends CrudRepository<CandidateEntity, String>, JpaSpecificationExecutor<CandidateEntity> {

	/**
	* Returns all Candidates matching the filter options
	* @param filterOptions - options to filter Candidates on
	* @return Candidates matching the fitler options
	*/
	public default Page<CandidateEntity> findAll(CandidateFilterOptions filterOptions, Pageable pageable) {
		
		//START
		//Page<CandidateEntity> ce = this.findAll(new FilterSpecification(filterOptions), pageable);
		
		
		//END
		
		return this.findAll(new FilterSpecification(filterOptions), pageable);
	}
	
	/**
	* Returns all Candidates matching the filter options
	* @param filterOptions - options to filter Candidates on
	* @return Candidates matching the filter options
	*/
	public default Iterable<CandidateEntity> findAll(CandidateFilterOptions filterOptions) {
		return this.findAll(new FilterSpecification(filterOptions));
	}
			
	/**
	* FilterSpecifiation reflecting the values in the FilterOptiions
	* @author K Parkings
	*/
	public static class FilterSpecification implements Specification<CandidateEntity>{
		
		private static final long serialVersionUID = -4056828138503774297L;

		private CandidateFilterOptions filterOptions;
		
		/**
		* Constructor
		* @param filterOptions - Information on how to set up the FilterSpecification
		*/
		public FilterSpecification(CandidateFilterOptions filterOptions){
			this.filterOptions = filterOptions;
		}
		
		/**
		* Refer to the JpaSpecificationExecutor interface for details 
		*/
		@Override
		public Predicate toPredicate(Root<CandidateEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
			
			//Expression<String> cityExpression 				= root.get("city");
			//Expression<String> dutchExpression 			= root.get("perm");
			//Expression<String> englishExpression 			= root.get("perm");
			//Expression<String> frenchExpression 			= root.get("perm");
			//Expression<String> skillsExpression 			= root.get("perm");
			
			List<Predicate> predicates = new ArrayList<>();
			
			
			if (!this.filterOptions.getCandidateIds().isEmpty()) {
				Predicate candidateIdFltr 						= root.get("candidateId").in(filterOptions.getCandidateIds());
				
				predicates.add(candidateIdFltr);
			}
			
			if (!this.filterOptions.getCountries().isEmpty()) {
				Predicate countriesFltr 						= root.get("country").in(filterOptions.getCountries());
				predicates.add(countriesFltr);
				
			}
			
			if (!this.filterOptions.getFunctions().isEmpty()) {
				Predicate functionsFltr 						= root.get("function").in(filterOptions.getFunctions());
				predicates.add(functionsFltr);
			}
			
			if (!this.filterOptions.isFreelance()) {
				Expression<String> freelanceExpression 			= root.get("freelance");
				predicates.add(criteriaBuilder.equal(freelanceExpression, 	FREELANCE.TRUE));
			}
			
			if (!this.filterOptions.isPerm()) {
				Expression<String> permExpression 				= root.get("perm");
				predicates.add(criteriaBuilder.equal(permExpression, 		PERM.TRUE));
			}
			
			if (this.filterOptions.getYearsExperienceGtEq() > 0 ) {
				Expression<Integer> yearsExperienceExpression 	= root.get("yearsExperience");
				predicates.add(criteriaBuilder.greaterThanOrEqualTo(yearsExperienceExpression, this.filterOptions.getYearsExperienceGtEq()));
			}
			
			if (this.filterOptions.getYearsExperienceLtEq() > 0 ) {
				Expression<Integer> yearsExperienceExpression 	= root.get("yearsExperience");
				predicates.add(criteriaBuilder.lessThanOrEqualTo(yearsExperienceExpression, this.filterOptions.getYearsExperienceLtEq()));
			}
			
			//predicates.add(criteriaBuilder.equal(cityExpression, 		"Noordwijk"));
			
			Expression<String> sortExpression 				= root.get("candidateId");
			
			if (this.filterOptions.getOrderAttribute().isPresent()) {
				sortExpression 				= root.get(filterOptions.getOrderAttribute().get());
				if (this.filterOptions.getOrder().get() == RESULT_ORDER.asc) {
					query.orderBy(criteriaBuilder.asc(sortExpression));
				} else {
					query.orderBy(criteriaBuilder.desc(sortExpression));
				}
				
			} else {
				
				query.orderBy(criteriaBuilder.desc(sortExpression));
			}
			
			return criteriaBuilder.and(predicates.stream().toArray(n -> new Predicate[n]));
			
		}
		
	}
	
}