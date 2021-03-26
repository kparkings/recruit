import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { ReactiveFormsModule } from '@angular/forms';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NewCandidateComponent } from './new-candidate/new-candidate.component';
import { HttpClientModule } from '@angular/common/http';
import { ViewCandidatesComponent } from './view-candidates/view-candidates.component';
import { LoginUserComponent } from './login-user/login-user.component';
import { AuthService } from './auth.service';
import { AuthGuardService } from './auth-guard.service';
import { RouterModule} from '@angular/router';

@NgModule({
  declarations: [
    AppComponent,
    NewCandidateComponent,
    ViewCandidatesComponent,
    LoginUserComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule
  ],
  providers: [AuthGuardService, AuthService],
  bootstrap: [AppComponent]
})
export class AppModule { }
