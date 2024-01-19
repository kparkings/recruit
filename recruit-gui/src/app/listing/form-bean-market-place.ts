import { UntypedFormControl, UntypedFormGroup } from "@angular/forms";

export class FormBeanMarketPlace {
   
	public static getInstance():UntypedFormGroup{
		return new UntypedFormGroup({
     		startDate:				new UntypedFormControl(''),
			lastSubmissionDate:		new UntypedFormControl(),
    	   	comments:				new UntypedFormControl(),
		});
	}
	
}