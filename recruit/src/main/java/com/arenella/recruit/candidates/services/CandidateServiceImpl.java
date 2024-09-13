package com.arenella.recruit.candidates.services;

import java.io.IOException;
import java.security.Principal;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.arenella.recruit.adapters.events.CandidateAccountCreatedEvent;
import com.arenella.recruit.adapters.events.CandidateDeletedEvent;
import com.arenella.recruit.adapters.events.CandidateNoLongerAvailableEvent;
import com.arenella.recruit.adapters.events.CandidatePasswordUpdatedEvent;
import com.arenella.recruit.adapters.events.CandidateUpdateEvent;
import com.arenella.recruit.adapters.events.CandidateUpdatedEvent;
import com.arenella.recruit.adapters.events.ContactRequestEvent;
import com.arenella.recruit.authentication.spring.filters.ArenellaRoleManager;
import com.arenella.recruit.candidates.adapters.CandidateCreatedEvent;
import com.arenella.recruit.candidates.adapters.ExternalEventPublisher;
import com.arenella.recruit.candidates.beans.Candidate;
import com.arenella.recruit.candidates.beans.CandidateExtractedFilters;
import com.arenella.recruit.candidates.beans.CandidateExtractedFilters.CandidateExtractedFiltersBuilder;
import com.arenella.recruit.candidates.beans.CandidateFilterOptions;
import com.arenella.recruit.candidates.beans.CandidateSearchAccuracyWrapper;
import com.arenella.recruit.candidates.beans.CandidateSearchAlert;
import com.arenella.recruit.candidates.beans.CandidateSkill;
import com.arenella.recruit.candidates.beans.CandidateUpdateRequest;
import com.arenella.recruit.candidates.beans.Contact;
import com.arenella.recruit.candidates.beans.Contact.CONTACT_TYPE;
import com.arenella.recruit.candidates.beans.PendingCandidate;
import com.arenella.recruit.candidates.beans.RecruiterCredit;
import com.arenella.recruit.candidates.beans.Candidate.CANDIDATE_TYPE;
import com.arenella.recruit.candidates.beans.Candidate.Photo;
import com.arenella.recruit.candidates.beans.Candidate.Photo.PHOTO_FORMAT;
import com.arenella.recruit.candidates.beans.Candidate.Rate;
import com.arenella.recruit.candidates.controllers.CandidateController.CANDIDATE_UPDATE_ACTIONS;
import com.arenella.recruit.candidates.controllers.CandidateValidationException;
import com.arenella.recruit.candidates.controllers.SavedCandidate;
import com.arenella.recruit.candidates.dao.CandidateRecruiterCreditDao;
import com.arenella.recruit.candidates.dao.PendingCandidateDao;
import com.arenella.recruit.candidates.dao.RecruiterContactDao;
import com.arenella.recruit.candidates.entities.PendingCandidateEntity;
import com.arenella.recruit.candidates.enums.FUNCTION;
import com.arenella.recruit.candidates.extractors.DocumentFilterExtractionUtil;
import com.arenella.recruit.candidates.extractors.SkillExtractor;
import com.arenella.recruit.candidates.repos.CandidateRepository;
import com.arenella.recruit.candidates.utils.CandidateSuggestionUtil;
import com.arenella.recruit.candidates.utils.CandidateSuggestionUtil.suggestion_accuracy;
import com.arenella.recruit.candidates.utils.GeoZoneSearchUtil;
import com.arenella.recruit.candidates.utils.GeoZoneSearchUtil.GEO_ZONE;
import com.arenella.recruit.candidates.utils.SkillsSynonymsUtil;
import com.arenella.recruit.curriculum.enums.FileType;				//TODO: [KP] Why are we referencing other service
import com.arenella.recruit.emailservice.adapters.RequestSendEmailCommand;
import com.arenella.recruit.emailservice.beans.Email.EmailRecipient;
import com.arenella.recruit.emailservice.beans.Email.EmailTopic;
import com.arenella.recruit.emailservice.beans.Email.EmailType;
import com.arenella.recruit.emailservice.beans.Email.Sender;
import com.arenella.recruit.emailservice.beans.Email.EmailRecipient.ContactType;
import com.arenella.recruit.emailservice.beans.Email.Sender.SenderType;
import com.arenella.recruit.newsfeed.beans.NewsFeedItem.NEWSFEED_ITEM_TYPE;

import co.elastic.clients.elasticsearch.ElasticsearchClient;

import com.arenella.recruit.candidates.utils.PasswordUtil;
import com.arenella.recruit.candidates.dao.CandidateSearchAlertDao;
import com.arenella.recruit.candidates.dao.CandidateSkillsDao;
import com.arenella.recruit.candidates.dao.SavedCandidateDao;

import com.arenella.recruit.candidates.utils.CandidateFunctionExtractor;
import com.arenella.recruit.candidates.utils.CandidateImageFileSecurityParser;
import com.arenella.recruit.candidates.utils.CandidateImageManipulator;

import com.arenella.recruit.candidates.dao.SkillUpdateStatDao;

/**
* Provides services related to Candidates
* @author K Parkings
*/
@Service
public class CandidateServiceImpl implements CandidateService{
	
	@Autowired
	private CandidateSkillsDao					skillsDao;

	@Autowired
	private PendingCandidateDao 				pendingCandidateDao;

	@Autowired
	private CandidateStatisticsService 			statisticsService;
	
	@Autowired
	private ExternalEventPublisher				externalEventPublisher;
	
	@Autowired
	private CandidateSuggestionUtil				suggestionUtil;
	
	@Autowired
	private SkillsSynonymsUtil					skillsSynonymsUtil;
	
	@Autowired
	private CandidateSearchAlertDao				skillAlertDao;
	
