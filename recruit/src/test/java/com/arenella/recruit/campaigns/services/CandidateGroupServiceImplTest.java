package com.arenella.recruit.campaigns.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.arenella.recruit.campaign.dao.CandidateGroupEntityDao;
import com.arenella.recruit.campaigns.beans.Campaign;
import com.arenella.recruit.campaigns.beans.CandidateGroup;
import com.arenella.recruit.campaigns.beans.Participation;
import com.arenella.recruit.campaigns.beans.Participation.ParticipantType;
import com.arenella.recruit.campaigns.beans.Role;

/**
* Unit tests for the CandidateGroupServiceImpl class 
*/
@ExtendWith(MockitoExtension.class)
class CandidateGroupServiceImplTest {

	@Mock
	private CandidateGroupEntityDao mockCandidateGroupDao;
	
	@Mock
	private CampaignService 		mockCampaignService;
	
	@InjectMocks
	private CandidateGroupServiceImpl service;
	
	/**
	* Tests fetching Groups for Campaign 
	*/
	@Test
	void testFetchCandidateGroupsForCampaign() {
		
		final UUID campaignId = UUID.randomUUID();
		
		when(this.mockCandidateGroupDao.findCandidateGroupsForCampaign(campaignId)).thenReturn(Set.of(CandidateGroup.builder().build(),CandidateGroup.builder().build()));
		
		assertEquals(2, this.service.fetchCandidateGroupsForCampaign(campaignId).size());
		
	}
	
	/**
	* Tests deletions of CandidateGroup
	*/
	@Test
	void testDeleteCandidateGroup() {
		
		final UUID candidateGroupId = UUID.randomUUID();
		
		this.service.deleteCandidateGroup(candidateGroupId);
		
		 verify(this.mockCandidateGroupDao).deleteById(candidateGroupId);
		
	}
	
	/**
	* Tests deletions of CandidateGroup
	*/
	@Test
	void testFindGroupById() {
		
		final UUID candidateGroupId = UUID.randomUUID();
		
		when(this.mockCandidateGroupDao.fetchGroupingById(candidateGroupId)).thenReturn(Optional.of(CandidateGroup.builder().build()));
		
		Optional<CandidateGroup> group = this.service.findGroupById(candidateGroupId);
		
		assertNotNull(group.get());
		
	}
	
	/**
	* Tests if there are no Edit or Admin rights at either the Campaign or Role level that an
	* exception is throws
	*/
	@Test
	void testSaveGandidateGroupNoParticipationRights() {
		
		final String currentUser = "rec1";
		final String candidateId = "123";
		final UUID groupId = UUID.randomUUID();
		final UUID campaignId = UUID.randomUUID();
		final UUID roleId = UUID.randomUUID();
		final CandidateGroup candidateGroup = CandidateGroup.builder().id(groupId).campaignId(campaignId).roleId(roleId).candidateId(candidateId).build();
		final Campaign campaign = Campaign
				.builder()
					.id(campaignId)
				.build();
		
		when( this.mockCandidateGroupDao.fetchCandidateGroup(campaignId, Optional.of(roleId), candidateId)).thenReturn(Optional.of(candidateGroup));
		when(this.mockCampaignService.fetchCampaignById(campaignId, currentUser)).thenReturn(campaign);
		
		
		assertThrows(IllegalArgumentException.class, () -> {
			this.service.saveGandidateGroup(candidateGroup, currentUser);
		});
		
	}
	
	/**
	* Tests if there are no Edit or Admin rights at either the Campaign or Role level that an
	* exception is throws
	*/
	@Test
	void testSaveGandidateGroupViewParticipationRightsCampaignLevel() {
		
		final String currentUser = "rec1";
		final String candidateId = "123";
		final UUID groupId = UUID.randomUUID();
		final UUID campaignId = UUID.randomUUID();
		final UUID roleId = UUID.randomUUID();
		final CandidateGroup candidateGroup = CandidateGroup.builder().id(groupId).campaignId(campaignId).roleId(roleId).candidateId(candidateId).build();
		final Campaign campaign = Campaign
				.builder()
					.id(campaignId)
					.participants(Set.of(Participation.builder().contactId(currentUser).type(ParticipantType.VIEW).build()))
				.build();
		
		when( this.mockCandidateGroupDao.fetchCandidateGroup(campaignId, Optional.of(roleId), candidateId)).thenReturn(Optional.of(candidateGroup));
		when(this.mockCampaignService.fetchCampaignById(campaignId, currentUser)).thenReturn(campaign);
		
		assertThrows(IllegalArgumentException.class, () -> {
			this.service.saveGandidateGroup(candidateGroup, currentUser);
		});
		
	}
	
