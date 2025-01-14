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

  viewMyWishlist(communityId: string) {
    return this.http.get(`${this.apiUrl}/community/${communityId}/myWishlist`);
  }

  addWish(communityId: string, wishData: any): Observable<string> {
    return this.http.post(`${this.apiUrl}/wish/addWish/${communityId}`, wishData, {responseType: 'text'});
  }

  deleteWish(wishId: number): Observable<string> {
    return this.http.delete(`${this.apiUrl}/wish/deleteWish/${wishId}`, {responseType: 'text'});
  }

  editWish(wishId: number, wishData: any): Observable<string> {
    return this.http.put(`${this.apiUrl}/wish/editWish/${wishId}`, wishData, {responseType: 'text'});
  }

}
