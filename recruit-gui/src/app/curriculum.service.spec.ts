import { TestBed } from '@angular/core/testing';

import { CurriculumService } from './curriculum.service';
import { HttpClient, HttpHandler }  	                                from '@angular/common/http';

describe('CurriculumService', () => {
  let service: CurriculumService;

  beforeEach(() => {
  //  TestBed.configureTestingModule({});
	
	
	TestBed.configureTestingModule({
		providers: [ HttpClient, HttpHandler ],
	});
	
	
	
	
    service = TestBed.inject(CurriculumService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
