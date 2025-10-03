import { TestBed } from '@angular/core/testing';
import { HttpClient, HttpHandler }  	                                from '@angular/common/http';

import { RecruiterService } from './recruiter.service';

describe('RecruiterService', () => {
  let service: RecruiterService;

  beforeEach(() => {
    TestBed.configureTestingModule({
		providers: [ HttpClient, HttpHandler ],
	});
    service = TestBed.inject(RecruiterService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
