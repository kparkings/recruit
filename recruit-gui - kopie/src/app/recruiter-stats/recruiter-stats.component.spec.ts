import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RecruiterStatsComponent } from './recruiter-stats.component';

describe('RecruiterStatsComponent', () => {
  let component: RecruiterStatsComponent;
  let fixture: ComponentFixture<RecruiterStatsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RecruiterStatsComponent ]
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
