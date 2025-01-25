import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class WishService {
  private apiUrl = 'http://localhost:8080';


  constructor(private http: HttpClient) {
  }

  addWish(communityId: string, wishData: any): Observable<string> {
    return this.http.post(`${this.apiUrl}/wish/addWish/${communityId}`, wishData, {responseType: 'text'});
  }

  deleteWish(communityId: string, wishId: number): Observable<string> {
    return this.http.delete(`${this.apiUrl}/wish/deleteWish/${communityId}/${wishId}`, {responseType: 'text'});
  }



}
