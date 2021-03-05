import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { NewCandidateComponent } from './new-candidate/new-candidate.component';


const routes: Routes = [
  {path: '', component: NewCandidateComponent},
  {path: 'new-candidate', component: NewCandidateComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
