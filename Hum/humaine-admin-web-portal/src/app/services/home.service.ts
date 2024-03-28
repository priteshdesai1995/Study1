import { Injectable } from '@angular/core';
import { map } from 'rxjs/operators';
import { URLS } from '../_constant/api.config';
import { DataService } from './data-service.service';

@Injectable({
  providedIn: 'root'
})
export class HomeService {

  constructor(private dataService: DataService) { }
  
  getRegisteredCustomers() {
    return this.dataService.get(URLS.registeredCustomers).pipe(
      map((data) => {
        return data;
      })
    )
  }

}