	@Autowired
	private DocumentFilterExtractionUtil		documentFilterExtractionUtil;
	
	@Autowired
	private CandidateFunctionExtractor			candidateFunctionExtractor;
	
	@Autowired
	private SavedCandidateDao					savedCandidateDao;
	
	@Autowired
	private CandidateImageFileSecurityParser	imageFileSecurityParser;
	
	@Autowired
	private CandidateImageManipulator			imageManipulator;
	
	@Autowired
	private RecruiterContactDao					contactDao;
	
	@Autowired
	private CandidateRecruiterCreditDao			creditDao;
	
	@Autowired
	private SkillExtractor						skillsExtractor;
	
	@Autowired
	private CandidateRepository					candidateRepo;
	
	@Autowired
	private ElasticsearchClient 				esClient;
	
	@Autowired
	private SkillUpdateStatDao					skillUpdateStatDao;
	
	@Autowired
	private ArenellaRoleManager					roleManager;
	
	/**
	* Refer to the CandidateService Interface for Details
	*/
	@Override
	public void updateContact(String recruiterId, String email, String firstName, String surname) {
	
		Contact contact = Contact.builder()
					.contactType(CONTACT_TYPE.RECRUITER)
					.email(email)
					.firstname(firstName)
					.surname(surname)
					.userId(recruiterId)
				.build();
	
		this.contactDao.persit(contact);
		
	}
	
	/**
	* Refer to the CandidateService Interface for Details
	*/
	@Override
	public void persistCandidate(Candidate candidate) throws Exception{
		
		if (roleManager.isRecruiter()) {
			
			Optional<Contact> recruiter = this.contactDao.getByTypeAndId(CONTACT_TYPE.RECRUITER, this.getAuthenticatedUserId()); 
			
			candidate.setEmail(recruiter.orElseThrow().getEmail());
			candidate.setCandidateType(CANDIDATE_TYPE.MARKETPLACE_CANDIDATE);
			candidate.setOwnerId(this.getAuthenticatedUserId());
		} else {
			candidate.setCandidateType(CANDIDATE_TYPE.CANDIDATE);
			candidate.setOwnerId(null);
		}
		
		//IF Recruiter use recruiter email address and dont do this check
		if (roleManager.isAdmin() && this.candidateRepo.emailInUse(candidate.getEmail(),esClient)) {
			throw new CandidateValidationException("Canidate with this Email alredy exists.");
		}
		
		Optional<PendingCandidate> optPendingCandidate = pendingCandidateDao.fetchPendingCandidateByEmail(candidate.getEmail()).stream().findFirst();
		
		if (optPendingCandidate.isPresent()) {
			
			PendingCandidate pendingCandidate = optPendingCandidate.get();
			
			candidate.setIntroduction(pendingCandidate.getIntroduction());
			candidate.setPhoto(pendingCandidate.getPhoto().isEmpty() 	? null : pendingCandidate.getPhoto().get());
			
			if	(pendingCandidate.getRateContract().isPresent()) {
				candidate.setRateContract(pendingCandidate.getRateContract().get());
			}
		
			if	(pendingCandidate.getRatePerm().isPresent()) {
				candidate.setRatePerm(pendingCandidate.getRatePerm().get());
			}
		}
		
		long candidateId = candidateRepo.saveCandidate(candidate);
		
		String password 			= PasswordUtil.generatePassword();
		String encryptedPassword 	= PasswordUtil.encryptPassword(password);
		
		if (!roleManager.isRecruiter()) {
			this.externalEventPublisher
				.publishCandidateAccountCreatedEvent(new CandidateAccountCreatedEvent(candidate.getCandidateId(), encryptedPassword));
			this.externalEventPublisher
			.publishCandidateCreatedEvent(CandidateCreatedEvent
					.builder()
						.candidate(candidate)
						.candidateId(String.valueOf(candidateId))
					.build());
			
			RequestSendEmailCommand command = RequestSendEmailCommand
					.builder()
						.emailType(EmailType.EXTERN)
						.recipients(Set.of(new EmailRecipient<UUID>(UUID.randomUUID(),candidate.getCandidateId(), ContactType.CANDIDATE)))
						.sender(new Sender<>(UUID.randomUUID(), "", SenderType.SYSTEM, "kparkings@gmail.com"))
						.title("Arenella-ICT - Account created")
						.topic(EmailTopic.CANDIDATE_ACCOUNT_CREATED)
						.model(Map.of("firstname",candidate.getFirstname(),"userid",candidate.getCandidateId(),"password",password))
						.persistable(false)
					.build();
			
			this.externalEventPublisher.publishSendEmailCommand(command);
			
			CandidateUpdateEvent event = CandidateUpdateEvent
					.builder()
						.itemType(NEWSFEED_ITEM_TYPE.CANDIDATE_ADDED)
						.candidateId(Integer.valueOf(candidate.getCandidateId()))
						.firstName(candidate.getFirstname())
						.surname(candidate.getSurname())
						.roleSought(candidate.getRoleSought())
					.build(); 
			
			externalEventPublisher.publishCandidateUpdateEvent(event);
		}
				
	}
	
