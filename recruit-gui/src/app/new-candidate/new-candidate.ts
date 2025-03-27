import { Language}                            from './language';

/**
* Object to mapp a new candidates details to 
* the backend API structure
* @author: K Parkings 
*/
export class NewCandidate {

	/**
	* Unique identifier of the Candidate
	*/
	candidateId:		string 			= "";
	
	/**
	* first name of the Candidate
	*/
	firstname:			string			= "";

	/**
	* Surname of the Candidate
	*/
	surname:			string 			= "";
	
	/**
	* Candidates email address
	*/
	email: 				string 			= "";
	
	/**
	* Role sought by candidate
	*/
	roleSought: 		string 			= "";

	/**
	* Functions the candidate performs
	*/
	functions: 		Array<string> 		= new Array<string>();

	/**
	* Country where the candidate is located 
	*/
	country: 			string 				= "";
	
	/**
	* City where the Candidate is located
	*/
	city: 				string 				= "";
	
	/**
	* Whether or not the Candidate is lookig for perm positions
	*/
	perm:			string 					= "";
	
	/**
	* Whether or not the Candidate is looking for freelance positions
	*/
	freelance: 			string 				= "";
	
	/**
	* Years of experience the candidate has
	*/
	yearsExperience: 	string 				= "";
	
	/**
	* List of skills the Candidate has
	*/
	skills: 			Array<string> 	= new Array<string>();
	
	/**
	* Languages the candidate speaks
	*/
	languages: 			Array<Language> 	= new Array<Language>();
				
	

}