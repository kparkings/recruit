import { TestBed } from '@angular/core/testing';

import { CampaingsService } from './campaings.service';

describe('CampaingsService', () => {
  let service: CampaingsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CampaingsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
