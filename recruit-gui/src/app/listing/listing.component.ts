import { Component, OnInit } from '@angular/core';
import { ListingService }								from '../listing.service';
import { Listing}										from './listing';

@Component({
  selector: 'app-listing',
  templateUrl: './listing.component.html',
  styleUrls: ['./listing.component.css']
})
export class ListingComponent implements OnInit {

  	constructor(private listingService:ListingService) { }

  	ngOnInit(): void {
		this.fetchListings();	
  	}

	public activeView:string					= 'list';
	
	private	pageSize:number						= 8;
  	public	totalPages:number					= 0;
  	public	currentPage:number					= 0;

	public listings:Array<Listing>				= new Array<Listing>();
	public selectedListing:Listing				= new Listing();

	/**
	* Switches to Add Listing view
	*/
	public showList():void{
		this.activeView 			= 'list';
		this.selectedListing		= new Listing();
	}
	
	/**
	* Switches to Show Listing view
	*/
	public showListingDetails(selectedListing?:Listing):void{
		
		this.activeView 			= 'show';
		
		if (selectedListing) {
			this.selectedListing	= selectedListing;	
		}
		
	}
	
	/**
	* Retrieves listings belonging to the Recruiter
	*/
	public fetchListings():void{
	
		this.listingService
			.fetchAllListings('created',"desc", this.currentPage, this.pageSize)
				.subscribe(data => {
					this.totalPages = data.totalPages;
					this.listings 	= data.content;
				}, 
				err => {
					console.log("Error retrieving listings for all recruiters" + JSON.stringify(err));			
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

}
