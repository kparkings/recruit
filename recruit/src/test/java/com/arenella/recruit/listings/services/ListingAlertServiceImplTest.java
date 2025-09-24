package com.arenella.recruit.listings.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.arenella.recruit.authentication.spring.filters.ClaimsUsernamePasswordAuthenticationToken;
import com.arenella.recruit.listings.beans.ListingAlert;
import com.arenella.recruit.listings.beans.ListingAlertFilterOptions;
import com.arenella.recruit.listings.dao.ListingAlertDao;

/**
* Unit tests for the ListingAlertService class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
class ListingAlertServiceImplTest {

	@Mock
	private ListingAlertDao 										mockListingAlertDao; 
	
	@Mock
	private ClaimsUsernamePasswordAuthenticationToken				mockPrincipal;
	
	@InjectMocks
	private ListingAlertServiceImpl 								service = new ListingAlertServiceImpl();

	/**
	* Tests adding a new alert
	* @throws Exception
	*/
	@Test
	void testAddListingAlert() {
	
		ArgumentCaptor<ListingAlert> argCapt = ArgumentCaptor.forClass(ListingAlert.class);
		
		Mockito.doNothing().when(this.mockListingAlertDao).addAlert(argCapt.capture());
		service.addListingAlert(ListingAlert.builder().build(), this.mockPrincipal);
	
		Mockito.verify(this.mockListingAlertDao).addAlert(Mockito.any(ListingAlert.class));
		
		assertNotNull(argCapt.getValue().getId());
		assertNotNull(argCapt.getValue().getCreated());
		
	}
	
	/**
	* If the User has authenticated as a Candidate adds their Candidate Id
	* @throws Exception
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	void testAddListingAlert_isCandidate() {
		
		final String candidateId = "123";
		
		ArgumentCaptor<ListingAlert> argCapt = ArgumentCaptor.forClass(ListingAlert.class);
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_CANDIDATE"));

		Mockito.when(mockPrincipal.getAuthorities()).thenReturn(authorities);
		Mockito.when(mockPrincipal.getName()).thenReturn(candidateId);
		
		Mockito.doNothing().when(this.mockListingAlertDao).addAlert(argCapt.capture());
		service.addListingAlert(ListingAlert.builder().build(), this.mockPrincipal);
	
		Mockito.verify(this.mockListingAlertDao).addAlert(Mockito.any(ListingAlert.class));
		
		assertNotNull(argCapt.getValue().getId());
		assertEquals(Long.valueOf(candidateId), argCapt.getValue().getUserId().get());
		assertNotNull(argCapt.getValue().getCreated());
		
	}
	
	/**
	* Tests fetch via Filters
	* @throws Exception
	*/
	@Test
	void testFetchListingAlerts() {
		
		ListingAlertFilterOptions filters = ListingAlertFilterOptions.builder().build();
		
		Mockito.when(this.mockListingAlertDao.findAll(filters)).thenReturn(Set.of(ListingAlert.builder().build()));
		
		Set<ListingAlert> alerts = service.fetchListingAlerts(filters);
		
		Mockito.verify(this.mockListingAlertDao).findAll(filters);
		
		assertFalse(alerts.isEmpty());
		
	}
	
	/**
	* Tests deleting of an existing Alert
	* @throws Exception
	*/
	@Test
	void testDeleteListingAlert() {
		
		final UUID alertId = UUID.randomUUID();
		
		service.deleteListingAlert(alertId);
	
		Mockito.verify(this.mockListingAlertDao).deleteById(alertId);
	
	}
	
}