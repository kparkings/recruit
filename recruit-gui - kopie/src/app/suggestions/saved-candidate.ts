import { Candidate }		 	                from './candidate';
/**
* Class represents a Candidate the User has added to their
* list of saved Candidates
*/
export class SavedCandidate{
	public userId:string			= '';
	public candidateId:number		= 0;
	public notes:string				= '';
	public candidate:Candidate		= new Candidate();
	public isRemoved:boolean		= false;
}