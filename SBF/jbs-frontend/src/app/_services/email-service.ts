import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { CONFIG } from '../config/app-config';
import { map } from 'rxjs/operators';
import { Email } from './../model/email';

@Injectable({
  providedIn: 'root',
})
export class EmailService {
  constructor(private http: HttpClient) {}

  getAllEmailList() {
    let header = new HttpHeaders();
    header = header.set("Version", "V1");
    header = header.set('Content-Type', 'application/json');
    return this.http.get<any>(CONFIG.getAllEmailListURL,{headers: header}).pipe(
      map((data) => {
        return data;
      })
    );
  }

  getEmailById(id: number) {
    let header = new HttpHeaders();
    header = header.set("Version", "V1");
    header = header.set('Content-Type', 'application/json');
    return this.http.get<any>(CONFIG.getEmailByIdURL + id,{headers: header}).pipe(
      map((data) => {
        return data;
      })
    );
  }

  updateEmail(emailData: any, id: number) {
    let header = new HttpHeaders();
    header = header.set("Version", "V1");
    header = header.set('Content-Type', 'application/json');
    return this.http.post<any>(CONFIG.updateEmailURL + id, emailData,{headers: header}).pipe(
      map((response) => {
        return response;
      })
    );
  }


  addEmail(emailData: any) {
    let header = new HttpHeaders();
    header = header.set("Version", "V1");
    header = header.set('Content-Type', 'application/json');
    return this.http.post<any>(CONFIG.addEmailURL, emailData,{headers: header}).pipe(
      map((response) => {
        return response;
      })
    );
  }


  changeEmailStatus(status: string, id: number) {
    let header = new HttpHeaders();
    header = header.set("Version", "V1");
    header = header.set('Content-Type', 'application/json');
    return this.http
      .put<any>(CONFIG.changeEmailStatusURL, { id: id, status: status },{headers: header})
      .pipe(
        map((response) => {
          return response;
        })
      );
  }
}
