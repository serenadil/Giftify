import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {Wish} from '../../model/Wish';

@Injectable({
  providedIn: 'root'
})
export class WishService {
  private apiUrl = 'http://localhost:8080/wish';


  constructor(private http: HttpClient) {}

  addWish(communityId : number, wish: Wish) : Observable<string> {
    return this.http.post<string>(`${this.apiUrl}/addWish/${communityId}`, wish);
  }

  deleteWish(wishId: number) : Observable<string> {
    return this.http.delete<string>(`${this.apiUrl}/deleteWish/${wishId}`);
  }

  editWish(wishId : number, wish : Wish) : Observable<string> {
    return this.http.put<string>(`${this.apiUrl}/editWish/${wishId}`, wish);
  }
}