	/**
	* Test happy path with Edit participation at Campaign Level
	* exception is throws
	*/
	@Test
	void testSaveGandidateGroupEditParticipationRightsCampaignLevel() {
		
		final String currentUser = "rec1";
		final String candidateId = "123";
		final UUID groupId = UUID.randomUUID();
		final UUID campaignId = UUID.randomUUID();
		final CandidateGroup candidateGroup = CandidateGroup.builder().id(groupId).campaignId(campaignId).candidateId(candidateId).build();
		final Campaign campaign = Campaign
				.builder()
					.id(campaignId)
					.participants(Set.of(Participation.builder().contactId(currentUser).type(ParticipantType.EDIT).build()))
				.build();
		
		when( this.mockCandidateGroupDao.fetchCandidateGroup(campaignId, Optional.empty(), candidateId)).thenReturn(Optional.of(candidateGroup));
		when(this.mockCampaignService.fetchCampaignById(campaignId, currentUser)).thenReturn(campaign);
		
		this.service.saveGandidateGroup(candidateGroup, currentUser);
		
		verify(this.mockCandidateGroupDao).saveGrouping(any());
		
	}
	
	/**
	* Test happy path with Admin participation at Campaign Level
	* exception is throws
	*/
	@Test
	void testSaveGandidateGroupAdminParticipationRightsCampaignLevel() {
		
		final String currentUser = "rec1";
		final String candidateId = "123";
		final UUID groupId = UUID.randomUUID();
		final UUID campaignId = UUID.randomUUID();
		final CandidateGroup candidateGroup = CandidateGroup.builder().id(groupId).campaignId(campaignId).candidateId(candidateId).build();
		final Campaign campaign = Campaign
				.builder()
					.id(campaignId)
					.participants(Set.of(Participation.builder().contactId(currentUser).type(ParticipantType.ADMIN).build()))
				.build();
		
		when( this.mockCandidateGroupDao.fetchCandidateGroup(campaignId, Optional.empty(), candidateId)).thenReturn(Optional.of(candidateGroup));
		when(this.mockCampaignService.fetchCampaignById(campaignId, currentUser)).thenReturn(campaign);
		
		this.service.saveGandidateGroup(candidateGroup, currentUser);
		
		verify(this.mockCandidateGroupDao).saveGrouping(any());
		
	}
	
	/**
	* Tests if there are no Edit or Admin rights at either the Campaign or Role level that an
	* exception is throws
	*/
	@Test
	void testSaveGandidateGroupViewParticipationRightsRoleLevel() {
		
		final String currentUser = "rec1";
		final String candidateId = "123";
		final UUID groupId = UUID.randomUUID();
		final UUID campaignId = UUID.randomUUID();
		final UUID roleId = UUID.randomUUID();
		final CandidateGroup candidateGroup = CandidateGroup.builder().id(groupId).campaignId(campaignId).roleId(roleId).candidateId(candidateId).build();
		final Campaign campaign = Campaign
				.builder()
					.id(campaignId)
					.role(Role.builder()
							.id(roleId)
							.participants(Set.of(Participation.builder().contactId(currentUser).type(ParticipantType.VIEW).build()))		
					.build())
				.build();
		
		when( this.mockCandidateGroupDao.fetchCandidateGroup(campaignId, Optional.of(roleId), candidateId)).thenReturn(Optional.of(candidateGroup));
		when(this.mockCampaignService.fetchCampaignById(campaignId, currentUser)).thenReturn(campaign);
		
		
		assertThrows(IllegalArgumentException.class, () -> {
			this.service.saveGandidateGroup(candidateGroup, currentUser);
		});
		
	}
	
	/**
	* Tests happy path with participation at Role level as EDIT
	*/
	@Test
	void testSaveGandidateGroupEditParticipationRightsRoleLevel() {
		
		final String currentUser = "rec1";
		final String candidateId = "123";
		final UUID groupId = UUID.randomUUID();
		final UUID campaignId = UUID.randomUUID();
		final UUID roleId = UUID.randomUUID();
		final CandidateGroup candidateGroup = CandidateGroup.builder().id(groupId).campaignId(campaignId).roleId(roleId).candidateId(candidateId).build();
		final Campaign campaign = Campaign
				.builder()
					.id(campaignId)
					.role(Role.builder()
							.id(roleId)
							.participants(Set.of(Participation.builder().contactId(currentUser).type(ParticipantType.EDIT).build()))		
					.build())
				.build();
		
		when( this.mockCandidateGroupDao.fetchCandidateGroup(campaignId, Optional.of(roleId), candidateId)).thenReturn(Optional.of(candidateGroup));
		when(this.mockCampaignService.fetchCampaignById(campaignId, currentUser)).thenReturn(campaign);
		
		this.service.saveGandidateGroup(candidateGroup, currentUser);
		
		verify(this.mockCandidateGroupDao).saveGrouping(any());
		
	}
	
