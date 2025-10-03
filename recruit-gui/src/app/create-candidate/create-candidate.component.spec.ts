import { ComponentFixture, TestBed } 		from '@angular/core/testing';
import { CreateCandidateComponent } 		from './create-candidate.component';
import { TranslateModule} 					from '@ngx-translate/core';
import { provideHttpClient } 				from '@angular/common/http';
import { provideHttpClientTesting } 		from '@angular/common/http/testing';
import { ReactiveFormsModule } from '@angular/forms';

describe('CreateCandidateComponent', () => {
  let component: CreateCandidateComponent;
  let fixture: ComponentFixture<CreateCandidateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
	  imports: [TranslateModule.forRoot(),ReactiveFormsModule],
      declarations: [ CreateCandidateComponent ],
	  providers: [ provideHttpClient(), provideHttpClientTesting(), ReactiveFormsModule],
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CreateCandidateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
