/**
* Represnets a candidate
* @author: K Parkings 
*/
export class RecruiterUpdateRequest {

	/**
	* Recruiters UserId (Unique Id for Recruiter)
	*/
	userId:string		= "";
	
	/**
	* Recruiters Firstname
	*/
	firstName:string 	= "";
	
	/**
	* Recruiters Surname
	*/
	surname:string 		= "";
	
	/**
	* Recruiters current Email address
	*/
	email:string 		= "";
	
	/**
	* Recruiters current Company
	*/
	companyName:string 	= "";
	
	/**
	* Recruiters current Company Country
	*/
	companyCountry:string 	= "";
	
	/**
	* Recruiters current Company Address
	*/
	companyAddress:string 	= "";
	
	/**
	* Recruiters current Company VAT Number
	*/
	companyVatNumber:string 	= "";
	
	/**
	* Recruiters current Company Registation Number
	*/
	companyRegistrationNumber:string 	= "";
	
	/**
	* Primary language spoken by the Recruiter
	*/
	language:string 	= "";
	
}