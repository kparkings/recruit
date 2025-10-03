import { TestBed } from '@angular/core/testing';

import { StaticDataService } from './static-data.service';
import { TranslateModule} 					from '@ngx-translate/core';
import { provideHttpClient } 				from '@angular/common/http';
import { provideHttpClientTesting } 		from '@angular/common/http/testing';

describe('StaticDataService', () => {
  let service: StaticDataService;

  beforeEach(() => {
    TestBed.configureTestingModule({
		imports: [TranslateModule.forRoot()],
		providers: [ provideHttpClient(), provideHttpClientTesting(), ],
	});
    service = TestBed.inject(StaticDataService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
