import { Injectable }                             		from '@angular/core';
import { HttpClient, HttpHeaders }  					from '@angular/common/http';
import { Observable }        		         			from 'rxjs';
import { NewPendingCandidate }                   		from './create-candidate/new-pending-candidate';
import { CandidateFunction }                      		from './candidate-function';
import { environment }									from './../environments/environment';
import { ExtractedFilters }                     		from './suggestions/extracted-filters';
import { SavedCandidate }		 	                	from './suggestions/saved-candidate';
import { NewCandidateRequest } 							from './new-candidate/new-candidate-request';
import { CandidateProfile } 							from './candidate-profile';
import { UpdateCandidateRequest } 						from './new-candidate/update-candidate-request';
import { CandidateSkill } 								from './accounts/candidate-skill';
import { TranslateService } 							from '@ngx-translate/core';
import { CandidateTotals } 								from './candidate-totals';
import { SupportedLanguage } 							from './supported-language';
import { GeoZone } 										from './geo-zone';
import { SupportedCountry } 							from './supported-candidate';
import { UpdateCityRequest } 							from './accounts/update-city-request';
import { City } 										from './city';
import { SuggestionsSearchRequest } 					from './suggestions/suggestion-search-request';
import { NewSavedSearchRequest } 						from './suggestions/new-saved-search-req';
import { SavedCandidateSearch } 						from './suggestions/saved-search';
import { UpdateSavedSearchRequest }						from './suggestions/update-saved-search-req';
import { SearchStats } 									from './search-stats';
import { of } 											from 'rxjs';

/**
* Services for new Candidates
* @author: K Parkings  
*/
@Injectable({
  providedIn: 'root'
})
export class CandidateServiceService {
    
    public geoZones:Array<GeoZone>						= new Array<GeoZone>();
    public languages:Array<SupportedLanguage> 			= new Array<SupportedLanguage>();
    public supportedCountries:Array<SupportedCountry> 	= new Array<SupportedCountry>();
	public securityLevels:Array<string>					= new Array<string>();
   
    /**
  	* Constructor
  	*/
	constructor(private httpClient: HttpClient, private translate:TranslateService) { 

	}
	
	/**
  	* Supported countries. This is different to the countries the user can filter on in the 
  	* suggestions page. Until there are enough candidates to make it work adding the filter we
  	* maintin two separate country lists. The GeoZone filters will give access to candidates outside 
  	* of the stadard coutries until there are enough candidates to add separate filters for the new countries
  	* NB: Needs to eventually be refactored with initialzeCounties 
  	*/
	public async initializeSupportedCountries():Promise<any>{
	
		const backendUrl:string = environment.backendUrl +'candidate/countries';	
		const config 			= await this.httpClient.get<any>(backendUrl,  { observe: 'response', withCredentials: true}).toPromise();
 
 		config.body.forEach( (country: SupportedCountry) => {
			this.supportedCountries.push(new SupportedCountry(''+country.name, country.iso2Code, country.humanReadable));	
		});
		
		this.securityLevels.push("SC");
		this.securityLevels.push("DV");
		this.securityLevels.push("NATO");
 
 	   	Object.assign(this, config);
    	
    	return config;
		
	}
	
	/**
  	* Sets the available GeoZones
  	* NB: Needs to come from the backend 
  	*/
	public async initializeGeoZones():Promise<any>{
		
		const backendUrl:string = environment.backendUrl +'candidate/geo-zone';
    	const config 			= await this.httpClient.get<any>(backendUrl,  { observe: 'response', withCredentials: true}).toPromise();
    	
		this.geoZones = new Array<GeoZone>();
		
		config.body.forEach( (geoZone: string) => {
			this.geoZones.push(new GeoZone(''+geoZone));	
		});
		
		this.geoZones = this.geoZones.sort((a,b)=>{ 
			return a.geoZoneId < b.geoZoneId ? -1 : 0;
		});
		
		Object.assign(this, config);
    	
    	return config;
    	
	}
	
