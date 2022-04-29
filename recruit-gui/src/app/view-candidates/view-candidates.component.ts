import { Component, OnInit }					from '@angular/core';
import { CandidateServiceService }				from '../candidate-service.service';
import { Candidate }							from './candidate';
import { CandidateFunction }					from '../candidate-function';
import { NgbModal}								from '@ng-bootstrap/ng-bootstrap';
import { FormGroup, FormControl }				from '@angular/forms';
import { environment }							from '../../environments/environment';
import { Router}								from '@angular/router';

@Component({
  selector: 'app-view-candidates',
  templateUrl: './view-candidates.component.html',
  styleUrls: ['./view-candidates.component.css']
})
export class ViewCandidatesComponent implements OnInit {

	public functionTypes:				Array<CandidateFunction>	= new Array<CandidateFunction>();
  	public candidates:					Array<Candidate>			= new Array<Candidate>();
  	public yearsExperienceOptions:		Array<number>				= new Array<number>();
	public selectedCandidateSkills:		string						= "";

  	/**
  	* Filters
  	*/
  	public	activeFilter:					string						= '';
  	private	sortColumn:						string						= 'candidateId';
  	private	sortOrder:						string						= 'desc'
  	public	showSortOptons:					boolean						= false;
  	public	selectedSortOrderForFilter:		string						= '';
  	public	countryFiltNL:					boolean						= false;
  	public	countryFiltBE:					boolean						= false;
  	public	countryFiltUK:					boolean						= false;
  	private	pageSize:						number						= 8;
  	public	totalPages:						number						= 0;
  	public	currentPage:					number						= 0;
  	public	functionOptionsPage1:			Array<CandidateFunction>	= new Array<CandidateFunction>();
  	public	functionOptionsPage2:			Array<CandidateFunction>	= new Array<CandidateFunction>();

	/**
	* Info Boxes
	*/
	public	activeInfoBox:					string						= '';
	
	/**
  	* Constructor
	*/
	constructor(public candidateService:CandidateServiceService, private modalService: NgbModal, private router: Router) {

		this.getYearsExperienceOption();
      
		let counter:number = 0;

		this.candidateService.loadFunctionTypes().forEach(funcType => {

			this.functionTypes.push(funcType);
			this.functionTypeFilterForm.addControl(funcType.id, new FormControl(''));

			if (counter < 8) {
        		this.functionOptionsPage1.push(funcType);
      		} else {
        		this.functionOptionsPage2.push(funcType);
      		}

      		counter = counter +1;

    	});

	}

	public showCandidateSkills(content: any, candidateSkills:string):void {
		
		this.selectedCandidateSkills = candidateSkills;
		this.activeInfoBox = 'showCandidateSkills';
		this.modalService.open(content, { centered: true });

	}
	
	/**
	* Fetches and displays the next page 
	* of candidtes
	*/
	public nextPage(): void{

    	if ((this.currentPage + 1) < this.totalPages) {
      		this.currentPage = this.currentPage + 1;
      		this.fetchCandidates(false);
    	}
    
	}

