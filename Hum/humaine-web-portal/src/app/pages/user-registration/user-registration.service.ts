import { Injectable } from '@angular/core';
import { URLS } from '../../core/_constant/api.config';
import { DataService } from '../../services/data-service.service';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class UserRegistrationService {
  constructor(
    private dataService: DataService ) {}
  signUp(data: any) {
    return this.dataService.post(URLS.signUp, data).pipe(
      map((data) => {
        return data;
      })
    );
  }

  verifyEmail(data: any) {
    return this.dataService.post(URLS.emailVerify, data).pipe(
      map((data) => {
        return data;
      })
    );
  }

  registerAccount(data: any) {
    return this.dataService.post(URLS.register, data).pipe(
      map((data) => {
        return data;
      })
    );
  }

  getAccountDetails() {
    return this.dataService.get(URLS.getAccountDetails).pipe(
      map((data) => {
        return data;
      })
    );
  }
 
}
