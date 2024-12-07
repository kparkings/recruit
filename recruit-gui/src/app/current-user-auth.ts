import { Router } from "@angular/router";

export class CurrentUserAuth{

	/**
	* Whether or not the Use is a Candidate
	*/
	public getLoggedInUserId():string{
		return ""+sessionStorage.getItem("userId");
	}
		
	/**
	* Whether or not the User is a Admin
	*/
	public isAdmin():boolean{
		return sessionStorage.getItem('isAdmin') === 'true';
	}
		
	/**
	* Whether or not User is a Recruiter
	*/
	public isRecruiter():boolean{
		return sessionStorage.getItem('isRecruiter') === 'true';
	}
	
	/**
	* Whether or not the User is a Candidate
	*/
	public isCandidate():boolean{
		return sessionStorage.getItem('isCandidate') === 'true';
	}
	
	/**
	* Whether user has a paid subscription 
	*/
	public hasPaidSubscription():boolean{
		return sessionStorage.getItem('hasPaidSubscription') === 'true';
	}
	
	/**
	* Logs user out of the system by removig their 
	* data from sessionStorage 
	*/
	public doLogout(router:Router):void{
		sessionStorage.removeItem('isAdmin');
		sessionStorage.removeItem('isRecruter');
		sessionStorage.removeItem('isCandidate');
		sessionStorage.removeItem('loggedIn');
		router.navigate(['login-user']);
	}
		
}