export class ListingStatistics{
	public viewsPerWeek:Array<ViewItem> 			= new Array<ViewItem>();
	public contactRequestsPerWeek:Array<ViewItem> 	= new Array<ViewItem>();
	public viewsThisWeek:number	 					= 0;
	public viewsToday:number 						= 0;
	public contactRequestsThisWeek:number 			= 0;
	public contactRequestsToday:number 				= 0;
}

export class ViewItem{
	public bucketName:string = '';
	public count:string = '';
}