import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CandidateStaisticsComponent } from './candidate-staistics.component';

describe('CandidateStaisticsComponent', () => {
  let component: CandidateStaisticsComponent;
  let fixture: ComponentFixture<CandidateStaisticsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CandidateStaisticsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CandidateStaisticsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
