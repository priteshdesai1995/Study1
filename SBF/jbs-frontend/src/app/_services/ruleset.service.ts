import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { CONFIG } from '../config/app-config';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class RulesetService {
  constructor(private http: HttpClient) {}

  getRulesetList(data: any) {
    let header = new HttpHeaders();
    header = header.set('Content-Type', 'application/json');
    header = header.set('Version', 'V1');
    return this.http.post<any>(CONFIG.getRulesetListURL, data, {headers: header}).pipe(
      map((response) => {
        return response;
      })
    );
  }
  createRuleset(data: any) {
    let header = new HttpHeaders();
    header = header.set('Content-Type', 'application/json');
    header = header.set('Version', 'V1');
    return this.http.post<any>(CONFIG.createRulesetURL, data, {headers: header}).pipe(
      map((response) => {
        return response;
      })
    );
  }
  updateRuleset(data: any) {
    let header = new HttpHeaders();
    header = header.set('Content-Type', 'application/json');
    header = header.set('Version', 'V1');
    return this.http.post<any>(CONFIG.updateRulesetURL, data, {headers: header}).pipe(
      map((response) => {
        return response;
      })
    );
  }
  getRulesetById(data: any) {
    let header = new HttpHeaders();
    header = header.set('Content-Type', 'application/json');
    header = header.set('Version', 'V1');
    return this.http.post<any>(CONFIG.getRulesetByIdURL+data.id, data, {headers: header}).pipe(
      map((response) => {
        return response;
      })
    );
  }
  changeRulesetStatus(data: any) {
    let header = new HttpHeaders();
    header = header.set('Content-Type', 'application/json');
    header = header.set('Version', 'V1');
    return this.http.post<any>(CONFIG.changeRulesetStatusURL, data, {headers: header}).pipe(
      map((response) => {
        return response;
      })
    );
  }
  deleteRuleset(data: any) {
    let header = new HttpHeaders();
    header = header.set('Content-Type', 'application/json');
    header = header.set('Version', 'V1');
    return this.http.post<any>(CONFIG.deleteRulesetURL, data, {headers: header}).pipe(
      map((response) => {
        return response;
      })
    );
  }
}
