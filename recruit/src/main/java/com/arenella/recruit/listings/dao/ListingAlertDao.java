package com.arenella.recruit.listings.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.arenella.recruit.listings.beans.Listing;
import com.arenella.recruit.listings.beans.ListingAlert;
import com.arenella.recruit.listings.beans.ListingAlertFilterOptions;

/**
* Repository for ListingAlertEntities
* @author K Parkings
*/
public interface ListingAlertDao extends CrudRepository<ListingAlertEntity, UUID>, JpaSpecificationExecutor<ListingAlertEntity>{

	/**
	* Persists a new Alert
	* @param alert
	*/
	public default void addAlert(ListingAlert alert) {
		this.save(ListingAlertEntity.convertToEntity(alert));
	}
	
	/**
	* Returns Alerts matching the filters
	* @param filterOptions - options to filter the Alerts on
	* @return Matching Alert's
	*/
	public default Set<ListingAlert> findAll(ListingAlertFilterOptions filterOptions) {
		
		List<ListingAlertEntity> matches =  this.findAll(new FilterSpecification(filterOptions));
		
		return matches.stream().map(match -> ListingAlertEntity.convertFromEntity(match)).collect(Collectors.toCollection(LinkedHashSet::new));
		
	}
	
	/**
	* 
	* @author K Parkings
	*/
	public static class FilterSpecification implements Specification<ListingAlertEntity>{

		private static final long serialVersionUID = 2778934779924481177L;
		private transient ListingAlertFilterOptions filterOptions;
		
		/**
		* Constructor
		* @param filterOptions - Filter option information
		*/
		public FilterSpecification(ListingAlertFilterOptions filterOptions) {
			this.filterOptions = filterOptions;
		}
		
		/**
		* Refer to the Specification interface for details 
		*/
		@Override
		public Predicate toPredicate(Root<ListingAlertEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
			
			List<Predicate> predicates = new ArrayList<>();
			
			/*Contract Type*/
			if (!this.filterOptions.getContractType().isEmpty()) {
				
				Listing.listing_type type = this.filterOptions.getContractType().get();
				
				Expression<String> contractTypeExpression = root.get("contractType");
			
				if (type != Listing.listing_type.BOTH) {
					predicates.add(contractTypeExpression.in(Set.of(Listing.listing_type.BOTH, type)));
				}
			}
			
			/*Countries*/
			if (!this.filterOptions.getCountries().isEmpty()) {
				
				Expression<Collection<Listing.country>> countryValues = root.get("countries");
				
				this.filterOptions.getCountries().forEach(country -> 
					predicates.add(criteriaBuilder.isMember(country, countryValues))
				);
				
			}
			
			/*Categories*/
			if (!this.filterOptions.getCategories().isEmpty()) {
		
				Expression<Collection<Listing.TECH>> categoryValues = root.get("categories");
				
				this.filterOptions.getCategories().forEach(category -> 
					predicates.add(criteriaBuilder.isMember(category, categoryValues))
				);
				
			
			}
			
			return criteriaBuilder.and(predicates.stream().toArray(n -> new Predicate[n]));
		}
		
	}
	
}