	/**
	* Refer to the CandidateService Interface for Details
	*/
	@Override
	public void updateCandidate(String candidateId, CANDIDATE_UPDATE_ACTIONS updateAction) {
		
		Candidate candidate = this.candidateRepo.findCandidateById(Long.valueOf(candidateId)).orElseThrow(() -> new RuntimeException("Cannot perform update on unknown Candidate: " + candidateId));
		
		NEWSFEED_ITEM_TYPE newsFeedItemType;
		boolean createNewsFeedItem = (candidate.isAvailable() && updateAction == CANDIDATE_UPDATE_ACTIONS.disable) || (!candidate.isAvailable() && updateAction == CANDIDATE_UPDATE_ACTIONS.enable);
	
		switch (updateAction) {
			case enable: {
				candidate.makeAvailable();
				newsFeedItemType = NEWSFEED_ITEM_TYPE.CANDIDATE_BECAME_AVAILABLE;
				break;
			}
			case disable: {
				candidate.noLongerAvailable();
				this.externalEventPublisher.publishCandidateNoLongerAvailableEvent(new CandidateNoLongerAvailableEvent(Long.valueOf(candidate.getCandidateId())));
				newsFeedItemType = NEWSFEED_ITEM_TYPE.CANDIDATE_BECAME_UNAVAILABLE;
				break;
			}
			default: {
				throw new IllegalArgumentException("Unknown update action requested for Candidate");
			}
		}
		
		this.candidateRepo.saveCandidate(candidate);
		
		if (createNewsFeedItem) {
			CandidateUpdateEvent event = CandidateUpdateEvent
					.builder()
						.itemType(newsFeedItemType)
						.candidateId(Integer.valueOf(candidateId))
						.firstName(candidate.getFirstname())
						.surname(candidate.getSurname())
						.roleSought(candidate.getRoleSought())
					.build(); 
			
			externalEventPublisher.publishCandidateUpdateEvent(event);
		}
		
	}

	/**
	* Refer to the CandidateService Interface for Details
	*/
	@Override
	public void persistPendingCandidate(PendingCandidate pendingCandidate) {
		
		if (this.pendingCandidateDao.emailInUse(pendingCandidate.getEmail())) {
			throw new CandidateValidationException("Canidate with this Email alredy exists.");
		}

		if (pendingCandidate.getPendingCandidateId() == null) {
			throw new IllegalArgumentException("Cannot create candidate with no Id ");
		}
		
		if (this.pendingCandidateDao.existsById(pendingCandidate.getPendingCandidateId())) {
			throw new IllegalArgumentException("Cannot create candidate with Id " + pendingCandidate.getPendingCandidateId());
		}
		
		if (pendingCandidate.getPhoto().isPresent()) {
			
			if (!imageFileSecurityParser.isSafe(pendingCandidate.getPhoto().get().getImageBytes())) {
				throw new IllegalArgumentException("Invalid file type detected"); 
			}
			
		}
		
		this.pendingCandidateDao.save(PendingCandidateEntity.convertToEntity(pendingCandidate));
		
	}

	/**
	* Refer to the CandidateService Interface for Details
	*/
	@Override
	public Set<PendingCandidate> getPendingCandidates() {
		
		return StreamSupport.stream(this.pendingCandidateDao.findAll().spliterator(), false)
				.map(PendingCandidateEntity::convertFromEntity).collect(Collectors.toCollection(LinkedHashSet::new));
	}
	
	/**
	* Refer to the CandidateService Interface for Details
	*/
	@Override
	public suggestion_accuracy doTestCandidateAlert(long candidateId, CandidateFilterOptions filterOptions) throws Exception{
		
		CandidateFilterOptions suggestionFilterOptions = CandidateFilterOptions
				.builder()
					.skills(filterOptions.getSkills())
				.build();

		filterOptions.getSkills().clear();
		
		Set<Candidate> results = this.candidateRepo.findCandidates(filterOptions, esClient, 10000);
		
		if (results.isEmpty()) {
			return suggestion_accuracy.poor;
		}
		
		Candidate candidate = results.stream().findFirst().get();
		
		this.skillsSynonymsUtil.addSynonymsForSkills(candidate.getSkills(), candidate.getSkills());
		
		CandidateSearchAccuracyWrapper 	wrappedCandidate 	= new CandidateSearchAccuracyWrapper(candidate);
		
		if (suggestionUtil.isPerfectMatch(wrappedCandidate, suggestionFilterOptions, Set.of())) {
			return suggestion_accuracy.perfect;
		}
		
		if (suggestionUtil.isExcellentMatch(wrappedCandidate, suggestionFilterOptions, Set.of())) {
			return suggestion_accuracy.excellent;
		}
		
		if (suggestionUtil.isGoodMatch(wrappedCandidate, suggestionFilterOptions, Set.of())) {
			return suggestion_accuracy.good;
		}
		
		return suggestion_accuracy.poor;
		
	}
	
	/**
	* Adds any new skills searched on to the DB
	* @param skills - Skills from current search
	*/
	private void extractAndPersistNewSkills(Set<String> skills){
	
		Set<String> newSkills = new HashSet<>();
		
		skills.stream().map(skill -> preprocessSkill(skill)).forEach(skill -> {		
			if( !this.skillsDao.existsById(skill)) {
				newSkills.add(skill);
			}
		});
		
		if (!newSkills.isEmpty()) {
			this.skillsDao.persistSkills(newSkills);
		}	
		
	}
	
	/**
	* Performs pre-processing of skill to ensure the skill
	* is represented correctly in the system regardless of 
	* case and whitespace
	* @param skill - raw skill value
	* @return PreProcessed skill
	*/
	//TODO: [KP] This is duplicate logic. Should be done via shared pre-processing object
	private static String preprocessSkill(String skill) {
		
		skill = skill.toLowerCase();
		skill = skill.trim();
		
		return skill;
		
	}
	
	/**
	* Whether or not the user has a paid subscription
	* @return Whether or not the user has a paid subscription
	*/
	private boolean hasPaidSubscription(boolean isSystemRequest) {
		
		if (isSystemRequest) {
			return false;
		}
		
		if (this.roleManager.isRecruiter()) {
			
			return this.hasPaidSubscription(this.getAuthenticatedUserId());
			
		}
		
		return false;
		
	}
	
