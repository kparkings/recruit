import { Component, OnInit }							from '@angular/core';
import { UntypedFormGroup, UntypedFormControl }			from '@angular/forms';
import { CandidateServiceService }						from '../candidate-service.service';
import { CurriculumService }							from '../curriculum.service';
import { CandidateFunction }							from '../candidate-function';
import { NgbModal, NgbModalOptions}						from '@ng-bootstrap/ng-bootstrap'
import { ViewChild }									from '@angular/core';
import { Router}										from '@angular/router';
import { PendingCandidate }								from './pending-candidate';
import { environment }									from '../../environments/environment';
import { NewCandidateRequest, Rate, Language } 			from './new-candidate-request';
import { CandidateProfile } 							from '../candidate-profile';
import { CandidateNavService } 							from '../candidate-nav.service';
import { LanguageOption } 								from './language-option';
import { AppComponent} 									from '../app.component';
import { TranslateService } 							from '@ngx-translate/core';
import { Country } from '../country';
import { SupportedLanguage } from '../supported-language';
import { SupportedCountry } from '../supported-candidate';

@Component({
  selector: 'app-new-candidate',
  templateUrl: './new-candidate.component.html',
  styleUrls: ['./new-candidate.component.css']
})
export class NewCandidateComponent implements OnInit {

@ViewChild('feedbackBox', 			{ static: false }) private feedbackBox:any;
@ViewChild('validationBox', 		{ static: false }) private validationBox:any;
@ViewChild('pendingCandidateBox', 	{ static: false }) private pendingCandidateBoxChild:any;

    public functionTypes:	 	Array<CandidateFunction> 	= new Array<CandidateFunction>();
	public pendingCandidates: 	Array<PendingCandidate> 	= new Array<PendingCandidate>();
	
	public currentPendingCandidate:PendingCandidate | null = null;
   
  	public selectOptionLangDutch:string = '';
  	public selectOptionLangEnglish:string = '';
  	public selectOptionLangFrench:string = '';

	public boop:Date = new Date();
	public isEdit:boolean = false;
    pendingCandidateBox: any;

	public isMobile:boolean = false;
	public mobileBtnClass:string = '';
	public mobilePage:string = '';
	
	public showBackBtn:boolean = false;
	public countries:Array<SupportedCountry> 			= new Array<SupportedCountry>();
	public supportedLanguages:Array<SupportedLanguage> 	= new Array<SupportedLanguage>();
	
