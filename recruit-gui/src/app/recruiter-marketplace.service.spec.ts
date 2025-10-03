import { TestBed } from '@angular/core/testing';

import { RecruiterMarketplaceService } from './recruiter-marketplace.service';
import { HttpClient, HttpHandler }  	                                from '@angular/common/http';

describe('RecruiterMarketplaceService', () => {
  let service: RecruiterMarketplaceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
		providers: [ HttpClient, HttpHandler ],
	});
    service = TestBed.inject(RecruiterMarketplaceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
