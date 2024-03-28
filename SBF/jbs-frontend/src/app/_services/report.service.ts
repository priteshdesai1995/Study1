import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { CONFIG } from '../config/app-config';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class ReportService {
  constructor(private http: HttpClient) {}
  getUserReportList(data: any) {
    let header = new HttpHeaders();
    header = header.set('Content-Type', 'application/json');
    header = header.set('Version', 'V1');
    return this.http.post<any>(CONFIG.getUserReportListURL, data, {headers: header}).pipe(
      map((response) => {
        return response;
      })
    );
  }

  deleteUserReport(id: number) {
    let header = new HttpHeaders();
    header = header.set('Content-Type', 'application/json');
    header = header.set('Version', 'V1');
    return this.http.delete<any>(CONFIG.deleteUserReportURL + id, {headers: header}).pipe(
      map((data) => {
        return data;
      })
    );
  }
  changeReportStatus(status: string, id: number) {
    let header = new HttpHeaders();
    header = header.set('Content-Type', 'application/json');
    header = header.set('Version', 'V1');
    return this.http
      .post<any>(CONFIG.changeReportStatusURL, { reportId: id, status: status }, {headers: header})
      .pipe(
        map((response) => {
          return response;
        })
      );
  }
}
