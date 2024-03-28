
import { Injectable } from '@angular/core';
import { URLS } from '../../core/_constant/api.config';
import { DataService } from '../../services/data-service.service';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class SettingService {
  constructor(
    private dataService: DataService ) {}
  

  getAPIKey() {
    return this.dataService.get(URLS.settingAccount).pipe(
      map((data) => {
        return data;
      })
    );
  }
  updateAPIKEY(){
    return this.dataService.put(URLS.settingAPIKEY).pipe(
      map((data) => {
        return data;
      })
    );

  }
}
