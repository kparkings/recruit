
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
	* Country where the candidate is located 
	*/
	country: 			string 			= '';
	
	/**
	* City where the Candidate is located
	*/
	city: 				string 			= '';
	
	/**
	* Role sought by Candidates
	*/
	roleSought: 		string 			= '';
	
	/**
	* Whether the Candidates is aviable for work
	*/
	available:			boolean			= true;

}