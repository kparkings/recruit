import { NgModule }							from '@angular/core';
import { BrowserModule }					from '@angular/platform-browser';
import { ReactiveFormsModule }				from '@angular/forms';
import { AppRoutingModule }					from './app-routing.module';
import { AppComponent }						from './app.component';
import { NewCandidateComponent }			from './new-candidate/new-candidate.component';
import { HttpClientModule }					from '@angular/common/http';
import { LoginUserComponent }				from './login-user/login-user.component';
import { AuthService }						from './auth.service';
import { AuthGuardService }					from './auth-guard.service';
import { HomeComponent }					from './home/home.component';
import { NgbModule }						from '@ng-bootstrap/ng-bootstrap';
import { BrowserAnimationsModule }			from '@angular/platform-browser/animations';
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
import { RecruiterAlertsComponent } 		from './recruiter-alerts/recruiter-alerts.component';
import { FaqComponent } 					from './faq/faq.component';
import { EmailComponent } 					from './email/email.component';
import { RecruiterProfileComponent } 		from './recruiter-profile/recruiter-profile.component';
import { TupleStrValueByPos }				from './recruiter-profile/tuple-string-pos-pipe';
import { EnumToHumanReadableValue }			from './recruiter-profile/enum-to-hr-pipe';
import { RecruiterStatsComponent } 			from './recruiter-stats/recruiter-stats.component';
import { CandidateInfoBoxComponent } 		from './candidate-info-box/candidate-info-box.component';
import { NewsfeedComponent } 				from './newsfeed/newsfeed.component';
import { InlineCvComponent}					from './suggestions/inline-cv/inline-cv.component';
import { SavedCandidatesComponent}			from './suggestions/saved-candidates/saved-candidates.component';
import { CandidateProfileComponent}			from './suggestions/candidate-profile/candidate-profile.component';

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
    RecruiterAlertsComponent,
    FaqComponent,
    EmailComponent,
    RecruiterProfileComponent,
	TupleStrValueByPos,
	EnumToHumanReadableValue,
 	RecruiterStatsComponent,
 	SuggestionsComponent,
 	NewsfeedComponent,
 	InlineCvComponent,
 	SavedCandidatesComponent,
 	CandidateProfileComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    BrowserAnimationsModule,
	NgChartsModule.forRoot()
  ],
  providers: [AuthGuardService, AuthService, CookieService],
  bootstrap: [AppComponent]
})
export class AppModule { }
