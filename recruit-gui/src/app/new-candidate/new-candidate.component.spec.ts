import { ComponentFixture, TestBed } 		from '@angular/core/testing';
import { NewCandidateComponent } 			from './new-candidate.component';
import { TranslateModule} 					from '@ngx-translate/core';
import { provideHttpClient } 				from '@angular/common/http';
import { provideHttpClientTesting } 		from '@angular/common/http/testing';
import { AppComponent} 						from '../app.component';
import { ReactiveFormsModule } from '@angular/forms';

describe('NewCandidateComponent', () => {
  let component: NewCandidateComponent;
  let fixture: ComponentFixture<NewCandidateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
		imports: [TranslateModule.forRoot(),ReactiveFormsModule],
      	declarations: [ NewCandidateComponent ]		,
		providers: [ provideHttpClient(), provideHttpClientTesting(), AppComponent],
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NewCandidateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
