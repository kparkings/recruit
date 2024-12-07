import { InfoItemBlock, InfoItemConfig, InfoItemRowKeyValue, InfoItemRowKeyValueFlag, InfoItemRowKeyValueMaterialIcon, InfoItemRowSingleValue } from '../candidate-info-box/info-item';
import { TranslateService } 														from '@ngx-translate/core';
import { CurrentUserAuth }															from '../current-user-auth';
import { CandidateProfile } 														from '../candidate-profile';
import { SupportedCountry } 														from '../supported-candidate';

export class InfoPaneUtil{

	private currentUserAuth:CurrentUserAuth = new CurrentUserAuth();
	
	constructor(public candidateProfile:CandidateProfile, public translate:TranslateService, public supportedCountries:Array<SupportedCountry>){}
	
	public generateInfoPane():InfoItemConfig{
	
		let infoItemConfig 	= new InfoItemConfig();
		
		console.log(JSON.stringify(this.candidateProfile));
			
		infoItemConfig.setProfilePhoto(this.candidateProfile?.photo?.imageBytes);
		
			if (!this.currentUserAuth.isCandidate()){
				infoItemConfig.setShowContactButton(true);
			}
		
			//Location
			let recruiterBlock:InfoItemBlock = new InfoItemBlock();
			recruiterBlock.setTitle(this.translate.instant('info-item-title-location'));
			recruiterBlock.addRow(new InfoItemRowKeyValueFlag(this.translate.instant('info-item-title-country'),this.getFlagClassFromCountry(this.candidateProfile.country)));
			recruiterBlock.addRow(new InfoItemRowKeyValue(this.translate.instant('info-item-title-city'),this.candidateProfile.city));
			infoItemConfig.addItem(recruiterBlock);
			
			//Languages Block
			let languageBlock:InfoItemBlock = new InfoItemBlock();
			languageBlock.setTitle(this.translate.instant('info-item-title-languages'));
			this.candidateProfile.languages.forEach(lang => {
					languageBlock.addRow(new InfoItemRowKeyValueMaterialIcon(this.getLanguage(lang.language),this.getMaterialIconClassFromLangLevel(lang.level)));
			});
			
			languageBlock.sort();
			
			infoItemConfig.addItem(languageBlock);
			
			//Contract Type Block
			if (this.candidateProfile.freelance == 'TRUE' || this.candidateProfile.perm == 'TRUE') {
				let contractTypeBlock:InfoItemBlock = new InfoItemBlock();
				contractTypeBlock.setTitle(this.translate.instant('info-item-title-contract-type'));
				if (this.candidateProfile.freelance == 'TRUE'){		
					contractTypeBlock.addRow(new InfoItemRowKeyValueMaterialIcon(this.translate.instant('info-item-title-contract'),"available-check-icon"));
				}
				if (this.candidateProfile.perm == 'TRUE'){		
					contractTypeBlock.addRow(new InfoItemRowKeyValueMaterialIcon(this.translate.instant('info-item-title-permanent'),"available-check-icon"));
				}
				infoItemConfig.addItem(contractTypeBlock);
			}
			
			//Contract Rate 
			if(this.hasContractRate()) {
				let contractRateBlock:InfoItemBlock = new InfoItemBlock();
				contractRateBlock.setTitle(this.translate.instant('info-item-contract-rate'));
				contractRateBlock.addRow(new InfoItemRowSingleValue(this.getContractRate()));
				infoItemConfig.addItem(contractRateBlock);
			}
		
			//Perm Rate 
			if(this.hasPermRate()) {
				let permRateBlock:InfoItemBlock = new InfoItemBlock();
				permRateBlock.setTitle(this.translate.instant('info-item-title-perm-rate'));
				permRateBlock.addRow(new InfoItemRowSingleValue(this.getPermRate()));
				infoItemConfig.addItem(permRateBlock);
			}
		
			//Years Experience 
			let yearsExperienceBlock:InfoItemBlock = new InfoItemBlock();
			yearsExperienceBlock.setTitle(this.translate.instant('info-item-title-years-experience'));
			yearsExperienceBlock.addRow(new InfoItemRowSingleValue(""+this.candidateProfile.yearsExperience));
			infoItemConfig.addItem(yearsExperienceBlock);
			
			//Secuirty Level
			let securityClearanceBlock:InfoItemBlock = new InfoItemBlock();
			securityClearanceBlock.setTitle(this.translate.instant('info-item-title-security-clearance'));
			securityClearanceBlock.addRow(new InfoItemRowKeyValue(this.translate.instant('info-item-security-clearance'),this.candidateProfile.securityClearance));
			infoItemConfig.addItem(securityClearanceBlock);
			
			
			//Requires Sponsorship
			let requiresSponsorhipBlock:InfoItemBlock = new InfoItemBlock();
			requiresSponsorhipBlock.setTitle(this.translate.instant('info-item-title-requires-sponsorship'));
			requiresSponsorhipBlock.addRow(new InfoItemRowKeyValue(this.translate.instant('info-item-requires-sponsorship'),this.candidateProfile.requiresSponsorship ? this.translate.instant('yes') : this.translate.instant('no')));
			infoItemConfig.addItem(requiresSponsorhipBlock);
			
			
			//Availability
			let availabilityBlock:InfoItemBlock = new InfoItemBlock();
			availabilityBlock.setTitle(this.translate.instant('info-item-title-availability'));
			if (this.candidateProfile.daysOnSite) {
				availabilityBlock.addRow(new InfoItemRowKeyValue(this.translate.instant('info-item-title-max-days-on-site'), this.formatHumanReadableDaysOnsite(this.candidateProfile.daysOnSite)));
			}
			if (this.candidateProfile.availableFromDate) {
				availabilityBlock.addRow(new InfoItemRowKeyValue(this.translate.instant('info-item-title-available-from'),""+this.candidateProfile.availableFromDate));
			}
			infoItemConfig.addItem(availabilityBlock);
	
			console.log(JSON.stringify(infoItemConfig));
			
		return infoItemConfig;
	
	}
	
