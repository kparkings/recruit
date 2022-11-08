export class NewCandidate{
	public summary:Array<NewCandidateSummaryItem> = new Array<NewCandidateSummaryItem>();
}

export class NewCandidateSummaryItem{
	public country:string 			= '';
	public yearsExperience:number 	= 0;
	public functionDesc:string 		= '';
}