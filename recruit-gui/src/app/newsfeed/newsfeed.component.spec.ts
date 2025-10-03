import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TranslateModule} 					from '@ngx-translate/core';
import { provideHttpClient } 				from '@angular/common/http';
import { provideHttpClientTesting } 		from '@angular/common/http/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { NewsfeedComponent } from './newsfeed.component';

describe('NewsfeedComponent', () => {
  let component: NewsfeedComponent;
  let fixture: ComponentFixture<NewsfeedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
		imports: [TranslateModule.forRoot(),ReactiveFormsModule],
      	declarations: [ NewsfeedComponent ],
	  	providers: [ provideHttpClient(), provideHttpClientTesting(),ReactiveFormsModule],
    })
    .compileComponents();

    fixture = TestBed.createComponent(NewsfeedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
  
});

