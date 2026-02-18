import { Injectable }                             	from '@angular/core';
import { HttpClient, HttpResponse, HttpHeaders }  	from '@angular/common/http';
import { Observable, BehaviorSubject  }             from 'rxjs';
import { environment }								from './../environments/environment';
import { NewOfferedCandidate } 						from './recruiter-marketplace/new-offered-candidate';
import { OfferedCandidate } 						from './recruiter-marketplace/offered-candidate';
import { NewOpenPosition } 							from './recruiter-marketplace/new-open-position';
import { OpenPosition } 							from './recruiter-marketplace/open-position';
import { SupportedCountry } 						from './supported-candidate';

@Injectable({
  providedIn: 'root'
})
export class RecruiterMarketplaceService {

	public unseenMpPosts 					= new BehaviorSubject<number>(0);
	public unseenMpPostsOfferedCandidates 	= new BehaviorSubject<number>(0);
	public unseenMpPostsOpenPositions 		= new BehaviorSubject<number>(0);
	public supportedCountries:Array<SupportedCountry> 	= new Array<SupportedCountry>();
	
	/**
  	* Initializes the countries available in the Marketplace
  	*/
	public async initializeSupportedCountries():Promise<any>{
	
		const backendUrl:string = environment.backendUrl +'candidate/countries';	//TODO: [KP] Create and point to endpoint in marketplace
		const config 			= await this.httpClient.get<any>(backendUrl,  { observe: 'response', withCredentials: true}).toPromise();
 
 		config.body.forEach( (country: SupportedCountry) => {
			this.supportedCountries.push(new SupportedCountry(''+country.name, country.iso2Code, country.humanReadable));	
		});
 
 	   	Object.assign(this, config);
    	
    	return config;
		
	}
	
	/**
	* Returns Countries supported by the system 
	*/
	public getSupportedCountries():Array<SupportedCountry>{
		return this.supportedCountries;	
	}
	
	/**
	* Refreshed MP data
	*/
	public updateUnseenMpPosts():void{
		this.fetchMPPosts();
	}

	/**
	* Fetches the total number of posts not yet viewed
	* by the user
	*/
	public fetchUnseenMPPosts():Observable<number>{
		return this.unseenMpPosts;
	}
	
	/**
	* Fetches the total number of offered candidate posts not yet viewed
	* by the user
	*/
	public fetchUnseenOfferedCandidates():Observable<number>{
		return this.unseenMpPostsOfferedCandidates;
	}
	
	/**
	* Fetches the total number of open position posts not yet viewed
	* by the user
	*/
	public fetchUnseenOpenPositions():Observable<number>{
		return this.unseenMpPostsOpenPositions;
	}
	
	public fetchUnseenOpenPositionCount():Observable<number>{
		const backendUrl:string = environment.backendUrl +'v1/open-position/count';
				
		return this.httpClient.get<any>(backendUrl, this.httpOptions);
	}
	
	/**
	* Fetchs MP post info
	*/
	private fetchMPPosts():void{
		
		if (sessionStorage.getItem("userId")) {
			let recruiterId:string = sessionStorage.getItem("userId")+'';
			
			this.unseenMpPosts.next(0);
			this.unseenMpPostsOfferedCandidates.next(0);
			this.unseenMpPostsOpenPositions.next(0);
			
			this.fetchOpenPositions().subscribe(positions => {
				this.unseenMpPosts.next(this.unseenMpPosts.getValue() + positions.filter( p => !p.viewed && p.recruiter.recruiterId != recruiterId).length);
				this.unseenMpPostsOpenPositions.next(positions.filter( p => !p.viewed && p.recruiter.recruiterId != recruiterId).length);
				
			});
		}
		
	}

	/**
	* Constructor
	* @param httpClient - for sending httpRequests to backend
	*/
	constructor(private httpClient: HttpClient) { 
		
	}

	httpOptions = {
		headers: new HttpHeaders({ 'Content-Type': 'application/json' }), withCredentials: true
	};

	headers = { 'content-type': 'application/json', 'Cache-Control': 'no-cache, no-store', 'Pragma': 'no-cache'};
	
