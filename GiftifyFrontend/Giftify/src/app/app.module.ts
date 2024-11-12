
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppComponent } from './app.component';
import { AuthModule } from '@auth0/auth0-angular'; 

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AuthModule.forRoot({
      domain: 'http://dev-ulqacsn8epa2w2rq.eu.auth0.com',   
      clientId: 'zfIvLjriUklGDr5J7SbiEDc0Vc7EVOFM',        
      authorizationParams: {
        redirect_uri: window.location.origin 
      }
    })
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
