import { Component, OnInit }							from '@angular/core';
import { UntypedFormGroup, UntypedFormControl }						from '@angular/forms';
import { CandidateServiceService }						from '../candidate-service.service';
import { CurriculumService }							from '../curriculum.service';
import { CandidateFunction }							from '../candidate-function';
import { NgbModal, NgbModalOptions}						from '@ng-bootstrap/ng-bootstrap'
import { ViewChild }									from '@angular/core';
import { Router}										from '@angular/router';
import { PendingCandidate }								from './pending-candidate';
import { environment }									from '../../environments/environment';
import { NewCandidateRequest, Rate, Language } 					from './new-candidate-request';
import { CandidateProfile } from '../candidate-profile';

@Component({
  selector: 'app-new-candidate',
  templateUrl: './new-candidate.component.html',
  styleUrls: ['./new-candidate.component.css']
})
export class NewCandidateComponent implements OnInit {

@ViewChild('feedbackBox', { static: false }) private content:any;
@ViewChild('pendingCandidateBox', { static: false }) private pendingCandidateBoxChild:any;

    public functionTypes:	 	Array<CandidateFunction> 	= new Array<CandidateFunction>();
	public pendingCandidates: 	Array<PendingCandidate> 	= new Array<PendingCandidate>();
	
	public currentPendingCandidate:PendingCandidate | null = null;
   
  	public selectOptionLangDutch:string = '';
  	public selectOptionLangEnglish:string = '';
  	public selectOptionLangFrench:string = '';

	public boop:Date = new Date();

	/**
  	* Constructor
  	*/
  	constructor(private curriculumService: CurriculumService , private candidateService: CandidateServiceService , private modalService: NgbModal, private router: Router) {
    
    	this.candidateService.loadFunctionTypes().forEach(funcType => {
      		this.functionTypes.push(funcType);
    	});
    	
    	this.languageOptions.push("NONE");
    	this.languageOptions.push("BASIC");
    	this.languageOptions.push("PROFICIENT");
    	
    	this.languages.push(new Language("DUTCH","UNKNOWN"));
    	this.languages.push(new Language("FRENCH","PROFICIENT"));
    	this.languages.push(new Language("ENGLISH","BASIC"));
		console.log("XX1");
		/**
		* Is Edit  
		*/
		if(this.hasLastPage() ) {
			var lastPage:string | null  =  sessionStorage.getItem("last-page");
		console.log("XX2");
			/**
			* Marketpalce Edit 
			*/
			if (lastPage === 'rec-mp') {
				console.log("XX3");
				var candidateId:string | null  =  sessionStorage.getItem("mp-edit-candidate");
				this.candidateService.getCandidateProfileById(""+candidateId).subscribe(candidate => {
					this.populateForEdit(candidate);
				});
			}
			
			if (lastPage === 'candidate-profile') {
				console.log("XX4");
				this.candidateService.getCandidateProfileById(""+sessionStorage.getItem("userId")).subscribe(candidate => {
					this.populateForEdit(candidate);
				});
			}
		} else {
			console.log("XX5");
			this.candidateService.fetchPendingCandidates().forEach(data => {
			
			let pendingCandidates: Array<PendingCandidate> = data;
			
			pendingCandidates.forEach(pc => {
                    this.pendingCandidates.push(pc);
            });
			
			if (this.isPendingCandidates() && this.isAuthenticatedAsAdmin()) {
				this.openPendingCandidatesBox();
			}
		});
		
		}

  	}
  	
