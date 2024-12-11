import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class HomeService {
  private apiUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  getAccountInfo(): Observable<any> {
    return this.http.get(`${this.apiUrl}/accountInfo`);
  }

  getUserCommunities(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/getCommunities`);
  }

  joinCommunity(accessCode: string): Observable<string> {
    return this.http.post(`${this.apiUrl}/join/${accessCode}`, null, { responseType: 'text' });
  }

  createCommunity(communityData: any): Observable<string> {
    return this.http.post(`${this.apiUrl}/createCommunity`, communityData, { responseType: 'text' });
  }
}
