import { Component, OnInit } 							from '@angular/core';
import { FormGroup, FormControl }						from '@angular/forms';
import { ListingService }								from '../listing.service';
import { RecruiterService }								from '../recruiter.service';
import { NgbModal, NgbModalOptions}						from '@ng-bootstrap/ng-bootstrap';
import { ViewChild }									from '@angular/core';
import { Listing}										from './listing';
import { Candidate}										from './candidate';
import { CandidateServiceService }						from '../candidate-service.service';
import { environment }									from '../../environments/environment';

@Component({
  selector: 'app-recruiter-listings',
  templateUrl: './recruiter-listings.component.html',
  styleUrls: ['./recruiter-listings.component.css']
})
export class RecruiterListingsComponent implements OnInit {

	@ViewChild('feedbackBox', { static: false }) private content:any;
	//@ViewChild('recommendedCandidates', { static: false }) private recommendedCandidates:any;

  	constructor(private listingService:ListingService, private modalService: NgbModal, private recruiterService:RecruiterService, public candidateService:CandidateServiceService) { }
	
	ngOnInit(): void {
	
		this.recruiterService.getOwnRecruiterAccount().subscribe(data => {
			this.recruiterId 					= data.userId;
			this.recruiterFirstName				= data.firstName;
			this.recruiterSurname				= data.surname;
			this.recruiterEmail					= data.email;
			this.recruiterCompany				= data.companyName;
			this.fetchListings();			
		});
	
		this.loadYearsExperienceValues();
	
  	}

	private  recruiterId:string							= '';
	private  recruiterFirstName:string 					= '';
	private  recruiterSurname:string					= '';
	private  recruiterEmail:string 						= '';
	private  recruiterCompany:string					= '';
	
	public skills:Array<string> 						= new Array<string>();
	public listings:Array<Listing>						= new Array<Listing>();
	public activeView:string							= 'list';
	public activeSubView:string							= 'none';
	public selectedListing:Listing						= new Listing();
	
	public feedbackBoxClass:string          			= '';
  	public feedbackBoxTitle                 			= '';
  	public feedbackBoxText:string           			= '';
	public validationErrors:Array<string>				= new Array<string>();
	public enabldeDeleteOption:boolean					= false;
	
	private	pageSize:number								= 8;
  	public	totalPages:number							= 0;
  	public	currentPage:number							= 0;
	public 	yearsExperienceValues:Array<number>			= new Array<number>();		
	
	public showSuggestedCandidate:boolean 				= false;
	public suggestedCandidate:Candidate			 		= new Candidate();
	public listingForSuggestedCandidate:Listing 		= new Listing();			
  	

	public newListingFormBean:FormGroup 				= new FormGroup({
     
		title:				new FormControl(''),
		type:				new FormControl(),
       	country:			new FormControl(),
		location:			new FormControl(),	
		experienceYears:	new FormControl(),
		rate:				new FormControl(),
		rateCurrency:		new FormControl(),
		description:		new FormControl(),
		contactName:		new FormControl(),
		contactCompany:		new FormControl(),
		contactEmail:		new FormControl(),
		langDutch:			new FormControl(),
		langEnglish:		new FormControl(),
		langFrench:			new FormControl(),
		skill:				new FormControl()
		
	});
	
	public reset():void{
		
		this.newListingFormBean  = new FormGroup({
     
			title:				new FormControl(''),
			type:				new FormControl(),
	       	country:			new FormControl(),
			location:			new FormControl(),	
			experienceYears:	new FormControl(),
			rate:				new FormControl(),
			rateCurrency:		new FormControl(),
			description:		new FormControl(),
			contactName:		new FormControl(),
			contactCompany:		new FormControl(),
			contactEmail:		new FormControl(),
			langDutch:			new FormControl(),
			langEnglish:		new FormControl(),
			langFrench:			new FormControl(),
			skill:				new FormControl()
			
		});
		
		this.skills 				= new Array<string>();
		this.validationErrors 		= new Array<string>();
		this.enabldeDeleteOption	= false;
		
	}
	
	/**
	* Opens the specified Dialog box
	*/
	public open(content:any, msg:string, success:boolean):void {
    
	    if (success) {
	      //Currently not used
	    } else {
	      this.feedbackBoxTitle = 'Validation errors';
	      this.feedbackBoxClass = 'feedback-failure';
	    }
	
	   let options: NgbModalOptions = {
	    	 centered: true
	   };

		this.modalService.open(this.content, options);

  }
	