  	/** 
	* Populates Profile fields for Edit 
	*/
	private populateForEdit(candidate:CandidateProfile):void {
		
		console.log("XXX " + JSON.stringify(candidate));
		this.offeredCandidateFormBean.get('candidateRoleTitle')?.setValue(candidate.roleSought);
		this.offeredCandidateFormBean.get('email')?.setValue(candidate.email);
		this.offeredCandidateFormBean.get('country')?.setValue(candidate.country);
		this.offeredCandidateFormBean.get('firstName')?.setValue(candidate.firstname);
		this.offeredCandidateFormBean.get('surname')?.setValue(candidate.surname);
      	this.offeredCandidateFormBean.get('function')?.setValue(candidate.function);
       	this.offeredCandidateFormBean.get('roleSought')?.setValue(candidate.roleSought);
       	this.offeredCandidateFormBean.get('city')?.setValue(candidate.city);
       	//this.offeredCandidateFormBean.get('location')?.setValue(candidate.);
		this.offeredCandidateFormBean.get('perm')?.setValue(candidate.perm);
		this.offeredCandidateFormBean.get('freelance')?.setValue(candidate.freelance);
		this.offeredCandidateFormBean.get('daysOnSite')?.setValue(candidate.daysOnSite);
		//this.offeredCandidateFormBean.get('availableFromDate')?.setValue();
		this.offeredCandidateFormBean.get('yearsExperience')?.setValue(candidate.yearsExperience);
		this.offeredCandidateFormBean.get('comments')?.setValue(candidate.comments);
		this.offeredCandidateFormBean.get('introduction')?.setValue(candidate.introduction);
		//skill:					new UntypedFormControl(),
		//language:				new UntypedFormControl(),
		this.offeredCandidateFormBean.get('permCurrency')?.setValue(candidate.ratePerm.currency);
		this.offeredCandidateFormBean.get('permTimeUnit')?.setValue(candidate.ratePerm.period);
		this.offeredCandidateFormBean.get('permFrom')?.setValue(candidate.ratePerm.valueMin);
		this.offeredCandidateFormBean.get('permTo')?.setValue(candidate.ratePerm.valueMax);
		this.offeredCandidateFormBean.get('contractCurrency')?.setValue(candidate.rateContract.currency);
		this.offeredCandidateFormBean.get('contractTimeUnit')?.setValue(candidate.rateContract.period);
		this.offeredCandidateFormBean.get('contractFrom')?.setValue(candidate.rateContract.valueMin);
		this.offeredCandidateFormBean.get('contractTo')?.setValue(candidate.rateContract.valueMax);

		this.coreSkills = candidate.skills;
		
		this.languages = candidate.languages;
		
		
	}

  	/**
  	*  Init 
  	*/
  	ngOnInit(): void {

  	}

	/**
	* Returns whether or not there are pending candidayes 
	* waiting to be processed 
	*/
	public isPendingCandidates():boolean{
		return this.pendingCandidates.length > 0;
	}
	
	public openPendingCandidatesBox():void {
    
      	let options: NgbModalOptions = {
     		centered: true
   		};

  		this.modalService.open(this.pendingCandidateBoxChild, options);
  	}
	

	/**
  	* Registers a new Candidate with the backend
  	*/
  	public addCandidate(): void {
		  
		//START  
		let candidate:NewCandidateRequest = new NewCandidateRequest();
		 
		 
		candidate.candidateId 						= this.candidateId;
		candidate.firstname							= this.offeredCandidateFormBean.get('firstName')!.value;
		candidate.surname							= this.offeredCandidateFormBean.get('surname')!.value;
		candidate.email								= this.offeredCandidateFormBean.get('email')!.value;
		candidate.roleSought						= this.offeredCandidateFormBean.get('roleSought')!.value;
		candidate.function							= this.offeredCandidateFormBean.get('function')!.value;
		candidate.country							= this.offeredCandidateFormBean.get('country')!.value;
		candidate.city								= this.offeredCandidateFormBean.get('city')!.value;
		candidate.perm								= this.offeredCandidateFormBean.get('perm')!.value;
		candidate.freelance							= this.offeredCandidateFormBean.get('freelance')!.value;
		candidate.yearsExperience 					= this.offeredCandidateFormBean.get('yearsExperience')!.value;
		candidate.languages 						= this.languages;
		candidate.skills 							= this.coreSkills;
		candidate.comments 							= this.offeredCandidateFormBean.get('comments')!.value;
		candidate.introduction 						= this.offeredCandidateFormBean.get('introduction')!.value;
		candidate.daysOnSite 						= this.offeredCandidateFormBean.get('daysOnSite')!.value;
		
		
		//
		let permCurrency:string 		= this.offeredCandidateFormBean.get('permCurrency')!.value;
		let permTimeUnit:string 		= this.offeredCandidateFormBean.get('permTimeUnit')!.value;
		let permFrom:string 			= this.offeredCandidateFormBean.get('permFrom')!.value;
		let permTo:string 				= this.offeredCandidateFormBean.get('permTo')!.value;
		
		if (permCurrency && permTimeUnit) {
			let permRate:Rate 		= new Rate();
			permRate.currency 		= permCurrency;
			permRate.period 		= permTimeUnit;
			permRate.valueMin 		= Number.parseFloat(permFrom);
			permRate.valueMax 		= Number.parseFloat(permTo);
			candidate.ratePerm 		= permRate;
		}
		
		let contractCurrency:string 		= this.offeredCandidateFormBean.get('contractCurrency')!.value;
		let contractTimeUnit:string 		= this.offeredCandidateFormBean.get('contractTimeUnit')!.value;
		let contractFrom:string 			= this.offeredCandidateFormBean.get('contractFrom')!.value;
		let contractTo:string 				= this.offeredCandidateFormBean.get('contractTo')!.value;
		
		if (contractCurrency && contractTimeUnit) {
			let contractRate:Rate 	= new Rate();
			contractRate.currency 	= contractCurrency;
			contractRate.period 	= contractTimeUnit;
			contractRate.valueMin 	= Number.parseFloat(contractFrom);
			contractRate.valueMax 	= Number.parseFloat(contractTo);
			candidate.rateContract 		= contractRate;
		}
		
		//
		candidate.availableFromDate 				= this.offeredCandidateFormBean.get('availableFromDate')!.value;
		  
		//END
    	this.candidateService.addCandidate(candidate, this.profileImageFile).subscribe(d=>{
    	  	this.open('feedbackBox', "Success",  true);
		
			if (this.currentPendingCandidate) {
				this.curriculumService.deletePendingCurriculum(this.currentPendingCandidate?.pendingCandidateId).subscribe(res => {
				
				});	
			}

			
    	});
  	};

