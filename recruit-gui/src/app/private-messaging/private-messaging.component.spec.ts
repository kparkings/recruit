import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PrivateMessagingComponent } from './private-messaging.component';

describe('PrivateMessagingComponent', () => {
  let component: PrivateMessagingComponent;
  let fixture: ComponentFixture<PrivateMessagingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PrivateMessagingComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PrivateMessagingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
