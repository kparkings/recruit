import { TestBed } from '@angular/core/testing';

import { PublicMessagingService } from './public-messaging.service';

describe('PublicMessagingService', () => {
  let service: PublicMessagingService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PublicMessagingService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
