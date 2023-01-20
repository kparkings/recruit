import { Language}                            from './language';

/**
* Represnets a candidate
* @author: K Parkings 
*/
export class Candidate {

	/**
	* Unique identifier of the Candidate
	*/
	candidateId:		string 			= '';
	
	/**
	* first name of the Candidate
	*/
	firstname:			string			= '';

	/**
	* Surname of the Candidate
	*/
	surname:			string 			= '';
	
	/**
	* Candidates email address
	*/
	email: 				string 			= '';

	/**
	* Role sought by Candidates
	*/
	roleSought: 		string 			= '';
	
	/**
	* Function the candidate performs
	*/
	function: 			string 			= '';

	/**
	* Country where the candidate is located 
	*/
	country: 			string 			= '';
	
	/**
	* City where the Candidate is located
	*/
	city: 				string 			= '';
	
	/**
	* Whether or not the Candidate is lookig for perm positions
	*/
	perm:			string 			= '';
	
	/**
	* Whether or not the Candidate is looking for freelance positions
	*/
	freelance: 			string 		= '';
	
	/**
	* Years of experience the candidate has
	*/
	yearsExperience: 	string 		= '';
	
	/**
	* List of skills the Candidate has
	*/
	skills: 			Array<string> 	= new Array<string>();
	
	/**
	* Someone in the network has marked the Candiate as being unavailable 
	*/
	flaggedAsUnavailable: boolean = false;
	
	/**
	* Languages the candidate speaks
	*/
	languages: 			Array<Language> 	= new Array<Language>();
	
	/**
	* How accurate the Candidates languages are in relation to the Listings language requirements
	*/
	accuracyLanguages:string 				= 'poor';
	
	/**
	* How accurate the Candidates Skills are in relation to the Listings Skills requirements
	*/
	accuracySkills:string 					= 'poor';
	
}