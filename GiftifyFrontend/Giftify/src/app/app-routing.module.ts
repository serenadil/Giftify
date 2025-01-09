import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from './components/login/login.component';
import {RegisterComponent} from './components/register/register.component';
import {HomeComponent} from './components/home/home.component';
import {CommunityComponent} from './components/community/community.component';
import {AuthGuard} from './authUtil/authGuard';
import {WishlistComponent} from './components/wishlist/wishlist.component';


const routes: Routes = [
  // {path: 'login', component: LoginComponent},
  // {path: 'register', component: RegisterComponent},
  // {path: '', redirectTo: '/login', pathMatch: 'full'},
  // {path: 'home', component: HomeComponent, canActivate: [AuthGuard]},
  // { path: 'community/:id', component: CommunityComponent },
  // {path: 'community', component: CommunityComponent},
  {path: 'wishlist', component: WishlistComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
