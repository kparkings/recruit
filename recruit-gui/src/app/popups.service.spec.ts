import { TestBed } from '@angular/core/testing';

import { PopupsService } from './popups.service';
import { HttpClient, HttpHandler }  	                                from '@angular/common/http';

describe('PopupsService', () => {
  let service: PopupsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
		providers: [ HttpClient, HttpHandler ],
	});
    service = TestBed.inject(PopupsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