	/**
	* Refer to the CandidateService Interface for Details
	*/
	@Override
	public Page<CandidateSearchAccuracyWrapper> getCandidateSuggestions(CandidateFilterOptions filterOptions, Integer maxSuggestions, boolean unfiltered)throws Exception{
		return this.getCandidateSuggestions(filterOptions, maxSuggestions, unfiltered, false);
	}
	
	/**
	* Refer to the CandidateService Interface for Details
	*/
	@Override
	public Page<CandidateSearchAccuracyWrapper> getCandidateSuggestions(CandidateFilterOptions filterOptions, Integer maxSuggestions, boolean unfiltered, boolean isSystemRequest) throws Exception{
		
		final Set<CandidateSearchAccuracyWrapper> 	suggestions 		= new LinkedHashSet<>();
		
		int 										pageCounter 		= 0;
		Set<String> 								suggestionIds 		= new HashSet<>();
		AtomicReference<suggestion_accuracy> 		accuracy 			= new AtomicReference<>(suggestion_accuracy.perfect);
		Pageable 									pageable 			= PageRequest.of(0,750);
		Optional<Boolean>							available		 	= filterOptions.isAvailable();
		
		/**
		* Recruiters may only view unavailable candidates if they have a paid subscription 
		* Candidates need to be able to view their own profile even if their profile is not 
		* active as do Admin users 
		*/
		if (!hasPaidSubscription(isSystemRequest) && !this.roleManager.isAdmin(isSystemRequest)) {
			filterOptions.setAvailable(null);
			filterOptions.setIncludeRequiresSponsorship(false);
		}
		
		/**
		* Only allows Requires sponsorship results in the case 
		* - Either the User is Admin
		* - The user has a paid subscription
		* - The User is Recruiter and the Owner of the Candidates profile
		* - The User is fetching their own profile
		*/
		if(	!this.roleManager.isAdmin(isSystemRequest) 
			&& !hasPaidSubscription(isSystemRequest)) {
				filterOptions.setIncludeRequiresSponsorship(null);
		}
		
		/**
		* Candidate can view own profiel
		*/
		if (this.roleManager.isCandidate(isSystemRequest)) {
			filterOptions.setIncludeRequiresSponsorship(true);
		}
		
		/**
		* Recruiter can view own candidate
		*/
		if (this.roleManager.isRecruiter(isSystemRequest) && filterOptions.getOwnerId().isPresent() && this.getAuthenticatedUserId().equals(filterOptions.getOwnerId().get())) {
			filterOptions.setIncludeRequiresSponsorship(true);
		}
		
		/**
		* Only Admin and recruiters should be able to filter on GEO_ZONES as this is a paid 
		* feature 
		*/
		if (!this.roleManager.isAdmin(isSystemRequest) && !this.roleManager.isRecruiter(isSystemRequest)) {
			filterOptions.removeGeoZones();
		}
		
		if (!filterOptions.getGeoZones().isEmpty()) {
			
			GEO_ZONE[] geoZones = filterOptions.getGeoZones().toArray(new GEO_ZONE[] {});
			GeoZoneSearchUtil.fetchCountriesFor(geoZones).stream().forEach(country -> filterOptions.addCountry(country));
		}
		
		/**
		* Admin and Recruiters with a paid subscription can apply an available filter  
		*/
		if (this.roleManager.isAdmin(isSystemRequest) || (hasPaidSubscription(isSystemRequest))) { 
			filterOptions.setAvailable(available.isEmpty() ? null : available.get());
		}
		
		if (StringUtils.hasText(filterOptions.getSearchText())) {
			Set<FUNCTION> functionToFilterOn = this.candidateFunctionExtractor.extractFunctions(filterOptions.getSearchText());
			filterOptions.setFunctions(functionToFilterOn);
		}
		this.externalEventPublisher.publishSearchedSkillsEvent(filterOptions.getSkills());
		
		extractAndPersistNewSkills(filterOptions.getSkills());
		
		CandidateFilterOptions suggestionFilterOptions = CandidateFilterOptions
																		.builder()
																			.languages(filterOptions.getLanguages())
																			.skills(filterOptions.getSkills())
																			.build();

		if (!isSystemRequest) {
			this.statisticsService.logCandidateSearchEvent(filterOptions);
		}
		
		filterOptions.getSkills().clear();
	
		CandidateExtractedFiltersBuilder searchTermFilter = CandidateExtractedFilters.builder();
		
		if (Optional.ofNullable(filterOptions.getSearchText()).isPresent()) {
			skillsExtractor.extractFilters(" " + filterOptions.getSearchText().toLowerCase() + " ", searchTermFilter);
		}
		Set<String> searchTermKeywords = searchTermFilter.build().getSkills();
		
		/**
		* Special case. If searching on single candidate by Id, clear other filters and add in 
		* just the candidate Id with max 1 result
		*/
		if (filterOptions.getSearchText() != null && filterOptions.getSearchText().startsWith("C#")) {
			
			String candidateId = filterOptions.getSearchText().substring(2);

			if (candidateId != null && !candidateId.equals("") &&  org.apache.commons.lang3.StringUtils.isNumeric(candidateId)) {
				filterOptions.reset();
				filterOptions.setCandidateIds(Set.of(candidateId));
				maxSuggestions = 1;
			} 
				
		} 
		
		/**
		* Not exactly unfiltered. The filters required to implement the business rules will still ba applied. If however the User has 
		* not specified filters w 
		*/
		if (unfiltered) {
			
			List<CandidateSearchAccuracyWrapper> canidates = candidateRepo.findAll(filterOptions, this.esClient, pageable).getContent().stream().map(CandidateSearchAccuracyWrapper::new).toList();
			suggestions.addAll(canidates);
			suggestions.stream().forEach(s -> {
				s.setAccuracyLanguages(suggestion_accuracy.perfect);
				s.setAccuracySkills(suggestion_accuracy.perfect);
			});
			return new PageImpl<>(suggestions.stream().limit(maxSuggestions).collect(Collectors.toCollection(LinkedList::new)));			
		}
		
		boolean isTotalFilteredResultsLesstThanSinglePage = false;
		Page<Candidate> candidates = null;
		
		while (true) {
		
			if (!isTotalFilteredResultsLesstThanSinglePage) {
				candidates = candidateRepo.findAll(filterOptions, this.esClient, pageable);
			}
			
			/**
			* Will only ever be 1 page so no need to keep fetching from storage 
			*/
			if (pageCounter == 0 && candidates.getContent().size() < maxSuggestions) {
				isTotalFilteredResultsLesstThanSinglePage = true;
			}
			
			candidates.getContent().stream().filter(c -> !suggestionIds.contains(c.getCandidateId())).forEach(candidate -> {
		
				this.skillsSynonymsUtil.addSynonymsForSkills(candidate.getSkills(), candidate.getSkills());
				
				CandidateSearchAccuracyWrapper 	wrappedCandidate 	= new CandidateSearchAccuracyWrapper(candidate);
				boolean 						isMatch 			= false;
				
				switch(accuracy.get()) {
					case perfect:{
						isMatch = suggestionUtil.isPerfectMatch(wrappedCandidate, suggestionFilterOptions, searchTermKeywords);
						break;
					}
					case excellent:{
						isMatch = suggestionUtil.isExcellentMatch(wrappedCandidate, suggestionFilterOptions, searchTermKeywords);
						break;
					}
					case good:{
						isMatch = suggestionUtil.isGoodMatch(wrappedCandidate, suggestionFilterOptions, searchTermKeywords);
						break;
					}
					case average:{
						isMatch = suggestionUtil.isAverageMatch(wrappedCandidate, suggestionFilterOptions, searchTermKeywords);
						break;
					}
					case poor:{
						isMatch = suggestionUtil.isPoorMatch(wrappedCandidate, suggestionFilterOptions, searchTermKeywords);
						break;
					}
				}
			
				if (isMatch) {
					suggestions.add(wrappedCandidate);
					suggestionIds.add(candidate.getCandidateId());
				}
				
			});
		
			if (suggestions.size() >= maxSuggestions) {
				return new PageImpl<>(suggestions.stream().limit(maxSuggestions).sorted((CandidateSearchAccuracyWrapper s1, CandidateSearchAccuracyWrapper s2) -> s1.getAccuracySkillsAsNumber().compareTo(s2.getAccuracySkillsAsNumber())).collect(Collectors.toCollection(LinkedList::new)));
			} else if (!(candidates.getTotalPages() >= pageCounter)) {
			
				pageCounter = -1;
				
				switch(accuracy.get()) {
					case perfect:{
						accuracy.set(suggestion_accuracy.excellent);
						break;
					}
					case excellent:{
						accuracy.set(suggestion_accuracy.good);
						break;
					}
					case good:{
						accuracy.set(suggestion_accuracy.average);
						break;
					}
					case average:{
						accuracy.set(suggestion_accuracy.poor);
						break;
					}
					case poor:{
						return new PageImpl<>(suggestions.stream().limit(maxSuggestions).sorted((CandidateSearchAccuracyWrapper s1, CandidateSearchAccuracyWrapper s2) -> s1.getAccuracySkillsAsNumber().compareTo(s2.getAccuracySkillsAsNumber())).collect(Collectors.toCollection(LinkedList::new)));
					}
					
				} 
					
			}
			
			pageCounter = pageCounter + 1;
			pageable 	= PageRequest.of(pageCounter,100);
		}

	}

