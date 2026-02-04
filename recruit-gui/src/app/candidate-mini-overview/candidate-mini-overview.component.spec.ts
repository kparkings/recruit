import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CandidateMiniOverviewComponent } from './candidate-mini-overview.component';

describe('CandidateMiniOverviewComponent', () => {
  let component: CandidateMiniOverviewComponent;
  let fixture: ComponentFixture<CandidateMiniOverviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CandidateMiniOverviewComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CandidateMiniOverviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
