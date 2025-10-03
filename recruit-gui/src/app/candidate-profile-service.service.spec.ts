import { TestBed } from '@angular/core/testing';

import { CandidateProfileService } from './candidate-profile-service.service';
import { HttpClient, HttpHandler }  	                                from '@angular/common/http';
import {TranslateStore, TranslateLoader, TranslateCompiler,TranslateParser, MissingTranslationHandler} from '@ngx-translate/core';

describe('CandidateProfileService', () => {
  let service: CandidateProfileService;

  beforeEach(() => {
    TestBed.configureTestingModule({
		providers: [ HttpClient, HttpHandler, TranslateStore, TranslateLoader, TranslateCompiler,TranslateParser, MissingTranslationHandler ],
	});
    service = TestBed.inject(CandidateProfileService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
