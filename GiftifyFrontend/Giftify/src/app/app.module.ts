import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {HttpClientModule} from '@angular/common/http';
import { LoginComponent } from './login/login.component';
import {FormsModule} from '@angular/forms';
import { CommonModule } from '@angular/common';
import { WishComponent } from './wish/wish.component';
import { AddWishComponent } from './components/wish/add-wish/add-wish.component';
import { EditWishComponent } from './components/wish/edit-wish/edit-wish.component';
import { DeleteWishComponent } from './components/wish/delete-wish/delete-wish.component';


@NgModule({
  declarations: [
    AppComponent,
    WishComponent,
    AddWishComponent,
    EditWishComponent,
    DeleteWishComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    LoginComponent, CommonModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
