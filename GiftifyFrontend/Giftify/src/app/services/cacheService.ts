import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class CacheService {
  private cache = new Map<string, any>();


  set(key: string, data: any): void {
    this.cache.set(key, data);
  }


  get(key: string): any | null {
    return this.cache.get(key) || null;
  }


  clear(key: string): void {
    this.cache.delete(key);
  }


  clearAll(): void {
    this.cache.clear();
  }
}
