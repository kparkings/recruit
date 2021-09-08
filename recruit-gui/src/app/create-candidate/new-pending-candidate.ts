/**
* Class represents a Candidate uploaded by themselves that 
* needs to be further processed and accepted into the system
*/
export class NewPendingCandidate{
	
	/**
	* Unique Id of the Candidate. Must be the same as the Curriculum
	*/
	public pendingCandidateId:string = "";
	
	/**
	* Candidates firstname
	*/
	public firstname:string = "";
	
	/**
	* Candidates surname
	*/
	public surname:string = "";
	
	/**
	* Candidates email address
	*/
	public email:string = "";
	
	/**
	* Whether or not the Candidate is interested in Contract positions
	*/
	public freelance:boolean = false;
	
	/**
	* Whether or not the Candidate is interested in Perm positions
	*/
	public perm:boolean = false;
	
}