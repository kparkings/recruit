import { Component, Input } 								from '@angular/core';
import { InfoItemConfig } 							from 'src/app/candidate-info-box/info-item';
import { CandidateProfile } 						from 'src/app/candidate-profile';
import { CandidateServiceService}					from '../candidate-service.service';
import { InfoPaneUtil } 							from '../suggestions/info-pane-util';
import { SupportedCountry } 						from '../supported-candidate';
import { AppComponent} 								from '../app.component';
import { TranslateService} 							from "@ngx-translate/core";

@Component({
  selector: 'app-candidate-mini-overview',
  standalone:false,
  templateUrl: './candidate-mini-overview.component.html',
  styleUrls: ['./candidate-mini-overview.component.css','./candidate-mini-overview.component-mob.css']
})
export class CandidateMiniOverviewComponent {
	
	@Input() 	candidateId:				string 					= "";
	
	public supportedCountries:Array<SupportedCountry>			= new Array<SupportedCountry>();
	public candidate:CandidateProfile 									= new CandidateProfile();
	public infoItemConfig:InfoItemConfig 			= new InfoItemConfig();
	constructor(private readonly candidateService:CandidateServiceService, 
				private readonly appComponent:AppComponent, 
				private translate:TranslateService){
					
				}
	
	private initSupportedCountries():void{		
		this.supportedCountries = this.candidateService.getSupportedCountries();
		
	}
	
	ngOnInit(){
		console.log("boop " + this.candidateId);
		this.loadCandidate(this.candidateId);
	}
	
	public loadCandidate(candidateId:string):void{
		
		this.candidateService.getCandidateProfileById(candidateId).subscribe(profile => {
					this.candidate = profile;	
					let infoPaneUtil 		= new InfoPaneUtil(this.candidate, this.translate, this.supportedCountries); 
					this.infoItemConfig 	= infoPaneUtil.generateInfoPane();
					this.infoItemConfig.setShowContactButton(false);
					this.infoItemConfig.setShowEmailButton(false);
					this.appComponent.showCandidateProfileView = true;
				});
	}
	
	public init():void {
		console.log("Init so available in app.component");
	}
	
	public back():void{
		this.appComponent.hideCandidateProfile();
	}
	
}
