import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchbarComponentListing } from './searchbar.component';
import { TranslateModule} 					from '@ngx-translate/core';
import { provideHttpClient } 				from '@angular/common/http';
import { provideHttpClientTesting } 		from '@angular/common/http/testing';
import { ActivatedRoute, provideRouter } from '@angular/router';

describe('SearchbarComponentListing', () => {
  let component: SearchbarComponentListing;
  let fixture: ComponentFixture<SearchbarComponentListing>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
		imports: [TranslateModule.forRoot() ],
      	declarations: [ SearchbarComponentListing ],
		providers: [ provideHttpClient(), provideHttpClientTesting(), provideRouter([
				          { path: 'listing', component: SearchbarComponentListing }
				        ])],
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(SearchbarComponentListing);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