	/**
	* Refer to the CandidateService for details 
	*/
	@Override
	public void updateCandidatesLastAvailabilityCheck(long candidateId) {
		
		Candidate candidate = candidateRepo.findCandidateById(candidateId).orElseThrow(() -> new IllegalArgumentException("Unknown candidate Id " + candidateId));
		candidate.setCandidateAvailabilityChecked();
		
		this.candidateRepo.saveCandidate(candidate);
		
	}

	/**
	* Refer to the CandidateService for details 
	*/
	@Override
	public void addSearchAlert(CandidateSearchAlert alert, String searchText) {
		
		alert.initAsNewAlert(this.getAuthenticatedUserId());
		
		if (!searchText.isBlank()) {
			Set<FUNCTION> functionToFilterOn = this.candidateFunctionExtractor.extractFunctions(searchText);
			alert.setFunctions(functionToFilterOn);
		}
		
		this.skillAlertDao.saveAlert(alert);
		
	}
	
	/**
	* Refer to the CandidateService for details 
	*/
	@Override
	public void deleteSearchAlert(UUID id) {
		
		Optional<CandidateSearchAlert> alertOpt = this.skillAlertDao.getchAlertById(id);
		
		CandidateSearchAlert alert = alertOpt.orElseThrow(() -> new IllegalArgumentException("Unknown SearchAlert Id " + id));
		
		if (!this.getAuthenticatedUserId().equals(alert.getRecruiterId())) {
			throw new IllegalArgumentException("Unable to delete SearchAlert");
		}
		
		this.skillAlertDao.deleteById(id);
		
	}

	/**
	* Refer to the CandidateService for details 
	*/
	@Override
	public Set<CandidateSearchAlert> getAlertsForCurrentUser() {
		return this.skillAlertDao.fetchAlertsByRecruiterId(getAuthenticatedUserId());
	}
	
	/**
	* Retrieves the Id of the current Recruiter
	* @return id from security context
	*/
	private String getAuthenticatedUserId() {
		return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
	}

