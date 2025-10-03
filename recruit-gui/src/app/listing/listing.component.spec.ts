import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListingComponent } from './listing.component';
import { TranslateModule} 					from '@ngx-translate/core';
import { provideHttpClient } 				from '@angular/common/http';
import { provideHttpClientTesting } 		from '@angular/common/http/testing';
import { ActivatedRoute, provideRouter, RouterModule } 								from '@angular/router';
import { SearchbarComponentListing } from './searchbar/searchbar.component';

describe('ListingComponent', () => {
  let component: ListingComponent;
  let fixture: ComponentFixture<ListingComponent>;


  beforeEach(async () => {
    await TestBed.configureTestingModule({
		imports: [TranslateModule.forRoot() ],
      	declarations: [ ListingComponent, SearchbarComponentListing ],
	   providers: [ provideHttpClient(), provideHttpClientTesting(), provideRouter([
		          { path: 'listing', component: ListingComponent }
		        ])],
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ListingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