	/**
  	* Constructor
  	*/
  	constructor(private curriculumService: 			CurriculumService, 
  				private candidateService: 			CandidateServiceService,
  				private modalService: 				NgbModal,
  				private router: 					Router,
  				private candidateNavService: 		CandidateNavService,
  				private appComponent:				AppComponent,
  				private translate:					TranslateService) {
    
    	this.countries 			= this.candidateService.getSupportedCountries();
    	this.supportedLanguages = this.candidateService.getLanguages();
    	
    	this.initializeOfferedCandidateForm();
    
    	this.candidateService.loadFunctionTypes().forEach(funcType => {
      		this.functionTypes.push(funcType);
    	});
    	
    	this.languageOptions.push(new LanguageOption("UNKNOWN", 	this.translate.instant('new-candidate-no')));
    	this.languageOptions.push(new LanguageOption("BASIC", 		this.translate.instant('new-candidate-basic')));
    	this.languageOptions.push(new LanguageOption("PROFICIENT", 	this.translate.instant('new-candidate-yes')));
    	
		if (this.candidateNavService.isRouteActive()) {
			this.candidateService.getCandidateProfileById(this.candidateNavService.getCandidateId()).subscribe(candidate => {
				this.populateForEdit(candidate);
				this.showBackBtn = true;
			});
		} else {
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
		this.candidateId = candidate.candidateId;
		this.offeredCandidateFormBean.get('candidateRoleTitle')?.setValue(candidate.roleSought);
		this.offeredCandidateFormBean.get('email')?.setValue(candidate.email);
		this.offeredCandidateFormBean.get('country')?.setValue(candidate.country);
		this.offeredCandidateFormBean.get('firstName')?.setValue(candidate.firstname);
		this.offeredCandidateFormBean.get('surname')?.setValue(candidate.surname);
      	this.offeredCandidateFormBean.get('function')?.setValue(candidate.function);
       	this.offeredCandidateFormBean.get('roleSought')?.setValue(candidate.roleSought);
       	this.offeredCandidateFormBean.get('city')?.setValue(candidate.city);
       	this.offeredCandidateFormBean.get('perm')?.setValue(candidate.perm);
		this.offeredCandidateFormBean.get('freelance')?.setValue(candidate.freelance);
		this.offeredCandidateFormBean.get('daysOnSite')?.setValue(candidate.daysOnSite);
		this.offeredCandidateFormBean.get('yearsExperience')?.setValue(candidate.yearsExperience);
		this.offeredCandidateFormBean.get('comments')?.setValue(candidate.comments);
		this.offeredCandidateFormBean.get('introduction')?.setValue(candidate.introduction);
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

		this.languages.forEach(lang => {
			this.offeredCandidateFormBean.get(lang.language)?.setValue(lang.level);
			(<HTMLInputElement>document.getElementById('langOpt'+lang.language)).value = lang.level;	
		});	
		
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
      	this.pendingCandidateBoxChild.nativeElement.showModal();
  	}
  	  	
  	public openValidationBox():void {
    	this.validationBox.nativeElement.showModal();
  	}
	

	private addCandidateVaidates(candidate:NewCandidateRequest):boolean{
		
		this.validationErrors = new Array<string>();
		
		if(candidate.candidateId == null || candidate.candidateId == ''){
			this.validationErrors.push("Curriculum");
		}
		
		if(candidate.firstname == null || candidate.firstname == ''){
			this.validationErrors.push("Firstname");
		}
		
		if(candidate.surname == null || candidate.surname == ''){
			this.validationErrors.push("Surname");
		}
		
		if(candidate.email == null || candidate.email == ''){
			this.validationErrors.push("Email");
		}					
		
		if(candidate.roleSought == null || candidate.roleSought == ''){
			this.validationErrors.push("Role Sought");
		}	
		
		if(candidate.country == null ||candidate.country == ''){
			this.validationErrors.push("Country");
		}
		
		if(candidate.city == null || candidate.city == ''){
			this.validationErrors.push("Location");
		}
		
		return this.validationErrors.length == 0;
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
		
		candidate.languages = new Array<Language>();
		this.supportedLanguages.forEach(lang => {
			candidate.languages.push(new Language(lang.languageCode, this.offeredCandidateFormBean.get(lang.languageCode)!.value));
		});
		
		if (this.isRecruiter()) {
			candidate.email = "useRecruiters";
		}
		
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
		
		candidate.availableFromDate 				= this.offeredCandidateFormBean.get('availableFromDate')!.value;
		
		if(!this.addCandidateVaidates(candidate)){
			this.openValidationBox();
			return;
		} 
		
		if (this.candidateNavService.isEditMode()) {
			this.candidateService.updateCandidate(candidate.candidateId, candidate, this.profileImageFile).subscribe(d=>{
    	  		this.open(true);
    	  		this.appComponent.refreschUnreadAlerts();			
    		});
		} else {
			this.candidateService.addCandidate(candidate, this.profileImageFile).subscribe(d=>{
				this.appComponent.refreschUnreadAlerts();
    	  		this.open(true);
					if (this.currentPendingCandidate) {
						this.curriculumService.deletePendingCurriculum(this.currentPendingCandidate?.pendingCandidateId).subscribe(res => {
					});	
				}
    		});	
		}
    	
  	};

  	public feedbackBoxClass:string            = '';
  	public feedbackBoxTitle                   = '';
  	public feedbackBoxText:string             = '';

	public open(success:boolean):void {
    
    	if (success) {
    	  this.feedbackBoxTitle = this.translate.instant('new-candidate-success-title');
    	  this.feedbackBoxText = this.translate.instant('new-candidate-success');
    	  this.feedbackBoxClass = 'feedback-success';
    	} else {
    	  this.feedbackBoxTitle = this.translate.instant('new-candidate-failure-title');
    	  this.feedbackBoxText = this.translate.instant('new-candidate-failure');
    	  this.feedbackBoxClass = 'feedback-failure';
    	}

      	let options: NgbModalOptions = {
     		centered: true
   		};

  		this.feedbackBox.nativeElement.showModal();
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
  public closeValidationModal(): void {
    this.modalService.dismissAll();
  }
  
  public validationErrors:Array<string> = new Array<string>();
  
  private curriculumFile!:File;
  private profileImageFile!:File;
  
  public uploadCurriculumFile(event:any):void{
	  
  		if (event.target.files.length <= 0) {
  			return;
  		}
  	
  		this.curriculumFile = event.target.files[0];
  
  		if (this.candidateNavService.isEditMode()) {
	
			this.curriculumService.updateCurriculum(this.candidateId, this.curriculumFile).subscribe(data=>{
			
			this.candidateId = data.id;
			this.coreSkills	= new Array<string>();
			
			data.skills.forEach((skill: string) => {
				this.coreSkills.push(skill);
			});
			
			this.feedbackBoxTitle = 'Success';
    	  	this.feedbackBoxText = 'CV Updated';
    	  	this.feedbackBoxClass = 'feedback-success';
			
			this.feedbackBox.nativeElement.showModal();
			
			this.appComponent.refreschUnreadAlerts();
			
    	});
		} else {
			this.curriculumService.uploadCurriculum(this.curriculumFile).subscribe(data=>{
			
			this.offeredCandidateFormBean.get('email')?.setValue(data.emailAddress);
			
			this.candidateId = data.id;
			this.coreSkills	= new Array<string>();
			
			data.skills.forEach((skill: string) => {
				this.coreSkills.push(skill);
			});
			
			this.appComponent.refreschUnreadAlerts();
			
    	});
  		}
  		
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
      		this.offeredCandidateFormBean.get('introduction')?.setValue(pendingCandidate.introduction);
      		
      		this.coreSkills = candidate.skills;

      		if (pendingCandidate.perm) {
				  
				this.offeredCandidateFormBean.get('perm')?.setValue("TRUE");
				
				this.offeredCandidateFormBean.get('permCurrency')?.setValue(pendingCandidate.ratePerm?.currency);
				this.offeredCandidateFormBean.get('permTimeUnit')?.setValue(pendingCandidate.ratePerm?.period);
				this.offeredCandidateFormBean.get('permFrom')?.setValue(pendingCandidate.ratePerm?.valueMin);
				this.offeredCandidateFormBean.get('permTo')?.setValue(pendingCandidate.ratePerm?.valueMax);
				
			} else {
				this.offeredCandidateFormBean.get('perm')?.setValue("FALSE");
			}

			if (pendingCandidate.freelance) {
				this.offeredCandidateFormBean.get('freelance')?.setValue("TRUE");
				this.offeredCandidateFormBean.get('contractCurrency')?.setValue(pendingCandidate.rateContract?.currency);
				this.offeredCandidateFormBean.get('contractTimeUnit')?.setValue(pendingCandidate.rateContract?.period);
				this.offeredCandidateFormBean.get('contractFrom')?.setValue(pendingCandidate.rateContract?.valueMin);
				this.offeredCandidateFormBean.get('contractTo')?.setValue(pendingCandidate.rateContract?.valueMax);
				
			} else {
				this.offeredCandidateFormBean.get('freelance')?.setValue("FALSE");
			}

			this.pendingCandidateBoxChild.nativeElement.close();
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
	
	
	public offeredCandidateFormBean:UntypedFormGroup = new UntypedFormGroup({});
	public initOfferedCandidateForm():void{
		
	}
	
	
	
	/**
	* Returns the previous OfferedCandidate list (all/own candidate)
	*/		
	public showOfferedCandidates():void{
		this.candidateNavService.doNextMove("back", this.candidateNavService.getCandidateId());
	}
	
	/**
	* Constructs offeredCandidateFormBean
	*/	
	public initializeOfferedCandidateForm():void{
		this.offeredCandidateFormBean = new UntypedFormGroup({
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
		
		this.supportedLanguages = this.supportedLanguages.sort((a,b)=> {
			return this.translate.instant(a.languageCode) < this.translate.instant(b.languageCode) ? -1 : 0;
		});
		
		this.supportedLanguages.forEach(lang => {
			this.offeredCandidateFormBean.addControl(lang.languageCode, new UntypedFormControl("UNKNOWN"));	
		});
		
	}
	
	public languageOptions:Array<LanguageOption> = new Array<LanguageOption>();
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
	
	/**
	* Whether or not the User is a Admin
	*/
	public isAdmin():boolean{
		return sessionStorage.getItem('isAdmin') === 'true';
	}
	
	/**
	* Whether or not User is a Recruiter
	*/
	public isRecruiter():boolean{
		return sessionStorage.getItem('isRecruiter') === 'true';
	}
	
	public setLanguageFormValue(langCode:string, langSelection:string):void{
		this.offeredCandidateFormBean.get(langCode)?.setValue(langSelection);
	}
	
}
