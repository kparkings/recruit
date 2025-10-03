import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RecruitersComponent } from './recruiters.component';
import { TranslateModule} 					from '@ngx-translate/core';
import { provideHttpClient } 				from '@angular/common/http';
import { provideHttpClientTesting } 		from '@angular/common/http/testing';

describe('RecruitersComponent', () => {
  let component: RecruitersComponent;
  let fixture: ComponentFixture<RecruitersComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
		imports: [TranslateModule.forRoot()],
		      	declarations: [ RecruitersComponent ],
				providers: [ provideHttpClient(), provideHttpClientTesting(), ],
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(RecruitersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
