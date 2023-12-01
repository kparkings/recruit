import { Injectable } 							from '@angular/core';
import { NgbModal, NgbModalOptions }			from '@ng-bootstrap/ng-bootstrap';
import { Observable, BehaviorSubject }			from 'rxjs';             

/**
* Services for popups
*/
@Injectable({
  providedIn: 'root'
})
export class PopupsService {

	public validationExceptions = new BehaviorSubject(new Array<string>());
	
	/**
	* Constructor
	*/
  	constructor(private modalService:NgbModal) { }

	/**
	* Closes the popups
	*/
	public closeModal(): void {
		this.modalService.dismissAll();
		this.clearValidationErrors();
	}
	
	/**
	* Opens the popups
	*/
	public openModal(modalBox:any):void{
		//let options: NgbModalOptions = {
	    //	 centered: true
	  	//};

		//this.modalService.open(modalBox, options);
		modalBox.nativeElement.showModal();
	}
	
	public fetchValidationErrors():Observable<Array<string>>{
		return this.validationExceptions;
	}
	
	public clearValidationErrors(){
		return this.validationExceptions.next(new Array<string>());
	}
	
	public setValidationErrors(exceptions:Array<string>){
		console.log("AND SET IT IN THE SERVICE ");
		return this.validationExceptions.next(exceptions);
	}
	
}
