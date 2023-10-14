import { Injectable } 					from '@angular/core';
import { Router}						from '@angular/router';

/**
* Handles routing logic for Canidate profile interaction 
*/
@Injectable({
  providedIn: 'root'
})
export class CandidateNavService {
	
	/**
	* Construction 
	*/
  	constructor(private router: Router) { }
  	
  	/**
	* Reset 
	*/
	public reset():void{
		sessionStorage.removeItem("candidate-mode");
		sessionStorage.removeItem("candidate-nav-route");
		sessionStorage.removeItem("candidate-nav-route-candidate-id");
	}

	/**
	* Initiates the route when user is Candidate 
	*/
	public startCandidateProfileRouteForCandidate():void{
		this.reset();
		sessionStorage.setItem("candidate-nav-route", "candidate");
	}

	/**
	* Initiates the route when user is Admin 
	*/	
	public startCandidateProfileRouteForAdmin():void{
		this.reset();
		sessionStorage.setItem("candidate-nav-route", "admin");
	}
	
		/**
	* Initiates the route when user is Rectuiter 
	*/
	public startCandidateProfileRouteForRecruiter():void{
		this.reset();
		sessionStorage.setItem("candidate-nav-route", "recruiter");
	}
	
	public isRouteActive():boolean{
		return sessionStorage.getItem("candidate-nav-route") != null;
	}
	
	/**
	* Returns the candateId of the Candidate being interacted with 
	*/
	public getCandidateId():string{
		return ""+sessionStorage.getItem("candidate-nav-route-candidate-id");
	}
	
	/**
	* Returns the candateId of the Candidate being interacted with 
	*/
	public isEditMode():boolean{
		return ""+sessionStorage.getItem("candidate-mode") == 'edit';
	}
	
	public doNextMove(move:string, candidateId:string|null):void{
		
		if (!sessionStorage.getItem("candidate-nav-route")){
			return;
		}
		
		const route:string = ""+sessionStorage.getItem("candidate-nav-route");
		
		sessionStorage.setItem("candidate-nav-route-candidate-id",""+candidateId);
		
		if (route == 'candidate') {
			this.doNextCandidateMove(move);
		}
		
		if (route == 'admin') {
			this.doNextAdminMove(move);
		}
		
		if (route == 'recruiter') {
			this.doNextRecruiterMove(move);
		}
		
	}
	
	private doNextCandidateMove(move:string):void{
		
		if (move == 'edit'){
			sessionStorage.setItem("candidate-mode", "edit");
			this.router.navigate(['new-candidate']);
		}
		
		if (move == 'back'){
			sessionStorage.removeItem("candidate-mode");
			this.router.navigate(['suggestions']);
		}
		
	}
	
	private doNextAdminMove(move:string):void{
		
		if (move == 'edit'){
			sessionStorage.setItem("candidate-mode", "edit");
			this.router.navigate(['new-candidate']);
		}
		
		if (move == 'back'){
			sessionStorage.removeItem("candidate-mode");
			this.router.navigate(['suggestions']);
		}
		
	}
	
	private doNextRecruiterMove(move:string):void{
		
		if (move == 'view'){
			sessionStorage.removeItem("candidate-mode");
			this.router.navigate(['suggestions']);
		}
		
		if (move == 'edit'){
			sessionStorage.setItem("candidate-mode", "edit");
			sessionStorage.setItem("candidate-nav-route-recruiter-edit",""+this.getCandidateId());
			this.router.navigate(['new-candidate']);
		}
		
		if (move == 'back'){
			sessionStorage.removeItem("candidate-mode");
			if(sessionStorage.getItem("candidate-nav-route-recruiter-edit")){
				sessionStorage.removeItem("candidate-nav-route-recruiter-edit");
				this.router.navigate(['suggestions']);	
			} else {
				this.router.navigate(['recruiter-marketplace']);	
			}
			
		}
		
	}

}
