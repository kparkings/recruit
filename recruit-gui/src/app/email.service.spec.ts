import { TestBed } from '@angular/core/testing';

import { EmailService } from './email.service';
import { HttpClient, HttpHandler }  	                                from '@angular/common/http';

describe('EmailService', () => {
  let service: EmailService;

  beforeEach(() => {
    TestBed.configureTestingModule({
		providers: [ HttpClient, HttpHandler ],
	});
    service = TestBed.inject(EmailService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
