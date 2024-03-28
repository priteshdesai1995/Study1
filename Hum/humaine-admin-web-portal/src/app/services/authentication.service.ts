import { Router } from '@angular/router';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, Subject } from 'rxjs';
import { ToasterService } from '../utility/notify.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { URLS } from '../_constant/api.config';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  removeLoginToken() {
    localStorage.removeItem("user");
  }
  logout() {
    localStorage.removeItem("user");
    this.router.navigate(['/login']);
  }
  constructor(
    private toaster: ToasterService,
    private router: Router,
    private http: HttpClient
  ) { }

  login(username,password) {
    let header = new HttpHeaders({ 'content-type': 'application/x-www-form-urlencoded' });
    const body = new URLSearchParams();
    body.set('username', username);
    body.set('password', password);
    body.set('grant_type', 'password');
    body.set('client_id', 'common')
    body.set('client_secret', 'Brain@2020')
    return this.http.post(URLS.login, body.toString(), { headers: header });
  }

  isAuthenticated(): boolean {
    const token = this.getLoginToken();
    return token ? true : false;
  }

  getLoginToken(): any {
    const currentUser = localStorage.getItem('user');
    if (!currentUser) {
      this.router.navigate(['/login'] );
    } else {
      let user = JSON.parse(currentUser);
      if (user['user_role']['roleName'] == 'ROLE_SUPERADMIN') {
        return user['access_token'];
      } else {
        return null; 
      }
    }
  }

  setLoginToken(data) {
    localStorage.setItem('user', JSON.stringify(data));
  }

  setRememberData(loginData: any) {
    localStorage.removeItem('rememberData');
    localStorage.setItem('rememberData', loginData);
  }

  isRemember() {
    return localStorage.getItem('rememberData');
  }

}
