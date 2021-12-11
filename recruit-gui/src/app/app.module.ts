import { NgModule }						from '@angular/core';
import { BrowserModule }				from '@angular/platform-browser';
import { ReactiveFormsModule }			from '@angular/forms';
import { AppRoutingModule }				from './app-routing.module';
import { AppComponent }					from './app.component';
import { NewCandidateComponent }		from './new-candidate/new-candidate.component';
import { HttpClientModule }				from '@angular/common/http';
import { ViewCandidatesComponent }		from './view-candidates/view-candidates.component';
import { LoginUserComponent }			from './login-user/login-user.component';
import { AuthService }					from './auth.service';
import { AuthGuardService }				from './auth-guard.service';
import { HomeComponent }				from './home/home.component';
import { NgbModule }					from '@ng-bootstrap/ng-bootstrap';
import { MatIconModule }				from '@angular/material/icon';
import { BrowserAnimationsModule }		from '@angular/platform-browser/animations';
import { StatisticsComponent }			from './statistics/statistics.component';
import { ChartsModule }					from 'ng2-charts';
import { AccountsComponent } 			from './accounts/accounts.component';
import { CreateCandidateComponent } 	from './create-candidate/create-candidate.component';
import { RecruiterAccountComponent } from './recruiter-account/recruiter-account.component';
import { RecruiterListingsComponent } from './recruiter-listings/recruiter-listings.component';
import { ListingComponent } from './listing/listing.component';

@NgModule({
  declarations: [
    AppComponent,
    NewCandidateComponent,
    ViewCandidatesComponent,
    LoginUserComponent,
    HomeComponent,
    StatisticsComponent,
    AccountsComponent,
    CreateCandidateComponent,
    RecruiterAccountComponent,
    RecruiterListingsComponent,
    ListingComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    NgbModule,
    MatIconModule,
    BrowserAnimationsModule,
	ChartsModule
  ],
  providers: [AuthGuardService, AuthService],
  bootstrap: [AppComponent]
})
export class AppModule { }
