import { TestBed } from '@angular/core/testing';

import { RecruiterMarketplaceService } from './recruiter-marketplace.service';

describe('RecruiterMarketplaceService', () => {
  let service: RecruiterMarketplaceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RecruiterMarketplaceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
