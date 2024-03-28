import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { CONFIG } from '../config/app-config';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class ManagecontactService {
  constructor(private http: HttpClient) {}

  getAllContactList(filterData: any) {
    let header = new HttpHeaders();
    header = header.set('Content-Type', 'application/json');
    header = header.set('Version', 'V1');
    return this.http.post<any>(CONFIG.getAllContactListURL, filterData,{headers: header}).pipe(
      map((response) => {
        return response;
      })
    );
  }

  deleteContact(id: number) {
    return this.http.delete<any>(CONFIG.deleteContactURL + id).pipe(
      map((response) => {
        return response;
      })
    );
  }

  changeStatus(data: any) {
    return this.http.post<any>(CONFIG.changeStatusContactListURL, data).pipe(
      map((response) => {
        return response;
      })
    );
  }
}
