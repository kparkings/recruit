import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RecruiterAlertsComponent } from './recruiter-alerts.component';

describe('RecruiterAlertsComponent', () => {
  let component: RecruiterAlertsComponent;
  let fixture: ComponentFixture<RecruiterAlertsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RecruiterAlertsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RecruiterAlertsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