	/**
  	* Sets the available countrues
  	* NB: Needs to come from the backend 
  	*/
	public async initializeSupportedLanguages():Promise<any>{
		
		const backendUrl:string = environment.backendUrl +'candidate/languages';
    	const config 			= await this.httpClient.get<any>(backendUrl,  { observe: 'response', withCredentials: true}).toPromise();
    	
    	this.languages = new Array<SupportedLanguage>();
			
		config.body.forEach( (lang: string) => {
			this.languages.push(new SupportedLanguage(''+lang));	
		});
		
		this.languages = this.languages.sort((a,b)=>{ 
			return a.languageCode < b.languageCode ? -1 : 0;
		});
		
		Object.assign(this, config);
    	
    	return config;
    	
	}
		
	httpOptions = {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' }), withCredentials: true, 
    };

	httpOptionsFileUpload = {
      headers: new HttpHeaders({ }), withCredentials: true
    };
    
	/**
	* Returns Cities that have been added to the System as part to a Candidate profile
	* that need to be processed and activated by the admin 
	*/
	public getCities(): Observable<Array<City>>{
		const backendUrl:string = environment.backendUrl +'city/awaiting-activation';
		
		return this.httpClient.get<Array<City>>(backendUrl, this.httpOptions);
	}
	
	/**
	* Returns Cities of a given country 
	*/
	public getCitiesForCountry(country:string): Observable<Array<City>>{
		
		const backendUrl:string = environment.backendUrl +'city/'+country;
		
		if (!country) {
			return of(new Array<City>());
		}
		
		return this.httpClient.get<Array<City>>(backendUrl, this.httpOptions);
	}
	
	/**
	* Updtaes and activates a City
	*/
	public updateCity(city:City,lat:number, lon:number):Observable<any>{
					
		let updateRequest:UpdateCityRequest = new UpdateCityRequest(city.country, city.name, lat, lon, true);
	
		const backendUrl:string = environment.backendUrl +'city';
		
		return this.httpClient.put<any>(backendUrl, JSON.stringify(updateRequest), this.httpOptions);
		
	}
	
	/**
	* Deletes an existing CIty
	*/
	public deleteCity(city:City): Observable<void>{
		const backendUrl:string = environment.backendUrl +'city/country/' + city.country+ '/city/'+city.name;

		return this.httpClient.delete<any>(backendUrl, this.httpOptions);

	}
	
	/**
	* Makes a SearchRequest for retrieving Candidates matching the Filters selected by the User
	*/
	public getCandidateSuggestions(searchRequest:SuggestionsSearchRequest): Observable<any>{
		
		const backendUrl:string = environment.backendUrl +'submitCandidateSearchRequest/?orderAttribute=candidateId&order=desc&page=0&unfiltered=true&size=112&backendRequestId=1&searchText=&available=true';
	
		return this.httpClient.post<any>(backendUrl, JSON.stringify(searchRequest), {headers: new HttpHeaders({ 'Content-Type': 'application/json' }),  withCredentials: true, responseType: "json", observe: "response"});
		
	}
  	
	/**
  	* Returns a Candidate by its Id 
  	*/
  	public getCandidateProfileById(candidateId:string): Observable<CandidateProfile>{
      
		const backendUrl:string = environment.backendUrl +'candidate/'+candidateId;
  
    	return this.httpClient.get<any>(backendUrl, this.httpOptions);
  	}
	
	/**
	* Enables a Candidate  
	*/
	enableCandidate(candidateId: string) {
	
		const backendUrl:string = environment.backendUrl +'candidate/'+candidateId+'/?action=enable';
			
		this.httpClient.put<any>(backendUrl, '{}',  this.httpOptions).subscribe(data => {
			
		});
	}
	 
	/**
	* Update a Candidate profile 
	*/
	public updateCandidate(candidateId:string, candidate: UpdateCandidateRequest, profileImage:File):Observable<any> {
	
		const backendUrl:string = environment.backendUrl +'candidate/'+candidateId + '/profile';
		
		var fd = new FormData();
		fd.append('file', profileImage);
  		fd.append("profile", new Blob([JSON.stringify(candidate)], { type: 'application/json' }));
	
		return this.httpClient.put<any>(backendUrl, fd, {headers: new HttpHeaders({ }), withCredentials: true});
		
	}
	
	/**
	* Update a Candidate CV 
	*/
	public updateCandidateCV(candidateId:string, cvFile:File):Observable<any> {
	
		const backendUrl:string = environment.backendUrl +'curriculum/'+candidateId;
		
		var fd = new FormData();
		fd.append('file', cvFile);
  	
		return this.httpClient.put<any>(backendUrl, fd, {headers: new HttpHeaders({ }), withCredentials: true});
		
	}
	
