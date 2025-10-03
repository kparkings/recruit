import { TestBed } from '@angular/core/testing';

import { ListingService } from './listing.service';
import { HttpClient, HttpHandler }  	                                from '@angular/common/http';

describe('ListingService', () => {
  let service: ListingService;

  beforeEach(() => {
    TestBed.configureTestingModule({
		providers: [ HttpClient, HttpHandler ],
	});
    service = TestBed.inject(ListingService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
