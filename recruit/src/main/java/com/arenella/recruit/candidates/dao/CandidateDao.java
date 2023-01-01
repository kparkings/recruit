package com.arenella.recruit.candidates.dao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.arenella.recruit.candidates.beans.Candidate;
import com.arenella.recruit.candidates.beans.CandidateFilterOptions;
import com.arenella.recruit.candidates.beans.Language.LANGUAGE;
import com.arenella.recruit.candidates.entities.CandidateEntity;
import com.arenella.recruit.candidates.entities.CandidateRoleStatsView;
import com.arenella.recruit.candidates.entities.LanguageEntity;
import com.arenella.recruit.candidates.enums.FREELANCE;
import com.arenella.recruit.candidates.enums.PERM;
import com.arenella.recruit.candidates.enums.RESULT_ORDER;

/**
* Defines DAO functions for interacting with CandidateEntity objects
* @author K Parkings
*/
public interface CandidateDao extends CrudRepository<CandidateEntity, Long>, JpaSpecificationExecutor<CandidateEntity> {

	/**
	* Returns all Candidates matching the filter options
	* @param filterOptions - options to filter Candidates on
	* @return Candidates matching the fitler options
	*/
	public default Page<CandidateEntity> findAll(CandidateFilterOptions filterOptions, Pageable pageable) {
		return this.findAll(new FilterSpecification(filterOptions), pageable);
	}
	
	/**
	* If Candidate found return candidate. Otherwise empty
	* @param candidateId - Id to search for
	* @return Candidate matching id
	*/
	public default Optional<Candidate> findCandidateById(long candidateId){
		
		Optional<CandidateEntity> entity = this.findById(candidateId);
		
		if (entity.isEmpty()) {
			return Optional.empty();
		}
		
		return Optional.of(CandidateEntity.convertFromEntity(entity.get()));
		
	}
	
	public default void saveCandidate(Candidate candidate) {
		this.save(CandidateEntity.convertToEntity(candidate));
	}
	
	/**
	* Returns all Candidates matching the filter options
	* @param filterOptions - options to filter Candidates on
	* @return Candidates matching the filter options
	*/
	public default Iterable<CandidateEntity> findAll(CandidateFilterOptions filterOptions) {
		return this.findAll(new FilterSpecification(filterOptions));
	}
	
