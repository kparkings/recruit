import { UntypedFormControl, UntypedFormGroup } from "@angular/forms";

export class FormBeanFilterByJobSpec {
   
	public static getInstance():UntypedFormGroup{
		return new UntypedFormGroup({
     		specAsText:				new UntypedFormControl('Enter Job specification Text here...'),
		});
	}
	
}