	/**
	* Fetches Candidates being offered by Recruiters
	*/
	public fetchOfferedCandidates(): Observable<Array<OfferedCandidate>>{
		
		const backendUrl:string = environment.backendUrl +'v1/offered-candidate';
		
		return this.httpClient.get<any>(backendUrl, this.httpOptions);
	}

	/**
	* Fetches OpenPositions being offered by Recruiters
	*/
	public fetchOpenPositions(): Observable<Array<OpenPosition>>{
		
		const backendUrl:string = environment.backendUrl +'v1/open-position';
		
		return this.httpClient.get<any>(backendUrl, this.httpOptions);
	}
		
	/**
	* Fetches Candidates being offered by Recruiters
	*/
	//TODO: Why not get this from SC and if not make sure check done on BE for correct Recruiter
	public fetchRecruitersOwnOfferedCandidates(recruiterId:string): Observable<Array<OfferedCandidate>>{
		
		const backendUrl:string = environment.backendUrl +'v1/offered-candidate/rectuiter/'+recruiterId;
		
		return this.httpClient.get<any>(backendUrl, this.httpOptions);
	}
	
	/**
	* Fetches Candidates being offered by Recruiters
	*/
	//TODO: Why not get this from SC and if not make sure check done on BE for correct Recruiter
	public fetchRecruitersOwnOpenPositions(recruiterId:string): Observable<Array<OpenPosition>>{
		
		const backendUrl:string = environment.backendUrl +'v1/open-position/rectuiter/'+recruiterId;
		
		return this.httpClient.get<any>(backendUrl, this.httpOptions);
	}
	

	/**
	* Deletes a Candidate being offered by Recruiters
	*/
	public deleteRecruitersOwnOfferedCandidates(candidateId:string): Observable<Array<OfferedCandidate>>{
		
		const backendUrl:string = environment.backendUrl +'v1/offered-candidate/'+candidateId;
		
		return this.httpClient.delete<any>(backendUrl, this.httpOptions);
	}
	
	/**
	* Deletes an Open Position being offered by Recruiters
	*/
	public deleteRecruitersOwnOpenPositions(openPositionId:string): Observable<Array<OpenPosition>>{
		
		const backendUrl:string = environment.backendUrl +'v1/open-position/'+openPositionId;
		
		return this.httpClient.delete<any>(backendUrl, this.httpOptions);
	}
		
	/**
	* Registers a Listing with the backend
	*/
	public registerOfferedCandidate(candidateRoleTitle:string, 
									country:string,
									location:string,
									contractType:string,
									daysOnSite:string,
									renumeration:string,
									availableFromDate:string,	
									yearsExperience:string,	
									description:string,
									comments:string, 			
									languages:Array<string>,
									skills:Array<string>):Observable<any>{
		
		const backendUrl:string = environment.backendUrl +'v1/offered-candidate';
		
		let offeredCandidate:NewOfferedCandidate = new NewOfferedCandidate();
		
		offeredCandidate.candidateRoleTitle 	= candidateRoleTitle; 
		offeredCandidate.country 				= country;
		offeredCandidate.location 				= location;
		offeredCandidate.contractType 			= contractType;
		offeredCandidate.daysOnSite 			= daysOnSite;
		offeredCandidate.renumeration 			= renumeration;
		offeredCandidate.availableFromDate 		= availableFromDate;	
		offeredCandidate.yearsExperience 		= yearsExperience;	
		offeredCandidate.description 			= description;
		offeredCandidate.comments 				= comments; 			
		offeredCandidate.spokenLanguages 		= languages;
		offeredCandidate.coreSkills 			= skills;
		
		return this.httpClient.post<any>(backendUrl, JSON.stringify(offeredCandidate), this.httpOptions);
	
	}
	