  	public feedbackBoxClass:string            = '';
  	public feedbackBoxTitle                   = '';
  	public feedbackBoxText:string             = '';

	public open(content:any, msg:string, success:boolean):void {
    
    	if (success) {
    	  this.feedbackBoxTitle = 'Success';
    	  this.feedbackBoxText = 'Candidate Added';
    	  this.feedbackBoxClass = 'feedback-success';
    	} else {
    	  this.feedbackBoxTitle = 'Failure';
    	  this.feedbackBoxText = 'Unable to add Candidate';
    	  this.feedbackBoxClass = 'feedback-failure';
    	}

      	let options: NgbModalOptions = {
     		centered: true
   		};

  		this.modalService.open(this.content, options);
  	}

    /**
  *  Closes the confirm popup
  */
  public closeModal(): void {
    this.modalService.dismissAll();
    this.router.navigate(['suggestions']);
  }

  /**
  *  Closes the confirm popup
  */
  public closePendingCandidateModal(): void {
    this.modalService.dismissAll();
  }
  
  private curriculumFile!:File;
  private profileImageFile!:File;
  
  public uploadCurriculumFile(event:any):void{
  
  		if (event.target.files.length <= 0) {
  			return;
  		}
  	
  		this.curriculumFile = event.target.files[0];
  
  		this.curriculumService.uploadCurriculum(this.curriculumFile).subscribe(data=>{
			
			this.offeredCandidateFormBean.get('email')?.setValue(data.emailAddress);
			
			this.candidateId = data.id;
			this.coreSkills	= new Array<string>();
			
			data.skills.forEach((skill: string) => {
				this.coreSkills.push(skill);
			});
			
    	});
  		
  }
  
  public updateProfileImageFile(event:any):void{
  
  		if (event.target.files.length <= 0) {
  			return;
  		}
  	
  		this.profileImageFile = event.target.files[0];
  		
  }
   
  public isSelected(language:string):void{
      
      switch(language) {
          case 'dutch':{
           //   this.selectOptionLangDutch = this.formBean.get('dutch')!.value;
             return;
          } 
          case 'english':{
              return;
          } 
      }
      
  }
  

	/**
	* Sends request to delete Pending candidate
	*/
	public deletePendingCurriculum(pendingCandidate:PendingCandidate, closeModal:boolean):void{
		this.curriculumService.deletePendingCurriculum(pendingCandidate.pendingCandidateId).subscribe(res => {
			if	(closeModal == true) {
				this.closeModal();
			}
		});
	}
	
	/**
	* Loads a PendingCandidates details intothe New candidate screen
	*/
	public loadPendingCandidate(pendingCandidate:PendingCandidate):void{
		
		this.curriculumService.makePendingCurriculumActive(pendingCandidate.pendingCandidateId).subscribe(candidate=>{
      		
      		this.candidateId = candidate.id;
      		this.offeredCandidateFormBean.get('firstName')?.setValue(pendingCandidate.firstname);
      		this.offeredCandidateFormBean.get('surname')?.setValue(pendingCandidate.surname);
      		this.offeredCandidateFormBean.get('email')?.setValue(pendingCandidate.email);
      		this.offeredCandidateFormBean.get('freelance')?.setValue(pendingCandidate.freelance);
      		this.offeredCandidateFormBean.get('perm')?.setValue(pendingCandidate.perm);
      		this.coreSkills = candidate.skills;

      		if (pendingCandidate.perm) {
				this.offeredCandidateFormBean.get('perm')?.setValue("TRUE");
			} else {
				this.offeredCandidateFormBean.get('perm')?.setValue("FALSE");
			}

			if (pendingCandidate.freelance) {
				this.offeredCandidateFormBean.get('freelance')?.setValue("TRUE");
			} else {
				this.offeredCandidateFormBean.get('freelance')?.setValue("FALSE");
			}

			this.closePendingCandidateModal();
    	});
		
	}
	
