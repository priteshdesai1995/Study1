import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { RequestOptions } from "@angular/http";
import { Observable } from "rxjs";
import { CONFIG } from "../config/app-config";
import { RegisterRequest } from "../model/registerrequest";

@Injectable({
  providedIn: 'root',
})
export class RegisterService {
  constructor(private http: HttpClient) { }

  //register
  signUp(signUpRequest: RegisterRequest): Observable<any> {
    let header = new HttpHeaders();
    header = header.set('Content-Type', 'application/json');
    header = header.set('Version', 'V1');
    return this.http
      .post<any>(CONFIG.registerURL, signUpRequest, { headers: header, responseType: 'json' });

  }

  changePassword(data: any): Observable<any> {
    let header = new HttpHeaders();
    header = header.set('Content-Type', 'application/json');
    header = header.set('Version', 'V1');
    header = header.set('language', '');
    const currentUser = JSON.parse(localStorage.getItem('user'));
    // header = header.append('Authorization', `Bearer ${currentUser.access_token}`);
    return this.http.put(CONFIG.changeProfilePassURL, data, { headers: header, responseType: 'json' });
  }
  getProfile(): Observable<any> {
    const currentUser = JSON.parse(localStorage.getItem('user'));
    let userId = currentUser.userId;
    let header = new HttpHeaders();
    header = header.set('Content-Type', 'application/json');
    header = header.set('Version', 'V1');
    return this.http.get(CONFIG.getUserProfileIdURL+`/${userId}`, { headers: header,responseType: 'json' });
  }
}