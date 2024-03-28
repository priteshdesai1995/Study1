import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { CONFIG } from '../config/app-config';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class SuggestionService {
  constructor(private http: HttpClient) {}

  getUserSuggestionList() {
    return this.http.get<any>(CONFIG.userSuggestionListURL).pipe(
      map((data) => {
        // login successful if there's a jwt token in the response
        return data;
      })
    );
  }
  createUserSuggestion(data: any) {
    return this.http.post<any>(CONFIG.userSuggestionCreateURL, data).pipe(
      map((response) => {
        return response;
      })
    );
  }
  getAdminSuggestionList(data: any) {
    let header = new HttpHeaders();
    header = header.set('Content-Type', 'application/json');
    header = header.set('Version', 'V1');
    return this.http.post<any>(CONFIG.adminSuggestionListURL, data, {headers: header}).pipe(
      map((response) => {
        return response;
      })
    );
  }
  changeSuggestionStatus(status: string, notes: string, id: number) {
    console.log(id);
    return this.http
      .put<any>(CONFIG.suggestionChangeStatusURL + id, { status: status, notes: notes })
      .pipe(
        map((response) => {
          return response;
        })
      );
  }
  deleteSuggestion(id: number) {
    return this.http.delete<any>(CONFIG.suggestionDeleteURL + id).pipe(
      map((response) => {
        return response;
      })
    );
  }
  getActiveCategoryList(id: any) {
    let header = new HttpHeaders();
    header = header.set('Content-Type', 'application/json');
    header = header.set('Version', 'V1');
    return this.http.get<any>(CONFIG.getActiveCategoryListURL + id, {headers: header}).pipe(
      map((data) => {
        return data;
      })
    );
  }
  getExportSuggestionList(data: any, type: string) {
    let header = new HttpHeaders();
    header = header.set('Content-Type', 'application/json');
    header = header.set('Version', 'V1');
    let route = CONFIG.exportSuggestionExcelURL;
    if (type === 'pdf') {
      route = CONFIG.exportSuggestionPDFURL;
    } else if (type === 'csv') {
      route = CONFIG.exportSuggestionCSVURL;
    }
    return this.http.post<any>(route, data, {headers: header, responseType: 'blob' as 'json'}).pipe(
      map((response) => {
        return response;
      })
    );
  }
}
