package com.arenella.recruit.campaign.dao;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import com.arenella.recruit.campaigns.beans.Appointment;
import com.arenella.recruit.campaigns.entities.AppointmentEntity;

/**
* Repository for working directly with Appointments
*/
@Repository
public interface AppointmentEntityDao extends ListCrudRepository<AppointmentEntity, UUID>{

	/**
	* If available returns the Appointment matching the given id
	* @param id - Id of the Appointment
	* @return Appointment
	*/
	default Optional<Appointment> fetchAppointmentById(UUID id){
		return this.findById(id).map(AppointmentEntity::fromEntity);
	}
	
}
