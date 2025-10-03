import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RecruiterListingsComponent } from './recruiter-listings.component';
import { TranslateModule} 					from '@ngx-translate/core';
import { provideHttpClient } 				from '@angular/common/http';
import { provideHttpClientTesting } 		from '@angular/common/http/testing';
import { ReactiveFormsModule } from '@angular/forms';

describe('RecruiterListingsComponent', () => {
  let component: RecruiterListingsComponent;
  let fixture: ComponentFixture<RecruiterListingsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
		imports: [TranslateModule.forRoot(),ReactiveFormsModule],
      	declarations: [ RecruiterListingsComponent ],
		providers: [ provideHttpClient(), provideHttpClientTesting()],
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RecruiterListingsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