	/**
	* Sends a request to add a new PendingCandidate
	* @param pendingCandidate 	- Contains details of the new Pending Candidate
	* @param profileImage		- Optional profile image
	*/
	public addPendingCandidate(pendingCandidate: NewPendingCandidate, profileImage:File| any): Observable<any>{
		
		const backendUrl:string = environment.backendUrl +'pending-candidate';
		
		var fd = new FormData();
		fd.append('file', profileImage);
  		fd.append("candidate", new Blob([JSON.stringify(pendingCandidate)], { type: 'application/json' }));
	
		return this.httpClient.post<any>(backendUrl, fd, {headers: new HttpHeaders({ }), withCredentials: true});
	}
	
	/**
	* Retrieves Pending candidates
	*/
	public fetchPendingCandidates():Observable<any>{
		
		const backendUrl:string = environment.backendUrl +'pending-candidate';
		
		return this.httpClient.get<any>(backendUrl, this.httpOptions);
		
	}
	
	/**
	* Adds a new Candidate 
	*/
	public addCandidate(newCandidateRequest:NewCandidateRequest, profileImage:File| any): Observable<any>{

	    const backendUrl:string = environment.backendUrl +'candidate';
	    
	    var fd = new FormData();
		fd.append('profileImage', profileImage);
  		fd.append("candidate", new Blob([JSON.stringify(newCandidateRequest)], { type: 'application/json' }));
		
		return this.httpClient.post<any>(backendUrl, fd, {headers: new HttpHeaders({ }), withCredentials: true});
	
	}
	
