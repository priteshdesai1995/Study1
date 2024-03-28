import { MenuLinks } from './../core/_enums/MenuLinks';
import { Router } from '@angular/router';
import { getValueByKey, isEmpty } from './../core/_utility/common';
import { Injectable } from '@angular/core';
import { DataService } from './data-service.service';
import { BehaviorSubject, Observable, Subject } from 'rxjs';
import { URLS } from '../core/_constant/api.config';
import { CONFIGCONSTANTS } from '../core/_constant/app-constant';
import * as CryptoJS from 'crypto-js';
import * as _ from 'lodash';
import { environment } from '../../../src/environments/environment'

import {
  CognitoUserPool,
  CognitoUser,
  AuthenticationDetails
} from "amazon-cognito-identity-js";
import { ToasterService } from '../core/_utility/notify.service';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  private key: string = '';
  loginDetector: BehaviorSubject<boolean>;
  loginDetector$;

  constructor(private dataService: DataService,
    private toaster: ToasterService,
    private router: Router
  ) {
    const isLoggedIN: boolean = this.getLoginToken() ? true : false;
    this.loginDetector = new BehaviorSubject<boolean>(isLoggedIN);
    this.loginDetector$ = this.loginDetector.asObservable();
  }

  authenticateCongnito(data): Observable<any> {
    let authResult = new Subject<any>();
    const CogAuthData = new AuthenticationDetails(data);
    const CogUserPool = new CognitoUserPool(environment.cognitoPool);
    const CogUser = new CognitoUser({
      Username: data.Username,
      Pool: CogUserPool
    });
    CogUser.authenticateUser(CogAuthData, {
      onSuccess: result => {
        authResult.next(result);
      },
      onFailure: err => {
        authResult.error(err);
      }
    });
    return authResult.asObservable();
  }

  forgetPasswordCongnito(data): Observable<any> {
    let authResult = new Subject<any>();
    const CogUserPool = new CognitoUserPool(environment.cognitoPool);
    const CogUser = new CognitoUser({
      Username: data.Username,
      Pool: CogUserPool
    });
    CogUser.forgotPassword({
      onSuccess: function (result) {
        authResult.next(result);
      },
      onFailure: function (err) {
        authResult.error(err);
      },
      inputVerificationCode: function (data) {
        authResult.next(data);
      }
    });
    return authResult.asObservable();
  };

  passwordReset(data): Observable<any> {
    let authResult = new Subject<any>();
    const CogUserPool = new CognitoUserPool(environment.cognitoPool);
    const CogUser = new CognitoUser({
      Username: data.Username,
      Pool: CogUserPool
    });
    CogUser.confirmPassword(data.verificationCode, data.newPassword, {
      onSuccess() {
        authResult.next("password changed");

      },
      onFailure(err) {
        authResult.error(err);
      }
    });
    return authResult.asObservable();
  }

  getLoginToken(): any {
    const localObj = this.getStoreData();
    return getValueByKey(localObj, 'token', undefined);
  }

  isRemeber() {
    return localStorage.getItem('rememberData');
  }

  setRememberData(loginData: any) {
    localStorage.removeItem('rememberData');
    localStorage.setItem('rememberData', loginData);
  }


  setLoginToken(token: string): void {
    let obj = this.getStoreData();
    if (isEmpty(obj)) {
      obj = {
        token: null,
        account: {
        }
      }
    }
    obj['token'] = token;
    this.setDataToStorage(obj);
  }

  getAccountDetails(): any {
    const localObj = this.getStoreData();
    return getValueByKey(localObj, 'account', undefined);
  }

  setAccountDetails(data): void {
    let obj = this.getStoreData();
    if (isEmpty(obj)) {
      obj = {
        token: null,
        account: {
        }
      }
    }
    if (!isEmpty(data)) {
      obj['account'] = data;
    }
    this.setDataToStorage(obj);
  }

  setDataToStorage(data) {
    localStorage.setItem('user', this.getEncryptedString(JSON.stringify(data)));
  }

  getStoreData() {
    const data = localStorage.getItem('user');
    if (isEmpty(data)) {
      return {}
    }
    return JSON.parse(this.getDecryptedString(data));
  }

  isAuthenticated(): boolean {
    const token = this.getLoginToken();
    return token ? true : false;
  }


  removeLoginToken(): void {
    localStorage.removeItem('user');
    this.loginDetector.next(false);
  }

  logout() {
    if (this.isAuthenticated()) {
      localStorage.removeItem('user');
    }
    this.router.navigate(['/login']);

  }

  getEncryptedString(text: string) {
    let secret_key: string = CONFIGCONSTANTS.secret_key;
    let iv = CONFIGCONSTANTS.iv;
    const message = CryptoJS.enc.Utf8.parse(text);
    secret_key = CryptoJS.enc.Utf8.parse(secret_key);
    iv = CryptoJS.enc.Utf8.parse(iv);

    var ciphertext = CryptoJS.AES.encrypt(message, secret_key, {
      iv: iv,
      mode: CryptoJS.mode.CBC,
      padding: CryptoJS.pad.Pkcs7,
    });
    return ciphertext.toString()
  }

  getDecryptedString(text: string) {
    let secret_key: string = CONFIGCONSTANTS.secret_key;
    let iv = CONFIGCONSTANTS.iv;
    const message = text;
    secret_key = CryptoJS.enc.Utf8.parse(secret_key);
    iv = CryptoJS.enc.Utf8.parse(iv);

    var bytes = CryptoJS.AES.decrypt(message.toString(), secret_key, {
      iv: iv,
      mode: CryptoJS.mode.CBC,
      padding: CryptoJS.pad.Pkcs7,
    });
    return bytes.toString(CryptoJS.enc.Utf8);
  }

  checkIfRegistrationPending(redirectToRegistrationPage = true, pendingRegistrationCallBack?: CallableFunction) {
    const authenticated = this.isAuthenticated();
    const account = this.getAccountDetails();
    const status = getValueByKey(account, 'status', false);
    if (authenticated) {
      if (!status) {
        if (redirectToRegistrationPage) {
          this.router.navigate(['/registration']);
        } else {
          pendingRegistrationCallBack(false);
        }
      }
      this.router.navigate(['/dashboard']);
    }
  }
  getMenuCount(menu: MenuLinks) {
    return getValueByKey(this.getStoreData(), 'account.menuCounts.' + menu, 0);
  }

  setMenuCount(menu: MenuLinks, count: number) {
    const storedData = this.getStoreData();
    _.set(storedData, 'account.menuCounts.'+ menu, count);
    this.setDataToStorage(storedData);
  }

  incrementMenuCount(menu: MenuLinks) {
    const storedData = this.getStoreData();
    let count = this.getMenuCount(menu);
    count++;
    this.setMenuCount(menu, count);
  }
}
