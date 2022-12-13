/**
* Class represents statistics relating to the number of views of 
* an entity in the Marketplace by a specific Recruiter
*/
export class MarketplaceRecruiterViewStat{
	
	constructor(
		public recruiterId:string,
		public viewsToday:number,
		public viewsThisWeek:number){
			
		}
		
		
}

export class MarketplaceRecruiterViewStatResponse{
	
	constructor(public stats:Array<MarketplaceRecruiterViewStat>){
			
	}
		
		
}