import { NgModule } from '@angular/core';
import { RouterModule, Routes, CanActivate } from '@angular/router';
import { NewCandidateComponent } from './new-candidate/new-candidate.component';
import { ViewCandidatesComponent } from './view-candidates/view-candidates.component';
import { LoginUserComponent } from './login-user/login-user.component';
import { AuthGuardService } from './auth-guard.service';

const routes: Routes = [
  {path: '', component: NewCandidateComponent, canActivate: [AuthGuardService]},
  {path: 'new-candidate', component: NewCandidateComponent, canActivate: [AuthGuardService]},
  {path: 'view-candidates', component: ViewCandidatesComponent, canActivate: [AuthGuardService]},
  {path: 'login-user', component: LoginUserComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
