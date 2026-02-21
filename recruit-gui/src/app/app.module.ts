import { APP_INITIALIZER, NgModule, inject, provideAppInitializer }		from '@angular/core';
import { BrowserModule }					from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule }	from '@angular/forms';
import { AppRoutingModule }					from './app-routing.module';
import { AppComponent }							from './app.component';
import { NewCandidateComponent }			from './new-candidate/new-candidate.component';
import { provideHttpClient, HttpClient }	from '@angular/common/http';
import { LoginUserComponent }				from './login-user/login-user.component';
import { AuthService }						from './auth.service';
import { AuthGuardService }					from './auth-guard.service';
import { HomeComponent }					from './home/home.component';
//import { BrowserAnimationsModule }			from '@angular/platform-browser/animations';
import { StatisticsComponent }				from './statistics/statistics.component';
import { NgChartsModule }					from 'ng2-charts';
import { AccountsComponent } 				from './accounts/accounts.component';
import { CreateCandidateComponent } 		from './create-candidate/create-candidate.component';
import { RecruiterAccountComponent } 		from './recruiter-account/recruiter-account.component';
import { RecruiterListingsComponent } 		from './recruiter-listings/recruiter-listings.component';
import { ListingComponent } 				from './listing/listing.component';
import { RecruiterSignupComponent } 		from './recruiter-signup/recruiter-signup.component';
import { SuggestionsComponent } 			from './suggestions/suggestions.component';
import { CookieService} 					from 'ngx-cookie-service';
import { RecruiterMarketplaceComponent } 	from './recruiter-marketplace/recruiter-marketplace.component';
import { FaqComponent } 					from './faq/faq.component';
import { ContactComponent } 				from './contact/contact.component';

import { EmailComponent } 					from './email/email.component';
import { RecruiterProfileComponent } 		from './recruiter-profile/recruiter-profile.component';
import { TupleStrValueByPos }				from './recruiter-profile/tuple-string-pos-pipe';
import { EnumToHumanReadableValue }			from './recruiter-profile/enum-to-hr-pipe';
import { RecruiterStatsComponent } 			from './recruiter-stats/recruiter-stats.component';
import { CandidateInfoBoxComponent } 		from './candidate-info-box/candidate-info-box.component';
import { NewsfeedComponent } 				from './newsfeed/newsfeed.component';
import { InlineCvComponent}					from './suggestions/inline-cv/inline-cv.component';
import { SearchbarComponent}				from './suggestions/searchbar/searchbar.component';
import { SearchbarComponentListing}			from './listing/searchbar/searchbar.component';

import { SavedCandidatesComponent}			from './suggestions/saved-candidates/saved-candidates.component';
import { CandidateProfileComponent}			from './suggestions/candidate-profile/candidate-profile.component';
import { RecruitersComponent}				from './accounts/recruiters/recruiters.component';
import { SubscriptionsComponent }			from './accounts/subscriptions/subscriptions.component';
import { CitiesComponent }					from './accounts/cities/cities.component';

import {TranslateLoader, TranslateModule} 	from '@ngx-translate/core';
import {TranslateHttpLoader} 				from '@ngx-translate/http-loader';
import { MatIconModule } 					from "@angular/material/icon";
import { CandidateServiceService } 			from './candidate-service.service';
import { RecruiterMarketplaceService } 		from './recruiter-marketplace.service';
import { ListingService } 					from './listing.service';

import { CandidateStatisticsComponent } 	from './candidate-statistics/candidate-statistics.component';
import { PrivateMessagingComponent } 		from './private-messaging/private-messaging.component';
import { CandidateMiniOverviewComponent } 	from './candidate-mini-overview/candidate-mini-overview.component'
import { PublicPostsComponent } 			from './newsfeed/public-posts/public-posts.component';
import { Observable } from 'rxjs';

@NgModule({
  declarations: [
    AppComponent,
    NewCandidateComponent,
    LoginUserComponent,
    HomeComponent,
    StatisticsComponent,
    AccountsComponent,
    CreateCandidateComponent,
    RecruiterAccountComponent,
    RecruiterListingsComponent,
    ListingComponent,
    CandidateInfoBoxComponent,
    RecruiterSignupComponent,
    SuggestionsComponent,
    RecruiterMarketplaceComponent,
    FaqComponent,
    ContactComponent,
    EmailComponent,
    RecruiterProfileComponent,
	TupleStrValueByPos,
	EnumToHumanReadableValue,
 	RecruiterStatsComponent,
 	SuggestionsComponent,
 	NewsfeedComponent,
 	InlineCvComponent,
	SearchbarComponent,
	SearchbarComponentListing,
 	SavedCandidatesComponent,
 	CandidateProfileComponent,
 	RecruitersComponent,
 	SubscriptionsComponent,
	CitiesComponent,
	CandidateStatisticsComponent,
	PrivateMessagingComponent,
	CandidateMiniOverviewComponent,
	PublicPostsComponent
	
	
  ],
  imports: [
    BrowserModule,
     MatIconModule,
    //HttpClientModule,
	
     TranslateModule.forRoot({
            loader: {
                provide: TranslateLoader,
                useFactory: HttpLoaderFactory,
                deps: [HttpClient]
            }
        }),
    AppRoutingModule,
    ReactiveFormsModule,
   // HttpClientModule,
   
	FormsModule,
    //BrowserAnimationsModule,
	NgChartsModule.forRoot()
  ],
  providers: [provideHttpClient(), AuthGuardService, AuthService, CookieService, CandidateServiceService,RecruiterMarketplaceService,ListingService, 
	//TODO: [KP] THis replaces APP_INITIALIZED but then the geozone and countries don't initialize properly. Needs investigation
	//provideAppInitializer(()=>{	
	
	//	const canServ 	= inject(CandidateServiceService);
	//	const listServ 	= inject(ListingService);
		
	//	canServ.initializeSupportedCountries();
	//	canServ.initializeSupportedLanguages();
	//	canServ.initializeGeoZones();
	//	listServ.initializeSupportedLanguages();
		
	//})
	//,
	{
      provide: APP_INITIALIZER,
      useFactory: initCandidateInfoBackendDataCalls,
      deps: [CandidateServiceService, ListingService], multi: true
    }
	],
  bootstrap: [AppComponent]
})
export class AppModule { }

export function HttpLoaderFactory(http: HttpClient): TranslateHttpLoader {
    return new TranslateHttpLoader(http);
}

/**
* Factory to initialize service and populate with data called from 
* API before service is available to components 
*/
export function initCandidateInfoBackendDataCalls(
  candidateService: CandidateServiceService,
  listingService: ListingService
) {
  return async () => {
	await candidateService.initializeSupportedCountries();
    await candidateService.initializeSupportedLanguages();
    await candidateService.initializeGeoZones();
	await listingService.initializeSupportedLanguages();
  };
}


