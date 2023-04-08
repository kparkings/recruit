/**
* Updated version of the Candidate
*/
export class UpdateCandidateProfileRequest{
	
	public firstname = '';
	public surname = '';
	public email = '';
	public roleSought = '';
	public function = '';
	public country = '';
	public city = '';
	public perm = '';
	public freelance = '';
	public yearsExperience = 0;
	public available = true;
	public languages:Array<LanguageOption> = new Array<LanguageOption>();
	
}

/**
* Language spoken by the Candidate
*/
export class LanguageOption{
		language:string 	= '';
		level:string 		= '';
	}