package com.arenella.recruit.listings.dao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.arenella.recruit.listings.beans.Listing;
import com.arenella.recruit.listings.beans.Listing.listing_type;
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
	* Returns all listing matching the filters 
	* @param filterOptions - Filter values to apply to the search results
	* @param pageable - Pagination information
	* @return results
	*/
	public default Set<Listing> findAllListings(ListingFilter filterOptions) {
		return this.findAll(new FilterSpecification(filterOptions)).stream().map(ListingEntity::convertFromEntity).collect(Collectors.toSet());
	}
	
	/**
	* Converts domain to entity and persists
	* @param listings
	*/
	public default void saveListings(Set<Listing> listings) {
		this.saveAll(listings.stream().map(l -> ListingEntity.convertToEntity(l, Optional.empty())).collect(Collectors.toSet()));
	}
	
	/**
	* Returns listing matching listingId if present
	* @param listingId - Unique id of the Listing
	*/
	public default Optional<Listing> findListingById(UUID listingId) {
		
		Optional<ListingEntity> entity =  this.findById(listingId);
		
		if (entity.isEmpty()) {
			return Optional.empty();
		}
		
		return entity.stream().map(ListingEntity::convertFromEntity).findAny();
		
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
			
			//this.filterOptions.getSearchTerm().ifPresent(searchTerm -> {
				
			//	if (!searchTerm.isBlank()) {
				
			//		Expression<String> ownerIdExpression 	= root.get("title");
			//		Expression<String> upperExpression 		= criteriaBuilder.upper(ownerIdExpression);
				
			//		predicates.add(criteriaBuilder.like(upperExpression, this.filterOptions.getSearchTerm().orElse("").toUpperCase()));
				
			//	}
			//});
			
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
				
				if (this.filterOptions.getType().get() == listing_type.CONTRACT_ROLE) {
					predicates.add(criteriaBuilder.notEqual(typeExpression, listing_type.PERM_ROLE));
				}
				
				if (this.filterOptions.getType().get() == listing_type.PERM_ROLE) {
					predicates.add(criteriaBuilder.notEqual(typeExpression, listing_type.CONTRACT_ROLE));
				}
			
			}
			
			/**
			* Apply type filter if value present 
			*/
			if (this.filterOptions.getListingAge().isPresent()) {
				
				Expression<LocalDateTime> typeExpression = root.get("created");
				
				LocalDateTime todayStart 		= LocalDateTime.now().minusHours(24);
				LocalDateTime todayEnd 			= LocalDateTime.now();
				
				LocalDateTime thisWeekStart 	= todayStart.minusDays(7);
				LocalDateTime thisWeekEnd 		= todayEnd;
				
				LocalDateTime thisMonthStart 	= todayStart.minusDays(31);
				LocalDateTime thisMonthEnd 		= todayEnd;
				
				
				//if (this.filterOptions.getListingAge().get() == LISTING_AGE.TODAY){
				//	predicates.add(criteriaBuilder.between(typeExpression, todayStart, todayEnd));
				//}
				
				//if (this.filterOptions.getListingAge().get() == LISTING_AGE.THIS_WEEK){
				//	predicates.add(criteriaBuilder.between(typeExpression, thisWeekStart, thisWeekEnd));
				//}

				//if (this.filterOptions.getListingAge().get() == LISTING_AGE.THIS_MONTH){
				//	predicates.add(criteriaBuilder.between(typeExpression, thisMonthStart, thisMonthEnd));
				//}
				
				predicates.add(criteriaBuilder.between(typeExpression, todayStart.minusDays(360), todayEnd));
				
			}
			
			/**
			* Apply country filter if value present 
			*/
			if (!this.filterOptions.getCountries().isEmpty()) {
				
				Expression<Collection<Listing.Country>> country = root.get("country");
				
				predicates.add(country.in(this.filterOptions.getCountries()));
				
			}
			
			/**
			* Only return active Listings 
			*/
			if (this.filterOptions.getActive().isPresent()) {
				Expression<Boolean> activeExpression = root.get("active");
				predicates.add(criteriaBuilder.equal(activeExpression, this.filterOptions.getActive().get()));
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
