import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CitiesComponent } from './cities.component';
import { TranslateModule } from '@ngx-translate/core';
import { provideHttpClient } 				from '@angular/common/http';
import { provideHttpClientTesting } 		from '@angular/common/http/testing';

describe('CitiesComponent', () => {
  let component: CitiesComponent;
  let fixture: ComponentFixture<CitiesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
		imports: [TranslateModule.forRoot()],
		declarations: [CitiesComponent],
		providers: [ provideHttpClient(), provideHttpClientTesting()],
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CitiesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