	public default Set<Candidate> findCandidates(CandidateFilterOptions filterOptions){
		return StreamSupport.stream(this.findAll(filterOptions).spliterator(), false).map(e -> CandidateEntity.convertFromEntity(e)).collect(Collectors.toCollection(LinkedHashSet::new));
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
			
			List<Predicate> predicates = new ArrayList<>();
			
			if (!this.filterOptions.getFirstname().isEmpty()) {
				Expression<String> permExpression 						= root.get("firstname");
				Expression<String> upperExpression 						= criteriaBuilder.upper(permExpression);
				predicates.add(criteriaBuilder.like(upperExpression, this.filterOptions.getFirstname().get().toUpperCase()));
			}
			
			if (!this.filterOptions.getSurname().isEmpty()) {
				Expression<String> permExpression 						= root.get("surname");
				Expression<String> upperExpression 						= criteriaBuilder.upper(permExpression);
				predicates.add(criteriaBuilder.like(upperExpression, this.filterOptions.getSurname().get().toUpperCase()));
			}
			
			
			if (!this.filterOptions.getEmail().isEmpty()) {
				Expression<String> permExpression 						= root.get("email");
				Expression<String> upperExpression 						= criteriaBuilder.upper(permExpression);
				predicates.add(criteriaBuilder.like(upperExpression, this.filterOptions.getEmail().get().toUpperCase()));
			}
			
			if (!this.filterOptions.getSkills().isEmpty()) {
				
				Expression<Collection<String>> skillValues = root.get("skills");
				
				this.filterOptions.getSkills().forEach(skill -> {
					predicates.add(criteriaBuilder.isMember(skill.toLowerCase().trim(), skillValues));
				});
				
			}
			
			if (!this.filterOptions.getDutch().isEmpty()) {
				Join<CandidateEntity,LanguageEntity> dutchJoin = root.join("languages", JoinType.INNER);
				
				dutchJoin.on(criteriaBuilder.and(
							criteriaBuilder.equal(dutchJoin.get("id").get("language"), LANGUAGE.DUTCH),
							criteriaBuilder.equal(dutchJoin.get("level"), this.filterOptions.getDutch().get())
						));
			}
			
			if (!this.filterOptions.getFrench().isEmpty()) {
				Join<CandidateEntity,LanguageEntity> frenchJoin = root.join("languages", JoinType.INNER);
				
				frenchJoin.on(criteriaBuilder.and(
							criteriaBuilder.equal(frenchJoin.get("id").get("language"), LANGUAGE.FRENCH),
							criteriaBuilder.equal(frenchJoin.get("level"), this.filterOptions.getFrench().get())
						));
			}
			
			if (!this.filterOptions.getEnglish().isEmpty()) {
				Join<CandidateEntity,LanguageEntity> frenchJoin = root.join("languages", JoinType.INNER);
				
				frenchJoin.on(criteriaBuilder.and(
							criteriaBuilder.equal(frenchJoin.get("id").get("language"), LANGUAGE.ENGLISH),
							criteriaBuilder.equal(frenchJoin.get("level"), this.filterOptions.getEnglish().get())
						));
			}
			
			if (!this.filterOptions.getCandidateIds().isEmpty()) {
				Predicate candidateIdFltr 						= root.get("candidateId").in(filterOptions.getCandidateIds());
				
				predicates.add(candidateIdFltr);
			}
			
			//TODO: [KP] need to add filter to show/hide inactive users
			Predicate isActiveFltr 						= root.get("available").in(true);
			predicates.add(isActiveFltr);
			
			
			if (!this.filterOptions.isFlaggedAsUnavailable().isEmpty()) {
				//Predicate isFlaggedAsUnavailableFltr 						= root.get("flaggedAsUnavailable").in(true);
				//predicates.add(isFlaggedAsUnavailableFltr);
				Expression<Boolean> flaggedAsUnavailableExpression 	= root.get("flaggedAsUnavailable");
				predicates.add(criteriaBuilder.equal(flaggedAsUnavailableExpression, true));
			}
			
			
			if (!this.filterOptions.getCountries().isEmpty()) {
				Predicate countriesFltr 						= root.get("country").in(filterOptions.getCountries());
				predicates.add(countriesFltr);
			}
			
			if (!this.filterOptions.getFunctions().isEmpty()) {
				Predicate functionsFltr 						= root.get("function").in(filterOptions.getFunctions());
				predicates.add(functionsFltr);
			}
			
			if (!this.filterOptions.isFreelance().isEmpty() && this.filterOptions.isFreelance().get()) {
				Expression<String> freelanceExpression 			= root.get("freelance");
				predicates.add(criteriaBuilder.equal(freelanceExpression, 	FREELANCE.TRUE));
			}
			
			if (!this.filterOptions.isPerm().isEmpty() && this.filterOptions.isPerm().get()) {
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
			
			if (this.filterOptions.getDaysSinceLastAvailabilityCheck().isPresent()) {
				Expression<LocalDate>  daysSinceLastAvailabilityCheck	= root.get("lastAvailabilityCheck");
				LocalDate cutOff = LocalDate.now().minusDays(this.filterOptions.getDaysSinceLastAvailabilityCheck().get());
				predicates.add(criteriaBuilder.lessThanOrEqualTo(daysSinceLastAvailabilityCheck, cutOff));
			}
			
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
	
	/**
	* Returns the number of Candidates that are either
	* actively looking for a new role or who are not 
	* actively looking for a new role
	* @param active - Whether the Candidates are looking for a new role
	* @return count of matching Candidates
	*/
	public Long countByAvailable(boolean active);
	
	/**
	* Retrieves stats showing the number of available candidates 
	* per function
	* @return candidate function stats
	*/
	@Query("Select new com.arenella.recruit.candidates.entities.CandidateRoleStatsView(c.function, count(c.function) ) from CandidateEntity c where c.available = true group by c.function order by c.function")
	public List<CandidateRoleStatsView> getCandidateRoleStats();

	@Query("from CandidateEntity c where c.available = true and c.registerd > :#{#since} order by c.registerd")
	public Set<CandidateEntity> findNewSinceLastDateRaw(@Param(value = "since") LocalDate since);
	
	/**
	* Returns all the new candidates registered after a given date
	* @param since - data after which candidates must have been registered
	* @return New candidates
	*/
	public default Set<Candidate> findNewSinceLastDate(LocalDate since){
		return this.findNewSinceLastDateRaw(since).stream().map(CandidateEntity::convertFromEntity).collect(Collectors.toSet());
	}
	
		
}