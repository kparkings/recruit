import { Component, OnInit }                              from '@angular/core';
import { CandidateServiceService }                        from '../candidate-service.service';
import { Candidate }                                      from './candidate';
import { CandidateFunction }                              from '../candidate-function';
import {NgbModal, ModalDismissReasons}                    from '@ng-bootstrap/ng-bootstrap';
import { ReactiveFormsModule, FormGroup, FormControl }    from '@angular/forms';

@Component({
  selector: 'app-view-candidates',
  templateUrl: './view-candidates.component.html',
  styleUrls: ['./view-candidates.component.css']
})
export class ViewCandidatesComponent implements OnInit {

  public functionTypes: Array<CandidateFunction>  = new Array<CandidateFunction>();
  public candidates:    Array<Candidate>          = new Array<Candidate>();

  /**
  * Filters
  */
  public  activeFilter: string                = '';
  private sortColumn:  string                 = 'candidateId';
  private sortOrder: string                   = 'desc'
  public  showSortOptons: boolean             = false;
  public  selectedSortOrderForFilter: string  = '';
  public  countryFiltNL: boolean              = false;
  public  countryFiltBE: boolean              = false;
  public  countryFiltUK: boolean              = false;
  private pageSize:number                     = 8;
  public  totalPages:number                   = 0;
  public  currentPage:number                  = 0;

  public functionOptionsPage1: Array<CandidateFunction> = new Array<CandidateFunction>();
  public functionOptionsPage2: Array<CandidateFunction> = new Array<CandidateFunction>();

  public nextPage(): void{

    if ((this.currentPage + 1) < this.totalPages) {
      this.currentPage = this.currentPage + 1;
      this.fetchCandidates(false);
    }
    
  }

  public previousPage(): void{
    if ((this.currentPage) > 0) {
      this.currentPage = this.currentPage - 1;
      this.fetchCandidates(false);
    }
  }


  public showNavPrev(): boolean{
    
    if (this.totalPages === -1) {
      return false;
    }

    return this.currentPage > 0;

  }

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

  public freelanceFilterForm: FormGroup = new FormGroup({
    include:  new FormControl('false'),
  });

  public permFilterForm: FormGroup = new FormGroup({
    include:  new FormControl('false'),
  });

  /**
  * Constructor
  */
  constructor(public candidateService:CandidateServiceService, private modalService: NgbModal) {

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

      candidate.candidateId       = c.candidateId;
      candidate.city              = c.city;
      candidate.country           = this.getCountryCode(c.country);
      candidate.freelance         = this.getFreelanceOption(c.freelance);
      candidate.roleSought        = c.roleSought;
      candidate.function          = c.function;
      candidate.perm              = this.getPermOption(c.perm);
      candidate.yearsExperience   = c.yearsExperience;
      candidate.languages = c.languages;
      candidate.skills = c.skills;

      this.candidates.push(candidate);

      });
        
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
                                                         + this.getFreelanceFilterParamString();
    return filterParams;
  }


  /**
  * Creates a query param string with the filter options to apply to the perm
  */
  private getPermFilterParamString(): string{
  
      if (this.permFilterForm.get('include')?.value !== 'true') {
        return '&perm=true';
      }

      return '&perm=false';
  }

    /**
  * Creates a query param string with the filter options to apply to the perm
  */
  private getFreelanceFilterParamString(): string{
  
      if (this.freelanceFilterForm.get('include')?.value !== 'true') {
        return '&freelance=true';
      }

      return '&freelance=false';
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
  * Returns the url to perform the download of the filterable candidate list
  */
  public getCandidateDownloadUrl(){
    return 'http://127.0.0.1:8080/candidate/download?' + this.getCandidateFilterParamString();
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

  public isFilterCandidateIdActiveClass(): string{
    return this.sortColumn === 'candidateId' ? 'filterSelected' : '';
  }

  public isFilterCountryActiveClass(): string{
    
    if (this.sortColumn === 'country') {
      return 'filterSelected';
    }

    if (this.getCountryFilterParamString() !== '') {
      return 'filterSelected';
    } 

    return '';

  }

  public isFilterCityActiveClass(): string{
    return this.sortColumn === 'city' ? 'filterSelected' : '';
  }

  public isFilterFunctionActiveClass(): string{
    
    if(this.sortColumn === 'roleSought') {
      return 'filterSelected';
    }

    if (this.getFunctionTypeFilterParamString() !== ''){
      return 'filterSelected';
    }

    return '';

  }

  public isFilterYearsExperienceActiveClass(): string{
    return this.sortColumn === 'yearsExperience' ? 'filterSelected' : '';
  }

  public isFilterPermActiveClass(): string{

    const permVal: any = this.permFilterForm.get('include')?.value;

    if (permVal !== 'false') {
      return 'filterSelected';
    }
    return '';
  }

    public isFilterFreelanceActiveClass(): string{

      const freelanceVal: any = this.freelanceFilterForm.get('include')?.value;

      if (freelanceVal !== 'false') {
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
      include:  new FormControl('false')
    });

    this.freelanceFilterForm = new FormGroup({
      include:  new FormControl('false')
    });

    this.functionTypeFilterForm = new FormGroup({});

    this.functionTypes.forEach(funcType => {
      this.functionTypeFilterForm.addControl(funcType.id,new FormControl(''));
    });

    this.fetchCandidates(true);

  }

}
