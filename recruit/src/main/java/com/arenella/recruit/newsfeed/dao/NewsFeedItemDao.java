package com.arenella.recruit.newsfeed.dao;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
import jakarta.persistence.criteria.CriteriaBuilder.In;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.arenella.recruit.newsfeed.beans.NewsFeedItem;
import com.arenella.recruit.newsfeed.beans.NewsFeedItemFilters;
import com.arenella.recruit.newsfeed.entity.NewsFeedItemEntity;

/**
* Repository for working with NewsFeedItemEntity objects
* @author K Parkings
*/
public interface NewsFeedItemDao extends CrudRepository<NewsFeedItemEntity, UUID>, JpaSpecificationExecutor<NewsFeedItemEntity>{

	/**
	* Persists a NewsFeedItem
	* @param item - Item to persist
	*/
	default void saveNewsFeedItem(NewsFeedItem item) {
		this.save(NewsFeedItemEntity.convertToEntity(item));
	}
	
	/**
	* Returns a page of NewsFeedItem results
	* @param pageable - defines page of results to return
	* @return NewsFeedItems
	*/
	default Set<NewsFeedItem> fetchNewsFeedItem(NewsFeedItemFilters filters){
		return this.findAll(new NewsFeedItemEntitySpecification(filters)).stream()
				.map(NewsFeedItemEntity::convertFromEntity)
				.limit(filters.getMaxResults().isEmpty() ? 100 : filters.getMaxResults().get())
				.collect(Collectors.toCollection(LinkedHashSet::new));
	}
	
	/**
	* Specification for filtering on NewsFeedItem objects
	* @author K parkings
	*/
	public class NewsFeedItemEntitySpecification implements Specification<NewsFeedItemEntity>{

		private static final long serialVersionUID = -7833724126499411522L;

		final NewsFeedItemFilters filters;
		
		/**
		* Constructor
		* @param filters - Contains info about what filters to apply to the results
		*/
		public NewsFeedItemEntitySpecification (NewsFeedItemFilters filters) {
			this.filters = filters;
		}
		
		/**
		* Refer to Specification interface for details 
		*/
		@Override
		public Predicate toPredicate(Root<NewsFeedItemEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
			
			List<Predicate> predicates = new ArrayList<>();
			
			if (this.filters.getMaxResults().isPresent()) {
				//Handle in stream
			}
			
			/**
			* Sets the Before Date/Time filter
			*/
			if (this.filters.getCreatedBefore().isPresent()) {
				
				Expression<LocalDateTime> expression = root.get("createBefore");
				predicates.add(criteriaBuilder.greaterThan(expression, filters.getCreatedBefore().get()));
			}
			
			/**
			* Sets Type Filters
			*/
			if (!this.filters.getTypes().isEmpty()) {
				
				In<NewsFeedItem.NEWSFEED_ITEM_TYPE> inClause = criteriaBuilder.in(root.get("itemType"));
				
				for (NewsFeedItem.NEWSFEED_ITEM_TYPE type : filters.getTypes()) {
				    inClause.value(type);
				}
				criteriaBuilder.in(inClause);
				
				predicates.add(criteriaBuilder.in(inClause));
				
			}
			
			Expression<String> sortExpression 				= root.get("created");
			query.orderBy(criteriaBuilder.desc(sortExpression));
			
			return criteriaBuilder.and(predicates.stream().toArray(n -> new Predicate[n]));
		}
		
	}
	
	/**
	* Deletes all NewsFeedItems about a given User
	* @param id - Unqiue id of the User
	*/
	default void deleteAllNewsFeedItemsForReferencedUserId(String id) {
		this.fetchNewsFeedItemsByReferencedUserId(id).stream().forEach(item -> {
			this.deleteById(item.getId());
		});
	}
	
	/**
	* Retrieved all NewsFeedItems about a given User
	* @param referencedUserId - Unique id of User
	* @return NewsFeedItems referencing the User
	*/
	@Query("from NewsFeedItemEntity where referencedUserId = :referencedUserId")
	Set<NewsFeedItemEntity> fetchNewsFeedItemsByReferencedUserId(String referencedUserId);
		
}