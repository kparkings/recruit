import { TestBed } from '@angular/core/testing';

import { CandidateProfileServiceService } from './candidate-profile-service.service';

describe('CandidateProfileServiceService', () => {
  let service: CandidateProfileServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CandidateProfileServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
