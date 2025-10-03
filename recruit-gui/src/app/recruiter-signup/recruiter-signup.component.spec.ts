import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RecruiterSignupComponent } from './recruiter-signup.component';
import { TranslateModule} 					from '@ngx-translate/core';
import { provideHttpClient } 				from '@angular/common/http';
import { provideHttpClientTesting } 		from '@angular/common/http/testing';
import { ReactiveFormsModule } from '@angular/forms';
describe('RecruiterSignupComponent', () => {
  let component: RecruiterSignupComponent;
  let fixture: ComponentFixture<RecruiterSignupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
		imports: [TranslateModule.forRoot(),ReactiveFormsModule],
      	declarations: [ RecruiterSignupComponent ],
	  	providers: [ provideHttpClient(), provideHttpClientTesting(), ],
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RecruiterSignupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
