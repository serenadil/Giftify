import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CommunityService {
  private apiUrl = 'http://localhost:8080/community';

  constructor(private http: HttpClient) {}

  removeUserFromCommunity(communityId: number, userId: number): Observable<string> {
    return this.http.delete(`${this.apiUrl}/removeUser/${communityId}/${userId}`, { responseType: 'text' });
  }

  closeCommunity(communityId: number): Observable<string> {
    return this.http.delete(`${this.apiUrl}/closeCommunity/${communityId}`, { responseType: 'text' });
  }

  deleteCommunity(communityId: number): Observable<string> {
    return this.http.delete(`${this.apiUrl}/deleteCommunity/${communityId}`, { responseType: 'text' });
  }

  updateCommunity(communityId: number, communityUpdateData: any): Observable<string> {
    return this.http.put(`${this.apiUrl}/updateCommunity/${communityId}`, communityUpdateData, { responseType: 'text' });
  }

  getParticipants(communityId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/participants/${communityId}`);
  }

  getGeneralInfo(communityId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/infoCommunity/${communityId}`);
  }

  viewDrawnName(communityId: number): Observable<string> {
    return this.http.get(`${this.apiUrl}/drawnName/${communityId}`, {responseType: "text"});
  }

  viewDrawnNameList(communityId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/drawnNameList/${communityId}`);
  }

  getWishlists(communityId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/wishlists/${communityId}`);
  }

}
