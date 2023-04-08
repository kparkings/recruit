import { PhotoAPIOutbound, CandidateProfile, Language, Rate } 	from './candidate-profile';

/**
* Updated version of the Candidate
*/
export class UpdateCandidateProfileRequest{
	
	public firstname 					= '';
	public surname 						= '';
	public email 						= '';
	public roleSought 					= '';
	public function 					= '';
	public country 						= '';
	public city 						= '';
	public perm 						= '';
	public freelance 					= '';
	public yearsExperience 				= 0;
	public available 					= true;
	public languages:Array<Language> 	= new Array<Language>();
	public introduction:string 			= '';
	public photo:PhotoAPIOutbound 		= new PhotoAPIOutbound();
	public rate:Rate 					= new Rate();
	
}
