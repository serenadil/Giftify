// auth.service.ts
import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpHeaders} from '@angular/common/http';
import {catchError, Observable, throwError} from 'rxjs';
import {AuthResponse} from '../../model/Auth/AuthResponse';
import {LoginRequest} from '../../model/Auth/LoginRequest';
import {Router} from '@angular/router';
import {RegisterRequest} from '../../model/Auth/RegisterRequest';


@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/auth';

  constructor(private http: HttpClient, private router: Router) {
  }

  login(loginRequest: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, loginRequest).pipe
    (catchError(this.handleError));
  }

  register(registerRequest: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/register`, registerRequest).pipe(
      catchError(this.handleError)
    );
  }

  refreshToken(): Observable<AuthResponse> {
    const refreshToken = sessionStorage.getItem('refreshToken');
    if (!refreshToken) {
      throw new Error('No refresh token found');
    }
    return this.http.post<AuthResponse>(`${this.apiUrl}/refresh_token`, {}, {
      headers: new HttpHeaders().set('Authorization', `Bearer ${refreshToken}`)
    });
  }

  private handleError(error: HttpErrorResponse): Observable<never> {
    let errorMessage = 'Unknown error!';
    if (error.error instanceof ErrorEvent) {
      errorMessage = `Error: ${error.error.message}`;
    } else {
      errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;
      if (error.error) {
        errorMessage += `\nDetails: ${error.error}`;
      }
    }
    return throwError(() => new Error(errorMessage));
  }

  saveTokens(authResponse: AuthResponse): void {
    sessionStorage.clear();
    if (authResponse && authResponse.access_token && authResponse.refresh_token) {
      sessionStorage.setItem('accessToken', authResponse.access_token);
      sessionStorage.setItem('refreshToken', authResponse.refresh_token);

    }
  }

  logout(): void {
    this.http.post<string>(`${this.apiUrl}/logout`, {}).subscribe({
      next: (response) => {
        console.log(response);
        sessionStorage.removeItem('accessToken');
        sessionStorage.removeItem('refreshToken');
        this.router.navigate(['/login']);
      },
      error: (err) => {
        console.error('Errore durante il logout:', err);
      },
    });
  }


  isAuthenticated(): boolean {
    return !!sessionStorage.getItem('accessToken');
  }

  getAccessToken(): string | null {
    return sessionStorage.getItem('accessToken');
  }


}
