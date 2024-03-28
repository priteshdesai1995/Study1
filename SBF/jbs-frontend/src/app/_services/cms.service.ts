import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { CONFIG } from '../config/app-config';
import { map } from 'rxjs/operators';
import { Cms } from './../model/cms';

@Injectable({
  providedIn: 'root',
})
export class CmsService {
  constructor(private http: HttpClient) { }

  getAllCmsList() {
    let header = new HttpHeaders();
    header = header.set('Content-Type', 'application/json');
    header = header.set('Version', 'V1');
    return this.http.get<any>(CONFIG.getAllCmsListURL, { headers: header }).pipe(
      map((data) => {
        // login successful if there's a jwt token in the response
        return data;
      })
    );
  }

  getCmsById(id: number) {
    let header = new HttpHeaders();
    header = header.set('Content-Type', 'application/json');
    header = header.set('Version', 'V1');
    return this.http.get<any>(CONFIG.getCmsByIdURL + id, { headers: header }).pipe(
      map((data) => {
        // login successful if there's a jwt token in the response
        return data;
      })
    );
  }

  createCms(cmsData: any) {
    let header = new HttpHeaders();
    header = header.set('Content-Type', 'application/json');
    header = header.set('Version', 'V1');
    return this.http.post<any>(CONFIG.createCmsURL, cmsData, { headers: header, responseType: 'json' }).pipe(
      map((response) => {
        return response;
      })
    );
  }

  updateCms(cmsData: any, id: number) {
    let header = new HttpHeaders();
    header = header.set('Content-Type', 'application/json');
    header = header.set('Version', 'V1');
    return this.http.put<any>(CONFIG.updateCmsURL + id, cmsData, { headers: header }).pipe(
      map((response) => {
        return response;
      })
    );
  }

  changeCmsStatus(status: string, id: number) {
    let header = new HttpHeaders();
    header = header.set("Version", "V1");
    header = header.set('Content-Type', 'application/json');
    return this.http
      .put<any>(CONFIG.changeCmsStatusURL, { id: id+"", status: status },{headers: header})
      .pipe(
        map((response) => {
          return response;
        })
      );
  }
  getCmsTemplate(id: number) {
    let header = new HttpHeaders();
    header = header.set("Version", "V1");
    header = header.set('Content-Type', 'application/json');
    return this.http.get<any>(CONFIG.loadPageBuilderURL + id,{headers: header}).pipe(
      map((data) => {
        return data;
      })
    );
  }
  getCmsComponentTemplate(id: number) {
    let header = new HttpHeaders();
    header = header.set("Version", "V1");
    header = header.set('Content-Type', 'application/json');
    return this.http.get<any>(CONFIG.loadComponentURL + id,{headers: header}).pipe(
      map((data) => {
        return data;
      })
    );
  }
}