	/**
	* Fetches and displays the previous page of Candidates
	*/
	public previousPage(): void{
    
		if ((this.currentPage) > 0) {
      		this.currentPage = this.currentPage - 1;
      		this.fetchCandidates(false);
    	}
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

	public countryFilterForm:FormGroup = new FormGroup({
     
    	NETHERLANDS:  new FormControl(''),
    	BELGIUM:      new FormControl(''),
    	UK:           new FormControl('')
   
	});

	public functionTypeFilterForm: FormGroup = new FormGroup({
    	//Set dynamically
	});

	public  skillFilterSelections:Array<string> = new Array<string>();

  	public skillFilterForm: FormGroup = new FormGroup({
		skill:  new FormControl(''),
	});
  
	/**
	* Adds new skill to filter 
	*/
	public updateSkillFilters():void{
		this.skillFilterSelections.push(this.skillFilterForm.get('skill')?.value)
      	this.skillFilterForm.get('skill')?.setValue('');
      	this.updateFilters();
  	}
  
  	public removeSkill(skill:string):void{
		this.skillFilterSelections = this.skillFilterSelections.filter(s => s  !== skill);
		this.updateFilters();
	}
  
	public freelanceFilterForm: FormGroup = new FormGroup({
		include:  new FormControl('false'),
	});

	public permFilterForm: FormGroup = new FormGroup({
		include:  new FormControl('false'),
	});
  
	public dutchFilterForm: FormGroup = new FormGroup({
		level: new FormControl(''),
	});
  
	public frenchFilterForm: FormGroup = new FormGroup({
		level:  new FormControl(''),
	});
  
	public englishFilterForm: FormGroup = new FormGroup({
		level:  new FormControl(''),
	});
  
	public yearsExperienceFilterForm:FormGroup = new FormGroup({
		yearsExperienceGtEq: new FormControl(''),
		yearsExperienceLtEq: new FormControl('')
	});
	
	/**
	* Returns whether the candidates are being sorted in 
	* ASC or DESC order
	* @return direction of the sorting for the candidate results 
	*/
	public sortOrderValue():string{
     
		if (this.sortColumn === this.activeFilter) {
        	return this.sortOrder;
      	}
      
      	return "";
      
   	}

	/**
  	* Initializes component
  	*/
  	ngOnInit(): void {
    	this.fetchCandidates(true);    
  	}

	/**
	* Retrieves candidates from the backend
	*/
	private fetchCandidates(resetPagination: boolean): void{
    
		if (resetPagination) {
        	this.totalPages = 0;
        	this.currentPage = 0;
		}

    	this.candidates = new Array<Candidate>();

    	this.candidateService.getCandidates(this.getCandidateFilterParamString()).subscribe( data => {
    
      		this.totalPages = data.totalPages;
  
      		data.content.forEach((c:Candidate) => {
        
        		const candidate:Candidate = new Candidate();

      			candidate.candidateId				= c.candidateId;
      			candidate.city						= c.city;
      			candidate.country					= this.getCountryCode(c.country);
      			candidate.freelance					= this.getFreelanceOption(c.freelance);
      			candidate.roleSought				= c.roleSought;
      			candidate.function					= c.function;
      			candidate.perm						= this.getPermOption(c.perm);
      			candidate.yearsExperience			= c.yearsExperience;
      			candidate.languages					= c.languages;
      			candidate.skills					= c.skills;
				candidate.flaggedAsUnavailable 		= c.flaggedAsUnavailable;

      			this.candidates.push(candidate);

			});
        
		}, err => {
			
			if (err.status === 401 || err.status === 0) {
				sessionStorage.removeItem('isAdmin');
				sessionStorage.removeItem('isRecruter');
				sessionStorage.removeItem('loggedIn');
				sessionStorage.setItem('beforeAuthPage', 'view-candidates');
				this.router.navigate(['login-user']);
			}
    	})

	}

	/**
	* Converts freelance option to display format
	*/
	private getFreelanceOption(freelance: string): string{

		switch (freelance){
        	case  'TRUE': {return 'X';}
        	case  'FALSE': {return '-';}
        	case  'UNKNOWN': {return '?';}
      	}

      	return '..'+freelance;

	}

    /**
	* Converts perm option to display format
	*/
	private getPermOption(perm: string): string{

		switch (perm){
        	case  'TRUE': {return 'X';}
        	case  'FALSE': {return '-';}
        	case  'UNKNOWN': {return '?';}
      	}

      	return '';

	}
  
	/**
	* Builds a query parameter string with the selected filter options
	*/
	private getCandidateFilterParamString():string{
    	
		const filterParams:string = 'orderAttribute=' + this.sortColumn
                                                         + "&order=" 
                                                         + this.sortOrder 
                                                         + '&page=' + this.currentPage
                                                         + '&size=' + this.pageSize
                                                         + this.getCountryFilterParamString() 
                                                         + this.getFunctionTypeFilterParamString()
                                                         + this.getPermFilterParamString()
                                                         + this.getFreelanceFilterParamString()
                                                         + this.getYearsExperienceFilterParamAsString()
                                                         + this.getDutchParamString()
                                                         + this.getFrenchParamString()
                                                         + this.getEnglishParamString()
                                                         + this.getSkillsParamString();
		return filterParams;
	
	}
  
	private getSkillsParamString():string{
      
		if (this.skillFilterSelections.length > 0) {
			return '&skills='+encodeURIComponent(this.skillFilterSelections.toString()); 
      	}

      return '';
 
	}
  
  	/**
  	* Creates a query param string with the filter options to apply to the dutch languge filter
  	*/
	private getDutchParamString():string{
  
		if (this.dutchFilterForm.get('level')?.value.length > 0 ) {
			return  '&dutch=' + this.dutchFilterForm.get('level')?.value;
		}
  
	return '';
  
	}
  
	/**
	* Creates a query param string with the filter options to apply to the french languge filter
	*/
	private getFrenchParamString():string{
   
       if (this.frenchFilterForm.get('level')?.value.length > 0 ) {
           return  '&french=' + this.frenchFilterForm.get('level')?.value;
       }
   
       return '';
   }
   
   /**
    * Creates a query param string with the filter options to apply to the english  languge filter
    */
    private getEnglishParamString():string{
    
        if (this.englishFilterForm.get('level')?.value.length > 0 ) {
            return  '&english=' + this.englishFilterForm.get('level')?.value;
        }
    
        return '';
    }

	/**
	* Creates a query param string with the filter options to apply to the perm
	*/
	private getPermFilterParamString(): string{
  
		if (this.permFilterForm.get('include')?.value == 'true') {
			return "&perm=true";
		}

      	return '';
	}
	
	/**
	* Creates a query param string with the filter options to apply to the perm
	*/
	private getFreelanceFilterParamString(): string{
  
		if (this.freelanceFilterForm.get('include')?.value == 'true') {
			return "&freelance=true";
		}

		return '';
	}
  
	private getYearsExperienceFilterParamAsString(): string{
      
		let values:string = '';
  
		if (this.yearsExperienceFilterForm.get('yearsExperienceGtEq')?.value.length > 0 ) {
			values = values  + '&yearsExperienceGtEq=' + this.yearsExperienceFilterForm.get('yearsExperienceGtEq')?.value;
		}
      
		if (this.yearsExperienceFilterForm.get('yearsExperienceLtEq')?.value.length > 0) {
			values = values  + '&yearsExperienceLtEq=' + this.yearsExperienceFilterForm.get('yearsExperienceLtEq')?.value;
		}
  
		return values;
      
	}
  
	public getYearsExperienceOption():void{
  
		let i:number = 1;
  
		while(i <= 100) {
			this.yearsExperienceOptions.push(i);''
			i = i + 1;
		}
      
	}


	/**
	* Creates a query param string with the filter options to apply to the candidate
	* search
	*/
	private getCountryFilterParamString(): string{

		const countries: Array<string> = new Array<string>();

    	if (this.countryFilterForm.get('NETHERLANDS')?.value === true) {
      		countries.push('NETHERLANDS');
    	}
    
    	if (this.countryFilterForm.get('BELGIUM')?.value === true) {
     	 	countries.push('BELGIUM');
    	}

    	if (this.countryFilterForm.get('UK')?.value === true) {
      		countries.push('UK');
    	}

    	if (countries.length === 0) {
      		return '';
    	}

    	return '&countries=' + countries.toString();
  	}

	/**
  	* Creates a query param string with the filter options to apply to the candidate
  	* search
  	*/
	private getFunctionTypeFilterParamString(): string{

		const functionTypes: Array<string> = new Array<string>();

		Object.keys(this.functionTypeFilterForm.controls).forEach(key => {
     
			const control: any = this.functionTypeFilterForm.get(key);
     
			if (control?.value === true) {
				functionTypes.push(key);
			} 
     
		});

		if (functionTypes.length === 0) {
			return '';
		}

		return '&functions=' + functionTypes.toString();

	}

	/**
	*  Returns the url to perform the download of the filterable candidate list
	*/
	public getCandidateDownloadUrl(){
		return  environment.backendUrl + 'candidate/download?'+ this.getCandidateFilterParamString();
	}
  
	/**
	*  Returns the url to perform the download of the candidates CV
	*/
	public getCurriculumDownloadUrl(curriculumId:string){
		return  environment.backendUrl + 'curriculum/'+ curriculumId;
	}

	/**
	* Returns the code identifying the country
	* @param country - Country to get the country code for
	*/
	public getCountryCode(country:string):string{

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

     	return 'NA';

  	}

	/**
  	* Returns the humand readable decription of a function 
  	* based upon its id
  	* @param id - id of the function to return the desc for 
  	*/
  	public pretifyFunction(id: string): string {
    	return this.functionTypes.filter(ft => ft.id === id)[0].desc;
	}

	/**
	* Opens the modal filter 
	* @param content - modal to display
	* @param column  - column to display modal for
	*/
	public open (content: any, column: string) {
    
		this.activeFilter = column;
    	this.showOrderFilter();
    	this.modalService.open(content, { centered: true });

    	if (this.activeFilter === this.sortColumn) {
      		this.selectedSortOrderForFilter = this.sortOrder;
    	} else {
      		this.selectedSortOrderForFilter = '';
    	}

  	}

	public selectedCandidate:Candidate = new Candidate();
	
	public openSettingsOptons(content: any, candidate: Candidate) {
    
		this.selectedCandidate = candidate;
		
    	this.modalService.open(content, { centered: true });

  	}

	/**
  	*  Closes the filter popup
  	*/
  	public closeModal(): void {
    	this.modalService.dismissAll();
  	}

  /**
  * Changes the order of the candidates to be based upon 
  * the selected colum and orders by the selected direction
  * @param order - asc | desc 
  */
  public updateSortOrder(order:any):void{

    let indx:number = order.selectedIndex;

    switch (indx) {
      case 0:{
          this.sortColumn = 'candidateId';
          this.sortOrder = 'desc'
          break;
      }
      case 1:{
        this.sortOrder = 'asc'
        this.sortColumn = this.activeFilter;
        break;
      }
      case 2:{
        this.sortOrder = 'desc'
        this.sortColumn = this.activeFilter;
        break;
      }
    }

    this.fetchCandidates(true);

  }

  /**
  * Fetches the candidates with the lastes filter selections applied 
  */
  public updateFilters(): void{
    this.fetchCandidates(true);
  }

  /**
  * Sets whether or not to show the Order filters based upon 
  * which column the filters are open for
  */
  public showOrderFilter():void{
    switch(this.activeFilter) {

      case 'candidateId':
      case 'country':
      case 'city':
      case 'roleSought':
      case 'yearsExperience':{
        this.showSortOptons = true;
        console.log("T");
        return;
      }

    }

    this.showSortOptons = false;

  }

	/**
	* Returns the appropriate CSS class for the filter icon
	* based upon whether the Filter is active or not
	*/
	public isFilterCandidateIdActiveClass(): string{
    	return this.sortColumn === 'candidateId' ? 'filterSelected' : '';
	}

	/**
	* Returns the appropriate CSS class for the filter icon
	* based upon whether the Filter is active or not
	*/
	public isFilterCountryActiveClass(): string{
    
		if (this.sortColumn === 'country') {
      		return 'filterSelected';
    	}

    	if (this.getCountryFilterParamString() !== '') {
      		return 'filterSelected';
    	} 

    	return '';

	}

	/**
	* Returns the appropriate CSS class for the filter icon
	* based upon whether the Filter is active or not
	*/
	public isFilterCityActiveClass(): string{
    	return this.sortColumn === 'city' ? 'filterSelected' : '';
	}

	/**
	* Returns the appropriate CSS class for the filter icon
	* based upon whether the Filter is active or not
	*/
	public isFilterFunctionActiveClass(): string{
    
		if (this.sortColumn === 'roleSought') {
			return 'filterSelected';
    	}

    	if (this.getFunctionTypeFilterParamString() !== ''){
      		return 'filterSelected';
    	}

    	return '';

  	}

	/**
	* Returns the appropriate CSS class for the filter icon
	* based upon whether the Filter is active or not
	*/
	public isFilterYearsExperienceActiveClass(): string{
      
		if (this.yearsExperienceFilterForm.get('yearsExperienceGtEq')?.value.length > 0) {
			return 'filterSelected';
		}
      
		if (this.yearsExperienceFilterForm.get('yearsExperienceLtEq')?.value.length > 0 ) {
			return 'filterSelected';
		}
   
		return this.sortColumn === 'yearsExperience' ? 'filterSelected' : '';
      
	}

	/**
	* Returns the appropriate CSS class for the filter icon
	* based upon whether the Filter is active or not
	*/
	public isFilterPermActiveClass(): string{

		const permVal: any = this.permFilterForm.get('include')?.value;

		if (permVal == 'true') {
      		return 'filterSelected';
    	}
    	
		return '';
  	
	}

	/**
	* Returns the appropriate CSS class for the filter icon
	* based upon whether the Filter is active or not
	*/
	public isFilterFreelanceActiveClass(): string{

		const freelanceVal: any = this.freelanceFilterForm.get('include')?.value;

      	if (freelanceVal == 'true') {
        	return 'filterSelected';
      	}

      	return '';
  	
	}
    
	/**
	* Returns the appropriate CSS class for the filter icon
	* based upon whether the Filter is active or not
	*/
    public isFilterSkillsActiveClass(): string{
      
		if (this.skillFilterSelections.length > 0) {
			return 'filterSelected';
		}
     
		return '';
		
     }

	/**
	* Returns the appropriate CSS class for the filter icon
	* based upon whether the Filter is active or not
	*/    
    public isFilterFrenchActiveClass(): string{

        const frenchVal: any = this.frenchFilterForm.get('level')?.value;

        if (frenchVal.length > 0) {
          return 'filterSelected';
        }

        return '';
    }

	/**
	* Returns the appropriate CSS class for the filter icon
	* based upon whether the Filter is active or not
	*/
    public isFilterEnglishActiveClass(): string{

        const englishVal: any = this.englishFilterForm.get('level')?.value;

        if (englishVal.length > 0) {
          return 'filterSelected';
        }

        return '';
    }
    
	/**
	* Returns the appropriate CSS class for the filter icon
	* based upon whether the Filter is active or not
	*/
    public isFilterDutchActiveClass(): string{

        const dutchVal: any = this.dutchFilterForm.get('level')?.value;

        if (dutchVal.length > 0) {
          return 'filterSelected';
        }

        return '';
    }

  /**
  * Resets the filters to their initial state
  */
  public resetFilters(): void{

    this.activeFilter                 = '';
    this.sortColumn                   = 'candidateId';
    this.sortOrder                    = 'desc'
    this.showSortOptons               = false;
    this.selectedSortOrderForFilter   = '';
    this.countryFiltNL                = false;
    this.countryFiltBE                = false;
    this.countryFiltUK                = false;

    this.countryFilterForm = new FormGroup({
     
      NETHERLANDS:  new FormControl(''),
      BELGIUM:      new FormControl(''),
      UK:           new FormControl('')
   
    });

    this.permFilterForm = new FormGroup({
      include:  new FormControl('')
    });

    this.freelanceFilterForm = new FormGroup({
      include:  new FormControl('')
    });
    
    this.yearsExperienceFilterForm = new FormGroup({
        yearsExperienceGtEq: new FormControl(''),
        yearsExperienceLtEq: new FormControl('')
    });

    this.functionTypeFilterForm = new FormGroup({});

    this.functionTypes.forEach(funcType => {
      this.functionTypeFilterForm.addControl(funcType.id,new FormControl(''));
    });
    
    this.dutchFilterForm = new FormGroup({
        level: new FormControl(''),
      });
    
    this.frenchFilterForm = new FormGroup({
        level:  new FormControl(''),
      });
    
    this.englishFilterForm = new FormGroup({
        level:  new FormControl(''),
      });
    
    this.skillFilterForm = new FormGroup({
        skill:  new FormControl(''),
      });
    
    this.skillFilterSelections = new Array<string>();

    this.fetchCandidates(true);

  }

	/**
	* Flags a Candidate as being potentially unavailable
	*/
	public markCandidateAsUnavailable():void {
		this.candidateService.markCandidateAsUnavailable(this.selectedCandidate.candidateId).subscribe(data => {});
		this.selectedCandidate.flaggedAsUnavailable = true;
	}

}
