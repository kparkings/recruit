package com.arenella.recruit.listings.services;

import java.security.Principal;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arenella.recruit.authentication.spring.filters.ClaimsUsernamePasswordAuthenticationToken;
import com.arenella.recruit.listings.beans.ListingAlert;
import com.arenella.recruit.listings.beans.ListingAlertFilterOptions;
import com.arenella.recruit.listings.dao.ListingAlertDao;

/**
* Services for interacting with ListingAlert's
* @author K Parkings
*/
@Service
public class ListingAlertServiceImpl implements ListingAlertService{

	@Autowired
	private ListingAlertDao listingAlertDao;
	
	/**
	* Refer to the ListingAlertService for details 
	*/
	@Override
	public void addListingAlert(ListingAlert alert, Principal currentUser) {
		
		alert.initializeAlert();
		
		if (currentUser != null && this.isCandidate(currentUser)) {
			alert.setUserId(Long.valueOf(currentUser.getName()));
		}
		
		this.listingAlertDao.addAlert(alert);
		
	}

	/**
	* Refer to the ListingAlertService for details 
	*/
	@Override
	public Set<ListingAlert> fetchListingAlerts(ListingAlertFilterOptions filters) {
		return this.listingAlertDao.findAll(filters);
	}

	/**
	* Refer to the ListingAlertService for details 
	*/
	@Override
	public void deleteListingAlert(UUID id) {
		this.listingAlertDao.deleteById(id);
		
	}
	
	/**
	* Returns whether or not the user is a Candidate
	* @return whether or not the user is a Candidate
	*/
	private boolean isCandidate(Principal principal) {
		return this.hasRole(principal, "ROLE_CANDIDATE");
	}
	
	/**
	* Returns whether or not the user has a given Role
	* @return whether or not the user has a given Role
	*/
	private boolean hasRole(Principal principal, String role) {
		
		ClaimsUsernamePasswordAuthenticationToken user = (ClaimsUsernamePasswordAuthenticationToken)principal;
		
		return user.getAuthorities().stream().filter(a -> a.getAuthority().equals(role)).findAny().isPresent();
		
	}
	
	//TODO: This structure for determine category for incoming listing not for alert
	private static class SearchTermToCategoryUtil{
		
		private Set<String> JAVA_KEYWORDS 				= Set.of();
		private Set<String> DOT_NET_KEYWORDS 			= Set.of();
		private Set<String> DEV_OPS_KEYWORDS 			= Set.of();
		private Set<String> NETWORKS_KEYWORDS			= Set.of();
		private Set<String> CLOUD_KEYWORDS 				= Set.of(); 
		private Set<String> WEB_KEYWORDS 				= Set.of(); 
		private Set<String> UI_UX_KEYWORDS 				= Set.of(); 
		private Set<String> PROJECT_NANAGMENT_KEYWORDS 	= Set.of(); 
		private Set<String> TESTING_KEYWORDS 			= Set.of(); 
		private Set<String> BUSINESS_ANALYSTS_KEYWORDS 	= Set.of(); 
		private Set<String> SECURITY_KEYWORDS 			= Set.of(); 
		private Set<String> IT_SUPPORT_KEYWORDS 		= Set.of(); 
		private Set<String> ARCHITECT_KEYWORDS 			= Set.of(); 
		private Set<String> BI_KEYWORDS 				= Set.of(); 
		private Set<String> REC2REC_KEYWORDS 			= Set.of();
		
		
		
	}

}
