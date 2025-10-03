import { TestBed } from '@angular/core/testing';

import { CreditsService } from './credits.service';
import { HttpClient, HttpHandler }  	                                from '@angular/common/http';

describe('CreditsService', () => {
  let service: CreditsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
		providers: [ HttpClient, HttpHandler ],
	});
    service = TestBed.inject(CreditsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