	/**
	* Returns human readable version of days onsite
	*/
	public formatHumanReadableDaysOnsite(value:string):string{
		switch(value){
			case 'ZERO': 	return this.translate.instant('info-item-days-onsite-fully-remote');
			case 'ONE': 	return "1";
			case 'TWO': 	return "2";
			case 'THREE': 	return "3";
			case 'FOUR': 	return "4";
			case 'FIVE':	return "5";
			default: 		return "";
		}
	}
	
	/**
	* Contract Rate info 
	*/
	public getPermRate():string{
		
		if (this.candidateProfile.ratePerm.valueMin != 0 && this.candidateProfile.ratePerm.valueMax != 0){
			return this.translate.instant('info-item-title-rate') + this.candidateProfile.ratePerm.currency + " "
			+ this.candidateProfile.ratePerm.valueMin 
			+ this.translate.instant('info-item-title-to')  + this.candidateProfile.ratePerm.valueMax 
			+ this.translate.instant('info-item-title-per') + this.candidateProfile.ratePerm.period.toLowerCase() ;
		}
		
		if (this.candidateProfile.ratePerm.valueMin == 0 && this.candidateProfile.ratePerm.valueMax != 0){
			return this.translate.instant('info-item-title-rate') + this.candidateProfile.ratePerm.currency 
			+ " " + this.candidateProfile.ratePerm.valueMax 
			+ this.translate.instant('info-item-title-location-per') + this.candidateProfile.ratePerm.period.toLowerCase() ;
		}
		
		if (this.candidateProfile.ratePerm.valueMin != 0 && this.candidateProfile.ratePerm.valueMax == 0){
			return this.translate.instant('info-item-title-rate') + this.candidateProfile.ratePerm.currency 
			+ " " + this.candidateProfile.ratePerm.valueMin 
			+ this.translate.instant('info-item-title-per') + this.candidateProfile.ratePerm.period.toLowerCase() ;
		}
		
		return "";
		
	}
	
	/**
	* Returns the flag css class for the Flag matching
	* the Country 
	*/
	public getFlagClassFromCountry(country:string):string{
		
		
		console.log("-------> " + country + " : " + this.supportedCountries.length);
		
		let sc:SupportedCountry = this.supportedCountries.filter(c => c.name == country)[0];
		
		return sc ?  "flag-icon-"+sc.iso2Code : '';
		
	}
	
	/**
	* Whether Contract Rate info is available 
	*/
	public hasContractRate():boolean{
		return this.candidateProfile.rateContract && (this.candidateProfile.rateContract.valueMin != 0 || this.candidateProfile.rateContract.valueMax != 0);
	}
		
	/**
	* Contract Rate info 
	*/
	public getContractRate():string{
			
		if (this.candidateProfile.rateContract.valueMin != 0 && this.candidateProfile.rateContract.valueMax != 0){
			return this.translate.instant('info-item-title-rate') + this.candidateProfile.rateContract.currency + " "
			+ this.candidateProfile.rateContract.valueMin 
			+ this.translate.instant('info-item-title-to')  + this.candidateProfile.rateContract.valueMax 
			+ this.translate.instant('info-item-title-per') + this.candidateProfile.rateContract.period.toLowerCase() ;
		}
		
		if (this.candidateProfile.rateContract.valueMin == 0 && this.candidateProfile.rateContract.valueMax != 0){
			return this.translate.instant('info-item-title-rate') + this.candidateProfile.rateContract.currency 
			+ " " + this.candidateProfile.rateContract.valueMax 
			+ this.translate.instant('info-item-title-per') + this.candidateProfile.rateContract.period.toLowerCase() ;
		}
		
		if (this.candidateProfile.rateContract.valueMin != 0 && this.candidateProfile.rateContract.valueMax == 0){
			return this.translate.instant('info-item-title-rate') + this.candidateProfile.rateContract.currency 
			+ " " + this.candidateProfile.rateContract.valueMin 
			+ this.translate.instant('info-item-title-per') + this.candidateProfile.rateContract.period.toLowerCase() ;
		}
		
		return "";
		
	}

	/**
	* Whether Perm Rate info is available 
	*/
	public hasPermRate():boolean{
		return this.candidateProfile.ratePerm && (this.candidateProfile.ratePerm.valueMin != 0 || this.candidateProfile.ratePerm.valueMax != 0);
	}

	/**
	* Returns the materialIcon css class for the Flag matching
	* the Country 
	*/
	private getMaterialIconClassFromLangLevel(langLevel:string):string{
		
		switch(langLevel){
			case "PROFICIENT":{return "lang-proficient-check-icon"}
			case "BASIC":{return "lang-basic-check-icon"}
			default: return "";
		}
			
	}
	
	/**
	* Returns the Humand readable version of the Language
	* @param country - Language to get the readable version for
	*/
	public getLanguage(lang:string):string{
		return this.translate.instant(lang);
	}	
	
		
}