	/**
	* Refer to the CandidateService for details 
	*/
	@Override
	public CandidateExtractedFilters extractFiltersFromDocument(FileType fileType, byte[] fileBytes) throws IOException {
		try {
			return documentFilterExtractionUtil.extractFilters(fileType, fileBytes);
		}catch(Exception e) {
			throw new RuntimeException("Unable to process job specification file");
		}
	}

	/**
	* Refer to the CandidateService for details 
	*/
	@Override
	public void addSavedCanidate(SavedCandidate savedCandidate) {
		
		if (savedCandidateDao.exists(savedCandidate.getUserId(), savedCandidate.getCandidateId())) {
			throw new IllegalArgumentException("Candidate already exists");
		}
		
		this.savedCandidateDao.persistSavedCandidate(savedCandidate);
		
	}

	/**
	* Refer to the CandidateService for details 
	*/
	@Override
	public Map<SavedCandidate, Candidate> fetchSavedCandidatesForUser() {
		
		Map<SavedCandidate, Candidate> candidates = new LinkedHashMap<>();
		
		this.savedCandidateDao.fetchSavedCandidatesByUserId(getAuthenticatedUserId()).stream().forEach(c -> {
			
			Optional<Candidate> candidate = candidateRepo.findCandidateById(c.getCandidateId());
			
			if (candidate.isPresent()) {
				candidates.put(c, candidate.get());
			} else {
				candidates.put(c, Candidate
						.builder()
							.candidateId(""+c.getCandidateId())
							.firstname("Candidate No Longer Available")
						.build());
			}
			
		});
		
		return candidates;
	}

	/**
	* Refer to the CandidateService for details 
	*/
	@Override
	public void removeSavedCandidate(long candidateId, Principal principal) {
		
		if (!savedCandidateDao.exists(principal.getName(), candidateId)) {
			throw new IllegalArgumentException("Unknown Saved Candidate");
		}
		
		this.savedCandidateDao.delete(principal.getName(), candidateId);
		
	}

	/**
	* Refer to the CandidateService for details 
	*/
	@Override
	public void updateSavedCandidate(SavedCandidate savedCandidate) {
	
		if (!savedCandidateDao.exists(savedCandidate.getUserId(), savedCandidate.getCandidateId())) {
			throw new IllegalArgumentException("Unknown Saved Candidate");
		}
		
		this.savedCandidateDao.updateSavedCandidate(savedCandidate);
	}

	/**
	* Refer to the CandidateService for details 
	*/
	@Override
	public Candidate fetchCandidate(String candidateId, String authernticatedUserId, Collection<GrantedAuthority> authorities) {
		
		final boolean systemRequest = false;
		
		boolean isAdmin 	= authorities.stream().filter(a -> a.getAuthority().equals("ROLE_ADMIN")).findAny().isPresent();		
		boolean isCandidate = authorities.stream().filter(a -> a.getAuthority().equals("ROLE_CANDIDATE")).findAny().isPresent();
		boolean isRecruiter = authorities.stream().filter(a -> a.getAuthority().equals("ROLE_RECRUITER")).findAny().isPresent();
		
		if (isCandidate &&  (!isAdmin || !isRecruiter)) {
			if(!candidateId.equals(authernticatedUserId)) {
				throw new IllegalArgumentException("Candidates can only view their own Profiles.");
			}
		}
		
		Optional<Candidate> candidate = this.candidateRepo.findCandidateById(Long.valueOf(candidateId));
		
		if (candidate.isEmpty()) {
			throw new IllegalArgumentException("Unknown Candidate.");
		}
		
		if (!candidate.get().isAvailable() && isRecruiter && !hasPaidSubscription(systemRequest)) { // Need to implement right hand check
			throw new IllegalArgumentException("Cant view unavailable candidates with unpaid subscriptin type.");
		}
		
		
		return candidate.get();
	}

