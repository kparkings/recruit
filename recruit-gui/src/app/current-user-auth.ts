export class CurrentUserAuth{

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
	
}