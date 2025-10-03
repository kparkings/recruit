import { ComponentFixture, TestBed } 		from '@angular/core/testing';
import { RecruiterProfileComponent }	 	from './recruiter-profile.component';
import { TranslateModule} 					from '@ngx-translate/core';
import { provideHttpClient } 				from '@angular/common/http';
import { provideHttpClientTesting } 		from '@angular/common/http/testing';
import { ReactiveFormsModule } from '@angular/forms';

describe('RecruiterProfileComponent', () => {
  let component: RecruiterProfileComponent;
  let fixture: ComponentFixture<RecruiterProfileComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
		imports: [TranslateModule.forRoot(),ReactiveFormsModule],
      	declarations: [ RecruiterProfileComponent ],
	  	providers: [ provideHttpClient(), provideHttpClientTesting(), ],
    })
    .compileComponents();

    fixture = TestBed.createComponent(RecruiterProfileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
