import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { CONFIG } from '../config/app-config';

@Injectable({
  providedIn: 'root',
})
export class SurveyService {
  constructor(private http: HttpClient) {}

  getSurveyList(SurveyData: any) {
    let header = new HttpHeaders();
    header = header.set('Content-Type', 'application/json');
    header = header.set('Version', 'V1');
    return this.http.post<any>(CONFIG.getSurveyListURL, SurveyData, {headers: header}).pipe(
      map((response) => {
        return response;
      })
    );
  }

  getSurveyDetailsById(id: number) {
    let header = new HttpHeaders();
    header = header.set('Content-Type', 'application/json');
    header = header.set('Version', 'V1');
    return this.http.get<any>(CONFIG.getSurveyDetailsByIdURL + id,{headers: header}).pipe(
      map((data) => {
        return data;
      })
    );
  }
  getSurveyById(id: number) {
    let header = new HttpHeaders();
    header = header.set('Content-Type', 'application/json');
    header = header.set('Version', 'V1');
    return this.http.get<any>(CONFIG.getSurveyByIdURL + id, {headers: header}).pipe(
      map((data) => {
        return data;
      })
    );
  }

  updateSurvey(SurveyData: any) {
    let header = new HttpHeaders();
    header = header.set('Content-Type', 'application/json');
    header = header.set('Version', 'V1');
    return this.http.post<any>(CONFIG.updateSurveyURL+SurveyData.surveyId, SurveyData, {headers: header}).pipe(
      map((response) => {
        return response;
      })
    );
  }

  createSurvey(SurveyData: any) {
    let header = new HttpHeaders();
    header = header.set('Content-Type', 'application/json');
    header = header.set('Version', 'V1');

    return this.http.post<any>(CONFIG.createSurveyURL, SurveyData, {headers : header}).pipe(
      map((response) => {
        return response;
      })
    );
  }

  changeSurveyStatus(status: string, id: number) {
    let header = new HttpHeaders();
    header = header.set('Content-Type', 'application/json');
    header = header.set('Version', 'V1');
    return this.http
      .post<any>(CONFIG.changeSurveyStatusURL, { surveyId: id, status: status }, {headers: header})
      .pipe(
        map((response) => {
          return response;
        })
      );
  }

  deleteSurvey(id: number) {
    let header = new HttpHeaders();
    header = header.set('Content-Type', 'application/json');
    header = header.set('Version', 'V1');
    return this.http.delete<any>(CONFIG.deleteSurveyURL + id, {headers: header}).pipe(
      map((response) => {
        return response;
      })
    );
  }
  createSurveyQuesAns(id, formdata) {
    let header = new HttpHeaders();
    header = header.set('Content-Type', 'application/json');
    header = header.set('Version', 'V1');
    return this.http.post<any>(CONFIG.createSurveyQuestionsURL+id, formdata, {headers : header}).pipe(
      map((response) => {
        return response;
      })
    );
  }
  updateSurveyQuesAns(id, formdata) {
    let header = new HttpHeaders();
    header = header.set('Content-Type', 'application/json');
    header = header.set('Version', 'V1');
    return this.http.post<any>(CONFIG.updateSurveyQuestionsURL+id, formdata, {headers: header}).pipe(
      map((response) => {
        return response;
      })
    );
  }
  getSurveyQuesAnsById(id: number) {
    let header = new HttpHeaders();
    header = header.set('Content-Type', 'application/json');
    header = header.set('Version', 'V1');
    return this.http.get<any>(CONFIG.getSurveyQuesAnsByIdURL + id, {headers: header}).pipe(
      map((data) => {
        return data;
      })
    );
  }
  getAnswerForTextType(SurveyData: any) {
    return this.http.post<any>(CONFIG.getAnswerForTextTypeURL, SurveyData).pipe(
      map((response) => {
        return response;
      })
    );
  }
  getAnswerForSingleMultipleType(SurveyData: any) {
    return this.http.post<any>(CONFIG.getAnswerForSingleMultipleTypeURL, SurveyData).pipe(
      map((response) => {
        return response;
      })
    );
  }
  // User Survey
  getActiveSurveyList(SurveyData: any) {
    return this.http.post<any>(CONFIG.getActiveSurveyListURL, SurveyData).pipe(
      map((response) => {
        return response;
      })
    );
  }
  updateUserSurveyAns(formdata) {
    return this.http.post<any>(CONFIG.updateUserSurveyAnsURL, formdata).pipe(
      map((response) => {
        return response;
      })
    );
  }
  getAnswerDetailsById(id: number) {
    return this.http.get<any>(CONFIG.getAnswerDetailsByIdURL + id).pipe(
      map((data) => {
        return data;
      })
    );
  }
  getSurveyUserReport(SurveyData: any) {
    return this.http.post<any>(CONFIG.getSurveyUserReportURL, SurveyData).pipe(
      map((response) => {
        return response;
      })
    );
  }
  notifySurveyUserReport(userData: any) {
    return this.http.post<any>(CONFIG.notifySurveyUserURL, userData).pipe(
      map((response) => {
        return response;
      })
    );
  }
}
