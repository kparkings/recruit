import { NgModule }										from '@angular/core';
import { RouterModule, Routes }							from '@angular/router';
import { NewCandidateComponent }						from './new-candidate/new-candidate.component';
import { StatisticsComponent }							from './statistics/statistics.component';
import { SuggestionsComponent }							from './suggestions/suggestions.component';
import { LoginUserComponent }							from './login-user/login-user.component';
import { AccountsComponent }							from './accounts/accounts.component';
import { ListingComponent }								from './listing/listing.component';
import { RecruiterAccountComponent }					from './recruiter-account/recruiter-account.component';
import { RecruiterListingsComponent }					from './recruiter-listings/recruiter-listings.component';
import { HomeComponent }								from './home/home.component';
import { EmailComponent }								from './email/email.component';
import { FaqComponent }									from './faq/faq.component';
import { CreateCandidateComponent }						from './create-candidate/create-candidate.component';
import { AuthGuardService }								from './auth-guard.service';
import { AdminGuardGuard }								from './admin-guard.guard';
import { RecruiterSignupComponent } 					from './recruiter-signup/recruiter-signup.component';
import { RecruiterMarketplaceComponent } 				from './recruiter-marketplace/recruiter-marketplace.component';
import { RecruiterProfileComponent } 					from './recruiter-profile/recruiter-profile.component';
import { RecruiterStatsComponent } 						from './recruiter-stats/recruiter-stats.component';
import { NewsfeedComponent } 							from './newsfeed/newsfeed.component';
import { ContactComponent } 							from './contact/contact.component';
import { CandidateStatisticsComponent } 				from './candidate-statistics/candidate-statistics.component';
import { CandidateMiniOverviewComponent }				from './candidate-mini-overview/candidate-mini-overview.component';
import { PricingComponent }								from './pricing/pricing.component';
import { FeaturesComponent }							from './features/features.component'

const routes: Routes = [
	{path: '', 								component: ListingComponent},
	{path: 'about', 						component: HomeComponent},
	{path: 'faq', 							component: FaqComponent},
	{path: 'contact', 						component: ContactComponent},
	{path: 'new-candidate', 				component: NewCandidateComponent, 						canActivate: [AuthGuardService]},
	{path: 'suggestions',	 				component: SuggestionsComponent, 						canActivate: [AuthGuardService]},
	{path: 'email',	 						component: EmailComponent, 								canActivate: [AuthGuardService]},
	{path: 'statistics', 					component: StatisticsComponent, 						canActivate: [AuthGuardService, AdminGuardGuard]},
	{path: 'accounts', 						component: AccountsComponent, 							canActivate: [AuthGuardService, AdminGuardGuard]},
	{path: 'app-candidate-mini-overview', 	component: CandidateMiniOverviewComponent, 				canActivate: [AuthGuardService]},
	{path: 'recruiter-account', 			component: RecruiterAccountComponent, 					canActivate: [AuthGuardService]},
	{path: 'recruiter-stats', 				component: RecruiterStatsComponent, 					canActivate: [AuthGuardService]},
	{path: 'recruiter-listings', 			component: RecruiterListingsComponent, 					canActivate: [AuthGuardService]},
	{path: 'recruiter-marketplace', 		component: RecruiterMarketplaceComponent, 				canActivate: [AuthGuardService]},
	{path: 'recruiter-profile', 			component: RecruiterProfileComponent, 					canActivate: [AuthGuardService]},
	{path: 'newsfeed', 						component: NewsfeedComponent, 							canActivate: [AuthGuardService]},
	{path: 'candidate-statistics', 			component: CandidateStatisticsComponent, 				canActivate: [AuthGuardService]},
	{path: 'login-user', 					component: LoginUserComponent},
	{path: 'login-user', 					component: LoginUserComponent},
	{path: 'listing/:id',		 			component: ListingComponent},
	{path: 'listing',		 				component: ListingComponent},
	{path: 'create-candidate', 				component: CreateCandidateComponent},
	{path: 'recruiter-signup', 				component: RecruiterSignupComponent},
	{path: 'pricing', 						component: PricingComponent},
	{path: 'features', 						component: FeaturesComponent},
	
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