	/**
	*  Closes the confirm popup
	*/
	public closeModal(): void {
		this.modalService.dismissAll();
		this.validationErrors = new Array<string>();
	}

	
	/**
	* Adds the current skill in the newListingFormBean 
	* to the list of skills required for the listing
	*/
	public addSkill():void {
		
		let skill:string = this.newListingFormBean.get('skill')?.value;
		
		skill = skill.trim();
		skill = skill.toLocaleLowerCase();
		
		if (!this.skills.includes(skill) && skill != '') {
			this.skills.push(skill.trim());	
			this.newListingFormBean.get('skill')?.setValue('');
		}
					
	}
	
	/**
	* Removes the selected skill from the 
	* list of skills required for the listing
	*/
	public removeSkill(skill:any):void{
		
		skill = skill.trim();
		skill = skill.toLocaleLowerCase();
		
		this.skills = this.skills.filter(s => s  !== skill);
	} 
	
	/**
	* Navigates to the specified step
	*/
	public updateSubviewStep(step:string):void{
		this.activeSubView = step;
	}
	
	/**
	* Switches to Add Listing view
	*/
	public showAdd():void{
		this.activeView 			= 'add';
		this.activeSubView 			= 'step1';
		this.selectedListing		= new Listing();
		this.enabldeDeleteOption 	= false;
		
		/**	
		* Default values
		*/
		this.newListingFormBean.get("contactName")?.setValue(this.recruiterFirstName + ' ' + this.recruiterSurname);
		this.newListingFormBean.get("contactEmail")?.setValue(this.recruiterEmail);
		this.newListingFormBean.get("contactCompany")?.setValue(this.recruiterCompany);
			
	}
	
	/**
	* Switches to Add Listing view
	*/
	public showList():void{
		this.activeView 			= 'list';
		this.activeSubView 			= 'none';
		this.selectedListing		= new Listing();
		this.enabldeDeleteOption 	= false;
	}
	
	/**
	* Switches to Show Listing view
	*/
	public showListingDetails(selectedListing?:Listing):void{
		
		this.activeView 			= 'show';
		this.activeSubView 			= 'none';
		this.enabldeDeleteOption 	= false;
		
		if (selectedListing) {
			this.selectedListing	= selectedListing;	
		}
		
	}
		
	/**
	* Switches to Edit Listing view
	*/
	public showEditListing():void{
		
		this.activeView 			= 'edit';
		this.activeSubView 			= 'step1';
		this.enabldeDeleteOption 	= false;
		
		/**	
		* Default values
		*/
		this.newListingFormBean.get("contactName")?.setValue(this.selectedListing.ownerName);
		this.newListingFormBean.get("contactEmail")?.setValue(this.selectedListing.ownerEmail);
		this.newListingFormBean.get("contactCompany")?.setValue(this.selectedListing.ownerCompany);
		
		this.newListingFormBean.get("title")?.setValue(this.selectedListing.title);
		this.newListingFormBean.get("type")?.setValue(this.selectedListing.type);
       	this.newListingFormBean.get("country")?.setValue(this.selectedListing.country);
		this.newListingFormBean.get("location")?.setValue(this.selectedListing.location);	
		this.newListingFormBean.get("experienceYears")?.setValue(this.selectedListing.yearsExperience);
		this.newListingFormBean.get("rate")?.setValue(this.selectedListing.rate);
		this.newListingFormBean.get("rateCurrency")?.setValue(this.selectedListing.currency);
		this.newListingFormBean.get("description")?.setValue(this.selectedListing.description);
		
		this.newListingFormBean.get("langDutch")?.setValue(this.selectedListing.languages.includes('DUTCH'));
		this.newListingFormBean.get("langEnglish")?.setValue(this.selectedListing.languages.includes('ENGLISH'));
		this.newListingFormBean.get("langFrench")?.setValue(this.selectedListing.languages.includes('FRENCH'));
		
		this.skills = this.selectedListing.skills;

		this.newListingFormBean.get("skill")?.setValue('');
	
	}
	
	/**
	* Switches to Previous Show option
	*/
	public showPreviousShowOption():void{
		
		if (this.activeView === 'add') {
			this.showList();
		}
		
		if (this.activeView === 'edit') {
			this.showListingDetails();
		}
		
	}
		
