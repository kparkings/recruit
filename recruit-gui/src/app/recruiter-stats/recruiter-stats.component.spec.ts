import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RecruiterStatsComponent } from './recruiter-stats.component';
import { TranslateModule} 					from '@ngx-translate/core';
import { provideHttpClient } 				from '@angular/common/http';
import { provideHttpClientTesting } 		from '@angular/common/http/testing';

describe('RecruiterStatsComponent', () => {
  let component: RecruiterStatsComponent;
  let fixture: ComponentFixture<RecruiterStatsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
		imports: [TranslateModule.forRoot()],
      	declarations: [ RecruiterStatsComponent ],
	  	providers: [ provideHttpClient(), provideHttpClientTesting(), ],
    })
    .compileComponents();

    fixture = TestBed.createComponent(RecruiterStatsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