	/**
	* Returns detials of avialable Canidate Functions
	*/
	public loadFunctionTypes(): Array<CandidateFunction>{
	
		const functionTypes: Array<CandidateFunction> = new Array<CandidateFunction>();
	
	    functionTypes.push(new CandidateFunction('SUPPORT',					this.translate.instant('func-support')));//'Support analyst'));
	    functionTypes.push(new CandidateFunction('BA',						this.translate.instant('func-ba')));//'Business Analyst'));
		functionTypes.push(new CandidateFunction('CSHARP_DEV',				this.translate.instant('func-csharp-dev')));//'C# Developer'));
		functionTypes.push(new CandidateFunction('DATA_SCIENTIST',			this.translate.instant('func-data-scientist')));//'Data Scientist'));
		functionTypes.push(new CandidateFunction('JAVA_DEV',				this.translate.instant('func-java-dev')));//'Java Developer'));
		functionTypes.push(new CandidateFunction('IT_RECRUITER',			this.translate.instant('func-it-recruiter')));//'IT Recruiter'));
		functionTypes.push(new CandidateFunction('IT_SECURITY',				this.translate.instant('func-it-security')));//'IT Security'));
		functionTypes.push(new CandidateFunction('NETWORK_ADMINISTRATOR',	this.translate.instant('func-network-admin')));//'Network Administrator'));
	    functionTypes.push(new CandidateFunction('PROJECT_MANAGER',			this.translate.instant('func-pmo')));//'Project Manager'));
	    functionTypes.push(new CandidateFunction('SCRUM_MASTER',			this.translate.instant('func-scrum-master')));//'Scrum Master'));
		functionTypes.push(new CandidateFunction('ARCHITECT',				this.translate.instant('func-software-architect')));//'Software Architect'));
	    functionTypes.push(new CandidateFunction('SOFTWARE_DEVELOPER',		this.translate.instant('func-software-dev')));//'Software Developer'));
		functionTypes.push(new CandidateFunction('SOFTWARE_DEV_IN_TEST',	this.translate.instant('func-support-sdet')));//'Software Dev In Test'));
		functionTypes.push(new CandidateFunction('TESTER',					this.translate.instant('func-tester')));//'Test Analyst'));
		functionTypes.push(new CandidateFunction('UI_UX',					this.translate.instant('func-uiux')));//'UI \ UX'));
	    functionTypes.push(new CandidateFunction('WEB_DEV',					this.translate.instant('func-web-dev')));//'Web Developer'));
		functionTypes.push(new CandidateFunction('RUBY',					this.translate.instant('func-support')));//'Support analyst'));
		
		functionTypes.push(new CandidateFunction('RUBY_ON_RAILS',			this.translate.instant('ruby-on-rails')));
		functionTypes.push(new CandidateFunction('GO',						this.translate.instant('go')));
		functionTypes.push(new CandidateFunction('REACT',					this.translate.instant('react')));
		functionTypes.push(new CandidateFunction('VUE',						this.translate.instant('vue')));//'Support analyst'));
		functionTypes.push(new CandidateFunction('NEXT',					this.translate.instant('next')));//'Support analyst'));
		functionTypes.push(new CandidateFunction('EXPRES',					this.translate.instant('expres')));//'Support analyst'));
		functionTypes.push(new CandidateFunction('RUST',					this.translate.instant('rust')));//'Support analyst'));
		functionTypes.push(new CandidateFunction('TEST_MANAGER',			this.translate.instant('test-manager')));//'Support analyst'));
		functionTypes.push(new CandidateFunction('PRODUCT_OWNER',			this.translate.instant('product-owner')));//'Support analyst'));
		functionTypes.push(new CandidateFunction('NODE',					this.translate.instant('node')));//'Support analyst'));
		functionTypes.push(new CandidateFunction('PYTHON',					this.translate.instant('python')));//'Support analyst'));
		functionTypes.push(new CandidateFunction('PHP',						this.translate.instant('php')));//'Support analyst'));
		functionTypes.push(new CandidateFunction('ANGULAR',					this.translate.instant('angular')));//'Support analyst'));
		functionTypes.push(new CandidateFunction('ANDROID',					this.translate.instant('android')));//'Support analyst'));
		functionTypes.push(new CandidateFunction('IOS',						this.translate.instant('ios')));//'Support analyst'));
		functionTypes.push(new CandidateFunction('KOTLIN',					this.translate.instant('kotlin')));//'Support analyst'));
		
		functionTypes.push(new CandidateFunction('CCPLUSPLUS',				this.translate.instant('ccplusplus')));//'c/c++'));
		functionTypes.push(new CandidateFunction('COBOL',					this.translate.instant('cobol')));//'cobol'));
		functionTypes.push(new CandidateFunction('SAP',						this.translate.instant('sap')));//'sap'));
		
		
		
		functionTypes.push(new CandidateFunction('SALESFORCE',				this.translate.instant('salesforce')));//'sap'));
		functionTypes.push(new CandidateFunction('SOFTWARE_ARCHITECT',		this.translate.instant('softwareArchitect')));//'sap'));
		functionTypes.push(new CandidateFunction('DATA_ARCHITECT',			this.translate.instant('dataArchitect')));//'sap'));
		functionTypes.push(new CandidateFunction('INFRASTRUCTURE_ARCHITECT',this.translate.instant('infrastructureArchitect')));//'sap'));
		functionTypes.push(new CandidateFunction('SOLUTIONS_ARCHITECT',		this.translate.instant('solutionsArchitect')));//'sap'));
		functionTypes.push(new CandidateFunction('ENTERPRISE_ARCHITECT',	this.translate.instant('enterpriseArchitect')));//'sap'));
				
		functionTypes.push(new CandidateFunction('SOFTWARE_MANAGER',		this.translate.instant('softwareManager')));
		functionTypes.push(new CandidateFunction('INFRASTRUCTURE_MANAGER',	this.translate.instant('infrastructureManager')));
		functionTypes.push(new CandidateFunction('CTO',						this.translate.instant('cto')));
		functionTypes.push(new CandidateFunction('DIRECTOR',				this.translate.instant('diretor')));
		functionTypes.push(new CandidateFunction('PROGRAMME_MANAGER',		this.translate.instant('programmeManager')));
		functionTypes.push(new CandidateFunction('DELIVERY_MANAGER',		this.translate.instant('deliveryManager')));
				
		return functionTypes;
	
	}

	/**
	* Sends a request to mark the canidate as being potentially unavilable. This doesnt make the candiadte 
	* unavialble but rather is a flag indicating that the Candidate is potentially unavilable
	*/
	public markCandidateAsUnavailable(candidateId:string): Observable<any>{
		
		const backendUrl:string = environment.backendUrl +'candidate/'+candidateId+'/flaggedAsUnavailable/true/';
	
		return this.httpClient.put<any>(backendUrl, "{}", this.httpOptions);
	
	}

