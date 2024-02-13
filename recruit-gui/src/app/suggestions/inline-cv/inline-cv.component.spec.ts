import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InlineCvComponent } from './inline-cv.component';

describe('InlineCvComponent', () => {
  let component: InlineCvComponent;
  let fixture: ComponentFixture<InlineCvComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InlineCvComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(InlineCvComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