	/**
	* Returns URL to download pening curriculum
	*/
	public getPendingCurriculumDownloadUrl(pendingCandidateId:string):string{
		return  environment.backendUrl + 'pending-curriculum/'+ pendingCandidateId;
	}

	/**
	* OfferedCandidate - Add details 
	*/
	coreSkills:Array<string>				= new Array<string>();
	spokenLanguages:Array<string>			= new Array<string>();
	public currentView:string 				= 'add';
	public candidateId:string = '';
	
	public offeredCandidateFormBean:UntypedFormGroup = new UntypedFormGroup({
     	candidateRoleTitle:		new UntypedFormControl(''),
		email:					new UntypedFormControl(),
		country:				new UntypedFormControl(),
		firstName:				new UntypedFormControl(),
		surname:				new UntypedFormControl(),
      	function:				new UntypedFormControl(),
       	roleSought:				new UntypedFormControl(),
       	city:					new UntypedFormControl(),
       	location:				new UntypedFormControl(),
		contractType:			new UntypedFormControl(),
		perm:					new UntypedFormControl('UNKNOWN'),
		freelance:				new UntypedFormControl('UNKNOWN'),
		daysOnSite:				new UntypedFormControl(),
		renumeration:			new UntypedFormControl(),
		availableFromDate:		new UntypedFormControl(new Date().toJSON().slice(0, 10)),
		yearsExperience:		new UntypedFormControl(),
		comments:				new UntypedFormControl(),
		introduction:			new UntypedFormControl(),
		skill:					new UntypedFormControl(),
		language:				new UntypedFormControl(),
		permCurrency:			new UntypedFormControl("EUR"),
		permTimeUnit:			new UntypedFormControl(),
		permFrom:				new UntypedFormControl(0.0),
		permTo:					new UntypedFormControl(0.0),
		contractCurrency:		new UntypedFormControl("EUR"),
		contractTimeUnit:		new UntypedFormControl(),
		contractFrom:			new UntypedFormControl(0.0),
		contractTo:				new UntypedFormControl(0.0),
	});

	public hasLastPage():boolean{
		
		var lastPage:string | null  =  sessionStorage.getItem("last-page");
		
		return lastPage != null;
	}
	
	/**
	* Returns the previous OfferedCandidate list (all/own candidate)
	*/		
	public showOfferedCandidates():void{
		
		var lastPage:string | null  =  sessionStorage.getItem("last-page");
		
		if (lastPage  && lastPage === 'rec-mp') {
			sessionStorage.setItem("mp-lastview", "showSupply");
			this.router.navigate(['recruiter-marketplace']);
		}
		
	}
	
	public publishOfferedCandidate():void{
		
	}
	
	public languageOptions:Array<string> = new Array<string>();
	public languages:Array<Language> = new Array<Language>();
	
	/**
	* Adds a Skill to the Candidates list of Skills
	*/
	public addOfferedCandidateSkill():void{
		
		let skillValue:string = this.offeredCandidateFormBean.get("skill")!.value; 
		
		if (this.coreSkills.indexOf(skillValue) < 0) {
			this.coreSkills.push(skillValue);
		}		
		
		this.offeredCandidateFormBean.get('skill')?.setValue('');
	
	}
	
	/**
	* Removes a Skill to the Candidates list of Skills
	*/
	public removeOfferedCandidateSkill(skill:string):void{
		this.coreSkills = this.coreSkills.filter(s => s != skill);
	}

	/**
	* Sets the Candidate level with a language
	*/
	public updateOfferedCandidateLanguage(language:Language, level:string):void{
		language.level = level;
	}

	/**
  	* Whether or not the user has authenticated as an Admin user 
  	*/
  	public isAuthenticatedAsAdmin():boolean {
    	return sessionStorage.getItem('isAdmin') === 'true';
  	}
  	
  	
  	public permRoleClass = "hide-row";
  	public permOnChange(newValue:any):void{
		if (newValue == 'TRUE') {
			this.permRoleClass = "show-row";
		} else {
			this.permRoleClass = "hide-row";
		}
	}
	
	public contractRoleClass = "hide-row";
  	public contractOnChange(newValue:any):void{
		if (newValue == 'TRUE') {
			this.contractRoleClass = "show-row";
		} else {
			this.contractRoleClass = "hide-row";
		}
	}
	
}
