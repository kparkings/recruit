import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RecruiterMarketplaceComponent } from './recruiter-marketplace.component';

describe('RecruiterMarketplaceComponent', () => {
  let component: RecruiterMarketplaceComponent;
  let fixture: ComponentFixture<RecruiterMarketplaceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RecruiterMarketplaceComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RecruiterMarketplaceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