	/**
	* removed the markAsUnavailable flag for a Candidate
	*/
	public removeMarkCandidateAsUnavailableFlag(candidateId:string): Observable<any>{
		
		const backendUrl:string = environment.backendUrl +'candidate/'+candidateId+'/flaggedAsUnavailable/false/';
	
		return this.httpClient.put<any>(backendUrl, "{}", this.httpOptions);
	
	}
	
	/**
	* Sends a request to mark the Candidate as having been checked and found to still be 
	* available
	*/
	public markCandidateAsAvailable(candidateId:string): Observable<any> {
		
		const backendUrl:string = environment.backendUrl +'candidate/'+candidateId+'/updateCandidatesLastAvailabilityCheck';
	
		return this.httpClient.put<any>(backendUrl, "{}", this.httpOptions);
		
	}
	
	/**
	* Registers an event showing a candidates profile was viewed
	*/
	public registerCandidateProfileViewed(candidateId:string): Observable<any> {
		
		const backendUrl:string = environment.backendUrl +'candidate/stat/views/'+candidateId;

		return this.httpClient.post<any>(backendUrl, "{}", this.httpOptions);
		
	}

	/**
	* Returns Recruiter search statistics
	*/
	public fetchSearchStats(): Observable<SearchStats>{
		
		const backendUrl:string = environment.backendUrl +'candidate/public/search-history';

	  	return this.httpClient.get<any>(backendUrl, this.httpOptions);

	}
		
	/**
  	* Uploads a Job description so that its search filters can be extracted 
  	*/
  	public extractFiltersFromDocument(jobSpecification:File): Observable<ExtractedFilters>{
  
  		var fd = new FormData();
  		fd.append('file', jobSpecification);
  
  		const backendUrl:string = environment.backendUrl + 'extract-filters';
  	
    	return this.httpClient.post<any>(backendUrl, fd, this.httpOptionsFileUpload);
  
  	}

	/**
  	* Uploads a Job description so that its search filters can be extracted 
  	*/
  	public extractFiltersFromText(jobSpecification:string): Observable<ExtractedFilters>{
  
  		const backendUrl:string = environment.backendUrl + 'extract-filters-text';
  	
  		return this.httpClient.post<any>(backendUrl, jobSpecification, this.httpOptions);
    	
  	}

	/**
	* Adds a Saved Candidate for a User
	*/
	public addSavedCandidate(savedCandidate:SavedCandidate): Observable<void>{
		
		const backendUrl:string = environment.backendUrl +'saved-candidate';
	
		return this.httpClient.post<any>(backendUrl, JSON.stringify(savedCandidate), this.httpOptions);

	}

	/**
	* Updates an existing  Saved Candidate for a User
	*/
	public updateSavedCandidate(savedCandidate:SavedCandidate): Observable<void>{
		
		const backendUrl:string = environment.backendUrl +'saved-candidate';
	
		return this.httpClient.put<any>(backendUrl, JSON.stringify(savedCandidate), this.httpOptions);

	}

	/**
	* Updates an existing  Saved Candidate for a User
	*/
	public fetchSavedCandidates(): Observable<Array<SavedCandidate>>{
		
		const backendUrl:string = environment.backendUrl +'saved-candidate';
	
		return this.httpClient.get<any>(backendUrl, this.httpOptions);

	}

	/**
	* Deletes an existing  Saved Candidate for a User
	*/
	public deleteSavedCandidate(candidateId:number): Observable<void>{
		
		const backendUrl:string = environment.backendUrl +'saved-candidate/' + candidateId;
	
		return this.httpClient.delete<any>(backendUrl, this.httpOptions);

	}
	
	/**
	* Deletes an existing Candidate for a User
	*/
	public deleteCandidate(candidateId:string): Observable<void>{
		const backendUrl:string = environment.backendUrl +'candidate/' + candidateId;
	
		return this.httpClient.delete<any>(backendUrl, this.httpOptions);

	}
	
