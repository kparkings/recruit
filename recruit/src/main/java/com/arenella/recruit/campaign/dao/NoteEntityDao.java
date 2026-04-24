package com.arenella.recruit.campaign.dao;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import com.arenella.recruit.campaigns.beans.Note;
import com.arenella.recruit.campaigns.entities.NoteEntity;

/**
* Repository for working with Campaign Note's 
*/
@Repository
public interface NoteEntityDao extends ListCrudRepository<NoteEntity, UUID>{

	/**
	* If present returns the Note associated with the id
	* @param noteId - Id of Note to fetch
	* @return If Present the Note
	*/
	default Optional<Note> fetchNoteById(UUID noteId) {
		return this.findById(noteId).map(NoteEntity::fromEntity);
	}
	
}