	/**
	* Creates a new Listing. 
	*/
	public publishListing():void{
		
		let ownerName:string 			= this.newListingFormBean.get('contactName')?.value; 
		let ownerCompany:string 		= this.newListingFormBean.get('contactCompany')?.value;
		let ownerEmail:string 			= this.newListingFormBean.get('contactEmail')?.value;
		let title:string 				= this.newListingFormBean.get('title')?.value;
		let description:string 			= this.newListingFormBean.get('description')?.value;
		let type:string 				= this.newListingFormBean.get('type')?.value;
		let country:string 				= this.newListingFormBean.get('country')?.value;	
		let location:string 			= this.newListingFormBean.get('location')?.value;	
		let yearsExperience:number 		= this.newListingFormBean.get('experienceYears')?.value;
		let rate:number 				= this.newListingFormBean.get('rate')?.value; 			
		let currency:string 			= this.newListingFormBean.get('rateCurrency')?.value; 		
		
		let langDutch:boolean 			= this.newListingFormBean.get('langDutch')?.value;
		let langEnglish:boolean 		= this.newListingFormBean.get('langEnglish')?.value;
		let langFrench:boolean 			= this.newListingFormBean.get('langFrench')?.value;
		
		let languages:Array<string> = new Array<string>();
		
		if (langDutch === true) {
			languages.push('DUTCH');
		}
		
		if (langEnglish === true) {
			languages.push('ENGLISH');
		}
		
		if (langFrench === true) {
			languages.push('FRENCH');
		}
		
		this.validationErrors	= new Array<string>();
		
		if (this.selectedListing.listingId === '') {
			
			this.listingService
				.registerListing(ownerName, 
								ownerCompany,
								ownerEmail,
								title,
								description,
								type,	
								country,	
								location,	
								yearsExperience,
								languages,
								this.skills,	
								rate, 			
								currency, 		
								false).subscribe( data => {
									this.reset();
									this.fetchListings();
									this.showList();
								}, err => {
									
									if(err.status === 400) {
										
										let failedFields:Array<any> = err.error;
										
										if (typeof err.error[Symbol.iterator] === 'function') {
											failedFields.forEach(failedField => {
												this.validationErrors.push(failedField.fieldMessageOrKey);
											});
											this.open('feedbackBox', "Failure",  false);
										} else {
											console.log("Failed to Update Listing " + JSON.stringify(err.error));
										}
										
									}
										
								});
								
		} else {
			
			this.listingService
				.updateListing( this.selectedListing.listingId,
								ownerName, 
								ownerCompany,
								ownerEmail,
								title,
								description,
								type,	
								country,	
								location,	
								yearsExperience,
								languages,
								this.skills,	
								rate, 			
								currency, 		
								false).subscribe( data => {
									this.reset();
									this.fetchListings();
									this.showList();
								}, err => {
									
									if(err.status === 400) {
										
										let failedFields:Array<any> = err.error;
										
										if (typeof err.error[Symbol.iterator] === 'function') {
											failedFields.forEach(failedField => {
												this.validationErrors.push(failedField.fieldMessageOrKey);
											});
											this.open('feedbackBox', "Failure",  false);
										} else {
											console.log("Failed to Update Listing " + JSON.stringify(err.error));
										}
										
									}
										
								});
		}
	
	}
	
	/**
	* Retrieves listings belonging to the Recruiter
	*/
	public fetchListings():void{
	
		this.listingService
			.fetchRecruiterListings(this.recruiterId, 'created',"desc", this.currentPage, this.pageSize)
				.subscribe(data => {
					this.totalPages = data.totalPages;
					this.listings 	= data.content;
				}, 
				err => {
					console.log("Error retrieving listings" + JSON.stringify(err));			
				});
		
	}
	
	/**
	* Whether or not the previous page button 
	* should be active
	* */
	public showNavPrev(): boolean{
    
		if (this.totalPages === -1) {
      		return false;
    	}

    	return this.currentPage > 0;

  	}

	/**
	* Whether or not the next page 
	* button should be active
	*/
	public showNavNext():boolean{
    
    	if (this.totalPages === 0) {
      		return false;
    	}

    	return this.currentPage < (this.totalPages -1);

	}
	
	/**
	* Fetches and displays the next page 
	* of candidtes
	*/
	public nextPage(): void{

    	if ((this.currentPage + 1) < this.totalPages) {
      		this.currentPage = this.currentPage + 1;
      		this.fetchListings();
    	}
    
	}