	/**
	* Fetches skills to be validated
	*/
	fetchSkillsToValidate(): Observable<Array<CandidateSkill>> {
       
       const backendUrl:string = environment.backendUrl +'candidate/skills/pending';
  
    	return this.httpClient.get<any>(backendUrl, this.httpOptions);
    
    }
    
    /**
	* Updates Skills that have been validated
	*/
    updateValidatedSkills(validatedSkills: Array<CandidateSkill>): Observable<void> {
       
        const backendUrl:string = environment.backendUrl +'candidate/skills';
	
		return this.httpClient.put<any>(backendUrl, JSON.stringify(validatedSkills), this.httpOptions);
    }
    
    /**
	* Updates availability status
	* @param action: enable | disable 
	*/
    setCandidateAvailability(candidateId: string, action:string): Observable<any> {
	
		const backendUrl:string = environment.backendUrl +'candidate/'+candidateId+'/?action=' + action;
			
		return this.httpClient.put<any>(backendUrl,  '{}', this.httpOptions);
	
	}
	
	/**
	* Fetches info about number of candidates and their availability 
	*/
	fetchCandidateTotals():Observable<CandidateTotals>{
		
		const backendUrl:string = environment.backendUrl + "public/candidate/_counts";
		
		return this.httpClient.get<any>(backendUrl, this.httpOptions);
		
	}
	
	/**
	* Returns GeoZones supported by the system 
	*/
	public getGeoZones():Array<GeoZone>{
		return this.geoZones;	
	}
	
	/**
	* Returns Countries supported by the system 
	*/
	public getSupportedCountries():Array<SupportedCountry>{
		return this.supportedCountries;	
	}
	
	/**
	* Returns Languages supported by the system 
	*/
	public getLanguages():Array<SupportedLanguage>{
		return this.languages;	
	}
	
	public getSecurityLevels():Array<string>{
		return this.securityLevels;
	}
	
	/**
	* Returns the code identifying the country
	* @param country - Country to get the country code for
	*/
	public getCountryCode(country:string):string{

		let result = this.supportedCountries.filter(c => c.name === country);
		
		return result[0] ? result[0].iso2Code.toUpperCase() : 'NA';

  	}
  	
  	/**
    * Sends request to reset users password. Email will be sent if user exists 
    * @param email - Email address of the User
    */
	public resetPassword(email:string): Observable<void>{

        const backendUrl:string     = environment.backendUrl + 'candidate/reset-password/'+email;
  
        return this.httpClient.put<any>(backendUrl, {}, this.httpOptions);

    }
	
	/** START Saved Searches */
	public saveSearchQuery(title:string, enableEmails:boolean, searchRequest:SuggestionsSearchRequest):Observable<void>{
		
		const backendUrl:string = environment.backendUrl +'candidate/saved-search-request';
			
		let req:NewSavedSearchRequest = new NewSavedSearchRequest();
		req.emailAlert = enableEmails;
		req.searchName = title;
		req.searchRequest = searchRequest;
		
		return this.httpClient.post<any>(backendUrl, JSON.stringify(req), this.httpOptions);
				
	}
	
	public getSavedSearchQueries(): Observable<Array<SavedCandidateSearch>>{
		
		const backendUrl:string = environment.backendUrl +'candidate/saved-search-request';
		
		return this.httpClient.get<Array<SavedCandidateSearch>>(backendUrl, this.httpOptions);
	}
	
	/**
	* Deletes Saved Query
	*/
	public deleteSearchQuery(id:string): Observable<void>{
		const backendUrl:string = environment.backendUrl +'candidate/saved-search-request/' + id;

		return this.httpClient.delete<any>(backendUrl, this.httpOptions);

	}
	
	/**
	* Updates Saved Query
	* @param action: enable | disable 
	*/
	public updateSearchQuery(id:string, savedCandidateSearch:SavedCandidateSearch): Observable<any> {

		const backendUrl:string = environment.backendUrl +'candidate/saved-search-request/' + id;
			
		let req:UpdateSavedSearchRequest = new UpdateSavedSearchRequest();
				req.emailAlert = savedCandidateSearch.emailAlert;
				req.searchName = savedCandidateSearch.searchName;
				req.searchRequest = savedCandidateSearch.searchRequest;
				
		return this.httpClient.put<any>(backendUrl,  JSON.stringify(req), this.httpOptions);

	}
	
	/** End Saved Searches */
			
}