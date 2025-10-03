import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SavedCandidatesComponent } from './saved-candidates.component';
import { TranslateModule } from '@ngx-translate/core';
import { provideHttpClient } 				from '@angular/common/http';
import { provideHttpClientTesting } 		from '@angular/common/http/testing';
import { SuggestionsComponent} 				from '../suggestions.component';
import { AppComponent} 						from '../../app.component';

describe('SavedCandidatesComponent', () => {
  let component: SavedCandidatesComponent;
  let fixture: ComponentFixture<SavedCandidatesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
		imports: [TranslateModule.forRoot()],
		declarations: [SavedCandidatesComponent],
		providers: [ provideHttpClient(), provideHttpClientTesting(), SuggestionsComponent, AppComponent],
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(SavedCandidatesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
