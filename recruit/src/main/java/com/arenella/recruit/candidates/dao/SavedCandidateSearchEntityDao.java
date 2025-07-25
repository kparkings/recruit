package com.arenella.recruit.candidates.dao;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import com.arenella.recruit.candidates.beans.SavedCandidateSearch;
import com.arenella.recruit.candidates.entities.SavedCandidateSearchEntity;

/**
* Repository for SavedCandidateSearchEntity objects 
*/
public interface SavedCandidateSearchEntityDao extends ListCrudRepository<SavedCandidateSearchEntity, UUID>{
	
	@Query("from SavedCandidateSearchEntity where userId = :userId order by searchName asc")
	Set<SavedCandidateSearchEntity> getEntityByUserId(String userId);
	
	@Query("from SavedCandidateSearchEntity where emailAlert = true")
	Optional<SavedCandidateSearchEntity> getEntitiesWithEmailAlertEnables();
	
	/**
	* Persists a Saved Search
	* @param savedSearch
	*/
	default void saveSearch(SavedCandidateSearch savedSearch) {
		this.save(SavedCandidateSearchEntity.toEntity(savedSearch));
	}
	
	/**
	* Retrieves Saved Searches for a USer
	* @return Users saved searches
	*/
	default Set<SavedCandidateSearch> fetchSavedCandidateSearchs(String userId) {
		return this.getEntityByUserId(userId).stream().map(SavedCandidateSearchEntity::fromEntity).collect(Collectors.toCollection(LinkedHashSet::new));
	}
	
	/**
	* If a SavedCandidateSearch matching the Id exists, return the SavedCandidateSearch
	* else empty
	* @param savedCandidateSearchId - Id to search on
	* @return where present the matching SavedCandidateSearch
	*/
	default Optional<SavedCandidateSearch> fetchSavedCandidateSearchById(UUID savedCandidateSearchId) {
		return this.findById(savedCandidateSearchId).map(SavedCandidateSearchEntity::fromEntity);
	}

	/**
	* Returns a Collection of all SavedCandidateSearches where the email alert has been 
	* enabled
	* @return with Enabled email alerts
	*/
	default Set<SavedCandidateSearch> fetchSavedCandidateSearchWithEmailAlert() {
		return this.getEntitiesWithEmailAlertEnables().stream().map(SavedCandidateSearchEntity::fromEntity).sorted().collect(Collectors.toCollection(LinkedHashSet::new));
	}
	
	
}
