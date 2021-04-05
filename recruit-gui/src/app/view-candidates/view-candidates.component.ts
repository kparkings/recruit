import { Component, OnInit }                              from '@angular/core';
import { CandidateServiceService }                        from '../candidate-service.service';
import { Candidate }                                      from './candidate';
import { CandidateFunction }                            from '../candidate-function';
import {NgbModal, ModalDismissReasons} from '@ng-bootstrap/ng-bootstrap';

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
  public activeFilter:string = '';
  private sortColumn:string = 'candidateId';
  private sortOrder:string = 'desc'
  public showSortOptons:boolean = false;

  /**
  * Constructor
  */
  constructor(private candidateService:CandidateServiceService, private modalService: NgbModal) {

    this.candidateService.loadFunctionTypes().forEach(funcType => {
      this.functionTypes.push(funcType);
    });

   }

  /**
  * Initializes component
  */
  ngOnInit(): void {
    this.fetchCandidates();    
  }

  /**
  * Retrieves candidates from the backend
  */
  private fetchCandidates():void{
    
    this.candidates = new Array<Candidate>();

    this.candidateService.getCandidates(this.getCandidateFilterParamString()).subscribe( data => {
    
      data.forEach((c:Candidate) => {
        
        const candidate:Candidate = new Candidate();

      candidate.candidateId       = c.candidateId;
      candidate.city              = c.city;
      candidate.country           = this.getCountryCode(c.country);
      candidate.freelance         = c.freelance;
      candidate.function          = c.function;
      candidate.perm              = c.perm;
      candidate.yearsExperience   = c.yearsExperience;
      candidate.languages = c.languages;
      candidate.skills = c.skills;

      this.candidates.push(candidate);

      });
        
    })

  }

  /**
  * Builder a query parameter string with the selected filter options
  */
  private getCandidateFilterParamString():string{
    let filterParams:string = 'orderAttribute=' + this.sortColumn + "&order=" + this.sortOrder;
    return filterParams;
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
  public open(content:any, column:string) {
    this.activeFilter = column;
    this.showOrderFilter();
    this.modalService.open(content, { centered: true });
  }

  /**
  * Changes the order of the candidates to be based upon 
  * the selected colum and orders by the selected direction
  * @param order - asc | desc 
  */
  public updateSortOrder(order:any){

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

    this.fetchCandidates();

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
      case 'function':
      case 'yearsExperience':{
        this.showSortOptons = true;
        console.log("T");
        return;
      }

    }

    this.showSortOptons = false;

  }

}
