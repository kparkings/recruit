import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RecruiterAccountComponent } from './recruiter-account.component';
import { TranslateModule} 					from '@ngx-translate/core';
import { provideHttpClient } 				from '@angular/common/http';
import { provideHttpClientTesting } 		from '@angular/common/http/testing';
import { ReactiveFormsModule } from '@angular/forms';

describe('RecruiterAccountComponent', () => {
  let component: RecruiterAccountComponent;
  let fixture: ComponentFixture<RecruiterAccountComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
		imports: [TranslateModule.forRoot(),ReactiveFormsModule],
      	declarations: [ RecruiterAccountComponent ],
	  	providers: [ provideHttpClient(), provideHttpClientTesting(), ],
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RecruiterAccountComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
