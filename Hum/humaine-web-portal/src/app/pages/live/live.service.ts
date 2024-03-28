import { Injectable } from '@angular/core';
import { map } from 'rxjs/operators';
import { URLS } from '../../core/_constant/api.config';
import { DataService } from '../../services/data-service.service';

@Injectable({
  providedIn: 'root'
})
export class LiveService {

  constructor(private dataService:DataService) { }

  getActiveDevice() {
    return this.dataService.get(URLS.liveActiveDevice).pipe(
      map((data) => {
        return data;
      })
    )
  };
  getTopCountries() {
    return this.dataService.get(URLS.liveTopCountries).pipe(
      map((data) => {
        return data;
      })
    )
  }
  getTileStatistics() {
    return this.dataService.get(URLS.liveTileStatistics).pipe(
      map((data) => {
        return data;
      })
    )
  };
  getSessionDuration(param) {
    return this.dataService.get(URLS.liveSessionDuration + param).pipe(
      map((data) => {
        return data;
      })
    )
  };
  getPageLoadTime(param) {
    return this.dataService.get(URLS.livePageLoadTime + param).pipe(
      map((data) => {
        return data;
      })
    )
  }
  getAPDEX(param:string) {
    return this.dataService.get(URLS.liveApdexScore + param).pipe(
      map((data) => {
        return data;
      })
    )
  }
  getHourlyDashboard() {
    return this.dataService.get(URLS.liveDashboard).pipe(
      map((data) => {
        return data;
      })
    )
  }
  getBounceRate() {
    return this.dataService.get(URLS.liveBounceRate).pipe(
      map((data) => {
        return data;
      })
    )
  }
  // make service for posting refresh time in database 
  postPageRefreshTime() {
    return this.dataService.post(URLS.livePostPageRefreshTime).pipe(
      map((data) => {
        return data;
      })
    )
  }
  // make service for geting last refresj time API
  getLastRefreshTime() {
    return this.dataService.get(URLS.liveLastRefreshTime).pipe(
      map((data) => {
        return data;
      })
    )
  }
}
