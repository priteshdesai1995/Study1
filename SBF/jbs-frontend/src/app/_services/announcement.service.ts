import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { CONFIG } from '../config/app-config';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class AnnouncementService {
  constructor(private http: HttpClient) {}

  getAllAnnouncementList(AnnouncementData: any) {
    let header = new HttpHeaders();
    header = header.set('Content-Type', 'application/json');
    header = header.set('Version', 'V1');
    return this.http.post<any>(CONFIG.getAllAnnouncementListURL, AnnouncementData, {headers: header}).pipe(
      map((response) => {
        return response;
      })
    );
  }

  createAnnouncement(AnnouncementData: any) {
    let header = new HttpHeaders();
    header = header.set('Content-Type', 'application/json');
    header = header.set('Version', 'V1');
    return this.http.post<any>(CONFIG.createAnnouncementURL, AnnouncementData, {headers: header}).pipe(
      map((response) => {
        return response;
      })
    );
  }

  getAnnouncementDetailsById(id: any) {
    let header = new HttpHeaders();
    header = header.set('Content-Type', 'application/json');
    header = header.set('Version', 'V1');
    return this.http.get<any>(CONFIG.getAnnouncementDetailsByIdURL+id, {headers: header}).pipe(
      map((response) => {
        return response;
      })
    );
  }

  getAnnouncementUserData(id: any) {
    let header = new HttpHeaders();
    header = header.set('Content-Type', 'application/json');
    header = header.set('Version', 'V1');
    return this.http.get<any>(CONFIG.getAnnouncementUserDataURL+id, {headers: header}).pipe(
      map((response) => {
        return response;
      })
    );
  }

  getAnnouncementUserSelectionList(AnnouncementData: any) {
    let header = new HttpHeaders();
    header = header.set('Content-Type', 'application/json');
    header = header.set('Version', 'V1');
    return this.http.post<any>(CONFIG.getAnnouncementUserSelectionListURL, AnnouncementData, {headers: header}).pipe(
      map((response) => {
        return response;
      })
    );
  }
}