	/**
	* Refer to the CandidateService for details 
	*/
	@Override
	public void updateCandidateProfile(CandidateUpdateRequest candidate) throws Exception{
		
		final String 	userId 			= this.getAuthenticatedUserId();
		final boolean 	isAdmin			= roleManager.isAdmin();
		final boolean 	isRecruiter		= roleManager.isRecruiter();
		final boolean 	isCandidate		= roleManager.isCandidate();
			
		if (isCandidate && !candidate.getCandidateId().equals(userId)) {
			throw new IllegalArgumentException("Cannot update another Candidates Profile");
		}
		
		if (!isAdmin && !isRecruiter && !isCandidate) {
			throw new IllegalArgumentException("You are not authorized to update Candidate profiles");
		}
		
		Candidate existingCandidate = this.candidateRepo.findCandidateById(Long.valueOf(candidate.getCandidateId())).orElseThrow(() -> new IllegalArgumentException("Cannot update Unknown Candidate"));
		
		if (!isRecruiter && this.candidateRepo.emailInUseByOtherUser(candidate.getEmail(), Long.valueOf(candidate.getCandidateId()),esClient)) {
			throw new IllegalStateException("Cannot update. Email address alread in use by anothe user");
		}
		
		//TODO: IF photo already exists and not specifically removed dont remote ( check what you did for recruiter profile )
		Photo	photo 			= null;
		Rate 	rateContract 	= null;
		Rate 	ratePerm		= null;
		
		if (candidate.getRateContract().isPresent()) {
			rateContract = candidate.getRateContract().get();
		}
		
		if (candidate.getRatePerm().isPresent()) {
			ratePerm = candidate.getRatePerm().get();
		}

		//TODO: [KP] Practically duplicate of convertToPhoto but input is different
		if (candidate.getPhotoBytes().isPresent()) {

			if (!imageFileSecurityParser.isSafe(candidate.getPhotoBytes().get())) {
				throw new RuntimeException("Invalid file type detected"); 
			}
		
			byte[] photoBytes = this.imageManipulator.toProfileImage(candidate.getPhotoBytes().get(), PHOTO_FORMAT.jpeg);
					
			photo = new Photo(photoBytes, PHOTO_FORMAT.jpeg);
			
		} else if (existingCandidate.getPhoto().isPresent()){
			photo = existingCandidate.getPhoto().get();
		}
		
		Candidate updatedCandidate = Candidate
				.builder()
					.candidateId(existingCandidate.getCandidateId())
					.city(candidate.getCity())
					.country(candidate.getCountry())
					.email(candidate.getEmail())
					.firstname(candidate.getFirstname())
					.flaggedAsUnavailable(existingCandidate.isFlaggedAsUnavailable())
					.freelance(candidate.isFreelance())
					.function(candidate.getFunction())
					.languages(candidate.getLanguages())
					.lastAvailabilityCheck(existingCandidate.getLastAvailabilityCheckOn())
					.perm(candidate.isPerm())
					.registerd(existingCandidate.getRegisteredOn())
					.roleSought(candidate.getRoleSought())
					.skills(candidate.getSkills().isEmpty() ? existingCandidate.getSkills() : candidate.getSkills())
					.surname(candidate.getSurname())
					.yearsExperience(candidate.getYearsExperience())
					.photo(photo)
					.rateContract(rateContract)
					.ratePerm(ratePerm)
					.introduction(candidate.getIntroduction())
					.available(existingCandidate.isAvailable())
					.ownerId(existingCandidate.getOwnerId().isEmpty() ? null : existingCandidate.getOwnerId().get())
					.daysOnSite(candidate.getDaysOnSite())
					.availableFromDate(candidate.getAvailableFromDate())
					.requiresSponsorship(candidate.getRequiresSponsorship())
					.securityClearance(candidate.getSecurityClearance())
				.build();
		
		this.candidateRepo.saveCandidate(updatedCandidate);
		this.externalEventPublisher.publishCandidateAccountUpdatedEvent(new CandidateUpdatedEvent(candidate.getCandidateId(), candidate.getFirstname(), candidate.getSurname(), candidate.getEmail()));
		
		CandidateUpdateEvent event = CandidateUpdateEvent
				.builder()
					.itemType(NEWSFEED_ITEM_TYPE.CANDIDATE_PROFILE_UPDATED)
					.candidateId(Integer.valueOf(candidate.getCandidateId()))
					.firstName(candidate.getFirstname())
					.surname(candidate.getSurname())
					.roleSought(candidate.getRoleSought())
				.build(); 
		
		externalEventPublisher.publishCandidateUpdateEvent(event);
		
	}
	
	/**
	* Checks if the currently authenticated user has 
	* a specific role
	* @param roleToCheck - Role to check
	* @return whether or not the user has the role
	*/
	//private boolean checkHasRole(String roleToCheck) {
	//	return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().filter(role -> role.getAuthority().equals(roleToCheck)).findAny().isPresent();
	//}
	
	
	//TODO: [KP]. Create a component. Component replaces checkHasRole.
	//1. Write once use anywhere ( Create in Spring project map)
	//2. We can pass in with the request when there is no user and it is system
	//3. Keeps spring security implementation outside of the service
	
	
	

	@Override
	public void deleteCandidate(String candidateId) {
		
		if (roleManager.isCandidate() && !this.getAuthenticatedUserId().equals(candidateId)) {
			throw new IllegalStateException("You cannot delete another Candidate from the System");
		}
		
		Candidate candidate = this.candidateRepo.findCandidateById(Long.valueOf(candidateId)).orElseThrow(() -> new IllegalArgumentException("Cannot delete a non existent Candidate"));
		
		if (roleManager.isRecruiter() && !this.getAuthenticatedUserId().equals(candidate.getOwnerId().get())) {
			throw new IllegalArgumentException("You cannot delete this Candidate from the System");
		}
		
		this.candidateRepo.deleteById(Long.valueOf(candidateId));
		this.skillUpdateStatDao.deleteById(Long.valueOf(candidateId));
		
		this.externalEventPublisher.publishCandidateDeletedEvent(new CandidateDeletedEvent(candidateId));
		
		/**
		* This is used to remove all NewsFeedItems referencing the 
		*/
		CandidateUpdateEvent event = CandidateUpdateEvent
				.builder()
					.itemType(NEWSFEED_ITEM_TYPE.CANDIDATE_DELETED)
					.candidateId(Integer.valueOf(candidateId))
				.build(); 
		
		externalEventPublisher.publishCandidateUpdateEvent(event);
		
	}

	/**
	* Refer to the CandidateService for details 
	*/
	@Override
	public Optional<Photo> convertToPhoto(Optional<MultipartFile> profilePhoto) throws IOException {
		
		Photo photo = null;
		
		if (profilePhoto.isPresent()) {
			
			if (!imageFileSecurityParser.isSafe(profilePhoto.get().getBytes())) {
				throw new RuntimeException("Invalid file type detected"); 
			}
		
			byte[] photoBytes = this.imageManipulator.toProfileImage(profilePhoto.get().getBytes(), PHOTO_FORMAT.jpeg);
					
			photo = new Photo(photoBytes, PHOTO_FORMAT.jpeg);
			
		}
		
		
		return Optional.ofNullable(photo);
	}

	/**
	* Refer to the CandidateService for details 
	*/
	@Override
	public void sendEmailToCandidate(String message, String candidateId, String title, String userId) {
		
		Candidate candidate = this.candidateRepo.findCandidateById(Long.valueOf(candidateId)).orElseThrow();
		
		if (candidate.getOwnerId().isPresent()) {
			this.externalEventPublisher
			.publishContactRequestEvent(new ContactRequestEvent(userId, candidate.getOwnerId().get(), "Contact Request for " + candidate.getFirstname() + " " + candidate.getSurname(), message));
		} else {
			this.externalEventPublisher
			.publishContactRequestEvent(new ContactRequestEvent(userId, candidateId, title, message));
		}
		
		
	}
	
