import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { ReactiveFormsModule } from '@angular/forms';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NewCandidateComponent } from './new-candidate/new-candidate.component';
import { HttpClientModule } from '@angular/common/http';
import { ViewCandidatesComponent } from './view-candidates/view-candidates.component';

@NgModule({
  declarations: [
    AppComponent,
    NewCandidateComponent,
    ViewCandidatesComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }