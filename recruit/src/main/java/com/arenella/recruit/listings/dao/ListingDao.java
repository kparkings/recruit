package com.arenella.recruit.listings.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

import com.arenella.recruit.listings.beans.ListingFilter;

/**
* Repository for Listings
* @author K Parkings
*/
public interface ListingDao extends CrudRepository<ListingEntity, UUID>, JpaSpecificationExecutor<ListingEntity>{

	/**
	* Returns page of results for ListingEntity objects matching the filter values 
	* @param filterOptions - Filter values to apply to the search results
	* @param pageable - Pagination information
	* @return results
	*/
	public default Page<ListingEntity> findAll(ListingFilter filterOptions, Pageable pageable) {
		return this.findAll(new FilterSpecification(filterOptions), pageable);
	}
	
	/**
	* Filter specification for filtering on ListingEntity objects
	* @author K Parkings
	*/
	public static class FilterSpecification implements Specification<ListingEntity>{

		private static final long serialVersionUID = 6498635816814624568L;

		private ListingFilter filterOptions;
		
		/**
		* Constructor
		* @param filterOptions - Information on how to set up the FilterSpecification
		*/
		public FilterSpecification(ListingFilter filterOptions){
			this.filterOptions = filterOptions;
		}
		
		/**
		* Refer to the JpaSpecificationExecutor interface for details 
		*/
		@Override
		public Predicate toPredicate(Root<ListingEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
			
			List<Predicate> predicates = new ArrayList<>();
			
			/**
			* Apply ownerId filter if value present 
			*/
			if (this.filterOptions.getOwnerId().isPresent()) {
				Expression<String> ownerIdExpression 	= root.get("ownerId");
				Expression<String> upperExpression 		= criteriaBuilder.upper(ownerIdExpression);
				predicates.add(criteriaBuilder.equal(upperExpression, this.filterOptions.getOwnerId().get().toUpperCase()));
			}
			
			/**
			* Apply listingId filter if value present 
			*/
			if (this.filterOptions.getListingId().isPresent()) {
				Expression<String> listingIdExpression = root.get("listingId");
				predicates.add(criteriaBuilder.equal(listingIdExpression, this.filterOptions.getListingId().get()));
			}
			
			/**
			* Apply type filter if value present 
			*/
			if (this.filterOptions.getType().isPresent()) {
				Expression<String> typeExpression = root.get("type");
				predicates.add(criteriaBuilder.equal(typeExpression, this.filterOptions.getType().get()));
			}
			
			/**
			* Apply country filter if value present 
			*/
			if (this.filterOptions.getCountry().isPresent()) {
				Expression<String> countryExpression = root.get("country");
				predicates.add(criteriaBuilder.equal(countryExpression, this.filterOptions.getCountry().get()));
			}
			
			/**
			* Order results by Data Desc. 
			* TODO:// Later provide more options to the user for sorting
			*/
			Expression<String> sortExpression 				= root.get("created");
			query.orderBy(criteriaBuilder.desc(sortExpression));
			
			return criteriaBuilder.and(predicates.stream().toArray(n -> new Predicate[n]));
		}
		
	}
}