import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {catchError, Observable, tap, throwError} from 'rxjs';
import {AuthResponse} from '../../model/Auth/AuthResponse';
import {Community} from '../../model/Community';
import {Router} from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class CommunityService {
  private apiUrl = 'http://localhost:8080/community';

  constructor(private http: HttpClient, private router: Router) {
  }

  getAccountInfo(): Observable<any> {
    return this.http.get('http://localhost:8080/accountInfo').pipe(
      catchError((error) => {
        return throwError(() => error);
      })
    );
  }

  removeUserFromCommunity(communityId: string, name: string | null): Observable<string> {
    return this.http.delete(`${this.apiUrl}/removeUser/${communityId}/${name}`, {responseType: 'text'});
  }

  closeCommunity(communityId: string): Observable<Object> {
    return this.http.post(`${this.apiUrl}/closeCommunity/${communityId}`, {responseType: 'text'});
  }

  deleteCommunity(communityId: string): Observable<string> {
    return this.http.delete(`${this.apiUrl}/deleteCommunity/${communityId}`, {responseType: 'text'});
  }

  updateCommunity(communityId: string, communityUpdateData: any): Observable<string> {
    return this.http.put(`${this.apiUrl}/updateCommunity/${communityId}`, communityUpdateData, {responseType: 'text'});
  }

  getGeneralInfo(communityId: string): Observable<Community> {
    return this.http.get<Community>(`${this.apiUrl}/infoCommunity/${communityId}`);
  }

  viewDrawnName(communityId: string): Observable<string> {
    return this.http.get(`${this.apiUrl}/drawnName/${communityId}`, {responseType: "text"});
  }

  viewParticipantList(communityId: string, accountCommunityName: string | null): Observable<any> {
    return this.http.get<any[]>(`${this.apiUrl}/${communityId}/participantList/${accountCommunityName}`);
  }

  viewUserCommunityName(communityId: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/getName/${communityId}`, {responseType: 'text'});
  }


  goBack() {
    this.router.navigate(['/home']);
  }

}
