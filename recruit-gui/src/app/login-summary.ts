/**
* Users login summary 
*/
export class LoginSummary{
	
	public userId:string			= '';
	public activityLevel:string		= 'NONE';
	public loginsThisWeeek:number 	= 0;
	public loginsLast30Days:number 	= 0;
	public loginsLast60Days:number 	= 0;
	public loginsLast90Days:number 	= 0;
}

/**
* Stats relating to User login behavior
*/
export class UserLoginStats{
	public activityHigh:number		=0;
	public activityMedium:number	=0;
	public activityLow:number		=0;
	public activityNone:number		=0;
	public loginSummaries:Array<LoginSummary> 	= new Array<LoginSummary>();
}