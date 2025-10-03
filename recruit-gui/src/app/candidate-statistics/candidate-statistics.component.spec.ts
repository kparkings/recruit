import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TranslateModule} 					from '@ngx-translate/core';
import { provideHttpClient } 				from '@angular/common/http';
import { provideHttpClientTesting } 		from '@angular/common/http/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { CandidateStatisticsComponent } from './candidate-statistics.component';

describe('CandidateStatisticsComponent', () => {
  let component: CandidateStatisticsComponent;
  let fixture: ComponentFixture<CandidateStatisticsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
		imports: [TranslateModule.forRoot(),ReactiveFormsModule],
      	declarations: [ CandidateStatisticsComponent ],
	  	providers: [ provideHttpClient(), provideHttpClientTesting(),ReactiveFormsModule],
    })
    .compileComponents();

    fixture = TestBed.createComponent(CandidateStatisticsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

