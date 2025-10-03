import { TestBed } from '@angular/core/testing';

import { RecruiterProfileService } from './recruiter-profile.service';
import { HttpClient, HttpHandler }  	                                from '@angular/common/http';

describe('RecruiterProfileService', () => {
  let service: RecruiterProfileService;

  beforeEach(() => {
    TestBed.configureTestingModule({
		providers: [ HttpClient, HttpHandler ],
	});
    service = TestBed.inject(RecruiterProfileService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
