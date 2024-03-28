import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { URLS } from '../_constant/api.config';
import { DataService } from './data-service.service';

@Injectable({
  providedIn: 'root',
})
export class UserRegistrationService {

  constructor(
    private dataService: DataService) { }

  getAccountDetails(accountId: number) {
    return this.dataService.get(URLS.getAccountDetails + accountId).pipe(
      map((data) => {
        return data;
      })
    );
  }
  editCustomer(data: any) {
    return this.dataService.put(URLS.customerEdit, data).pipe(
      map((data) => {
        return data;
      })
    );
  }

  getAccountEventsInfo(accountId: number) {
    return this.dataService.get(URLS.accountEventsInfo+accountId).pipe(
      map((data) => {
        return data;
      })
    );
  }

}
