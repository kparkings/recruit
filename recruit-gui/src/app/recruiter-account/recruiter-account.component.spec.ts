import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RecruiterAccountComponent } from './recruiter-account.component';

describe('RecruiterAccountComponent', () => {
  let component: RecruiterAccountComponent;
  let fixture: ComponentFixture<RecruiterAccountComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RecruiterAccountComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RecruiterAccountComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
