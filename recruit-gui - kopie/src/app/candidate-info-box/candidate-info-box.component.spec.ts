import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CandidateInfoBoxComponent } from './candidate-info-box.component';

describe('CandidateInfoBoxComponent', () => {
  let component: CandidateInfoBoxComponent;
  let fixture: ComponentFixture<CandidateInfoBoxComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CandidateInfoBoxComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CandidateInfoBoxComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});