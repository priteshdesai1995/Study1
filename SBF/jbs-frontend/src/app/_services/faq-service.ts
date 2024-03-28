import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { CONFIG } from '../config/app-config';
import { map } from 'rxjs/operators';
import { Faq } from './../model/faq';

@Injectable({
  providedIn: 'root',
})
export class FaqService {
  constructor(private http: HttpClient) {}

  getAllFaqList() {
    let header = new HttpHeaders();
    header = header.set("Version", "V1");
    header = header.set('Content-Type', 'application/json');
    return this.http.get<any>(CONFIG.getAllFaqListURL,{headers:header}).pipe(
      map((data) => {
        return data;
      })
    );
  }

  getFaqTopicList() {
    let header = new HttpHeaders();
    header = header.set("Version", "V1");
    header = header.set('Content-Type', 'application/json');
    return this.http.get<any>(CONFIG.getFaqTopicListURL,{headers: header}).pipe(
      map((data) => {
        return data;
      })
    );
  }

  getFaqById(id: number) {
    let header = new HttpHeaders();
    header = header.set("Version", "V1");
    header = header.set('Content-Type', 'application/json');
    return this.http.get<any>(CONFIG.getFaqByIdURL + id,{headers: header}).pipe(
      map((data) => {
        return data;
      })
    );
  }

  updateFaq(faqData: any, id: number) {
    let header = new HttpHeaders();
    header = header.set("Version", "V1");
    header = header.set('Content-Type', 'application/json');
    return this.http.put<any>(CONFIG.updateFaqURL + id, faqData,{headers: header}).pipe(
      map((response) => {
        return response;
      })
    );
  }

  createFaq(faqData: Faq) {
    let header = new HttpHeaders();
    header = header.set("Version", "V1");
    header = header.set('Content-Type', 'application/json');
    return this.http.post<any>(CONFIG.createFaqURL, faqData,{headers: header}).pipe(
      map((response) => {
        return response;
      })
    );
  }

  changeFaqStatus(status: string, id: number) {
    let header = new HttpHeaders();
    header = header.set("Version", "V1");
    header = header.set('Content-Type', 'application/json');
    return this.http
      .put<any>(CONFIG.changeFaqStatusURL, { id: id, status: status },{headers: header})
      .pipe(
        map((response) => {
          return response;
        })
      );
  }

  deleteFaq(id: number) {
    let header = new HttpHeaders();
    header = header.set("Version", "V1");
    header = header.set('Content-Type', 'application/json');
    return this.http.delete<any>(CONFIG.deleteFaqURL + id,{headers: header}).pipe(
      map((response) => {
        return response;
      })
    );
  }
}