	/**
	* Fetches and displays the previous page of Candidates
	*/
	public previousPage(): void{
    
		if ((this.currentPage) > 0) {
      		this.currentPage = this.currentPage - 1;
      		this.fetchListings();
    	}
  	}

	/**
	* Shows the confirm button to confirm deletion of the 
	* currently selected Listing
	*/
	public showConfirmDelete():void {
		this.enabldeDeleteOption = true;
	}
	
	/**
	* Calls the service to delete the selected Listing
	*/
	public performmDelete():void {
		this.enabldeDeleteOption = false;
		//call service to perform delete
		this.listingService.deleteRecruiterListing(this.selectedListing.listingId).subscribe(data => {
				this.reset();
				this.fetchListings();
				this.showList();
		},err => {
			console.log('Unable to delete Lisring ' + JSON.stringify(err));
		});
	}

	/**
	* Returns the code identifying the country
	* @param country - Country to get the country code for
	*/
	public getCountryCode(country:string):string{

		switch(country){
			case "NETHERLANDS":{
				return "The Netherlands";
			}
			case "BELGIUM":{
				return "Belgium";
			}
			case "UK":{
				return "United Kingdom";
			}
			case "IRELAND":{
				return "Ireland";
			}
			case "EU_REMOTE":{
				return "Remote within EU";
			}
			case "UK":{
				return "Remote within World";
			}
			default:{
				return 'NA';
			}
		}

  	}

	/**
	* Returns the code identifying the country
	* @param country - Country to get the country code for
	*/
	public getContractType(type:string):string{

		switch(type){
			case "CONTRACT_ROLE":{
				return "Contract";
			}
			case "PERM_ROLE":{
				return "Permanent";
			}
			case "BOTH":{
				return "Contract/Permanent";
			}
			default:{
				return 'NA';
			}
		}

  	}	

	/**
	* Returns the Humand readable version of the Language
	* @param country - Language to get the readable version for
	*/
	public getLanguage(lang:string):string{

		switch(lang){
			case "DUTCH":{
				return "Dutch";
			}
			case "FRENCH":{
				return "French";
			}
			case "ENGLISH":{
				return "English";
			}
			default:{
				return 'NA';
			}
		}

  	}	

	/**
	* Provides options for number of years of experience
	*/
	private loadYearsExperienceValues(){
		for (let count=0; count <= 100; count++) {
			this.yearsExperienceValues.push(count);
		}
	}
	
	public candidates:					Array<Candidate>			= new Array<Candidate>();

	/**
	* Retrieves candidates from the backend
	*/
	public fetchCandidateRecommendations(listing:Listing): void{
    
    	this.candidates 						= new Array<Candidate>();
		//let allCandidates:Array<CandidateSuggestion> 		= new Array<Candidate>();
		//let selectedCandidates:Array<Candidate> = new Array<Candidate>();

    	this.candidateService.getCandidates(this.getCandidateFilterParamString(listing)).subscribe( data => {

      		this.totalPages = data.totalPages;
  

				
				
      		data.content.forEach((c:Candidate) => {
        
				console.log('C ===> ' + JSON.stringify(c));

        		const candidate:Candidate = new Candidate();

      			candidate.candidateId			= c.candidateId;
      			candidate.city					= c.city;
      			candidate.country				= this.getCandidateCountryCode(c.country);
      			candidate.freelance				= this.getFreeProjectTypeOption(c.freelance);
      			candidate.roleSought			= c.roleSought;
      			candidate.function				= c.function;
      			candidate.perm					= this.getFreeProjectTypeOption(c.perm);
      			candidate.yearsExperience		= c.yearsExperience;
      			candidate.languages				= c.languages;
      			candidate.skills				= c.skills;
				candidate.flaggedAsUnavailable 	= c.flaggedAsUnavailable;
				candidate.firstname				= c.firstname;
				candidate.surname				= c.surname;
				candidate.email					= c.email;
				candidate.accuracyLanguages		= c.accuracyLanguages;
				candidate.accuracySkills		= c.accuracySkills
				
				this.candidates.push(candidate);
				
			});
			
			//allCandidates.forEach(potentialCandidate => {
				
			//	let skillPerception:string = this.getSkillScoreInternal(listing, potentialCandidate);
				
			//	if (selectedCandidates.length < 12) {
			//		if (skillPerception === 'Perfect') {
      		//			selectedCandidates.push(potentialCandidate);
			//		}		
			//	}
			//});	
			
			//allCandidates.forEach(potentialCandidate => {
				
			//	let skillPerception:string = this.getSkillScoreInternal(listing, potentialCandidate);
				
			//	if (selectedCandidates.length < 12) {
			//		if (skillPerception === 'Good') {
      		//			selectedCandidates.push(potentialCandidate);
			//		}		
			//	}
			//});		
			
			//allCandidates.forEach(potentialCandidate => {
				
			//	let skillPerception:string = this.getSkillScoreInternal(listing, potentialCandidate);
				
			//	if (selectedCandidates.length < 12) {
			//		if (skillPerception === 'Average') {
      		//			selectedCandidates.push(potentialCandidate);
			//		}		
			//	}
			//});	
			
			//allCandidates.forEach(potentialCandidate => {
				
			//	let skillPerception:string = this.getSkillScoreInternal(listing, potentialCandidate);
				
			//	if (selectedCandidates.length < 12) {
			//		if (skillPerception === 'Bad') {
      		//			selectedCandidates.push(potentialCandidate);
			//		}		
			//	}
			//});			
			
		});

		//this.candidates = selectedCandidates;

	}
	
