import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class WishService {
  private apiUrl = 'http://localhost:8080/wish';


  constructor(private http: HttpClient) {
  }

  addWish(communityId: number, wishData: any): Observable<string> {
    return this.http.post(`${this.apiUrl}/addWish/${communityId}`, wishData, {responseType: 'text'});
  }

  deleteWish(wishId: number): Observable<string> {
    return this.http.delete(`${this.apiUrl}/deleteWish/${wishId}`, {responseType: 'text'});
  }

  editWish(wishId: number, wishData: any): Observable<string> {
    return this.http.put(`${this.apiUrl}/editWish/${wishId}`, wishData, {responseType: 'text'});
  }

}
