import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})

export class DataService {
  constructor(private http: HttpClient) { }

  get(url: string, params?: any): Observable<any> {
    return this.http.get(`${url}`, { params: params });
  }
  getById(url: string, id): Observable<any> {
    const newUrl: string = `${url}/${id}`;
    return this.http.get(newUrl);
  }

  post(url: string, param?: any): Observable<any> {
    return this.http.post(`${url}`, param);
  }

  put(url: string, data?: any): Observable<any> {
    return this.http.put(`${url}`, data);
  }

  delete(url: string, id?, body = {}): Observable<any> {
      let newUrl: string = `${url}`;
      if (id) {
        newUrl+=`/${id}`;
      }
      return this.http.request('delete', newUrl, {body: body} );
  }

}
