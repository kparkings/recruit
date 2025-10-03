import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InlineCvComponent } from './inline-cv.component';
import { TranslateModule} 					from '@ngx-translate/core';
import { provideHttpClient } 				from '@angular/common/http';
import { provideHttpClientTesting } 		from '@angular/common/http/testing';
import { EnumToHumanReadableValue }			from '../../recruiter-profile/enum-to-hr-pipe';
describe('InlineCvComponent', () => {
  let component: InlineCvComponent;
  let fixture: ComponentFixture<InlineCvComponent>;
  InlineCvComponent
  beforeEach(async () => {
    await TestBed.configureTestingModule({
	  imports: [TranslateModule.forRoot()],
	  declarations: [InlineCvComponent],
      providers: [ provideHttpClient(), provideHttpClientTesting(), EnumToHumanReadableValue],
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(InlineCvComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
