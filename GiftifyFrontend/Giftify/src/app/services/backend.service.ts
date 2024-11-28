import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class BackendService {
  private apiUrl = 'http://localhost:8080/api'; // URL del backend

  constructor(private http: HttpClient) {}

  // Metodo per chiamare l'API
  getHelloMessage(): Observable<string> {
    return this.http.get(`${this.apiUrl}/hello`, { responseType: 'text' });
  }
}
