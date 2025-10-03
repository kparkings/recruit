import { TestBed } from '@angular/core/testing';

import { StatisticsService } from './statistics.service';
import { HttpClient, HttpHandler }  	                                from '@angular/common/http';

describe('StatisticsService', () => {
  let service: StatisticsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
		providers: [ HttpClient, HttpHandler ],
	});
    service = TestBed.inject(StatisticsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
