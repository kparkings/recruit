import { Injectable } 							from '@angular/core';
import { NgbModal }								from '@ng-bootstrap/ng-bootstrap';
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
  	constructor(private readonly modalService:NgbModal) { }

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
			modalBox.nativeElement.showModal();
	}
	
	public fetchValidationErrors():Observable<Array<string>>{
		return this.validationExceptions;
	}
	
	public clearValidationErrors(){
		return this.validationExceptions.next(new Array<string>());
	}
	
	public setValidationErrors(exceptions:Array<string>){
		return this.validationExceptions.next(exceptions);
	}
	
}