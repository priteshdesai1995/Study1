import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { CONFIG } from '../config/app-config';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class ActivityTrackingService {
  constructor(private http: HttpClient) {}

  getAllActivityTrackingList(activityData: any) {
    let header = new HttpHeaders();
    header = header.set('Content-Type', 'application/json');
    header = header.set('Version', 'V1');
    return this.http.post<any>(CONFIG.getAllActivityTrackingListURL, activityData, {headers: header}).pipe(
      map((response) => {
        return response;
      })
    );
  }

  deleteActivity(id: number) {
    let header = new HttpHeaders();
    header = header.set('Content-Type', 'application/json');
    header = header.set('Version', 'V1');
    return this.http.delete<any>(CONFIG.deleteActivityURL + id, {headers: header}).pipe(
      map((response) => {
        return response;
      })
    );
  }
}
