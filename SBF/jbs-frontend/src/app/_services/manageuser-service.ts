import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { CONFIG } from '../config/app-config';
import { map } from 'rxjs/operators';
import { Manageuser } from './../model/manageuser';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ManageuserService {
  constructor(private http: HttpClient) {}

  getAllManageUserListURL(userFilter: any) {
    let header = new HttpHeaders();
    header = header.set('Content-Type', 'application/json');
    header = header.set('Version', 'V1');
    return this.http.post<any>(CONFIG.getAllManageUserListURL,userFilter,{headers: header,responseType: 'json'}).pipe(
      map((response) => {
        return response;
      })
    );
  }

  getExportUserList(type: string) {
    let route = CONFIG.exportUsersExcelURL;
    let header = new HttpHeaders();
    if (type === 'pdf') {
      route = CONFIG.exportUsersPDFURL;
    } else if (type === 'csv') {
      route = CONFIG.exportUsersCSVURL;
      header = header.set('Accept', 'text/csv');
    }
    return this.http.get<any>(route,{ headers: header,responseType: 'blob' as 'json'});
  }

  getManagerUserById(userId: number) :Observable<any> {
    let header = new HttpHeaders();
    header = header.set('Content-Type', 'application/json');
    header = header.set('Version', 'V1');
    return this.http.get(CONFIG.getUserProfileIdURL+`/${userId}`, { headers: header,responseType: 'json' });
  }

  updateManageUser(ManageuserData: any, id: number) {
    let header = new HttpHeaders();
    header = header.set('Content-Type', 'application/json');
    header = header.set('Version', 'V1');
    return this.http.put<any>(CONFIG.updateManageUserURL + id, ManageuserData,{ headers: header,responseType: 'json'}).pipe(
      map((response) => {
        return response;
      })
    );
  }

  createManageUser(ManageuserData: any) {
    let header = new HttpHeaders();
    header = header.set('Content-Type', 'application/json');
    header = header.set('Version', 'V1');
    return this.http.post<any>(CONFIG.createManageUserURL, ManageuserData,{headers: header}).pipe(
      map((response) => {
        return response;
      })
    );
  }

  changeManageUserPassword(ManageuserData: any) {
    let header = new HttpHeaders();
    header = header.set('Content-Type', 'application/json');
    header = header.set('Version', 'V1');
    return this.http.put<any>(CONFIG.changeManageUserPassURL, ManageuserData,{headers: header,responseType:'json'}).pipe(
      map((response) => {
        return response;
      })
    );
  }

  changeManageUserStatus(id: number): Observable<any>{
    let header = new HttpHeaders();
    header = header.set('Content-Type', 'application/json');
    header = header.set('Version', 'V1');
    return this.http
      .get<any>(CONFIG.changeManageUserStatusURL+"?userId="+id,{headers: header,responseType: 'json'})
      .pipe(
        map((response) => {
          return response;
        })
      );
  }

  deleteManageUser(id: number) {
    return this.http.delete<any>(CONFIG.deleteManageUserURL + `/${id}`).pipe(
      map((response) => {
        return response;
      })
    );
  }

  lockManageUser(id: number) {
    return this.http
      .post<any>(CONFIG.lockManageUserURL, { lockable_id: id, lockable_type: 'user' })
      .pipe(
        map((response) => {
          return response;
        })
      );
  }

  unlockManageUser(id: number, is_self_locked: number) {
    return this.http
      .post<any>(CONFIG.unlockManageUserURL, { lockable_id: id, lockable_type: 'user', is_self_locked: is_self_locked })
      .pipe(
        map((response) => {
          return response;
        })
      );
  }

  deleteUserProfile(id: any) {
    return this.http.get<any>(CONFIG.deleteUserProfileURL + id).pipe(
      map((response) => {
        return response;
      })
    );
  }
  getActiveCategoryList(id) {
    let header = new HttpHeaders();
    header = header.set('Content-Type', 'application/json');
    header = header.set('Version', 'V1');
    return this.http.get<any>(CONFIG.getActiveCategoryListURL + id, {headers : header}).pipe(
      map((data) => {
        return data;
      })
    );
  }
  importCSVFile(formData: any) { 
    return this.http.post<any>(CONFIG.importCSVFileURL, formData).pipe(
      map((response) => {
        return response;
      })
    );
  }
  checkPassword(pass) {
    if (pass !== null) {
      return (pass as string).replace(/\*/g, '[*]').replace(/\^/g, '[^]').replace(/\$/g, '[$]');
    }
  }
  // Trim text for space issue
  trimText(str) {
    if (str) {
      return str.trim();
    } else {
      return '';
    }
  }
}
