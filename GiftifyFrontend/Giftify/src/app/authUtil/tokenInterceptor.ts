import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpInterceptor, HttpEvent, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, switchMap } from 'rxjs/operators';
import { AuthService } from '../services/auth.service';

@Injectable()
export class TokenInterceptor implements HttpInterceptor {
  constructor(private authService: AuthService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // Verifica se la richiesta riguarda il login o la registrazione, quindi non aggiungere il token
    if (req.url.includes('/auth/login') || req.url.includes('/auth/register')) {
      return next.handle(req);
    }

    const token = this.authService.getAccessToken();

    // Se esiste un token, aggiungilo come header Authorization
    if (token) {
      req = req.clone({
        headers: req.headers.set('Authorization', `Bearer ${token}`)
      });
    }

    return next.handle(req).pipe(
      catchError((error) => {
        if (error.status === 401 && !req.url.includes('/auth/refresh_token')) {
          // Se il token è scaduto e non stiamo già tentando di fare un refresh del token
          return this.authService.refreshToken().pipe(
            switchMap(() => {
              const refreshedToken = this.authService.getAccessToken();
              if (refreshedToken) {
                req = req.clone({
                  headers: req.headers.set('Authorization', `Bearer ${refreshedToken}`)
                });
              }
              return next.handle(req);
            })
          );
        }
        return throwError(() => error);
      })
    );
  }
}
