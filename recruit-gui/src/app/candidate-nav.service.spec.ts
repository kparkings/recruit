import { TestBed } from '@angular/core/testing';

import { CandidateNavService } from './candidate-nav.service';

describe('CandidateNavService', () => {
  let service: CandidateNavService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CandidateNavService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
