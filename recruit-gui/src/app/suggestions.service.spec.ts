import { TestBed } from '@angular/core/testing';

import { SuggestionsService } from './suggestions.service';
import { TranslateModule} 					from '@ngx-translate/core';
import { provideHttpClient } 				from '@angular/common/http';
import { provideHttpClientTesting } 		from '@angular/common/http/testing';

describe('SuggestionsService', () => {
  let service: SuggestionsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
		imports: [TranslateModule.forRoot()],
		providers: [ provideHttpClient(), provideHttpClientTesting(), ],
	});
    service = TestBed.inject(SuggestionsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
