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
				
	getDutch():string{

		const language:Language = this.languages.filter(l => l.language === 'DUTCH')[0];
		
		if (language) {
			return language.level;
		}

		return '-';
		
	}

	getEnglish():string{
		
		const language:Language = this.languages.filter(l => l.language === 'ENGLISH')[0];
		
		if (language) {
			return language.level;
		}

		return '-';
	}

	getFrench():string{
				const language:Language = this.languages.filter(l => l.language === 'FRENCH')[0];
		
		if (language) {
			return language.level;
		}

		return '-';
	}

	getSkills():string{
		
		let skillString = this.skills.join(', ');
		
		if (skillString.indexOf(', ') != -1) {
			skillString = skillString.replace(', ', '');
		}
		
		return skillString;
	}
}