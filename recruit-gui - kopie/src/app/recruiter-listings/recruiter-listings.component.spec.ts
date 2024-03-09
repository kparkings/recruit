import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RecruiterListingsComponent } from './recruiter-listings.component';

describe('RecruiterListingsComponent', () => {
  let component: RecruiterListingsComponent;
  let fixture: ComponentFixture<RecruiterListingsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RecruiterListingsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RecruiterListingsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