	/**
	* Returns the code identifying the country
	* @param country - Country to get the country code for
	*/
	public getCandidateCountryCode(country:string):string{

		switch(country){
			case "NETHERLANDS":{
				return "NL";
			}
			case "BELGIUM":{
				return "BE";
			}
			case "UK":{
				return "UK";
			}
		}

     	return '';

  	}

	/**
	* Converts freelance option to display format
	*/
	private getFreeProjectTypeOption(freelance: string): string{

		switch (freelance){
        	case  'TRUE': {return 'X';}
        	case  'FALSE': {return '-';}
        	case  'UNKNOWN': {return '?';}
      	}

      	return '..'+freelance;

	}
	
	/**
	* Builds a query parameter string with the selected filter options
	*/
	private getCandidateFilterParamString(listing:Listing):string{
    	
		const filterParams:string = 'orderAttribute=candidateId&order=desc'
                                                         + '&page=0'
                                                         + '&size=12'
														 + '&useSuggestions=true'
                                                         + this.getCountryFilterParamString(listing) 			
                                                         + this.getContractTypeParamString(listing)				
                                                         + this.getYearsExperienceFilterParamAsString(listing)
														 + this.getSkillsParamString(listing)
														 + this.getLanguagesParamString(listing);
					                                   
		return filterParams;
	
	}
	
	private getSkillsParamString(listing:Listing):string{
      
		if (listing.skills.length > 0) {

			let rawSkills:string = listing.skills.toString();
			let encodedSkills:string = '&skills='+encodeURIComponent(rawSkills);
			
			return encodedSkills; 
      	}

      return '';
 
	}
	
	/**
  	* Creates a query param string with the filter options to apply to the dutch languge filter
  	*/
	private getLanguagesParamString(listing:Listing):string{
  
		let paramString:string = '';
		
		if (listing.languages.indexOf('DUTCH')  >= 0 ) {
			paramString = paramString +  '&dutch=' + 'PROFICIENT';
		}
		
		if (listing.languages.indexOf('FRENCH')  >= 0 ) {
			paramString = paramString +  '&french=' + 'PROFICIENT';
		}
		
		if (listing.languages.indexOf('ENGLISH')  >= 0 ) {
			paramString = paramString +  '&english=' + 'PROFICIENT';
		}  
		
		return paramString;
  
	}
  
	/**
	* Adds filter string if country specifed in Listing
	*/
	public getCountryFilterParamString(listing:Listing):string{
		
		if (listing.country === ''){
			return '';
		}
		
		return '&countries=' + listing.country;
	}
	
	/**
	* Adds filter for perm positions if specified in the listing
	*/
	public getContractTypeParamString(listing:Listing):string{
		
		if (listing.type === 'CONTRACT_ROLE') {
			return '&freelance=true';
		}
		
		if (listing.type === 'PERM_ROLE') {
			return '&perm=true';
		}
	
      	return '';
	}
		
	/**
 	* Adds filter for years expeprience  if specified in the listing
	*/
	public getYearsExperienceFilterParamAsString(listing:Listing):string{
		
		if (listing.yearsExperience === 0) {
			return '';
		}
		
		return '&yearsExperienceGtEq=' + listing.yearsExperience;
	}

	/**
	* Shows list of candidates similar to job listing
	*/
	public showSuggestedCandidateListView():void{
		this.showSuggestedCandidate = false;
		this.suggestedCandidate = new Candidate();
	}
	