	/**
	* Registers a Listing with the backend
	*/
	public updateOfferedCandidate(	candidateId:string,
									candidateRoleTitle:string, 
									country:string,
									location:string,
									contractType:string,
									daysOnSite:string,
									renumeration:string,
									availableFromDate:string,	
									yearsExperience:string,	
									description:string,
									comments:string, 			
									languages:Array<string>,
									skills:Array<string>):Observable<any>{
		
		const backendUrl:string = environment.backendUrl +'v1/offered-candidate/'+candidateId;
		
		let offeredCandidate:NewOfferedCandidate = new NewOfferedCandidate();
		
		offeredCandidate.candidateRoleTitle 	= candidateRoleTitle; 
		offeredCandidate.country 				= country;
		offeredCandidate.location 				= location;
		offeredCandidate.contractType 			= contractType;
		offeredCandidate.daysOnSite 			= daysOnSite;
		offeredCandidate.renumeration 			= renumeration;
		offeredCandidate.availableFromDate 		= availableFromDate;	
		offeredCandidate.yearsExperience 		= yearsExperience;	
		offeredCandidate.description 			= description;
		offeredCandidate.comments 				= comments; 			
		offeredCandidate.spokenLanguages 		= languages;
		offeredCandidate.coreSkills 			= skills;
		
		return this.httpClient.put<any>(backendUrl, JSON.stringify(offeredCandidate), this.httpOptions);
	
	}
	
	/**
	* Registers an Open Position with the backend
	*/
	public registerOpenPosition(positionTitle:string,
								country:string,
								location:string,
								contractType:string,
								renumeration:string,
								startDate:Date,
								positionClosingDate:Date,
								description:string,
								comments:string,
								languages:Array<string>,
								skills:Array<string>):Observable<any>{
		
		const backendUrl:string = environment.backendUrl +'v1/open-position';
		
		let openPosition:NewOpenPosition = new NewOpenPosition();
		
		openPosition.positionTitle 			= positionTitle; 
		openPosition.country 				= country;
		openPosition.location 				= location;
		openPosition.contractType 			= contractType;
		openPosition.renumeration 			= renumeration;
		openPosition.startDate 				= startDate;	
		openPosition.positionClosingDate 	= positionClosingDate;	
		openPosition.description 			= description;
		openPosition.comments 				= comments; 			
		openPosition.spokenLanguages 		= languages;
		openPosition.skills 				= skills;
		
		return this.httpClient.post<any>(backendUrl, JSON.stringify(openPosition), this.httpOptions);
	
	}
	
	/**
	* Updates an existing open position with the backend
	*/
	public updateOpenPosition(
				openPositionId:string,
				positionTitle:string,
				country:string,
				location:string,
				contractType:string,
				renumeration:string,
				startDate:Date,
				positionClosingDate:Date,
				description:string,
				comments:string,
				languages:Array<string>,
				skills:Array<string>
			):Observable<any>{
		
		const backendUrl:string = environment.backendUrl +'v1/open-position/'+openPositionId;
		
		let openPosition:NewOpenPosition = new NewOpenPosition();
		
		openPosition.positionTitle 			= positionTitle; 
		openPosition.country 				= country;
		openPosition.location 				= location;
		openPosition.contractType 			= contractType;
		openPosition.renumeration 			= renumeration;
		openPosition.startDate 				= startDate;	
		openPosition.positionClosingDate 	= positionClosingDate;	
		openPosition.description 			= description;
		openPosition.comments 				= comments; 			
		openPosition.spokenLanguages 		= languages;
		openPosition.skills 				= skills;
		
		return this.httpClient.put<any>(backendUrl, JSON.stringify(openPosition), this.httpOptions);
	
	}
	
	/**
	* Registers an event indicating an Open Position was viewed
	*/
	public registerOpenPositionViewedEvent(id:string):Observable<any>{
		
		const backendUrl:string = environment.backendUrl +'v1/open-position/viewed/'+id;
		
		return this.httpClient.post<any>(backendUrl, null, this.httpOptions);
	
	}
	
	/**
	* Registers an event indicating an Offered Candidate was viewed
	*/
	public registerOfferedCandidateViewedEvent(id:string):Observable<any>{
		
		const backendUrl:string = environment.backendUrl +'v1/offered-candidate/viewed/'+id;
		
		return this.httpClient.post<any>(backendUrl, null, this.httpOptions);
	
	}
	
	/**
	* Performs a check to see if the User has access to the Curriculum either
	* because they do not use credt based acces or beause they have remaining 
	* credits 
	*/
	public doCreditCheck():Observable<boolean>{
		
		const backendUrl:string = environment.backendUrl + 'v1/open-position/creditCheck';
  	
		return this.httpClient.get<boolean>(backendUrl, this.httpOptions);
		
	}
	
}