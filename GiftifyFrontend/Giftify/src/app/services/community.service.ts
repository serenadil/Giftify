import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {catchError, Observable, tap, throwError} from 'rxjs';
import {AuthResponse} from '../../model/Auth/AuthResponse';
import {Community} from '../../model/Community';

@Injectable({
  providedIn: 'root'
})
export class CommunityService {
  private apiUrl = 'http://localhost:8080/community';

  constructor(private http: HttpClient) {}

  getAccountInfo(): Observable<any> {
    return this.http.get('http://localhost:8080/accountInfo').pipe(
      tap((response) => {
        console.log('Dati dell\'account:', response); // Stampa tutta la risposta
        // Se la risposta contiene un oggetto con i dati dell'account, puoi fare qualcosa del tipo:
        console.log('Nome dell\'account:', response);
        console.log('Email dell\'account:', response);
      }),
      catchError((error) => {
        console.error('Errore nel caricamento dell\'account:', error);
        return throwError(() => error);
      })
    );
  }

  removeUserFromCommunity(communityId: string, userId: number): Observable<string> {
    return this.http.delete(`${this.apiUrl}/removeUser/${communityId}/${userId}`, { responseType: 'text' });
  }

  closeCommunity(communityId: string): Observable<Object> {
    return this.http.post(`${this.apiUrl}/closeCommunity/${communityId}`, { responseType: 'text' });
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


  getRoleForCommunity(communityId: string): Observable<any> {
    return this.http.get<any[]>(`${this.apiUrl}/role/${communityId}`);
  }

  saveIds(community: Community): void {
    sessionStorage.clear();
    sessionStorage.setItem('id', String(community.id));
  }

}
