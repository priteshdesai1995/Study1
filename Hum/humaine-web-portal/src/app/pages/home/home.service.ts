import { Injectable } from '@angular/core';
import { URLS } from '../../core/_constant/api.config';
import { DataService } from '../../services/data-service.service';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class HomeService {
  constructor(private dataService: DataService) { }
  getDashboard() {
    return this.dataService.get(URLS.dashboard).pipe(
      map((data) => {
        return data;
      })
    )
  }

  getDashboardJourney(param) {
    return this.dataService.post(URLS.dashboardJourney,param).pipe(
      map((data) => {
        return data;
      })
    )
  }
}