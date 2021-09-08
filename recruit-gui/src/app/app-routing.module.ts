import { NgModule }										from '@angular/core';
import { RouterModule, Routes, CanActivate }			from '@angular/router';
import { NewCandidateComponent }						from './new-candidate/new-candidate.component';
import { StatisticsComponent }							from './statistics/statistics.component';
import { ViewCandidatesComponent }						from './view-candidates/view-candidates.component';
import { LoginUserComponent }							from './login-user/login-user.component';
import { AccountsComponent }							from './accounts/accounts.component';
import { HomeComponent }								from './home/home.component';
import { CreateCandidateComponent }						from './create-candidate/create-candidate.component';
import { AuthGuardService }								from './auth-guard.service';
import { AdminGuardGuard }								from './admin-guard.guard';


const routes: Routes = [
	{path: '', component: HomeComponent},
	{path: 'new-candidate', 		component: NewCandidateComponent, 			canActivate: [AuthGuardService, AdminGuardGuard]},
	{path: 'view-candidates', 		component: ViewCandidatesComponent, 		canActivate: [AuthGuardService]},
	{path: 'statistics', 			component: StatisticsComponent, 			canActivate: [AuthGuardService, AdminGuardGuard]},
	{path: 'accounts', 				component: AccountsComponent, 				canActivate: [AuthGuardService, AdminGuardGuard]},
	{path: 'login-user', 			component: LoginUserComponent},
	{path: 'login-user', 			component: LoginUserComponent},
	{path: 'create-candidate', 		component: CreateCandidateComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
