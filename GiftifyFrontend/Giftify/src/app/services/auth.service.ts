// auth.service.ts
import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {AuthResponse} from '../../model/Auth/AuthResponse';
import {LoginRequest} from '../../model/Auth/LoginRequest';
import {RegisterRequest} from '../../model/Auth/RegisterRequest';


@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/auth';

  constructor(private http: HttpClient) {
  }

  login(loginRequest: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, loginRequest);
  }

  register(registerRequest: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/register`, {registerRequest});
  }


  refreshToken(): Observable<AuthResponse> {
    const refreshToken = localStorage.getItem('refreshToken');
    if (!refreshToken) {
      throw new Error('No refresh token found');
    }

    return this.http.post<AuthResponse>(`${this.apiUrl}/refresh_token`, {}, {
      headers: new HttpHeaders().set('Authorization', `Bearer ${refreshToken}`)
    });
  }


  saveTokens(authResponse: AuthResponse): void {
    localStorage.setItem('accessToken', authResponse.accessToken);
    localStorage.setItem('refreshToken', authResponse.refreshToken);
  }

  logout(): void {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
  }

  isAuthenticated(): boolean {
    return !!localStorage.getItem('accessToken');
  }


  getAccessToken(): string | null {
    return localStorage.getItem('accessToken');
  }


}
