import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CandidateProfileComponent } from './candidate-profile.component';
import { TranslateModule } from '@ngx-translate/core';
import { provideHttpClient } 				from '@angular/common/http';
import { provideHttpClientTesting } 		from '@angular/common/http/testing';
import { AppComponent} 						from '../../app.component';
import { EnumToHumanReadableValue }			from '../../recruiter-profile/enum-to-hr-pipe';
import { ReactiveFormsModule } from '@angular/forms';

describe('CandidateProfileComponent', () => {
  let component: CandidateProfileComponent;
  let fixture: ComponentFixture<CandidateProfileComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
		imports: [TranslateModule.forRoot()],
      	declarations: [CandidateProfileComponent,EnumToHumanReadableValue],
		providers: [ provideHttpClient(), provideHttpClientTesting(), AppComponent,ReactiveFormsModule ],
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CandidateProfileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