	/**
	* Refer to the CandidateService for details 
	*/
	@Override
	public void addCreditsRecordForUser(String userId) {
		
		Optional<RecruiterCredit> creditOpt = this.creditDao.getByRecruiterId(userId);
		
		if (creditOpt.isPresent()) {
			throw new IllegalStateException("Candidate Credits already exist for user " + userId);
		}
		
		creditDao.persist(RecruiterCredit.builder().recruiterId(userId).credits(RecruiterCredit.DISABLED_CREDITS).build());
		
	}

	/**
	* Refer to the CandidateService for details 
	*/
	@Override
	public void updateCreditsForUser(String userId, int availableCredits) {
		
		this.updateCreditsForUser(userId, availableCredits, Optional.empty());
		
	}
	
	/**
	* Refer to the CandidateService for details 
	*/
	@Override
	public void updateCreditsForUser(String userId, int availableCredits, Optional<Boolean> hasPaidSubscription) {
	
		Optional<RecruiterCredit> creditOpt = this.creditDao.getByRecruiterId(userId);
		
		if (!creditOpt.isPresent()) {
			return;
		}
		
		RecruiterCredit credits = creditOpt.get();
		
		credits.setCredits(availableCredits);
		
		if (hasPaidSubscription.isPresent()) {
			credits.setPaidSubscription(hasPaidSubscription.get());
		}
		
		creditDao.persist(credits);
	
	}
	
	/**
	* Refer to the CandidateService for details 
	*/
	@Override
	public boolean hasCreditsLeft(String userName) {
		
		Optional<RecruiterCredit> credits = this.creditDao.getByRecruiterId(userName);
		
		if (credits.isEmpty()) {
			return false;
		}
		
		return credits.get().getCredits() > 0;
		
	}

	/**
	* Refer to the CandidateService for details 
	*/
	@Override
	public int getCreditCountForUser(String userId) {
		return this.getRecruiterCreditRecord(userId).getCredits();
	}
	
	/**
	* Refer to the CandidateService for details 
	*/
	@Override
	public boolean hasPaidSubscription(String userId) {
		
		Optional<RecruiterCredit> credits =  this.creditDao.getByRecruiterId(userId);
		
		if(credits.isEmpty()) {
			return false;	
		}
		
		return credits.get().hasPaidSubscription();
	}
	
	/**
	* Retrieves the Recruiter credit Record for the Recruiter
	* @param userId - Id of the Recruiter
	* @return Recruiter Credit information
	*/
	private RecruiterCredit getRecruiterCreditRecord(String userId) {
		
		Optional<RecruiterCredit> credits =  this.creditDao.getByRecruiterId(userId);
		
		if(credits.isEmpty()) {
			throw new IllegalArgumentException("Unknown User: " + userId);	
		}
		
		return credits.get();
		
	}

	/**
	* Refer to the CandidateService for details 
	*/
	@Override
	public CandidateExtractedFilters extractFiltersFromText(String jobspec) {
		return this.documentFilterExtractionUtil.extractFilters(jobspec);
	}

	/**
	* Refer to the CandidateService for details 
	*/
	@Override
	public Set<CandidateSkill> fetchPendingCandidateSkills() {
		return this.skillsDao.getValidationPendingSkills();
	}

	/**
	* Refer to the CandidateService for details 
	*/
	@Override
	public void updateCandidateSkills(Set<CandidateSkill> skills) {
		
		skills.stream().forEach(skill -> {
			if(!this.skillsDao.existsById(skill.getSkill())) {
				throw new IllegalStateException("Cant update unknown Skills status");
			}
		});
		
		this.skillsDao.persistExistingSkills(skills);
		
	}

	/**
	* Refer to the CandidateService for details 
	*/
	@Override
	public long getCountByAvailable(boolean available) {
		return this.candidateRepo.getCountByAvailable(available);
	}
	
	/**
	* Refer to the RecruiterService for details
	* @throws IllegalAccessException 
	*/
	@Override
	public void resetPassword(String emailAddress) {
		
		Set<Candidate> candidates;
		
		try {
			candidates = this.candidateRepo.findCandidates(CandidateFilterOptions.builder().email((""+emailAddress.toLowerCase())).build(), esClient, 2);
		}catch(Exception e) {
			throw new RuntimeException("Unable to reset password for candidate");
		}
		
		/**
		* Can only reset if exactly one recruiter with the email address 
		*/
		if (candidates.size() != 1) {
			throw new IllegalArgumentException("Cannot reset password");
		}
		
		Candidate candidate = candidates.stream().findFirst().get();
		
		String rawPassword = PasswordUtil.generatePassword();
		String encPassword = PasswordUtil.encryptPassword(rawPassword);
		
		this.externalEventPublisher.publishCandidatePasswordUpdated(new CandidatePasswordUpdatedEvent(candidate.getCandidateId(), encPassword));
		
		RequestSendEmailCommand command = RequestSendEmailCommand
				.builder()
					.emailType(EmailType.EXTERN)
					.recipients(Set.of(new EmailRecipient<UUID>(UUID.randomUUID(),candidate.getCandidateId(), ContactType.CANDIDATE)))
					.sender(new Sender<>(UUID.randomUUID(), "", SenderType.SYSTEM, "kparkings@gmail.com"))
					.title("Arenella-ICT - Password Reset")
					.topic(EmailTopic.PASSWORD_RESET)
					.model(Map.of("userId", candidate.getCandidateId(), "firstname",candidate.getFirstname(),"password",rawPassword))
					.persistable(false)
				.build();
		
		this.externalEventPublisher.publishSendEmailCommand(command);
		
	}
	
}