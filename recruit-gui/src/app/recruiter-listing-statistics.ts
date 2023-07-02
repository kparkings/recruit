/**
* Constainer for Statistics relating to Listing viewd of a Recruiter 
*/
export class RecruiterListingStatistics{
	
	public yearByWeekAllListing:Array<WeekStatBucket> = new Array<WeekStatBucket>();
	public listingStats:Array<ListingStat> = new Array<ListingStat>(); 
	
}

/**
* Container for a specific weeks statistics 
*/
export class WeekStatBucket{
	public weekNumber:number 	= 0;
	public count:number 		= 0;
}

/**
* Statistics for a specific Listing 
*/
export class ListingStat{
	public listingId:string 	= '';
	public title:string 		= '';
	public stats:Array<DayStatBucket> = new Array<DayStatBucket>();
}

/**
* Stats for Specific day 
*/
export class DayStatBucket{
	public bucket:Date = new Date();
	public count:number = 0;
}