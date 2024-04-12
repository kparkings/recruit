import { Injectable }                             		from '@angular/core';
import { HttpClient, HttpHeaders }  					from '@angular/common/http';
import { Observable }        		         			from 'rxjs';
import { NewPendingCandidate }                   		from './create-candidate/new-pending-candidate';
import { CandidateFunction }                      		from './candidate-function';
import { environment }									from './../environments/environment';
import { SearchAlert }		 	                    	from './recruiter-alerts/search-alert';
import { CandidateSearchAlert }                     	from './suggestions/candidate-search-alert';
import { ExtractedFilters }                     		from './suggestions/extracted-filters';
import { SavedCandidate }		 	                	from './suggestions/saved-candidate';
import { NewCandidateRequest } 							from './new-candidate/new-candidate-request';
import { CandidateProfile } 							from './candidate-profile';
import { UpdateCandidateRequest } 						from './new-candidate/update-candidate-request';
import { CandidateSkill } 								from './accounts/candidate-skill';
import { TranslateService } 							from '@ngx-translate/core';
import { CandidateTotals } 								from './candidate-totals';
import { Country } 										from './country';
import { SupportedLanguage } from './supported-language';
import { GeoZone } from './geo-zone';
import { SupportedCountry } from './supported-candidate';

/**
* Services for new Candidates
* @author: K Parkings  
*/
@Injectable({
  providedIn: 'root'
})
export class CandidateServiceService {
    
    public geoZones:Array<GeoZone>						= new Array<GeoZone>();
    public countries:Array<Country> 					= new Array<Country>();
    public languages:Array<SupportedLanguage> 			= new Array<SupportedLanguage>();
    public supportedCountries:Array<SupportedCountry> 	= new Array<SupportedCountry>();
   
    /**
  	* Constructor
  	*/
	constructor(private httpClient: HttpClient, private translate:TranslateService) { 
		this.initializeCountries();
		//this.initializeSupportedLanguages();
		//this.initializeGeoZones();
		//this.initializeSupportedCountries();
	}
	
	/**
  	* Sets the available countrues
  	* NB: Needs to come from the backend 
  	*/
	public initializeCountries():void{
		this.countries = new Array<Country>();
		this.countries.push(new Country('NL', 'NETHERLANDS'));
		this.countries.push(new Country('BE', 'BELGIUM'));
		this.countries.push(new Country('UK', 'UK'));
		this.countries.push(new Country('IE', 'REPUBLIC_OF_IRELAND'));
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
			this.supportedCountries.push(new SupportedCountry(''+country.name, country.iso2Code));	
		});
 
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
      headers: new HttpHeaders({ 'Content-Type': 'application/json' }), withCredentials: true
    };

	httpOptionsFileUpload = {
      headers: new HttpHeaders({ }), withCredentials: true
    };
    

  	/**
  	* Returns a list of available Candidates 
  	*/
  	public getCandidates(filterParams:string): Observable<any>{
      
		const backendUrl:string = environment.backendUrl +'candidate?'+filterParams;
    	
    	return this.httpClient.get<any>(backendUrl,  { observe: 'response', withCredentials: true});//
    	
  	}
  	
	/**
  	* Returns a Candidate by its Id 
  	*/
  	public getCandidateById(candidateId:string): Observable<any>{
      
		const backendUrl:string = environment.backendUrl +'candidate?orderAttribute=candidateId&order=desc&candidateId='+candidateId
  
    	return this.httpClient.get<any>(backendUrl, this.httpOptions);
  	}
  	
  	/**
  	* Returns a Candidate by its Id where the ownerId is specified
  	*/
  	public getCandidateByIdWithRecruiterAsOwner(candidateId:string, ownerId:string): Observable<any>{
      
		const backendUrl:string = environment.backendUrl +'candidate/?orderAttribute=candidateId&order=desc&candidateId='+candidateId+ "&ownerId=" + ownerId
  
    	return this.httpClient.get<any>(backendUrl, this.httpOptions);
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
	* Returns Candidates that are due to have their availability checked
	*/
	public fetchCandidatesDueForAvailabilityCheck(): Observable<any>{
		
		const backendUrl:string = environment.backendUrl +'candidate?daysSinceLastAvailabilityCheck=14&orderAttribute=candidateId&order=desc&page=0&size=1000&available=true';
  
    	return this.httpClient.get<any>(backendUrl, this.httpOptions);

	}
	
	/**
	* Returns Candidates that have been marked as unavailable but are due to have their availability checked
	*/
	public fetchUnavailableCandidatesDueForAvailabilityCheck(): Observable<any>{
		
		const backendUrl:string = environment.backendUrl +'candidate?daysSinceLastAvailabilityCheck=30&orderAttribute=candidateId&order=desc&page=0&size=1000&available=false';
  
    	return this.httpClient.get<any>(backendUrl, this.httpOptions);

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
	* Sends a request to mark the Candidate as having been checked and found to still be 
	* available
	*/
	public createCandidateSearchAlert(alert:CandidateSearchAlert): Observable<any> {
		
		const backendUrl:string = environment.backendUrl +'candidate/alert';
	
		return this.httpClient.post<any>(backendUrl, JSON.stringify(alert), this.httpOptions);
		
	}
	
	/**
	* Sends a request to mark the Candidate as having been checked and found to still be 
	* available
	*/
	public deleteCandidateSearchAlert(alert:SearchAlert): Observable<any> {
		
		const backendUrl:string = environment.backendUrl +'candidate/alert/' + alert.alertId;
	
		return this.httpClient.delete<any>(backendUrl, this.httpOptions);
		
	}

	/**
	* Returns Alerts for authenticated Recruiter
	*/
	public fetchCandidateSearchAlerts(): Observable<Array<SearchAlert>>{
		
		const backendUrl:string = environment.backendUrl +'candidate/alert';
  
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
		console.log("SENDING DELTE FROM HERE ");
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
	* Returns Countries that can be filtered on 
	*/
	public getCountries():Array<Country>{
		return this.countries;	
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
	
	/**
	* Returns the code identifying the country
	* @param country - Country to get the country code for
	*/
	public getCountryCode(country:string):string{

		let result = this.countries.filter(c => c.countryName === country);
		
		return result[0] ? result[0].countryCode : 'NA';

  	}
  			
}