	/**
	* Tests happy path with participation at Role level as ADMIN
	*/
	@Test
	void testSaveGandidateGroupAdminParticipationRightsRoleLevel() {
		
		final String currentUser = "rec1";
		final String candidateId = "123";
		final UUID groupId = UUID.randomUUID();
		final UUID campaignId = UUID.randomUUID();
		final UUID roleId = UUID.randomUUID();
		final CandidateGroup candidateGroup = CandidateGroup.builder().id(groupId).campaignId(campaignId).roleId(roleId).candidateId(candidateId).build();
		final Campaign campaign = Campaign
				.builder()
					.id(campaignId)
					.role(Role.builder()
							.id(roleId)
							.participants(Set.of(Participation.builder().contactId(currentUser).type(ParticipantType.ADMIN).build()))		
					.build())
				.build();
		
		when( this.mockCandidateGroupDao.fetchCandidateGroup(campaignId, Optional.of(roleId), candidateId)).thenReturn(Optional.of(candidateGroup));
		when(this.mockCampaignService.fetchCampaignById(campaignId, currentUser)).thenReturn(campaign);
		
		this.service.saveGandidateGroup(candidateGroup, currentUser);
		
		verify(this.mockCandidateGroupDao).saveGrouping(any());
		
	}
	
	/**
	* Tests happy path with participation at Campaign level for a ROLE with EDIT
	*/
	@Test
	void testSaveGandidateGroupAdminParticipationRightsRoleLWithCampaignLevelEDIT() {
		
		final String currentUser = "rec1";
		final String candidateId = "123";
		final UUID groupId = UUID.randomUUID();
		final UUID campaignId = UUID.randomUUID();
		final UUID roleId = UUID.randomUUID();
		final CandidateGroup candidateGroup = CandidateGroup.builder().id(groupId).campaignId(campaignId).roleId(roleId).candidateId(candidateId).build();
		final Campaign campaign = Campaign
				.builder()
					.id(campaignId)
					.participants(Set.of(Participation.builder().contactId(currentUser).type(ParticipantType.EDIT).build()))
					.role(Role.builder()
							.id(roleId)
					.build())
				.build();
		
		when( this.mockCandidateGroupDao.fetchCandidateGroup(campaignId, Optional.of(roleId), candidateId)).thenReturn(Optional.of(candidateGroup));
		when(this.mockCampaignService.fetchCampaignById(campaignId, currentUser)).thenReturn(campaign);
		
		this.service.saveGandidateGroup(candidateGroup, currentUser);
		
		verify(this.mockCandidateGroupDao).saveGrouping(any());
		
	}
	
	/**
	* Tests happy path with participation at Campaign level for a ROLE with ADMIN
	*/
	@Test
	void testSaveGandidateGroupAdminParticipationRightsRoleLWithCampaignLevelADMIN() {
		
		final String currentUser = "rec1";
		final String candidateId = "123";
		final UUID groupId = UUID.randomUUID();
		final UUID campaignId = UUID.randomUUID();
		final UUID roleId = UUID.randomUUID();
		final CandidateGroup candidateGroup = CandidateGroup.builder().id(groupId).campaignId(campaignId).roleId(roleId).candidateId(candidateId).build();
		final Campaign campaign = Campaign
				.builder()
					.id(campaignId)
					.participants(Set.of(Participation.builder().contactId(currentUser).type(ParticipantType.ADMIN).build()))
					.role(Role.builder()
							.id(roleId)
					.build())
				.build();
		
		when( this.mockCandidateGroupDao.fetchCandidateGroup(campaignId, Optional.of(roleId), candidateId)).thenReturn(Optional.of(candidateGroup));
		when(this.mockCampaignService.fetchCampaignById(campaignId, currentUser)).thenReturn(campaign);
		
		this.service.saveGandidateGroup(candidateGroup, currentUser);
		
		verify(this.mockCandidateGroupDao).saveGrouping(any());
		
	}
	
}
