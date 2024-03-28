import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { CONFIG } from '../config/app-config';
import { catchError, map, tap } from 'rxjs/operators';
import { EncrDecrService } from './encr-decr.service';
import { MultilingualService } from './multilingual.service';
import { TokenService } from './token.service';
import { Observable } from 'rxjs';
import { ToastrService } from 'ngx-toastr';
import { RegisterRequest } from '../model/registerrequest';

@Injectable({
  providedIn: 'root',
})
export class AuthenticationService {
  private loggedInStatus = false;
  redirectUrl = '';

  constructor(private http: HttpClient,
    private EncrDecr: EncrDecrService,
    private multilingualService: MultilingualService,
    private tokenService: TokenService,
    private toastr: ToastrService,
  ) { }

  setLoggedIn(value: boolean) {
    this.loggedInStatus = value;
  }

  get isLoggedIn() {
    return this.loggedInStatus;
  }

  // login(email: string, password: string) {
  //   return this.http
  //     .post<any>(CONFIG.userAuthURL, { email: email, password: password, role: 'ADMIN' })
  //     .pipe(
  //       map((user) => {
  //         // login successful if there's a jwt token in the response
  //         if (user.data && user.meta && user.meta.status === true) {
  //           // store user details and jwt token in local storage to keep user logged in between page refreshes
  //           const encrypted = this.EncrDecr.set(CONFIG.EncrDecrKey, user.data);
  //           localStorage.setItem('currentUser', encrypted);
  //           const firstName = user.data.user_detail.first_name ? user.data.user_detail.first_name : '';
  //           const lastName = user.data.user_detail.last_name ? user.data.user_detail.last_name : '';
  //           localStorage.setItem('fullName', firstName + ' ' + lastName);
  //           const languages = user.data.user_detail.languages;
  //           this.multilingualService.saveLanguage(languages);
  //         }
  //         return user;
  //       })
  //     );
  // }

  forgotPassword(email: string) {
    const param = new URLSearchParams().set("email", email);
    return this.http
      .post<any>(CONFIG.forgotPassURL + '?email=' + email, '')
      .pipe(
        map((response) => {
          if (response.status) {
            this.toastr.success("Email Sent!!");
            return response;
          } else {
            this.toastr.error("Something went wrong!!");
          }
        })
      );
  }

  logout() {
    // remove user from local storage to log user out
    localStorage.removeItem('currentUser');
    localStorage.removeItem('fullName');
    localStorage.removeItem('languages');
    // For remove all module sorting, pagination and filter session
    sessionStorage.clear();
  }

  validateResetPass(token) {
    return this.http
      .post<any>(CONFIG.validateResetPassURL + "?token=" + token, '')
      .pipe(
        map((response) => {
          return response;
        })
      );
  }

  resetPass(token: string, password: string, cPassword: string) {
    const resetPassword = {
      password: password,
      confirmPassword: cPassword
    }
    return this.http
      .put<any>(CONFIG.resetPassURL + "?token=" + token, resetPassword)
      .pipe(
        map((response) => {
          return response;
        })
      );
  }

  getPermissions() {
    let currentUserPermissions = [];
    const decrypted = localStorage.getItem('currentUser');
    const currentUser = this.EncrDecr.get(CONFIG.EncrDecrKey, decrypted);
    if (currentUser) {
      const currentUserJson = JSON.parse(currentUser);
      currentUserPermissions = currentUserJson.user_detail.permission || [];
    }
    return currentUserPermissions;
  }

  login(username: string, password: string) {
    let header = new HttpHeaders({ 'content-type': 'application/x-www-form-urlencoded' });
    const body = new URLSearchParams();
    body.set('username', username);
    body.set('password', password);
    body.set('grant_type', 'password');
    body.set('client_id', 'common')
    body.set('client_secret', 'Brain@2020')
    return this.http
      .post<any>(CONFIG.userAuthURL, body.toString(), { headers: header }
      )
      .pipe(
        map((user) => {
          console.log(user.userName);
          localStorage.setItem('currentUser', user.userName);
          localStorage.setItem('user', JSON.stringify(user));
          localStorage.setItem('accessToken', user.access_token);
          localStorage.setItem('fullName', user.fullName);
          // login successful if there's a jwt token in the response
          // if (user.data && user.meta && user.meta.status === true) {
          //   // store user details and jwt token in local storage to keep user logged in between page refreshes
          //   const encrypted = this.EncrDecr.set(CONFIG.EncrDecrKey, user.data);
          //   localStorage.setItem('currentUser', encrypted);
          //   const firstName = user.data.user_detail.first_name ? user.data.user_detail.first_name : '';
          //   const lastName = user.data.user_detail.last_name ? user.data.user_detail.last_name : '';
          //   localStorage.setItem('fullName', firstName + ' ' + lastName);
            const languages = [{"locale":"en","name":"English","text_direction":"LTR","icon":"icon-en"},{"locale":"ar","name":"Arabic","text_direction":"RTL","icon":"icon-ar"}];
            this.multilingualService.saveLanguage(languages);
          // }
          return user;
        })
      );
  }
  
}