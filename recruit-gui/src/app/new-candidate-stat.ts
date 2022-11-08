export class NewCandidateStatItem{
	public functionType:string = '';
	public count:number = 0;
}

export class NewCandidateStat{
	public candidates:Array<NewCandidateStatItem> = new Array<NewCandidateStatItem>();
}