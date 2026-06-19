package com.arenella.recruit.campaign.dao;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import com.arenella.recruit.campaigns.beans.Participation;
import com.arenella.recruit.campaigns.entities.ParticipationEntity;

/**
* Repository for working directly with Participations
*/
@Repository
public interface ParticipationEntityDao extends ListCrudRepository<ParticipationEntity, UUID>{

	/**
	* If available returns the Participation matching the given id
	* @param id - Id of the Participation
	* @return Participation
	*/
	default Optional<Participation> fetchParticipationById(UUID id){
		return this.findById(id).map(ParticipationEntity::fromEntity);
	}
	
	/**
	* Persists a Participation
	* @param participation - Participation to persist
	*/
	default void saveParticipation(Participation participation) {
		this.save(ParticipationEntity.toEntity(participation));
	}
	
}
