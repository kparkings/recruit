import { UntypedFormControl, UntypedFormGroup } from "@angular/forms";

/**
* Form Bean for creating new Listing 
*/
export class FormBeanNewListing {
   
	public static getInstance():UntypedFormGroup{
		return new UntypedFormGroup({
     		title:				new UntypedFormControl(''),
			type:				new UntypedFormControl(),
	       	country:			new UntypedFormControl(),
			location:			new UntypedFormControl(),	
			experienceYears:	new UntypedFormControl(),
			rate:				new UntypedFormControl(),
			rateCurrency:		new UntypedFormControl(),
			description:		new UntypedFormControl(),
			contactName:		new UntypedFormControl(),
			contactCompany:		new UntypedFormControl(),
			contactEmail:		new UntypedFormControl(),
			langDutch:			new UntypedFormControl(),
			langEnglish:		new UntypedFormControl(),
			langFrench:			new UntypedFormControl(),
			skill:				new UntypedFormControl()
		});
	}
}