import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {AuthResponse} from '../../model/Auth/AuthResponse';
import {Community} from '../../model/Community';

@Injectable({
  providedIn: 'root'
})
export class CommunityService {
  private apiUrl = 'http://localhost:8080/community';

  constructor(private http: HttpClient) {}

  removeUserFromCommunity(communityId: string, userId: number): Observable<string> {
    return this.http.delete(`${this.apiUrl}/removeUser/${communityId}/${userId}`, { responseType: 'text' });
  }

  closeCommunity(communityId: string): Observable<string> {
    return this.http.delete(`${this.apiUrl}/closeCommunity/${communityId}`, { responseType: 'text' });
  }

  deleteCommunity(communityId: string): Observable<string> {
    return this.http.delete(`${this.apiUrl}/deleteCommunity/${communityId}`, { responseType: 'text' });
  }

  updateCommunity(communityId:string, communityUpdateData: any): Observable<string> {
    return this.http.put(`${this.apiUrl}/updateCommunity/${communityId}`, communityUpdateData, { responseType: 'text' });
  }

  getParticipants(communityId:string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/participants/${communityId}`);
  }

  getGeneralInfo(communityId: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/infoCommunity/${communityId}`);
  }

  viewDrawnName(communityId: string): Observable<string> {
    return this.http.get(`${this.apiUrl}/drawnName/${communityId}`, {responseType: "text"});
  }

  viewDrawnNameList(communityId: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/drawnNameList/${communityId}`);
  }

  viewParticipantList(communityId: string, userId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/participantList/${communityId}/${userId}`);
  }

  getWishlists(communityId: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/wishlists/${communityId}`);
  }

  // getUserCommunityByName(name: string): Observable<any> {
  //   return this.http.get<any>(`${this.apiUrl}/communityInfo/${name}`);
  // }

  saveIds(community: Community): void {
    sessionStorage.clear();
    sessionStorage.setItem('id', String(community.id));
  }

}
