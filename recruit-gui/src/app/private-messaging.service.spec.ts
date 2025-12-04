import { TestBed } from '@angular/core/testing';

import { PrivateMessagingService } from './private-messaging.service';

describe('PrivateMessagingService', () => {
  let service: PrivateMessagingService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PrivateMessagingService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
