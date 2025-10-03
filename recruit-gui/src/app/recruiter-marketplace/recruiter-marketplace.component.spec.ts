import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RecruiterMarketplaceComponent } from './recruiter-marketplace.component';
import { TranslateModule} 					from '@ngx-translate/core';
import { provideHttpClient } 				from '@angular/common/http';
import { provideHttpClientTesting } 		from '@angular/common/http/testing';
import { ReactiveFormsModule } from '@angular/forms';

describe('RecruiterMarketplaceComponent', () => {
  let component: RecruiterMarketplaceComponent;
  let fixture: ComponentFixture<RecruiterMarketplaceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
		imports: [TranslateModule.forRoot(),ReactiveFormsModule],
      	declarations: [ RecruiterMarketplaceComponent ],
	  	providers: [ provideHttpClient(), provideHttpClientTesting(), ],
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RecruiterMarketplaceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
