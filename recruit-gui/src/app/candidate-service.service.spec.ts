import { TestBed } from '@angular/core/testing';

import { CandidateServiceService } from './candidate-service.service';
import { TranslateModule} 					from '@ngx-translate/core';
import { provideHttpClient } 				from '@angular/common/http';
import { provideHttpClientTesting } 		from '@angular/common/http/testing';

describe('CandidateServiceService', () => {
  let service: CandidateServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
		imports: [TranslateModule.forRoot()],
		providers: [ provideHttpClient(), provideHttpClientTesting(), ],
	});
    service = TestBed.inject(CandidateServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