	/**
	* Shows Candidate details
	*/
	public showSuggestedCandidateView(candidate:Candidate):void{
		
		this.showSuggestedCandidate = true;
		this.suggestedCandidate 	= candidate;
	}
	
	/**
	* Shows the user suggestions relating to their listing 
	*/
	public openSuggestions(popupRecommendations:any, listing:Listing):void{
	
		this.showSuggestedCandidate 		= false;
		this.suggestedCandidate	 			= new Candidate();
		this.listingForSuggestedCandidate 	= listing;
		
		this.fetchCandidateRecommendations(listing);
	
		let options: NgbModalOptions = {
			centered: true
		};
		
		this.modalService.open(popupRecommendations, options);

	}
	
	/**
	*  Returns the url to perform the download of the candidates CV
	*/
	public getCurriculumDownloadUrl(curriculumId:string){
		return  environment.backendUrl + 'curriculum/'+ curriculumId;
	}
	
	/**
	* Overloaded method to get skill score
	* called form html
	*/
	//public getSkillScore():string{
		
	//	let listing:Listing 				= this.listingForSuggestedCandidate;
	//	let candidate:Candidate 			= this.suggestedCandidate;
		
	//	return this.getSkillScoreInternal(listing, candidate);
		
	//}
	
	/**
	* Returns how good the match between the required skills in the listing
	* compare to the skills spoken by the candidate
	*/
	//private getSkillScoreInternal(listing:Listing, candidate:Candidate):string{
		
	//	let totalPossiblePoints:number 		= listing.skills.length;
	//	let actualPoints					= 0;
		
	//	listing.skills.forEach(skill => {
	//		if (candidate.skills.indexOf(skill) >= 0) {
	//			actualPoints = actualPoints + 1;
	//		}
	//	});
		
	//	return this.percentionValue(totalPossiblePoints, actualPoints);
		
	//}
	
	/**
	* Returns how good the match between the required languages in the listing
	* compare to the languages spoken by the candidate
	*/
	//public getLanguageMatchScore():string{
		
	//	let listing:Listing 				= this.listingForSuggestedCandidate;
	//	let candidate:Candidate 			= this.suggestedCandidate;
	//	let totalPossiblePoints:number 		= 0;
	//	let actualPoints					= 0;
		
	//	if (listing.languages.includes('DUTCH')) {
			
	//		totalPossiblePoints = totalPossiblePoints + 2;
			
	//		if (candidate.getDutch() === 'PROFICIENT') {
	//			actualPoints = actualPoints + 2;
	//		}
				
	//		if (candidate.getDutch() === 'BASIC') {
	//			actualPoints = actualPoints + 1;
	//		}
			
	//	}
		
	//	if (listing.languages.includes('ENGLISH')) {
			
	//		totalPossiblePoints = totalPossiblePoints + 2;
			
	//		if (candidate.getEnglish() === 'PROFICIENT') {
	//			actualPoints = actualPoints + 2;
	//		}
				
	//		if (candidate.getEnglish() === 'BASIC') {
	//			actualPoints = actualPoints + 1;
	//		}
	//				
	//	}
		
	//	if (listing.languages.includes('FRENCH')) {
	//		
	//		totalPossiblePoints = totalPossiblePoints + 2;
			
	//		if (candidate.getFrench() === 'PROFICIENT') {
	//			actualPoints = actualPoints + 2;
	//		}
				
	//		if (candidate.getFrench() === 'BASIC') {
	//			actualPoints = actualPoints + 1;
	//		}
			
	//	}
		
	//	return this.percentionValue(totalPossiblePoints, actualPoints);
		
	//}
	
	/**
	* Returns the perceptions of how well a requirement is met by the Candidate
	* in realtions to a requirement in the Listing
	*/
	//private percentionValue(totalPossiblePoints:number, actualPoints:number):string {
	
	//	if (totalPossiblePoints === 0) {
	//		return 'N/A';	
	//	}
		
	//	let per:number = (actualPoints / totalPossiblePoints) * 100;
		
	//	if (per === 100) {
	//		return 'Perfect';
	//	}
		
	//	if (per >= 75) {
	//		return "Good";
	//	}
		
	//	if (per >= 50) {
	//		return "Average";
	//	}
	//	
	//	if (per < 50) {
	//		return "Bad";
	//	}
		
	//	return '';
			
	//}
	
}