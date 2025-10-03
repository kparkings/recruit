import { TestBed } from '@angular/core/testing';

import { NewsfeedService } from './newsfeed.service';
import { HttpClient, HttpHandler }  	                                from '@angular/common/http';

describe('NewsfeedService', () => {
  let service: NewsfeedService;

  beforeEach(() => {
    TestBed.configureTestingModule({
		providers: [ HttpClient, HttpHandler ],
	});
    service = TestBed.inject(NewsfeedService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
